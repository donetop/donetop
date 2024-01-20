package com.donetop.oss.api.payment;

import com.donetop.common.api.Response;
import com.donetop.common.api.Response.OK;
import com.donetop.domain.entity.payment.PaymentInfo;
import com.donetop.dto.payment.PaymentInfoDTO;
import com.donetop.oss.api.payment.request.PaymentCancelRequest;
import com.donetop.oss.service.payment.PaymentService;
import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.donetop.oss.api.payment.PaymentAPIController.URI.CANCEL;
import static com.donetop.oss.api.payment.PaymentAPIController.URI.PLURAL;
import static org.springframework.data.domain.Sort.Direction.DESC;

@Validated
@RestController
@RequiredArgsConstructor
public class PaymentAPIController {

	public static class URI {
		public static final String SINGULAR = "/api/payment";
		public static final String PLURAL = "/api/payments";
		public static final String CANCEL = SINGULAR + "/cancel";
	}

	private final PaymentService paymentService;

	@GetMapping(value = PLURAL)
	public ResponseEntity<OK<Page<PaymentInfoDTO>>> get(@PageableDefault(sort = {"updateTime"}, direction = DESC) Pageable pageable,
														@QuerydslPredicate(root = PaymentInfo.class) final Predicate predicate) {
		return ResponseEntity.ok(OK.of(paymentService.list(predicate, pageable)));
	}

	@PostMapping(value = CANCEL)
	public ResponseEntity<Response> cancel(@Valid @RequestBody final PaymentCancelRequest request) {
		return ResponseEntity.ok(OK.of(paymentService.cancel(request)));
	}

}
