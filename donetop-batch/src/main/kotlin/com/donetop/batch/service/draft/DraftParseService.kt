package com.donetop.batch.service.draft

import com.donetop.batch.properties.ApplicationProperties
import com.donetop.batch.properties.Storage
import com.donetop.batch.service.crypto.CryptoService
import com.donetop.common.service.storage.LocalFileUtil.multipartFileFrom
import com.donetop.common.service.storage.LocalResource
import com.donetop.domain.entity.draft.Draft
import com.donetop.enums.draft.DraftStatus
import com.donetop.enums.draft.PaymentMethod
import org.jsoup.Jsoup
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.io.File
import java.io.IOException
import java.net.URL
import java.nio.channels.Channels
import java.time.LocalDateTime

@Service
class DraftParseService(
	private val draftAddressParser: DraftAddressParser,
	private val cryptoService: CryptoService,
	private val applicationProperties: ApplicationProperties,
	private val storage: Storage = applicationProperties.storage
) {

	private val log = LoggerFactory.getLogger(this.javaClass)
	private val notApplicableText = "N/A"

	fun draftFrom(id: Long, index: Int, rawListPage: String, rawDetailPage: String): Draft {
		val list = Jsoup.parse(rawListPage)
		val detail = Jsoup.parse(rawDetailPage)
		val customerName = detail.select("section#bo_v_info span.customer_name").text()
		val companyName = ""
		val inChargeName = list.select("tbody tr:nth-child($index) td.in_charge_name").text()
		val email = notApplicableText
		val phoneNumber = list.select("tbody tr:nth-child($index) td.phone_number").text()
		val categoryName = notApplicableText
		val draftStatus = DraftStatus.of(detail.select("section#bo_v_info span.draft_status").text())
		val address = draftAddressParser.addressFrom(detail.select("section#bo_v_info span.address").text())
		val price = detail.select("section#bo_v_info span.price").text().toLong()
		val paymentMethod = PaymentMethod.of(detail.select("section#bo_v_info span.payment_method").text())
		val memo = detail.select("section#bo_v_info textarea[name=\"memo\"]").text()
		val password = cryptoService.encrypt(phoneNumber.split("-").drop(2).joinToString(""))
		val createTime = LocalDateTime.parse(detail.select("section#bo_v_info span.datetime").text().replace(" ", "T"))
		val draft = Draft.builder()
			.id(id)
			.customerName(customerName)
			.companyName(companyName)
			.inChargeName(inChargeName)
			.email(email)
			.phoneNumber(phoneNumber)
			.categoryName(categoryName)
			.draftStatus(draftStatus)
			.address(address.base)
			.detailAddress(address.detail)
			.price(price)
			.paymentMethod(paymentMethod)
			.memo(memo)
			.password(password)
			.createTime(createTime)
			.updateTime(createTime)
			.build()
		log.info("[DRAFT_FROM] Id: $id, Index: $index, $draft")
		val thumbnailResources = detail.select("div#bo_v_con div#bo_v_thumbnail img")
			.map { it.attr("src") }
			.map { downloadFileUsingNIO(it) }
			.map { multipartFileFrom(it) }
			.map { LocalResource(it) }
		val imageResources = detail.select("div#bo_v_con div#bo_v_img img")
			.map { it.attr("src") }
			.map { downloadFileUsingNIO(it) }
			.map { multipartFileFrom(it) }
			.map { LocalResource(it) }
		log.info("[DRAFT_FROM] Thumbnail resources: $thumbnailResources")
		log.info("[DRAFT_FROM] Image resources: $imageResources")
		storage.clearTmpDir()
		return draft
	}

	@Throws(IOException::class)
	private fun downloadFileUsingNIO(urlStr: String): File {
		val fileName = urlStr.substringAfterLast("/")
		val file = File("${storage.tmp}/$fileName")
		file.createNewFile()
		val fos = file.outputStream()
		val rbc = Channels.newChannel(URL(urlStr).openStream())
		fos.channel.transferFrom(rbc, 0, Long.MAX_VALUE)
		fos.close()
		rbc.close()
		return file
	}

}