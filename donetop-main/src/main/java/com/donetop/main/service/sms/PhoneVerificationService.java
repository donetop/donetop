package com.donetop.main.service.sms;

import com.donetop.main.service.sms.exception.SmsVerificationException;
import com.donetop.main.service.sms.repository.VerificationCodeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Slf4j
@Service
@RequiredArgsConstructor
public class PhoneVerificationService {

	private final SmsSender smsSender;
	private final VerificationCodeRepository codeRepository;
	private static final long VERIFICATION_TTL = 180L;
	private static final int MAX_DAILY_REQUEST_LIMIT = 5;

	public void sendVerificationCode(String phoneNumber) {
		Long currentCount = codeRepository.incrementDailyCount(phoneNumber);
		if (currentCount > MAX_DAILY_REQUEST_LIMIT) {
			log.error("Exceeded the maximum allowed daily count for phoneNumber: {}", phoneNumber);
			throw new SmsVerificationException("오늘 인증번호 요청 횟수(5회)를 초과했습니다. 내일 다시 시도해주세요.");
		}

		String code = generateCode();
		codeRepository.save(phoneNumber, code, VERIFICATION_TTL);

		try {
			String messageText = String.format("[DONETOP] 휴대폰 인증번호 [%s]를 입력해주세요.", code);
			smsSender.sendSms(phoneNumber, messageText);
			log.info("Send verification code {} to {}", code, phoneNumber);
		} catch (Exception e) {
			throw new SmsVerificationException("SMS 발송 중 오류가 발생했습니다. 잠시 후 다시 시도해주세요.", e);
		}
	}

	public boolean verifyCode(String phoneNumber, String inputCode) {
		String savedCode = codeRepository.get(phoneNumber);

		if (savedCode != null && savedCode.equals(inputCode)) {
			codeRepository.remove(phoneNumber);
			return true;
		}
		return false;
	}

	private String generateCode() {
		SecureRandom random = new SecureRandom();
		return String.format("%06d", random.nextInt(1000000));
	}
}
