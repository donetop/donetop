package com.donetop.oss.service.user;

import com.donetop.dto.user.OSSUserDTO;

public interface OSSUserService {

	OSSUserDTO findUserBy(String username);
}
