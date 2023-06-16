package com.donetop.repository.draft;

import com.donetop.domain.entity.draft.Draft;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.format.DateTimeFormatter;

@Repository
@RequiredArgsConstructor
public class DraftDAO {

	private final JdbcTemplate jdbcTemplate;

	private final DraftRepository draftRepository;

	private static final String DRAFT_INSERT_QUERY = "INSERT INTO tbDraft (id, address, categoryName, companyName, createTime, customerName, detailAddress, draftStatus, email, inChargeName, memo, password, paymentInfoId, paymentMethod, phoneNumber, price, updateTime) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

	private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	public Draft saveAndGet(final Draft draft) {
		final Object[] params = new Object[] {
			draft.getId(),
			draft.getAddress(),
			draft.getCategoryName(),
			draft.getCompanyName(),
			draft.getCreateTime().format(DATE_TIME_FORMATTER),
			draft.getCustomerName(),
			draft.getDetailAddress(),
			draft.getDraftStatus().toString(),
			draft.getEmail(),
			draft.getInChargeName(),
			draft.getMemo(),
			draft.getPassword(),
			draft.getPaymentInfo(),
			draft.getPaymentMethod().toString(),
			draft.getPhoneNumber(),
			draft.getPrice(),
			draft.getUpdateTime().format(DATE_TIME_FORMATTER)
		};
		jdbcTemplate.update(DRAFT_INSERT_QUERY, params);
		return draftRepository.getById(draft.getId());
	}

}
