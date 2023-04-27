package com.donetop.repository.draft;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import com.donetop.domain.entity.draft.Draft;

@Repository
public interface DraftRepository extends JpaRepository<Draft, Long>, QuerydslPredicateExecutor<Draft> {

}
