package com.donetop.main.service.sms;

public interface SmsSender {
	void sendSms(String to, String content);
}
