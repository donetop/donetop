package com.donetop.main.api.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public abstract class Response {

	private HttpStatus status;

	private int code;

	@Getter
	@NoArgsConstructor
	public static class BadRequest<Reason> extends Response {

		private Reason reason;

		public BadRequest(final Reason reason) {
			super(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value());
			this.reason = reason;
		}

		public static <Reason> BadRequest<Reason> of(final Reason reason) {
			return new BadRequest<>(reason);
		}
	}

	@Getter
	@NoArgsConstructor
	public static class OK<Data> extends Response {

		private Data data;

		public OK(final Data data) {
			super(HttpStatus.OK, HttpStatus.OK.value());
			this.data = data;
		}

		public static <Data> OK<Data> of(final Data data) {
			return new OK<>(data);
		}
	}

}
