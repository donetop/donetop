package com.donetop.oss.service.payment;

import com.donetop.domain.entity.payment.PaymentInfo;
import com.donetop.dto.payment.PaymentInfoDTO;
import com.donetop.enums.payment.Detail;
import com.donetop.enums.payment.PGType;
import com.donetop.oss.api.payment.request.PaymentCancelRequest;
import com.donetop.repository.payment.PaymentInfoRepository;
import com.querydsl.core.types.Predicate;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@Primary
@Transactional
public class PaymentServiceImpl implements PaymentService {

	private final PaymentInfoRepository paymentInfoRepository;

	private final Map<PGType, PaymentService> paymentServiceDelegatorMap;

	public PaymentServiceImpl(final PaymentInfoRepository paymentInfoRepository,
							  final NHNPaymentServiceImpl nhnPaymentService) {
		this.paymentInfoRepository = paymentInfoRepository;
		this.paymentServiceDelegatorMap = Map.ofEntries(
			Map.entry(PGType.NHN, nhnPaymentService)
		);
	}

	@Override
	public Page<PaymentInfoDTO> list(final Predicate predicate, final Pageable pageable) {
		return paymentInfoRepository.findAll(predicate, pageable).map(PaymentInfo::toDTO);
	}

	@Override
	public Detail cancel(final PaymentCancelRequest request) {
		final PaymentService paymentService = paymentServiceDelegatorMap.get(request.getPGType());
		if (paymentService == null) throw new IllegalStateException("PG 타입이 유효하지 않습니다.");
		return paymentService.cancel(request);
	}
}
