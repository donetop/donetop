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

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CustomerPostCommentServiceImpl implements CustomerPostCommentService {

	private final CustomerPostRepository customerPostRepository;

	private final CustomerPostCommentRepository customerPostCommentRepository;

	private final UserService userService;

	private final String UNKNOWN_CUSTOMER_POST_MESSAGE = "존재하지 않는 고객 게시물입니다. id: %s";

	private final String UNKNOWN_COMMENT_MESSAGE = "존재하지 않는 댓글입니다. id: %s";

	@Override
	public long createNewCustomerPostComment(final CustomerPostCommentCreateRequest request) {
		final CustomerPost customerPost = customerPostRepository.findById(request.getCustomerPostId())
			.orElseThrow(() -> new IllegalStateException(String.format(UNKNOWN_CUSTOMER_POST_MESSAGE, request.getCustomerPostId())));
		final CustomerPostComment customerPostComment = customerPostCommentRepository.save(CustomerPostComment.of(request.getContent(), customerPost));
		log.info("[CREATE] customerPostCommentId: {}", customerPostComment.getId());
		return customerPostComment.getId();
	}

	@Override
	public long deleteCustomerPostComment(final long id, final User user) {
		if (!userService.findUserBy(Objects.requireNonNull(user).getUsername()).isAdmin())
			throw new IllegalStateException("허용되지 않은 요청입니다.");
		final CustomerPostComment customerPostComment = customerPostCommentRepository.findById(id)
			.orElseThrow(() -> new IllegalStateException(String.format(UNKNOWN_COMMENT_MESSAGE, id)));
		customerPostCommentRepository.delete(customerPostComment);
		log.info("[DELETE] customerPostCommentId: {}", customerPostComment.getId());
		return customerPostComment.getId();
	}

}
