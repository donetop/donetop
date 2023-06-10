package com.donetop.collect.service.draft

import com.donetop.collect.properties.ApplicationProperties
import com.donetop.collect.properties.Storage
import com.donetop.collect.service.crypto.CryptoService
import com.donetop.domain.entity.file.File
import com.donetop.domain.entity.folder.Folder
import com.donetop.enums.draft.DraftStatus
import com.donetop.enums.draft.PaymentMethod
import com.donetop.enums.folder.FolderType.DRAFT
import org.jsoup.Jsoup
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class DraftParseService(
    private val draftPhoneNumberParser: DraftPhoneNumberParser,
    private val draftAddressParser: DraftAddressParser,
    private val cryptoService: CryptoService,
    private val applicationProperties: ApplicationProperties,
    private val storage: Storage = applicationProperties.storage
) {

	private val log = LoggerFactory.getLogger(this.javaClass)

	fun draftFrom(id: Long, index: Int, rawListPage: String, rawDetailPage: String): DraftDTO? {
		val list = Jsoup.parse(rawListPage)
		val detail = Jsoup.parse(rawDetailPage)
		try {
			val customerName = detail.select("section#bo_v_info span.customer_name").text()
			val companyName = ""
			val inChargeName = list.select("tbody tr:nth-child($index) td.in_charge_name").text().ifEmpty { "장인순팀장" }
			val email = "N/A"
			val phoneNumber = draftPhoneNumberParser.phoneNumberFrom(list.select("tbody tr:nth-child($index) td.phone_number").text())
			val categoryName = "현수막"
			val draftStatus = DraftStatus.of(detail.select("section#bo_v_info span.draft_status").text())
			val address = draftAddressParser.addressFrom(detail.select("section#bo_v_info span.address").text())
			val price = detail.select("section#bo_v_info span.price").text().filter { it.isDigit() }.ifEmpty { "1000" }.toLong()
			val paymentMethod = PaymentMethod.of(detail.select("section#bo_v_info span.payment_method").text().ifEmpty { PaymentMethod.CASH.value() })
			val memo = detail.select("section#bo_v_info textarea[name=\"memo\"]").text()
			val password = cryptoService.encrypt(phoneNumber.split("-").drop(2).joinToString(""))
			val createTime = LocalDateTime.parse(detail.select("section#bo_v_info span.datetime").text().replace(" ", "T"))
			val folder = Folder.of(DRAFT, storage.root, id)
			val fileMap = (detail.select("div#bo_v_con div#bo_v_thumbnail img") + detail.select("div#bo_v_con div#bo_v_img img"))
				.map { it.attr("src") }
				.associateBy {
					val fileName = it.substringAfterLast("/")
					File(fileName, folder)
				}
			val draftDTO = DraftDTO(
				id = id,
				customerName = customerName,
				companyName = companyName,
				inChargeName = inChargeName,
				email = email,
				phoneNumber = phoneNumber,
				categoryName = categoryName,
				draftStatus = draftStatus,
				address = address.base,
				detailAddress = address.detail,
				price = price,
				paymentMethod = paymentMethod,
				memo = memo,
				password = password,
				createTime = createTime,
				fileMap = fileMap
			)
			log.info("[DRAFT_FROM] Id: $id, Index: $index, $draftDTO")
			return draftDTO
		} catch (e: Exception) {
			log.warn("[DRAFT_FROM] Id: $id, Index: $index, Exception occurred. Message: ${e.message}")
			return null
		}
	}

}