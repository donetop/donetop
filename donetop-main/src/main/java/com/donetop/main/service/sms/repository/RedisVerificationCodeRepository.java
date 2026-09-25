package com.donetop.main.service.sms.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
@ConditionalOnProperty(name = "sms.storage", havingValue = "redis", matchIfMissing = true)
public class RedisVerificationCodeRepository implements VerificationCodeRepository {

	private final StringRedisTemplate redisTemplate;
	private static final String CODE_PREFIX = "sms:auth:";
	private static final String COUNT_PREFIX = "sms:count:";
	private static final String VERIFIED_PREFIX = "sms:verified_phone:";

	@Override
	public void save(String phoneNumber, String code, long durationInSeconds) {
		redisTemplate.opsForValue().set(
			CODE_PREFIX + phoneNumber,
			code,
			durationInSeconds,
			TimeUnit.SECONDS
		);
	}

	@Override
	public String get(String phoneNumber) {
		return redisTemplate.opsForValue().get(CODE_PREFIX + phoneNumber);
	}

	@Override
	public void remove(String phoneNumber) {
		redisTemplate.delete(CODE_PREFIX + phoneNumber);
	}

	@Override
	public Long incrementDailyCount(String phoneNumber) {
		String redisKey = COUNT_PREFIX + phoneNumber;
		Long count = redisTemplate.opsForValue().increment(redisKey);
		if (count != null && count == 1) {
			LocalDateTime now = LocalDateTime.now();
			LocalDateTime endOfDay = LocalDateTime.now().with(LocalTime.MAX);
			Duration duration = Duration.between(now, endOfDay);

			redisTemplate.expire(redisKey, duration.getSeconds(), TimeUnit.SECONDS);
		}
		return count != null ? count : 0;
	}

	@Override
	public void saveVerifiedFlag(String phoneNumber, long ttlInSeconds) {
		redisTemplate.opsForValue().set(
			VERIFIED_PREFIX + phoneNumber,
			"true",
			ttlInSeconds,
			TimeUnit.SECONDS
		);
	}

	@Override
	public boolean existsVerifiedFlag(String phoneNumber) {
		return redisTemplate.hasKey(VERIFIED_PREFIX + phoneNumber);
	}

	@Override
	public void removeVerifiedFlag(String phoneNumber) {
		redisTemplate.delete(VERIFIED_PREFIX + phoneNumber);
	}
}
