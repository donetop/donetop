package com.donetop.main.api.nhn;

import com.donetop.main.api.common.Response;
import com.donetop.main.api.common.Response.OK;
import com.donetop.main.api.nhn.request.PageReturnRequest;
import com.donetop.main.api.nhn.request.PaymentRequest;
import com.donetop.main.api.nhn.request.TradeRegisterRequest;
import com.donetop.main.service.payment.NHNPaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.donetop.main.api.nhn.NHNAPIController.Uri.*;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
public class NHNAPIController {

	public static class Uri {
		public static final String NHN = "/api/nhn";
		public static final String PAYMENT = NHN + "/payment";
		public static final String TRADE_REGISTER = NHN + "/trade/register";
		public static final String PAGE_RETURN = NHN + "/page/return";
	}

	private final NHNPaymentService nhnPaymentService;

	@PostMapping(value = PAYMENT)
	public ResponseEntity<Response> payment(@Valid final PaymentRequest paymentRequest) throws Exception {
		return ResponseEntity.ok(OK.of(nhnPaymentService.payment(paymentRequest)));
	}

	@PostMapping(value = TRADE_REGISTER)
	public ResponseEntity<Response> tradeRegister(@Valid @RequestBody final TradeRegisterRequest tradeRegisterRequest) throws Exception {
		return ResponseEntity.ok(OK.of(nhnPaymentService.tradeRegister(tradeRegisterRequest)));
	}

	@PostMapping(value = PAGE_RETURN)
	public ResponseEntity<Response> pageReturn(final PageReturnRequest pageReturnRequest) {
		return ResponseEntity.ok(OK.of(pageReturnRequest));
	}

}
