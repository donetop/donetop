package com.donetop.batch.service.draft

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DraftPhoneNumberParserTest {

	private val draftPhoneNumberParser = DraftPhoneNumberParser()

	@Test
	fun parse_withEmptyStr_shouldDefault() {
		// given
		val phoneNumber = ""

		// when
		val parsedPhoneNumber = draftPhoneNumberParser.phoneNumberFrom(phoneNumber)

		// then
		assertThat(parsedPhoneNumber).isEqualTo(draftPhoneNumberParser.default)
	}

	@Test
	fun parse_with10DigitsStr_shouldDefault() {
		// given
		val phoneNumber = "010-123-1234"

		// when
		val parsedPhoneNumber = draftPhoneNumberParser.phoneNumberFrom(phoneNumber)

		// then
		assertThat(parsedPhoneNumber).isEqualTo(draftPhoneNumberParser.default)
	}

	@Test
	fun parse_with12DigitsStr_shouldDefault() {
		// given
		val phoneNumber = "0101-1234-1234"

		// when
		val parsedPhoneNumber = draftPhoneNumberParser.phoneNumberFrom(phoneNumber)

		// then
		assertThat(parsedPhoneNumber).isEqualTo(draftPhoneNumberParser.default)
	}

	@Test
	fun parse_with11DigitsStr_shouldNotDefault() {
		// given
		val phoneNumber = "010-1111-1111"

		// when
		val parsedPhoneNumber = draftPhoneNumberParser.phoneNumberFrom(phoneNumber)

		// then
		assertThat(parsedPhoneNumber).isNotEqualTo(draftPhoneNumberParser.default)
	}

}