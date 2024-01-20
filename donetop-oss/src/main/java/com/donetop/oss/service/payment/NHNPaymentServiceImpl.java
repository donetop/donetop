package com.donetop.oss.service.payment;

import com.donetop.payment.nhn.NHN;
import com.donetop.domain.entity.payment.PaymentHistory;
import com.donetop.domain.entity.payment.PaymentInfo;
import com.donetop.dto.payment.PaymentHistoryDTO;
import com.donetop.dto.payment.PaymentInfoDTO;
import com.donetop.enums.payment.Detail;
import com.donetop.enums.payment.Detail.NHNCancelDetail;
import com.donetop.enums.payment.Detail.NHNPaidDetail;
import com.donetop.enums.payment.PGType;
import com.donetop.enums.payment.PaymentStatus;
import com.donetop.oss.api.payment.request.PaymentCancelRequest;
import com.donetop.repository.payment.PaymentHistoryRepository;
import com.donetop.repository.payment.PaymentInfoRepository;
import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.PrivateKey;
import java.time.LocalDateTime;
import java.util.Objects;

import static com.donetop.common.api.Message.DISALLOWED_REQUEST;
import static com.donetop.common.api.Message.UNKNOWN_PAYMENT_INFO_WITH_ARGUMENTS;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class NHNPaymentServiceImpl implements PaymentService {

	private final NHN nhn;

	private final PaymentInfoRepository paymentInfoRepository;

	private final PaymentHistoryRepository paymentHistoryRepository;

	@Override
	public Page<PaymentInfoDTO> list(final Predicate predicate, final Pageable pageable) {
		throw new UnsupportedOperationException(DISALLOWED_REQUEST);
	}

	@Override
	public Detail cancel(final PaymentCancelRequest request) {
		final long paymentInfoId = request.getPaymentInfoId();
		final PaymentInfo paymentInfo = paymentInfoRepository.findById(paymentInfoId)
			.orElseThrow(() -> new IllegalStateException(String.format(UNKNOWN_PAYMENT_INFO_WITH_ARGUMENTS, paymentInfoId)));
		final PaymentHistoryDTO lastHistoryDTO = Objects.requireNonNull(paymentInfo.toDTO().getLastHistory());
		final NHNPaidDetail nhnPaidDetail = (NHNPaidDetail) lastHistoryDTO.getDetail();
		final String tno = nhnPaidDetail.getTno();

		final PrivateKey privateKey = nhn.loadSplMctPrivateKeyPKCS8();
		final String targetData = String.format("%s^%s^%s", nhn.getSiteCD(), tno, "STSC");
		final String signData = nhn.makeSignatureData(privateKey, targetData);

		try {
			final JSONObject jsonObject = new JSONObject()
				.put("site_cd", nhn.getSiteCD())
				.put("kcp_cert_info", nhn.getCertData())
				.put("kcp_sign_data", signData)
				.put("mod_type", "STSC") // 전체 승인취소 - STSC / 부분취소 - STPC
				.put("tno", tno);

			String rawData = nhn.doRequest(nhn.getCancelURL(), jsonObject);
			final NHNCancelDetail nhnCancelDetail = (NHNCancelDetail) PGType.NHN.detail(PaymentStatus.CANCELED, rawData);

			if (nhnCancelDetail.isAlreadyCanceled() && nhnCancelDetail.getTno() == null) { // 가맹점 관리자 사이트에서 취소된 경우
				rawData = new JSONObject(rawData).put("tno", tno).toString();
				nhnCancelDetail.setTno(tno);
			}

			if (nhnCancelDetail.isSuccess() || nhnCancelDetail.isAlreadyCanceled()) {
				paymentInfo.setUpdateTime(LocalDateTime.now()).setLastTransactionNumber(nhnCancelDetail.getTno());
				final PaymentHistory paymentHistory = new PaymentHistory().toBuilder()
					.pgType(PGType.NHN)
					.paymentStatus(PaymentStatus.CANCELED)
					.rawData(rawData)
					.paymentInfo(paymentInfo).build();
				paymentHistoryRepository.save(paymentHistory);
				log.info("[UPDATE_PAYMENT_INFO] paymentInfoId: {}", paymentInfo.getId());
				log.info("[SAVE_PAYMENT_HISTORY] paymentHistoryId: {}", paymentHistory.getId());
			}

			return nhnCancelDetail;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
