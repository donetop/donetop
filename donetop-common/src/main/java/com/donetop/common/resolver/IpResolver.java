package com.donetop.common.resolver;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
public class IpResolver {

	public static String getIpFromHeader(final HttpServletRequest request) {
		log.info("[GET_IP_FROM_HEADER] headers: {}", httpHeaders(request));
		String ip = request.getHeader("X-Forwarded-For");
		if (isUnknown(ip)) ip = request.getHeader("Proxy-Client-IP");
		if (isUnknown(ip)) ip = request.getHeader("WL-Proxy-Client-IP");
		if (isUnknown(ip)) ip = request.getHeader("HTTP_CLIENT_IP");
		if (isUnknown(ip)) ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		if (isUnknown(ip)) ip = request.getRemoteAddr();
		return ip;
	}

	private static HttpHeaders httpHeaders(final HttpServletRequest request) {
		return Collections.list(request.getHeaderNames())
			.stream()
			.collect(Collectors.toMap(
				Function.identity(),
				h -> Collections.list(request.getHeaders(h)),
				(oldValue, newValue) -> newValue,
				HttpHeaders::new
			));
	}

	private static boolean isUnknown(final String ip) {
        return ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip);
    }

}
