package com.donetop.repository.post;

import com.donetop.domain.entity.post.CustomerPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerPostRepository extends JpaRepository<CustomerPost, Long> {

}
