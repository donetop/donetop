package com.donetop.common.form;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.session.InvalidSessionStrategy;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Slf4j
public class InvalidCookieClearingStrategy implements InvalidSessionStrategy {

	private final List<Function<HttpServletRequest, Cookie>> cookiesToClear;

	public InvalidCookieClearingStrategy(final String... cookiesToClear) {
		Assert.notNull(cookiesToClear, "List of cookies cannot be null");
		final List<Function<HttpServletRequest, Cookie>> cookieList = new ArrayList<>();
		for (final String cookieName : cookiesToClear) {
			cookieList.add((request) -> {
				Cookie cookie = new Cookie(cookieName, null);
				String contextPath = request.getContextPath();
				String cookiePath = StringUtils.hasText(contextPath) ? contextPath : "/";
				cookie.setPath(cookiePath);
				cookie.setMaxAge(0);
				cookie.setSecure(request.isSecure());
				return cookie;
			});
		}
		this.cookiesToClear = cookieList;
	}

	@Override
	public void onInvalidSessionDetected(final HttpServletRequest request, final HttpServletResponse response) {
		log.debug("There's a request with invalid session id {}. So the session will be deleted.", request.getRequestedSessionId());
		this.cookiesToClear.forEach((f) -> response.addCookie(f.apply(request)));
	}
}
