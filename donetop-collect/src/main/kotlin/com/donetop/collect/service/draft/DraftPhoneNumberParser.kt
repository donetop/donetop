package com.donetop.collect.service.draft

import org.springframework.stereotype.Component

@Component
class DraftPhoneNumberParser {

	val default = "010-1234-1234"

	fun phoneNumberFrom(phoneNumber: String): String {
		if (phoneNumber.isEmpty()) return default
		val digits = phoneNumber.filter { it.isDigit() }
		if (digits.length != 11) return default
		return digits.substring(0, 3) + "-" + digits.substring(3, 7) + "-" + digits.substring(7)
	}

}