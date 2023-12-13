package com.donetop.main.api.common;

import com.donetop.domain.entity.post.CustomerPost;
import com.donetop.domain.entity.post.CustomerPostComment;
import com.donetop.repository.post.CustomerPostCommentRepository;
import org.junit.jupiter.api.AfterAll;
import org.springframework.beans.factory.annotation.Autowired;

public class CustomerPostCommentBase extends CustomerPostBase {

	@Autowired
	protected CustomerPostCommentRepository customerPostCommentRepository;

	@AfterAll
	void clearCustomerPostCommentBase() {
		customerPostCommentRepository.deleteAll();
	}

	protected CustomerPostComment saveSingleCustomerPostComment() {
		final CustomerPost customerPost = saveSingleCustomerPost();
		final CustomerPostComment customerPostComment = CustomerPostComment.of("my content", customerPost);
		return customerPostCommentRepository.save(customerPostComment);
	}

}
