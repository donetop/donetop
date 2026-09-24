package com.donetop.main.api.sms;

import com.donetop.common.api.Response;
import com.donetop.common.api.Response.BadRequest;
import com.donetop.main.service.sms.exception.SmsVerificationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice(assignableTypes = com.donetop.main.api.sms.PhoneVerificationController.class)
public class SmsExceptionHandler {

	@ExceptionHandler(SmsVerificationException.class)
	public ResponseEntity<Response> handleSmsVerificationException(SmsVerificationException e) {
		log.warn("SmsVerificationException occurred: {}", e.getMessage());
		return ResponseEntity.badRequest().body(BadRequest.of(e.getMessage()));
	}
}
