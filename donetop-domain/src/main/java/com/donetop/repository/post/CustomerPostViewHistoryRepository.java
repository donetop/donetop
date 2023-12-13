package com.donetop.repository.post;

import com.donetop.domain.entity.post.CustomerPostViewHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerPostViewHistoryRepository extends JpaRepository<CustomerPostViewHistory, Long> {
}
