package com.donetop.enums.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RoleType {

	NORMAL("ROLE_NORMAL"),
	ADMIN("ROLE_ADMIN");

	private final String role;
}
