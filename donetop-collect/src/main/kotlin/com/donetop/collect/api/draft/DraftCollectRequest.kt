package com.donetop.collect.api.draft

import com.donetop.collect.api.draft.DraftCollectRequest.Direction.ASC

data class DraftCollectRequest(
	val password: String,
	val startPageNum: Int,
	val endPageNum: Int,
	val refreshCookie: Boolean = true,
	val direction: Direction = ASC
) {

	init {
		require(password.isNotEmpty()) { "password shouldn't be empty." }
		require(startPageNum > 0) { "start page num should be greater than zero." }
		require(endPageNum > 0) { "end page num should be greater than zero." }
	}

	enum class Direction {
		ASC, DESC
	}

	fun getPageNumProgression(): IntProgression {
		val start = if (startPageNum <= endPageNum) startPageNum else endPageNum
		val end = if (startPageNum > endPageNum) startPageNum else endPageNum
		return if (direction == ASC) (start .. end) else (end downTo start)
	}

}