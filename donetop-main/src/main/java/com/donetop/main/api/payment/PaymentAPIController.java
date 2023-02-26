package com.donetop.main.api.payment;

import com.donetop.main.api.common.Response;
import com.donetop.main.api.common.Response.OK;
import com.donetop.main.api.payment.request.NHNPaymentRequest;
import com.donetop.main.service.payment.NHNPaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.donetop.main.api.payment.PaymentAPIController.Uri.NHN;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
public class PaymentAPIController {

	public static class Uri {
		public static final String SINGULAR = "/api/payment";
		public static final String NHN = SINGULAR + "/nhn";
	}

	private final NHNPaymentService nhnPaymentService;

	@PostMapping(value = NHN)
	public ResponseEntity<Response> nhn(@Valid NHNPaymentRequest paymentRequest) throws Exception {
		return ResponseEntity.ok(OK.of(nhnPaymentService.payment(paymentRequest)));
	}

}
