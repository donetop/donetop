package com.donetop.repository.draft;

import com.donetop.domain.entity.draft.QDraft;
import com.querydsl.core.types.dsl.StringPath;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

import com.donetop.domain.entity.draft.Draft;

@Repository
public interface DraftRepository extends JpaRepository<Draft, Long>, QuerydslPredicateExecutor<Draft>, QuerydslBinderCustomizer<QDraft> {

	@Override
	default void customize(final QuerydslBindings bindings, final QDraft root) {
		bindings.bind(String.class).first((StringPath path, String value) -> path.contains(value));
	}

}
