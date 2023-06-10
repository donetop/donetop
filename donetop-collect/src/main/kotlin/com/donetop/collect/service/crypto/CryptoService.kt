package com.donetop.collect.service.crypto

import com.donetop.common.Profile.AWS
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.util.*

interface CryptoService {
	fun encrypt(text: String): String
}

@Profile("!$AWS")
@Service
class NoOpCryptoService: CryptoService {

	override fun encrypt(text: String): String {
		return text
	}

}

@Profile(AWS)
@Service
class SHA256CryptoService: CryptoService {

	override fun encrypt(text: String): String {
		val messageDigest = MessageDigest.getInstance("SHA-256")
		messageDigest.update(text.toByteArray(StandardCharsets.UTF_8))
		return bytesToBase64Url(messageDigest.digest())
	}

	private fun bytesToBase64Url(bytes: ByteArray): String {
		return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes)
	}

}