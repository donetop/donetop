package com.donetop.main.service.user;

import com.donetop.main.api.user.request.UserCreateRequest;
import com.donetop.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;

	@Override
	public long createNewUser(final UserCreateRequest request) {
		return userRepository.save(request.toEntity()).getId();
	}

}
