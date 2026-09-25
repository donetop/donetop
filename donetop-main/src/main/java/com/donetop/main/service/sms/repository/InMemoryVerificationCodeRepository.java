package com.donetop.main.service.sms.repository;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
@ConditionalOnProperty(name = "sms.storage", havingValue = "local")
public class InMemoryVerificationCodeRepository implements VerificationCodeRepository {

	private final Map<String, String> code_store = new ConcurrentHashMap<>();
	private final Map<String, AtomicLong> count_store = new ConcurrentHashMap<>();
	private final Map<String, Long> verified_flag_store = new ConcurrentHashMap<>();

	@Override
	public void save(String phoneNumber, String code, long durationInSeconds) {
		code_store.put(phoneNumber, code);
	}

	@Override
	public String get(String phoneNumber) {
		return code_store.get(phoneNumber);
	}

	@Override
	public void remove(String phoneNumber) {
		code_store.remove(phoneNumber);
	}

	@Override
	public Long incrementDailyCount(String phoneNumber) {
		AtomicLong count = count_store.computeIfAbsent(phoneNumber, k -> new AtomicLong(0));
		return count.incrementAndGet();
	}

	@Override
	public void saveVerifiedFlag(String phoneNumber, long ttlInSeconds) {
		long expireAt = System.currentTimeMillis() + (ttlInSeconds * 1000);
		verified_flag_store.put(phoneNumber, expireAt);
	}

	@Override
	public boolean existsVerifiedFlag(String phoneNumber) {
		Long expireAt = verified_flag_store.get(phoneNumber);
		if (expireAt == null) {
			return false;
		}
		// TTL 만료 여부 확인
		if (System.currentTimeMillis() > expireAt) {
			verified_flag_store.remove(phoneNumber);
			return false;
		}
		return true;
	}

	@Override
	public void removeVerifiedFlag(String phoneNumber) {
		verified_flag_store.remove(phoneNumber);
	}
}
