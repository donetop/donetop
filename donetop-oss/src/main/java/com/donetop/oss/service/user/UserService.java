package com.donetop.oss.service.user;

import com.donetop.dto.ossuser.OSSUserDTO;

public interface UserService {

	OSSUserDTO findUserBy(String username);
}
