package com.donetop.main.api.sms;

import com.donetop.common.api.Response;
import com.donetop.common.api.Response.BadRequest;
import com.donetop.common.api.Response.OK;
import com.donetop.main.api.sms.request.VerificationCodeSendRequest;
import com.donetop.main.api.sms.request.VerificationCodeVerifyRequest;
import com.donetop.main.service.sms.PhoneVerificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.donetop.common.api.Message.*;
import static com.donetop.main.api.sms.PhoneVerificationController.URI.ENDPOINT;

@Slf4j
@Validated
@RestController
@RequestMapping(ENDPOINT)
@RequiredArgsConstructor
public class PhoneVerificationController {

	public static class URI {
		public static final String ENDPOINT = "/api/auth/phone";
	}

	private final PhoneVerificationService verificationService;

	@PostMapping("/send")
	public ResponseEntity<Response> sendVerificationCode(@Valid @RequestBody final VerificationCodeSendRequest request) {
		verificationService.sendVerificationCode(request.getPhoneNumber());
		return ResponseEntity.ok(OK.of(SMS_SEND_SUCCESS));
	}

	@PostMapping("/verify")
	public ResponseEntity<Response> verifyCode(@Valid @RequestBody final VerificationCodeVerifyRequest request) {
		boolean isValid = verificationService.verifyCode(request.getPhoneNumber(), request.getCode());
		if (isValid) {
			return ResponseEntity.ok(OK.of(SMS_VERIFY_SUCCESS));
		} else {
			return ResponseEntity.badRequest().body(BadRequest.of(SMS_VERIFY_FAIL));
		}
	}
}
