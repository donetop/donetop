package com.donetop.enums.draft;

public enum DraftStatus {
	HOLDING,
	WORKING,
	CHECK_REQUEST,
	PRINT_REQUEST,
	COMPLETED,
	;

	public static DraftStatus of(final String value) {
		for (final DraftStatus draftStatus : values()) {
			if (draftStatus.toString().equals(value)) return draftStatus;
		}
		throw new IllegalArgumentException("There's no valid enum value for " + value);
	}
}
