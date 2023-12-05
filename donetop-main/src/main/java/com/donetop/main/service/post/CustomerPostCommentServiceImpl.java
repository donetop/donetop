package com.donetop.main.service.post;

import com.donetop.domain.entity.post.CustomerPost;
import com.donetop.domain.entity.post.CustomerPostComment;
import com.donetop.main.api.post.request.CustomerPostCommentCreateRequest;
import com.donetop.main.service.user.UserService;
import com.donetop.repository.post.CustomerPostCommentRepository;
import com.donetop.repository.post.CustomerPostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

import static com.donetop.common.api.Message.*;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CustomerPostCommentServiceImpl implements CustomerPostCommentService {

	private final CustomerPostRepository customerPostRepository;

	private final CustomerPostCommentRepository customerPostCommentRepository;

	private final UserService userService;

	@Override
	public long createNewCustomerPostComment(final CustomerPostCommentCreateRequest request) {
		final CustomerPost customerPost = customerPostRepository.findById(request.getCustomerPostId())
			.orElseThrow(() -> new IllegalStateException(String.format(UNKNOWN_CUSTOMER_POST_WITH_ARGUMENTS, request.getCustomerPostId())));
		final CustomerPostComment customerPostComment = customerPostCommentRepository.save(CustomerPostComment.of(request.getContent(), customerPost));
		log.info("[CREATE] customerPostCommentId: {}", customerPostComment.getId());
		return customerPostComment.getId();
	}

	@Override
	public long deleteCustomerPostComment(final long id, final User user) {
		if (!userService.findUserBy(Objects.requireNonNull(user).getUsername()).isAdmin())
			throw new IllegalStateException(DISALLOWED_REQUEST);
		final CustomerPostComment customerPostComment = customerPostCommentRepository.findById(id)
			.orElseThrow(() -> new IllegalStateException(String.format(UNKNOWN_COMMENT_WITH_ARGUMENTS, id)));
		customerPostCommentRepository.delete(customerPostComment);
		log.info("[DELETE] customerPostCommentId: {}", customerPostComment.getId());
		return customerPostComment.getId();
	}

}
