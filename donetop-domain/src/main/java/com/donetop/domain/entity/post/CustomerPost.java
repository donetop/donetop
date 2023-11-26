package com.donetop.domain.entity.post;

import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;

//@Entity
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

	@Column(nullable = false, columnDefinition = "varchar(128) default ''")
	private String email;

	@Column(nullable = false, columnDefinition = "varchar(256) default ''")
	private String title;

	@Column(nullable = false, columnDefinition = "varchar(3000) default ''")
	private String content;

	@Builder.Default
	@Column(nullable = false)
	private LocalDateTime createTime = LocalDateTime.now();

	@Builder.Default
	@Column(nullable = false)
	private LocalDateTime updateTime = LocalDateTime.now();

	public CustomerPost updateCustomerName(final String customerName) {
		this.customerName = customerName;
		return this;
	}

	public CustomerPost updateEmail(final String email) {
		this.email = email;
		return this;
	}

	public CustomerPost updateTitle(final String title) {
		this.title = title;
		return this;
	}

	public CustomerPost updateContent(final String content) {
		this.content = content;
		return this;
	}

}
