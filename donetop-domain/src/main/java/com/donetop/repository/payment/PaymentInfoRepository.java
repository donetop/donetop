package com.donetop.repository.payment;

import com.donetop.domain.entity.payment.PaymentInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentInfoRepository extends JpaRepository<PaymentInfo, Long>, QuerydslPredicateExecutor<PaymentInfo> {
}
