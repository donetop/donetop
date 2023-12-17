package com.donetop.main.service.form;

import com.donetop.domain.entity.user.User;
import com.donetop.repository.user.UserRepository;
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
import static com.donetop.enums.user.RoleType.ADMIN;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
		if (!StringUtils.hasLength(username)) {
			throw new UsernameNotFoundException(WRONG_USERNAME);
		}

		User user = (username.equals(ADMIN.name().toLowerCase()) ? userRepository.findByRoleType(ADMIN) : userRepository.findByEmail(username))
			.orElseThrow(() -> new UsernameNotFoundException(WRONG_USERNAME));

		Set<SimpleGrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority(user.getRoleType().name()));

		return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
	}

}
