package com.donetop.enums.draft;

import com.donetop.enums.common.EnumDTO;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
public enum DraftStatus {
	HOLDING("대기중"),
	WORKING("시안작업중"),
	CHECK_REQUEST("시안확인요청"),
	PRINT_REQUEST("인쇄요청"),
	COMPLETED("출고완료"),
	;

	private final String value;

	public String value() {
		return this.value;
	}

	public EnumDTO toDTO() {
		return new EnumDTO(this.name(), this.value);
	}

	public static DraftStatus of(final String name) {
		for (final DraftStatus draftStatus : values()) {
			if (draftStatus.name().equals(name)) return draftStatus;
		}
		throw new IllegalArgumentException("There's no valid enum name for " + name);
	}

	public static List<EnumDTO> dtoList() {
		return Stream.of(values()).map(DraftStatus::toDTO).collect(Collectors.toList());
	}
}
