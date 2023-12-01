package com.donetop.main.service.post;

import com.donetop.domain.entity.post.CustomerPost;
import com.donetop.dto.post.CustomerPostDTO;
import com.donetop.main.api.post.request.CustomerPostCreateRequest;
import com.donetop.main.service.user.UserService;
import com.donetop.repository.post.CustomerPostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CustomerPostServiceImpl implements CustomerPostService {

	private final CustomerPostRepository customerPostRepository;

	private final UserService userService;

	private final String UNKNOWN_CUSTOMER_POST_MESSAGE = "존재하지 않는 고객 게시물입니다. id: %s";

	@Override
	public long createNewCustomerPost(final CustomerPostCreateRequest request) {
		final CustomerPost newCustomerPost = customerPostRepository.save(request.toEntity());
		log.info("[CREATE] customerPostId: {}", newCustomerPost.getId());
		return newCustomerPost.getId();
	}

	@Override
	public CustomerPostDTO getCustomerPost(final long id) {
		return customerPostRepository.findById(id)
			.orElseThrow(() -> new IllegalStateException(String.format(UNKNOWN_CUSTOMER_POST_MESSAGE, id)))
			.toDTO();
	}

	@Override
	public Page<CustomerPostDTO> getCustomerPost(final PageRequest request) {
		return customerPostRepository.findAll(request).map(CustomerPost::toDTO);
	}

	@Override
	public long deleteCustomerPost(final long id, final User user) {
		if (!userService.findUserBy(Objects.requireNonNull(user).getUsername()).isAdmin())
			throw new IllegalStateException("허용되지 않은 요청입니다.");
		final CustomerPost customerPost = customerPostRepository.findById(id)
			.orElseThrow(() -> new IllegalStateException(String.format(UNKNOWN_CUSTOMER_POST_MESSAGE, id)));
		customerPostRepository.delete(customerPost);
		log.info("[DELETE] customerPostId: {}", id);
		return id;
	}

}
