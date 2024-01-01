package com.donetop.main.service.post;

import com.donetop.common.resolver.IpResolver;
import com.donetop.domain.entity.post.CustomerPost;
import com.donetop.domain.entity.post.CustomerPostViewHistory;
import com.donetop.dto.post.CustomerPostDTO;
import com.donetop.main.api.post.request.CustomerPostCreateRequest;
import com.donetop.main.service.user.UserService;
import com.donetop.repository.post.CustomerPostRepository;
import com.donetop.repository.post.CustomerPostViewHistoryRepository;
import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

import static com.donetop.common.api.Message.DISALLOWED_REQUEST;
import static com.donetop.common.api.Message.UNKNOWN_CUSTOMER_POST_WITH_ARGUMENTS;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CustomerPostServiceImpl implements CustomerPostService {

	private final CustomerPostRepository customerPostRepository;

	private final CustomerPostViewHistoryRepository customerPostViewHistoryRepository;

	private final UserService userService;

	@Override
	public long createNewCustomerPost(final CustomerPostCreateRequest request) {
		final CustomerPost newCustomerPost = customerPostRepository.save(request.toEntity());
		log.info("[CREATE] customerPostId: {}", newCustomerPost.getId());
		return newCustomerPost.getId();
	}

	@Override
	public CustomerPostDTO getCustomerPost(final long id, final HttpServletRequest httpServletRequest) {
		final CustomerPost customerPost = customerPostRepository.findById(id)
			.orElseThrow(() -> new IllegalStateException(String.format(UNKNOWN_CUSTOMER_POST_WITH_ARGUMENTS, id)));
		final String ip = IpResolver.getIpFromHeader(httpServletRequest);
		if (!customerPost.isViewedBy(ip)) {
			log.info("[GET] customerPost(id: {}) has been newly viewed by {}.", customerPost.getId(), ip);
			customerPostViewHistoryRepository.save(CustomerPostViewHistory.of(ip, customerPost));
		}
		return customerPost.toDTO();
	}

	@Override
	public Page<CustomerPostDTO> getCustomerPost(final Predicate predicate, final PageRequest request) {
		return customerPostRepository.findAll(predicate, request).map(CustomerPost::toDTO);
	}

	@Override
	public long deleteCustomerPost(final long id, final User user) {
		if (!userService.findUserBy(Objects.requireNonNull(user).getUsername()).isAdmin())
			throw new IllegalStateException(DISALLOWED_REQUEST);
		final CustomerPost customerPost = customerPostRepository.findById(id)
			.orElseThrow(() -> new IllegalStateException(String.format(UNKNOWN_CUSTOMER_POST_WITH_ARGUMENTS, id)));
		customerPostRepository.delete(customerPost);
		log.info("[DELETE] customerPostId: {}", id);
		return id;
	}

}
