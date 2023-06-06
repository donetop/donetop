package com.donetop.batch.service.draft

import com.donetop.common.service.storage.LocalFileUtil
import com.donetop.common.service.storage.LocalResource
import com.donetop.common.service.storage.Resource
import com.donetop.domain.entity.draft.Draft
import com.donetop.domain.entity.file.File
import com.donetop.enums.draft.DraftStatus
import com.donetop.enums.draft.PaymentMethod
import java.io.IOException
import java.net.URL
import java.nio.channels.Channels
import java.time.LocalDateTime

data class DraftDTO(
	val id: Long,
	private val customerName: String,
	private val companyName: String,
	private val inChargeName: String,
	private val email: String,
	private val phoneNumber: String,
	private val categoryName: String,
	private val draftStatus: DraftStatus,
	private val address: String,
	private val detailAddress: String,
	private val price: Long,
	private val paymentMethod: PaymentMethod,
	private val memo: String,
	private val password: String,
	private val createTime: LocalDateTime,
	val fileMap: Map<File, String>
) {

	fun applyTo(draft: Draft): Draft {
		return draft
			.updateCustomerName(customerName)
			.updateCompanyName(companyName)
			.updateInChargeName(inChargeName)
			.updateEmail(email)
			.updatePhoneNumber(phoneNumber)
			.updateCategoryName(categoryName)
			.updateDraftStatus(draftStatus)
			.updateAddress(address)
			.updateDetailAddress(detailAddress)
			.updatePrice(price)
			.updatePaymentMethod(paymentMethod)
			.updateMemo(memo)
			.updatePassword(password)
			.setUpdateTime(LocalDateTime.now());
	}

	fun toEntity(): Draft {
		return Draft.builder()
			.id(id)
			.customerName(customerName)
			.companyName(companyName)
			.inChargeName(inChargeName)
			.email(email)
			.phoneNumber(phoneNumber)
			.categoryName(categoryName)
			.draftStatus(draftStatus)
			.address(address)
			.detailAddress(detailAddress)
			.price(price)
			.paymentMethod(paymentMethod)
			.memo(memo)
			.password(password)
			.createTime(createTime)
			.updateTime(createTime)
			.build()
	}

	fun getResources(path: String): List<Resource> {
		return fileMap.values
			.map { downloadFileUsingNIO(it, path) }
			.map { LocalFileUtil.multipartFileFrom(it) }
			.map { LocalResource(it) }
	}

	@Throws(IOException::class)
	private fun downloadFileUsingNIO(urlStr: String, path: String): java.io.File {
		val fileName = urlStr.substringAfterLast("/")
		val file = java.io.File("$path/$fileName")
		file.createNewFile()
		val fos = file.outputStream()
		val rbc = Channels.newChannel(URL(urlStr).openStream())
		fos.channel.transferFrom(rbc, 0, Long.MAX_VALUE)
		fos.close()
		rbc.close()
		return file
	}

}
