package com.donetop.oss.service.form;

import com.donetop.domain.entity.ossuser.OSSUser;
import com.donetop.repository.ossuser.OSSUserRepository;
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

import static com.donetop.common.api.Message.WRONG_USERNAME;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

	private final OSSUserRepository ossUserRepository;

	@Override
	public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
		if (!StringUtils.hasLength(username)) {
			throw new UsernameNotFoundException(WRONG_USERNAME);
		}

		OSSUser ossUser = ossUserRepository.findByName(username).orElseThrow(() -> new UsernameNotFoundException(WRONG_USERNAME));

		Set<SimpleGrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority(ossUser.getRoleType().name()));

		return new org.springframework.security.core.userdetails.User(username, ossUser.getPassword(), authorities);
	}

}
