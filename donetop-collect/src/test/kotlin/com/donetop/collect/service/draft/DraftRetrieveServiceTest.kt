package com.donetop.collect.service.draft

import com.donetop.collect.properties.ApplicationProperties
import com.donetop.collect.service.crypto.NoOpCryptoService
import com.donetop.common.Profile.DEVELOPMENT
import com.donetop.common.Profile.TEST
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import javax.annotation.PostConstruct

@ActiveProfiles(TEST, DEVELOPMENT)
@ExtendWith(SpringExtension::class)
@ContextConfiguration(initializers = [ConfigDataApplicationContextInitializer::class])
@EnableConfigurationProperties(value = [ApplicationProperties::class])
class DraftRetrieveServiceTest {

	@Autowired
	private lateinit var applicationProperties: ApplicationProperties

	private lateinit var draftRetrieveService: DraftRetrieveService

	@PostConstruct
	fun postConstruct() {
		val draftParseService = DraftParseService(DraftPhoneNumberParser(), DraftAddressParser(), NoOpCryptoService(), applicationProperties)
		draftRetrieveService = DraftRetrieveService(applicationProperties, draftParseService)
		draftRetrieveService.refreshCookieHeader()
	}

	@Test
    fun getDraftDTOList_withFirstPage_returnValidSizeList() {
		// given & when
		val draftDTOList = draftRetrieveService.getDraftDTOList(1)

		// then
		assertThat(draftDTOList.size).isEqualTo(20)
	}

	@Test
	fun getEndPageNum_withNothing_returnValidPageNum() {
		// given & when
		val endPageNum = draftRetrieveService.getEndPageNum()

		// then
		assertThat(endPageNum).isGreaterThan(0)
	}

}