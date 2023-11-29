package com.donetop.domain.entity.post;

import com.donetop.dto.post.CustomerPostCommentDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(
	name = "tbCustomerPostComment"
)
@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class CustomerPostComment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false, updatable = false)
	private long id;

	@Column(nullable = false, columnDefinition = "varchar(1024) default ''")
	private String content;

	@Builder.Default
	@Column(nullable = false)
	private LocalDateTime createTime = LocalDateTime.now();

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "customerPostId")
	private CustomerPost customerPost;

	public static CustomerPostComment of(final String content, final CustomerPost customerPost) {
		final CustomerPostComment customerPostComment = new CustomerPostComment().toBuilder()
			.content(content)
			.customerPost(customerPost)
			.build();
		customerPost.getCustomerPostComments().add(customerPostComment);
		return customerPostComment;
	}

	public CustomerPostCommentDTO toDTO() {
		return CustomerPostCommentDTO.builder()
			.id(this.id)
			.content(this.content)
			.createTime(this.createTime)
			.build();
	}

}
