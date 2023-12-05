package com.donetop.main.api.common;

import com.donetop.domain.entity.post.CustomerPost;
import com.donetop.repository.post.CustomerPostRepository;
import org.junit.jupiter.api.AfterAll;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CustomerPostBase extends UserBase {

	@Autowired
	protected CustomerPostRepository customerPostRepository;

	@AfterAll
	void clearCustomerPostBase() {
		customerPostRepository.deleteAll();
	}

	protected CustomerPost saveSingleCustomerPost() {
		return customerPostRepository.save(
			new CustomerPost().toBuilder()
				.customerName("jin")
				.email("jin@test.com")
				.title("jin's post")
				.content("my content")
				.build()
		);
	}

	protected List<CustomerPost> saveMultipleCustomerPost(final int size) {
		final List<CustomerPost> customerPosts = new ArrayList<>();
		LocalDateTime now = LocalDateTime.now();
		for (int i = 0; i < size; i++) {
			CustomerPost customerPost = new CustomerPost().toBuilder()
				.customerName("jin" + i)
				.email("jin@test.com")
				.title("jin's post" + i)
				.content("my content" + i)
				.createTime(now)
				.build();
			customerPosts.add(customerPost);
			now = now.plusDays(1L);
		}
		return customerPostRepository.saveAll(customerPosts);
	}

}
