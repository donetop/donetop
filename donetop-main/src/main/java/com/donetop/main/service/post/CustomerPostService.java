package com.donetop.main.service.post;

import com.donetop.dto.post.CustomerPostDTO;
import com.donetop.main.api.post.request.CustomerPostCreateRequest;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.User;

public interface CustomerPostService {

	long createNewCustomerPost(CustomerPostCreateRequest request);

	CustomerPostDTO getCustomerPost(long id);

	Page<CustomerPostDTO> getCustomerPost(Predicate predicate, PageRequest request);

	long deleteCustomerPost(long id, User user);

}
