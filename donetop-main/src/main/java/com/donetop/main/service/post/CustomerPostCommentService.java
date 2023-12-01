package com.donetop.main.service.post;

import com.donetop.main.api.post.request.CustomerPostCommentCreateRequest;
import org.springframework.security.core.userdetails.User;

public interface CustomerPostCommentService {

	long createNewCustomerPostComment(CustomerPostCommentCreateRequest request);

	long deleteCustomerPostComment(long id, User user);

}
