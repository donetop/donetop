package com.donetop.main.service.payment;

import com.donetop.payment.nhn.NHN;
import com.donetop.domain.entity.draft.Draft;
import com.donetop.domain.entity.payment.PaymentHistory;
import com.donetop.domain.entity.payment.PaymentInfo;
import com.donetop.enums.payment.Detail.NHNPaidDetail;
import com.donetop.enums.payment.PGType;
import com.donetop.enums.payment.PaymentStatus;
import com.donetop.main.api.nhn.request.PaymentRequest;
import com.donetop.main.api.nhn.request.TradeRegisterRequest;
import com.donetop.main.api.nhn.response.TradeRegisterResponse;
import com.donetop.repository.draft.DraftRepository;
import com.donetop.repository.payment.PaymentHistoryRepository;
import com.donetop.repository.payment.PaymentInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class NHNPaymentServiceImpl implements NHNPaymentService {

	private final NHN nhn;

	private final DraftRepository draftRepository;

	private final PaymentInfoRepository paymentInfoRepository;

	private final PaymentHistoryRepository paymentHistoryRepository;

	@Override
	public NHNPaidDetail payment(final PaymentRequest request) throws Exception {
		final JSONObject jsonObject = request.toJsonObject()
			.put("kcp_cert_info", nhn.getCertData());
		return savePaymentHistory(nhn.doRequest(nhn.getPaymentURL(), jsonObject));
	}

	@Override
	public TradeRegisterResponse tradeRegister(final TradeRegisterRequest request) throws Exception {
		final JSONObject jsonObject = request.toJsonObject()
			.put("kcp_cert_info", nhn.getCertData());
		return TradeRegisterResponse.of(nhn.doRequest(nhn.getTradeRegisterURL(), jsonObject));
	}

	private NHNPaidDetail savePaymentHistory(final String rawData) {
		final NHNPaidDetail nhnPaidDetail = (NHNPaidDetail) PGType.NHN.detail(PaymentStatus.PAID, rawData);
		final Long draftId = Long.valueOf(nhnPaidDetail.getOrder_no().split("-")[1]);
		final Draft draft = draftRepository.findById(draftId)
			.orElseThrow(() -> new IllegalStateException("결제 정보 저장을 위한 유효한 시안이 없습니다."));
		final PaymentInfo paymentInfo = draft.getOrNewPaymentInfo().setLastTransactionNumber(nhnPaidDetail.getTno());
		if (paymentInfo.isNew()) {
			paymentInfoRepository.save(paymentInfo);
			draft.addPaymentInfo(paymentInfo);
			log.info("[SAVE_PAYMENT_INFO] paymentInfoId: {}", paymentInfo.getId());
		} else {
			paymentInfo.setUpdateTime(LocalDateTime.now());
			log.info("[UPDATE_PAYMENT_INFO] paymentInfoId: {}", paymentInfo.getId());
		}
		final PaymentHistory paymentHistory = new PaymentHistory().toBuilder()
			.pgType(PGType.NHN)
			.paymentStatus(PaymentStatus.PAID)
			.rawData(rawData)
			.paymentInfo(paymentInfo).build();
		paymentHistoryRepository.save(paymentHistory);
		log.info("[SAVE_PAYMENT_HISTORY] paymentHistoryId: {}", paymentHistory.getId());
		return nhnPaidDetail;
	}

}
