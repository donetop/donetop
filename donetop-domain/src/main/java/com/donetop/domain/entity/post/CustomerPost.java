package com.donetop.domain.entity.post;

import com.donetop.dto.post.CustomerPostCommentDTO;
import com.donetop.dto.post.CustomerPostDTO;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

@Entity
@Table(
	name = "tbCustomerPost"
)
@DynamicUpdate
@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class CustomerPost {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false, updatable = false)
	private long id;

	@Column(nullable = false, columnDefinition = "varchar(128) default ''")
	private String customerName;

	@Column(nullable = false, columnDefinition = "varchar(256) default ''")
	private String title;

	@Column(nullable = false, columnDefinition = "varchar(3000) default ''")
	private String content;

	@Builder.Default
	@Column(nullable = false)
	private LocalDateTime createTime = LocalDateTime.now();

	@Builder.Default
	@OneToMany(mappedBy = "customerPost", cascade = CascadeType.REMOVE)
	private final List<CustomerPostComment> customerPostComments = new ArrayList<>();

	public CustomerPostDTO toDTO() {
		return CustomerPostDTO.builder()
			.id(this.id)
			.customerName(customerName)
			.title(this.title)
			.content(this.content)
			.createTime(this.createTime)
			.customerPostComments(
				this.customerPostComments.stream()
					.map(CustomerPostComment::toDTO)
					.sorted(comparing(CustomerPostCommentDTO::getCreateTime))
					.collect(toList())
			)
			.build();
	}

}
