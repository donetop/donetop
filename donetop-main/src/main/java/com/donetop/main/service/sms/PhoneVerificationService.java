package com.donetop.main.service.sms;

import com.donetop.main.service.sms.exception.SmsVerificationException;
import com.donetop.main.service.sms.repository.VerificationCodeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

import static com.donetop.common.api.Message.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class PhoneVerificationService {

	private final SmsSender smsSender;
	private final VerificationCodeRepository codeRepository;
	private static final long VERIFICATION_TTL = 180L;
	private static final long VERIFIED_FLAG_TTL = 300L;
	private static final int MAX_DAILY_REQUEST_LIMIT = 5;

	public void sendVerificationCode(String phoneNumber) {
		Long currentCount = codeRepository.incrementDailyCount(phoneNumber);
		if (currentCount > MAX_DAILY_REQUEST_LIMIT) {
			log.error("Exceeded the maximum allowed daily count for phoneNumber: {}", phoneNumber);
			throw new SmsVerificationException(SMS_SEND_LIMIT_EXCEEDED);
		}

		String code = generateCode();
		codeRepository.save(phoneNumber, code, VERIFICATION_TTL);

		try {
			String messageText = String.format(SMS_CONTENT_TEMPLATE, code);
			smsSender.sendSms(phoneNumber, messageText);
			log.info("Send verification code {} to {}", code, phoneNumber);
		} catch (Exception e) {
			throw new SmsVerificationException(SMS_SEND_FAIL, e);
		}
	}

	public boolean verifyCode(String phoneNumber, String inputCode) {
		String savedCode = codeRepository.get(phoneNumber);

		if (savedCode == null) {
			throw new SmsVerificationException(SMS_CODE_EXPIRED);
		}

		if (savedCode.equals(inputCode)) {
			codeRepository.remove(phoneNumber);
			codeRepository.saveVerifiedFlag(phoneNumber, VERIFIED_FLAG_TTL);
			return true;
		}
		return false;
	}

	public boolean checkAndConsumeVerification(String phoneNumber) {
		if (phoneNumber == null || phoneNumber.trim().isEmpty()) return false;

		if (codeRepository.existsVerifiedFlag(phoneNumber)) {
			codeRepository.removeVerifiedFlag(phoneNumber);
			return true;
		}
		return false;
	}

	private String generateCode() {
		SecureRandom random = new SecureRandom();
		return String.format("%06d", random.nextInt(1000000));
	}
}
