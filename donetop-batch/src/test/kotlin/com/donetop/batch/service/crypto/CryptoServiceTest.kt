package com.donetop.batch.service.crypto

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class CryptoServiceTest {

	private val noOpCryptoService = NoOpCryptoService()
	private val sha256CryptoService = SHA256CryptoService()

	@Test
	fun encrypt_withNoOpCryptoService_returnSameText() {
		// given
		val text = "password"

		// when & then
		assertThat(noOpCryptoService.encrypt(text)).isEqualTo(text)
	}

	@Test
	fun encrypt_withSHA256CryptoService_returnEncryptedText() {
		// given
		val text = "password"

		// when & then
		assertThat(sha256CryptoService.encrypt(text)).isEqualTo("XohImNooBHFR0OVvjcYpJ3NgPQ1qq73WKhHvch0VQtg")
	}

}