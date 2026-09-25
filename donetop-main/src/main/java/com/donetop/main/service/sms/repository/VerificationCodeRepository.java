package com.donetop.main.service.sms.repository;

public interface VerificationCodeRepository {
	void save(String phoneNumber, String code, long durationInSeconds);
	String get(String phoneNumber);
	void remove(String phoneNumber);
	Long incrementDailyCount(String key);
	void saveVerifiedFlag(String phoneNumber, long ttlInSeconds);
	boolean existsVerifiedFlag(String phoneNumber);
	void removeVerifiedFlag(String phoneNumber);
}
