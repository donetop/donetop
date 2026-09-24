package com.donetop.main.service.sms;

import com.solapi.sdk.SolapiClient;
import com.solapi.sdk.message.exception.SolapiEmptyResponseException;
import com.solapi.sdk.message.exception.SolapiMessageNotReceivedException;
import com.solapi.sdk.message.exception.SolapiUnknownException;
import com.solapi.sdk.message.model.Message;
import com.solapi.sdk.message.service.DefaultMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
@Component
@ConditionalOnProperty(name = "sms.provider", havingValue = "solapi")
public class SolapiSmsSender implements SmsSender {

	@Value("${solapi.api-key}")
	private String apiKey;

	@Value("${solapi.api-secret}")
	private String apiSecret;

	@Value("${solapi.sender-number}")
	private String senderNumber;

	private DefaultMessageService messageService;

	@PostConstruct
	public void init() {
		this.messageService = SolapiClient.INSTANCE.createInstance(apiKey, apiSecret);
		log.info("SolapiSmsSender is initialized.");
	}

	@Override
	public void sendSms(String to, String content) {
		Message message = new Message();
		message.setFrom(senderNumber);
		message.setTo(to.replace("-", ""));
		message.setText(content);
		try {
			this.messageService.send(message);
		} catch (SolapiMessageNotReceivedException | SolapiEmptyResponseException | SolapiUnknownException e) {
			throw new RuntimeException(e);
		}
	}
}
