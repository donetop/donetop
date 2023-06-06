package com.donetop.batch.service.draft

import com.donetop.batch.properties.ApplicationProperties
import com.donetop.batch.properties.DonetopPHP
import org.jsoup.Jsoup
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient

@Service
class DraftRetrieveService(
	private val applicationProperties: ApplicationProperties,
	private val draftParseService: DraftParseService,
	private val donetopPHP: DonetopPHP = applicationProperties.donetopPHP
) {

	private val log = LoggerFactory.getLogger(this.javaClass)
	private val webClient = WebClient.create(donetopPHP.url.base)
	private lateinit var cachedCookieHeader: MultiValueMap<String, String>

	fun getEndPageNum(): Int {
		val endPageNum = Jsoup.parse(rawListPage(0))
			.select("a.pg_end")
			.map { it.attr("href").substringAfter("page=").toInt() }
			.firstOrNull() ?: throw IllegalStateException("There's no valid end page num.")
		log.info("[GET_END_PAGE_NUM] End page num: $endPageNum")
		return endPageNum
	}

	fun getDraftDTOList(page: Int): List<DraftDTO> {
		log.info("[GET_DRAFT_DTO_LIST] Page: $page")
		val rawListPage = rawListPage(page)
		return Jsoup.parse(rawListPage)
			.select("tbody tr td.td_subject a")
			.map { it.attr("href").substringAfter("wr_id=").substringBefore("&").toLong() }
			.mapIndexed { index, id -> draftParseService.draftFrom(id, index + 1, rawListPage, rawDetailPage(id)) }
	}

	private fun rawListPage(page: Int): String {
		return getBodyIf2xxResponse(doRequestWithCookieHeader("${donetopPHP.url.board}&page=$page"))
	}

	private fun rawDetailPage(id: Long): String {
		return getBodyIf2xxResponse(doRequestWithCookieHeader("${donetopPHP.url.board}&wr_id=$id"))
	}

	private fun doRequestWithCookieHeader(uri: String): ResponseEntity<String> {
		return webClient
			.get()
			.uri(uri)
			.headers { it.addAll(cachedCookieHeader) }
			.retrieve()
			.toEntity(String::class.java)
			.block() ?: throw IllegalStateException("Response retrieving has failed.")
	}

	fun refreshCookieHeader(){
		val body: MultiValueMap<String, String> = LinkedMultiValueMap()
		body.add("mb_id", donetopPHP.admin.id)
		body.add("mb_password", donetopPHP.admin.password)
		val response = webClient
			.post()
			.uri(donetopPHP.url.login)
			.body(BodyInserters.fromFormData(body))
			.retrieve()
			.toEntity(String::class.java)
			.block() ?: throw IllegalStateException("Response retrieving has failed.")
		val cookieHeader: MultiValueMap<String, String> = LinkedMultiValueMap()
		cookieHeader["Cookie"] = response.headers["Set-Cookie"] ?: throw IllegalStateException("There's no Set-Cookie header.")
		log.info("[LOGIN] Login success. Retrieved cookie header: $cookieHeader")
		cachedCookieHeader = cookieHeader
	}

	private fun <T : Any?> getBodyIf2xxResponse(response: ResponseEntity<T>): T {
		if (!response.statusCode.is2xxSuccessful) throw IllegalStateException("${response.statusCode} response has arrived.")
		return response.body ?: throw IllegalStateException("There's no valid body.")
	}

}