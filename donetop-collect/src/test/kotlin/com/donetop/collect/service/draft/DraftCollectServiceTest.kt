package com.donetop.collect.service.draft

import com.donetop.collect.api.draft.DraftCollectRequest
import com.donetop.collect.properties.ApplicationProperties
import com.donetop.common.Profile
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.util.FileSystemUtils
import java.nio.file.Path

@SpringBootTest
@ActiveProfiles(Profile.TEST, Profile.DEVELOPMENT)
class DraftCollectServiceTest {

	@Autowired
	private lateinit var draftCollectService: DraftCollectService

	@Autowired
	private lateinit var applicationProperties: ApplicationProperties

	@AfterEach
	fun afterEach() {
		FileSystemUtils.deleteRecursively(Path.of(applicationProperties.storage.root))
	}

	@Test
	fun collect_withInvalidPassword_error() {
		// given
		val draftCollectRequest = DraftCollectRequest(
			password = "invalid",
			startPageNum = 1,
			endPageNum = 1
		)

		// when & then
		assertThrows<IllegalStateException> { draftCollectService.collect(draftCollectRequest) }
	}

	@Test
	fun collect_withInvalidStartPageNum_error() {
		// given
		val draftCollectRequest = DraftCollectRequest(
			password = applicationProperties.collectPassword,
			startPageNum = 123456,
			endPageNum = 1
		)

		// when & then
		assertThrows<IllegalStateException> { draftCollectService.collect(draftCollectRequest) }
	}

	@Test
	fun collect_withInvalidEndPageNum_error() {
		// given
		val draftCollectRequest = DraftCollectRequest(
			password = applicationProperties.collectPassword,
			startPageNum = 1,
			endPageNum = 123456
		)

		// when & then
		assertThrows<IllegalStateException> { draftCollectService.collect(draftCollectRequest) }
	}

	@Test
	fun collect_withValidRequest_success() {
		// given
		val draftCollectRequest = DraftCollectRequest(
			password = applicationProperties.collectPassword,
			startPageNum = 1,
			endPageNum = 1
		)

		// when
		val result = draftCollectService.collect(draftCollectRequest)

		// then
		assertThat(result.saveCount).isEqualTo(20)
		assertThat(result.updateCount).isEqualTo(0)
	}

}