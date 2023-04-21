package com.donetop.oss.service.payment;

import com.donetop.dto.payment.PaymentInfoDTO;
import com.donetop.enums.payment.Detail;
import com.donetop.oss.api.payment.request.PaymentCancelRequest;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface PaymentService {

	Page<PaymentInfoDTO> list(Predicate predicate, PageRequest request);

	Detail cancel(PaymentCancelRequest request);

}
