package com.donetop.common.api;

import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class Message {

	public static final String UNKNOWN_DRAFT = "존재하지 않는 시안입니다.";
	public static final String UNKNOWN_DRAFT_WITH_ARGUMENTS = UNKNOWN_DRAFT + " id: %s";
	public static final String SAME_DRAFT_VALUE = "이전과 동일한 값으로 변경할 수 없습니다.";
	public static final String UNKNOWN_COMMENT = "존재하지 않는 댓글입니다.";
	public static final String UNKNOWN_COMMENT_WITH_ARGUMENTS = UNKNOWN_COMMENT + " id: %s";
	public static final String UNKNOWN_CATEGORY = "존재하지 않는 카테고리입니다.";
	public static final String UNKNOWN_CATEGORY_WITH_ARGUMENTS = UNKNOWN_CATEGORY + " id: %s";
	public static final String DUPLICATED_CATEGORY_NAME = "동일한 이름의 카테고리가 이미 존재합니다.";
	public static final String UNKNOWN_FILE = "존재하지 않는 파일입니다.";
	public static final String UNKNOWN_FILE_WITH_ARGUMENTS = UNKNOWN_FILE + " id: %s";
	public static final String DUPLICATED_FILE = "동일한 파일이 이미 존재합니다.";
	public static final String UNKNOWN_CUSTOMER_POST = "존재하지 않는 고객 게시물입니다.";
	public static final String UNKNOWN_CUSTOMER_POST_WITH_ARGUMENTS = UNKNOWN_CUSTOMER_POST + " id: %s";
	public static final String UNKNOWN_PAYMENT_INFO = "존재하지 않는 결제 정보입니다.";
	public static final String UNKNOWN_PAYMENT_INFO_WITH_ARGUMENTS = UNKNOWN_PAYMENT_INFO + " id: %s";
	public static final String UNKNOWN_NOTICE = "존재하지 않는 공지사항 정보입니다.";
	public static final String UNKNOWN_NOTICE_WITH_ARGUMENTS = UNKNOWN_NOTICE + " id: %s";
	public static final String WRONG_ID_OR_PASSWORD = "아이디 또는 비밀번호가 일치하지 않습니다.";
	public static final String WRONG_USERNAME = "유저 이름이 일치하지 않습니다.";
	public static final String WRONG_PASSWORD = "비밀번호가 일치하지 않습니다.";
	public static final String DISALLOWED_REQUEST = "허용되지 않은 요청입니다.";
	public static final String NO_SESSION = "유효한 세션 정보가 없습니다.";
	public static final String NO_USER = "유효한 유저 정보가 없습니다.";
	public static final String FILE_SIZE_EXCEED = "파일 크기가 허용 가능한 범위를 초과했습니다. (파일당 최대 5MB)";
	public static final String INSUFFICIENT_AUTHENTICATION = "유저 이름 및 비밀번호를 모두 입력해주세요.";
	public static final String LOGOUT = "로그 아웃 성공.";
	public static final String ASK_ADMIN = "에러가 발생했습니다. 관리자에게 문의해주세요.";

	public static List<String> getFieldValues() {
		return Stream.of(Message.class.getDeclaredFields()).
			map(f -> {
				try {
					return (String) f.get(Message.class);
				} catch (IllegalAccessException e) {
					throw new RuntimeException(e);
				}
			}).collect(toList());
	}

}
