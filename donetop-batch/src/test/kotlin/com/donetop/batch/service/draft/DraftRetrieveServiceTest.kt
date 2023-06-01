package com.donetop.batch.service.draft

import com.donetop.batch.properties.ApplicationProperties
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

@ActiveProfiles("test")
@ExtendWith(SpringExtension::class)
@ContextConfiguration(initializers = [ConfigDataApplicationContextInitializer::class])
@EnableConfigurationProperties(value = [ApplicationProperties::class])
class DraftRetrieveServiceTest {

	@Autowired
	private lateinit var applicationProperties: ApplicationProperties

	private lateinit var draftRetrieveService: DraftRetrieveService

	@PostConstruct
	fun postConstruct() {
		val draftParseService = DraftParseService(DraftAddressParser(), applicationProperties)
		draftRetrieveService = DraftRetrieveService(applicationProperties, draftParseService)
		draftRetrieveService.refreshCookieHeader()
	}

		@Test
    fun getDraftList_withFirstPage_returnValidSizeList() {
		// given & when
		val elements = draftRetrieveService.getDraftList(0)

		// then
		assertThat(elements.size).isEqualTo(20)
	}

	@Test
	fun getEndPageNum_withNothing_returnValidPageNum() {
		// given & when
		val endPageNum = draftRetrieveService.getEndPageNum()

		// then
		assertThat(endPageNum).isGreaterThan(0)
	}

}