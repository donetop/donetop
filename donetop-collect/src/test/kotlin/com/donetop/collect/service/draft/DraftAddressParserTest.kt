package com.donetop.collect.service.draft

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DraftAddressParserTest {

	private val draftAddressParser = DraftAddressParser()

	@Test
	fun parse_with0spaceStr_detailShouldBeEmpty() {
		// given
		val fullAddress = "경기도"

		// when
		val address = draftAddressParser.addressFrom(fullAddress)

		// then
		assertThat(address.base).isEqualTo(fullAddress)
		assertThat(address.detail).isEmpty()
	}

	@Test
	fun parse_with1spaceStr_detailShouldBeEmpty() {
		// given
		val fullAddress = "경기도 수원시"

		// when
		val address = draftAddressParser.addressFrom(fullAddress)

		// then
		assertThat(address.base).isEqualTo(fullAddress)
		assertThat(address.detail).isEmpty()
	}

	@Test
	fun parse_with2spaceStr_detailShouldNotBeEmpty() {
		// given
		val fullAddress = "경기도 수원시 덕영대로1556번길"

		// when
		val address = draftAddressParser.addressFrom(fullAddress)

		// then
		assertThat(address.base).isEqualTo(fullAddress.split(" ").dropLast(2).joinToString(" "))
		assertThat(address.detail).isEqualTo(fullAddress.split(" ").drop(1).joinToString(" "))
	}

	@Test
	fun parse_with2moreSpaceStr_detailShouldNotBeEmpty() {
		// given
		val fullAddress = "경기도 수원시 덕영대로1556번길 16 F동 174호"

		// when
		val address = draftAddressParser.addressFrom(fullAddress)

		// then
		assertThat(address.base).isEqualTo(fullAddress.split(" ").dropLast(2).joinToString(" "))
		assertThat(address.detail).isEqualTo(fullAddress.split(" ").drop(4).joinToString(" "))
	}

}