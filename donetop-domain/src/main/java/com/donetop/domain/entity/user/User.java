package com.donetop.domain.entity.user;

import com.donetop.dto.user.UserDTO;
import com.donetop.enums.user.RoleType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(
	name = "tbUser",
	uniqueConstraints = {
		@UniqueConstraint(columnNames = {"email"}),
		@UniqueConstraint(columnNames = {"name"}),
	}
)
@Getter
@NoArgsConstructor
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false, updatable = false)
	private long id;

	@Column(nullable = false, columnDefinition = "varchar(32) default ''")
	private String email;

	@Column(nullable = false, columnDefinition = "varchar(512) default ''")
	private String password;

	@Column(nullable = false, columnDefinition = "varchar(512) default ''")
	private String name;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, columnDefinition = "varchar(10) default ''")
	private RoleType roleType = RoleType.NORMAL;

	@Column(nullable = false)
	private final LocalDateTime createTime = LocalDateTime.now();

	@Builder
	public User(final String email, final String password, final String name) {
		this.email = email;
		this.password = password;
		this.name = name;
	}

	public User updateRoleType(final RoleType roleType) {
		this.roleType = roleType;
		return this;
	}

	public User updatePassword(final String password) {
		this.password = password;
		return this;
	}

	public UserDTO toDTO() {
		final UserDTO userDTO = new UserDTO();
		userDTO.setId(this.id);
		userDTO.setEmail(this.email);
		userDTO.setName(this.name);
		userDTO.setRoleType(this.roleType);
		userDTO.setCreateTime(this.createTime);
		return userDTO;
	}
}
