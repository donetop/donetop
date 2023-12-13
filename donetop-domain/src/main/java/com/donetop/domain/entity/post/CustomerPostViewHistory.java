package com.donetop.domain.entity.post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(
	name = "tbCustomerPostViewHistory",
	uniqueConstraints = @UniqueConstraint(columnNames = {"viewerIp", "customerPostId"})
)
@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class CustomerPostViewHistory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false, updatable = false)
	private long id;

	@Column(nullable = false, columnDefinition = "varchar(20) default ''")
	private String viewerIp;

	@Builder.Default
	@Column(nullable = false)
	private LocalDateTime createTime = LocalDateTime.now();

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "customerPostId")
	private CustomerPost customerPost;

	public static CustomerPostViewHistory of(final String viewerIp, final CustomerPost customerPost) {
		final CustomerPostViewHistory customerPostViewHistory = new CustomerPostViewHistory().toBuilder()
			.viewerIp(viewerIp)
			.customerPost(customerPost)
			.build();
		customerPost.getCustomerPostViewHistories().add(customerPostViewHistory);
		return customerPostViewHistory;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		CustomerPostViewHistory that = (CustomerPostViewHistory) o;
		return Objects.equals(viewerIp, that.viewerIp);
	}

	@Override
	public int hashCode() {
		return Objects.hash(viewerIp);
	}
}
