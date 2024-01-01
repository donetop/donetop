package com.donetop.repository.post;

import com.donetop.domain.entity.post.CustomerPost;
import com.donetop.domain.entity.post.QCustomerPost;
import com.querydsl.core.types.dsl.StringPath;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerPostRepository extends JpaRepository<CustomerPost, Long>, QuerydslPredicateExecutor<CustomerPost>, QuerydslBinderCustomizer<QCustomerPost> {

	@Override
	default void customize(final QuerydslBindings bindings, final QCustomerPost root) {
		bindings.bind(String.class).first((StringPath path, String value) -> path.contains(value));
	}

}
