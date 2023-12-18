package com.donetop.enums.folder;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.security.InvalidParameterException;

@Getter
@RequiredArgsConstructor
public enum DomainType {
	DRAFT("draft/{id}"),
	CATEGORY("category/{id}"),
	DRAFT_COMMENT("draft/comment/{id}"),
	NOTICE("notice/{id}")
	;

	private final String path;

	public String buildPathFrom(final String base, final long id) {
		if (base == null || base.isEmpty()) throw new InvalidParameterException("Parameter 'base' can't be empty.");
		if (id < 1L) throw new InvalidParameterException("Parameter 'id' should be greater than 0L.");
		return addSlashIfAbsent(base) + this.path.replace("{id}", String.valueOf(id));
	}

	private String addSlashIfAbsent(final String path) {
		return path.endsWith("/") ? path : path + "/";
	}

}
