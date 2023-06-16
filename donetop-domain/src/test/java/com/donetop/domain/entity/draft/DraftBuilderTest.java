package com.donetop.domain.entity.draft;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class DraftBuilderTest {

	@Test
	void buildDraft_withoutValues_shouldHaveDefaultValues() {
		// given & when
		final Draft draft = new Draft().toBuilder().build();

		// then
		assertThat(draft.getCompanyName()).isEmpty();
		assertThat(draft.getMemo()).isEmpty();
		assertThat(draft.getDraftStatus()).isNotNull();
		assertThat(draft.getPaymentMethod()).isNotNull();
		assertThat(draft.getCreateTime()).isNotNull();
		assertThat(draft.getUpdateTime()).isNotNull();
	}

	@Test
	void buildDraft_withValues_shouldHaveThoseValuesAndDefaultValues() {
	    // given & when
		final Draft draft = new Draft().toBuilder()
			.customerName("jin")
			.companyName("jin company")
			.email("my email")
			.phoneNumber("000-0000-0000")
			.categoryName("배너")
			.address("my address")
			.detailAddress("my detail address")
			.memo("simple test")
			.password("my password").build();

		// then
		assertThat(draft.getId()).isEqualTo(0L);
		assertThat(draft.getCustomerName()).isNotEmpty();
		assertThat(draft.getCompanyName()).isNotEmpty();
		assertThat(draft.getEmail()).isNotEmpty();
		assertThat(draft.getPhoneNumber()).isNotEmpty();
		assertThat(draft.getCategoryName()).isNotNull();
		assertThat(draft.getAddress()).isNotEmpty();
		assertThat(draft.getDetailAddress()).isNotEmpty();
		assertThat(draft.getMemo()).isNotEmpty();
		assertThat(draft.getPassword()).isNotEmpty();
		assertThat(draft.getDraftStatus()).isNotNull();
		assertThat(draft.getPaymentMethod()).isNotNull();
		assertThat(draft.getCreateTime()).isNotNull();
		assertThat(draft.getUpdateTime()).isNotNull();
		assertThat(draft.getDraftFolders().size()).isEqualTo(0);
	}

}