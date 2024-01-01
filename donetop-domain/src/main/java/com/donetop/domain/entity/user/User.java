package com.donetop.domain.entity.user;

import com.donetop.dto.user.UserDTO;
import com.donetop.enums.user.RoleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(
	name = "tbUser",
	uniqueConstraints = {
		@UniqueConstraint(columnNames = {"email"})
	}
)
@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false, updatable = false)
	private long id;

	@Column(nullable = false, columnDefinition = "varchar(32) default ''")
	private String email;

	@Column(nullable = false, columnDefinition = "varchar(512) default ''")
	private String password;

	@Column(nullable = false, columnDefinition = "varchar(32) default ''")
	private String name;

	@Builder.Default
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, columnDefinition = "varchar(10) default ''")
	private RoleType roleType = RoleType.NORMAL;

	@Builder.Default
	@Column(nullable = false)
	private final LocalDateTime createTime = LocalDateTime.now();

	public User updateRoleType(final RoleType roleType) {
		this.roleType = roleType;
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
