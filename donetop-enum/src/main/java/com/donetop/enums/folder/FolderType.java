package com.donetop.enums.folder;

import lombok.RequiredArgsConstructor;

import java.security.InvalidParameterException;

@RequiredArgsConstructor
public enum FolderType {
	DRAFT_ORDER("order"),
	DRAFT_WORK("work")
	;

	private final String path;

	public String buildPathFrom(final String base, final long id) {
		if (base == null || base.isEmpty()) throw new InvalidParameterException("Parameter 'base' can't be empty.");
		if (id < 1L) throw new InvalidParameterException("Parameter 'id' should be greater than 0L.");
		final int indexOfId = base.lastIndexOf(String.valueOf(id));
		if (indexOfId < 0) throw new IllegalStateException("Parameter 'id' should be contained at parameter 'base'");
		return addSlashIfAbsent(base.substring(0, indexOfId) + this.path) + id;
	}

	private String addSlashIfAbsent(final String path) {
		return path.endsWith("/") ? path : path + "/";
	}

}
