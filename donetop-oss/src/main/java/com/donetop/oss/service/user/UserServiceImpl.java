package com.donetop.oss.service.user;

import com.donetop.dto.ossuser.OSSUserDTO;
import com.donetop.repository.ossuser.OSSUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.donetop.common.api.Message.NO_USER;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final OSSUserRepository ossUserRepository;

	@Override
	public OSSUserDTO findUserBy(final String username) {
		return ossUserRepository.findByName(username)
			.orElseThrow(() -> new IllegalStateException(NO_USER)).toDTO();
	}
}
