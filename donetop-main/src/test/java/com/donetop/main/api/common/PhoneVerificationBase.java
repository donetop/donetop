package com.donetop.main.api.common;

import com.donetop.main.service.sms.SmsSender;
import com.donetop.main.service.sms.repository.VerificationCodeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;

public class PhoneVerificationBase extends IntegrationBase {

	@MockBean
	protected SmsSender smsSender;

	@Autowired
	protected VerificationCodeRepository verificationCodeRepository;

	@BeforeEach
	void setUpPhoneVerificationBase() {
		doNothing().when(smsSender).sendSms(anyString(), anyString());
	}

}
