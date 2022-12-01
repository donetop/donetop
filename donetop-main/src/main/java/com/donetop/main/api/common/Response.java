package com.donetop.main.api.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public abstract class Response {

	private final HttpStatus status;

	private final int code;

	@Getter
	public static class BadRequest<Reason> extends Response {

		private final Reason reason;

		public BadRequest(final Reason reason) {
			super(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value());
			this.reason = reason;
		}

		public static <Reason> BadRequest<Reason> of(final Reason reason) {
			return new BadRequest<>(reason);
		}
	}

	@Getter
	public static class OK<Data> extends Response {

		private final Data data;

		public OK(final Data data) {
			super(HttpStatus.OK, HttpStatus.OK.value());
			this.data = data;
		}

		public static <Data> OK<Data> of(final Data data) {
			return new OK<>(data);
		}
	}

}
