package com.donetop.main.api.enums;

import com.donetop.enums.draft.DraftStatus;
import com.donetop.enums.common.EnumDTO;
import com.donetop.enums.draft.PaymentMethod;
import com.donetop.main.api.common.Response.OK;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.donetop.main.api.enums.EnumAPIController.URI.*;

@RestController
public class EnumAPIController {

	public static class URI {
		public static final String ROOT = "/api/enum";
		public static final String DRAFT_STATUS = ROOT + "/draftStatus";
		public static final String PAYMENT_METHOD = ROOT + "/paymentMethod";
	}

	@GetMapping(value = DRAFT_STATUS)
	public ResponseEntity<OK<List<EnumDTO>>> draftStatus() {
		return ResponseEntity.ok(OK.of(DraftStatus.dtoList()));
	}

	@GetMapping(value = PAYMENT_METHOD)
	public ResponseEntity<OK<List<EnumDTO>>> paymentMethod() {
		return ResponseEntity.ok(OK.of(PaymentMethod.dtoList()));
	}

}
