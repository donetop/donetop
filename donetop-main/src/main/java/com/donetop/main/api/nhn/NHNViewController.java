package com.donetop.main.api.nhn;

import com.donetop.main.api.nhn.request.PageReturnRequest;
import com.donetop.main.service.payment.NHNPaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

import static com.donetop.main.api.nhn.URI.*;

@Validated
@Controller
@RequiredArgsConstructor
public class NHNViewController {

	private final NHNPaymentService nhnPaymentService;

	@PostMapping(value = PAGE_RETURN)
	public String pageReturn(@Valid final PageReturnRequest pageReturnRequest) throws Exception {
		nhnPaymentService.payment(pageReturnRequest);
		return "redirect:" + pageReturnRequest.getParam_opt_1();
	}

}
