package com.donetop.oss.service.user;

import com.donetop.dto.user.OSSUserDTO;
import com.donetop.repository.user.OSSUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class OSSUserServiceImpl implements OSSUserService {

	private final OSSUserRepository ossUserRepository;

	@Override
	public OSSUserDTO findUserBy(final String username) {
		return ossUserRepository.findByName(username)
			.orElseThrow(() -> new IllegalStateException("유효한 유저 정보가 없습니다.")).toDTO();
	}
}
