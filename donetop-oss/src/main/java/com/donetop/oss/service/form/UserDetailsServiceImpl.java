package com.donetop.oss.service.form;

import com.donetop.domain.entity.user.OSSUser;
import com.donetop.repository.user.OSSUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.Set;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

	private final OSSUserRepository ossUserRepository;

	private final String loginFailMessage = "유저이름이 유효하지 않습니다.";

	@Override
	public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
		if (!StringUtils.hasLength(username)) {
			throw new UsernameNotFoundException(loginFailMessage);
		}

		OSSUser ossUser = (username.contains("@") ? ossUserRepository.findByEmail(username) : ossUserRepository.findByName(username))
			.orElseThrow(() -> new UsernameNotFoundException(loginFailMessage));

		Set<SimpleGrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority(ossUser.getRoleType().name()));

		return new org.springframework.security.core.userdetails.User(username, ossUser.getPassword(), authorities);
	}

}