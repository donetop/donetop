package com.donetop.collect.api.draft

import com.donetop.collect.api.draft.DraftCollectRequest.Direction.DESC
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class DraftCollectRequestTest {

	@Test
	fun initRequest_withEmptyPassword_error() {
		assertThrows<IllegalArgumentException> { DraftCollectRequest("", 1, 1) }
	}

	@Test
	fun initRequest_withMinusStartPageNum_error() {
		assertThrows<IllegalArgumentException> { DraftCollectRequest("test", -1, 1) }
	}

	@Test
	fun initRequest_withMinusEndPageNum_error() {
		assertThrows<IllegalArgumentException> { DraftCollectRequest("test", 1, -1) }
	}

	@Test
	fun getPageNumProgression_withSameStartAndEndPage_shouldHaveSameFirstAndLast() {
		val pageNumProgression = DraftCollectRequest("test", 1, 1).getPageNumProgression()
		assertThat(pageNumProgression.first).isEqualTo(pageNumProgression.last)
	}

	@Test
	fun getPageNumProgression_withASC_shouldHaveDifferentFirstAndLast() {
		val pageNumProgression = DraftCollectRequest("test", 2, 1).getPageNumProgression()
		assertThat(pageNumProgression.last).isGreaterThan(pageNumProgression.first)
	}

	@Test
	fun getPageNumProgression_withDESC_shouldHaveDifferentFirstAndLast() {
		val pageNumProgression = DraftCollectRequest("test", 2, 1, direction = DESC).getPageNumProgression()
		assertThat(pageNumProgression.first).isGreaterThan(pageNumProgression.last)
	}

}