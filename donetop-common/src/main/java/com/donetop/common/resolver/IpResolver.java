package com.donetop.common.resolver;

import javax.servlet.http.HttpServletRequest;

public class IpResolver {

	public static String getIpFromHeader(final HttpServletRequest request) {
		String ip = request.getHeader("X-Forwared-For");
		if (isUnknown(ip)) ip = request.getHeader("Proxy-Client-IP");
		if (isUnknown(ip)) ip = request.getHeader("WL-Proxy-Client-IP");
		if (isUnknown(ip)) ip = request.getHeader("HTTP_CLIENT_IP");
		if (isUnknown(ip)) ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		if (isUnknown(ip)) ip = request.getRemoteAddr();
		return ip;
	}

	private static boolean isUnknown(final String ip) {
        return ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip);
    }

}
