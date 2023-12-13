package com.donetop.repository.post;

import com.donetop.domain.entity.post.CustomerPostComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerPostCommentRepository extends JpaRepository<CustomerPostComment, Long> {

}
