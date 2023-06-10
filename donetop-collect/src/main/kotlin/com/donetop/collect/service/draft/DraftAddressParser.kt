package com.donetop.collect.service.draft

import org.springframework.stereotype.Component

@Component
class DraftAddressParser {

	data class Address(val base: String, val detail: String)

	fun addressFrom(fullAddress: String): Address {
		val words = fullAddress.split(" ")
		return if (words.size > 2) Address(words.dropLast(2).joinToString(" "), words.drop(words.size - 2).joinToString(" "))
		else Address(words.joinToString(" "), "")
	}

}