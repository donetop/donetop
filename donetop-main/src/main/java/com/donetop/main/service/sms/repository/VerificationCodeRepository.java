package com.donetop.main.service.sms.repository;

public interface VerificationCodeRepository {
	void save(String phoneNumber, String code, long durationInSeconds);
	String get(String phoneNumber);
	void remove(String phoneNumber);
	Long incrementDailyCount(String key);
}
