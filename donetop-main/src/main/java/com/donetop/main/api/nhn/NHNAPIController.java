package com.donetop.main.api.nhn;

import com.donetop.common.api.Response;
import com.donetop.common.api.Response.OK;
import com.donetop.main.api.nhn.request.PaymentRequest;
import com.donetop.main.api.nhn.request.TradeRegisterRequest;
import com.donetop.main.service.payment.NHNPaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.donetop.main.api.nhn.URI.*;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
public class NHNAPIController {

	private final NHNPaymentService nhnPaymentService;

	@PostMapping(value = PAYMENT)
	public ResponseEntity<Response> payment(@Valid final PaymentRequest paymentRequest) throws Exception {
		return ResponseEntity.ok(OK.of(nhnPaymentService.payment(paymentRequest)));
	}

	@PostMapping(value = TRADE_REGISTER)
	public ResponseEntity<Response> tradeRegister(@Valid @RequestBody final TradeRegisterRequest tradeRegisterRequest) throws Exception {
		return ResponseEntity.ok(OK.of(nhnPaymentService.tradeRegister(tradeRegisterRequest)));
	}

}
