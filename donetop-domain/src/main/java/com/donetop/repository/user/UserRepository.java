package com.donetop.repository.user;

import com.donetop.domain.entity.user.User;
import com.donetop.enums.user.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByEmail(String email);

	Optional<User> findByRoleType(RoleType roleType);

}
