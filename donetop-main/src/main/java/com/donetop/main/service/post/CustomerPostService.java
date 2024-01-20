package com.donetop.main.service.post;

import com.donetop.dto.post.CustomerPostDTO;
import com.donetop.main.api.post.request.CustomerPostCreateRequest;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;

import javax.servlet.http.HttpServletRequest;

public interface CustomerPostService {

	long createNewCustomerPost(CustomerPostCreateRequest request);

	CustomerPostDTO getCustomerPost(long id, HttpServletRequest request);

	Page<CustomerPostDTO> getCustomerPost(Predicate predicate, Pageable pageable);

	long deleteCustomerPost(long id, User user);

}
