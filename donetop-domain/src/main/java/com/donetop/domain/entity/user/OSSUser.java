package com.donetop.domain.entity.user;

import com.donetop.dto.user.OSSUserDTO;
import com.donetop.enums.user.RoleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(
	name = "tbOSSUser",
	uniqueConstraints = {
		@UniqueConstraint(columnNames = {"email"}),
		@UniqueConstraint(columnNames = {"name"}),
	}
)
@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class OSSUser {

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

	public OSSUser updateRoleType(final RoleType roleType) {
		this.roleType = roleType;
		return this;
	}

	public OSSUser updatePassword(final String password) {
		this.password = password;
		return this;
	}

	public OSSUserDTO toDTO() {
		final OSSUserDTO ossUserDTO = new OSSUserDTO();
		ossUserDTO.setId(this.id);
		ossUserDTO.setEmail(this.email);
		ossUserDTO.setName(this.name);
		ossUserDTO.setRoleType(this.roleType);
		ossUserDTO.setCreateTime(this.createTime);
		return ossUserDTO;
	}
}
