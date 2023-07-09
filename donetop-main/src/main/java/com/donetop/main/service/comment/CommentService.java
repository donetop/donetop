package com.donetop.main.service.comment;

import com.donetop.main.api.comment.request.CommentCreateRequest;
import org.springframework.security.core.userdetails.User;

public interface CommentService {

	long createNewComment(CommentCreateRequest request);

	long deleteComment(long id, User user);

}
