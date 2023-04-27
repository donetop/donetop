package com.donetop.main.service.user;

import com.donetop.dto.user.UserDTO;
import com.donetop.main.api.user.request.UserCreateRequest;

public interface UserService {

	long createNewUser(UserCreateRequest request);

	UserDTO findUserBy(String email);
}
