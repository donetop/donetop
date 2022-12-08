package com.donetop.domain.entity.draft;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class DraftBuilderTest {

	@Test
	void createDraft_withBuilder_shouldHaveValues() {
	    // given & when
		final Draft draft = Draft.builder()
			.customerName("jin")
			.address("my address")
			.price(1000)
			.memo("simple test")
			.password("my password").build();

		// then
		assertThat(draft.getId()).isEqualTo(0L);
		assertThat(draft.getCustomerName()).isNotEmpty();
		assertThat(draft.getAddress()).isNotEmpty();
		assertThat(draft.getPrice()).isGreaterThan(0L);
		assertThat(draft.getMemo()).isNotEmpty();
		assertThat(draft.getPassword()).isNotEmpty();
		assertThat(draft.getDraftStatus()).isNotNull();
		assertThat(draft.getPaymentMethod()).isNotNull();
		assertThat(draft.getCreateTime()).isNotNull();
		assertThat(draft.getUpdateTime()).isNotNull();
	}

	@Test
	void createDraft_withTestBuilder_shouldHaveValues() {
	    // given & when
		final LocalDateTime now = LocalDateTime.now();
		final Draft draft = Draft.testBuilder()
			.id(1L)
			.customerName("jin")
			.address("my address")
			.price(1000)
			.memo("simple test")
			.password("my password")
			.createTime(now)
			.updateTime(now).build();

		// then
		assertThat(draft.getId()).isEqualTo(1L);
		assertThat(draft.getCustomerName()).isNotEmpty();
		assertThat(draft.getAddress()).isNotEmpty();
		assertThat(draft.getPrice()).isGreaterThan(0L);
		assertThat(draft.getMemo()).isNotEmpty();
		assertThat(draft.getPassword()).isNotEmpty();
		assertThat(draft.getDraftStatus()).isNotNull();
		assertThat(draft.getPaymentMethod()).isNotNull();
		assertThat(draft.getCreateTime()).isNotNull();
		assertThat(draft.getUpdateTime()).isNotNull();
	}

}