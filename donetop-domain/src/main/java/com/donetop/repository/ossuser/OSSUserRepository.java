package com.donetop.repository.ossuser;

import com.donetop.domain.entity.ossuser.OSSUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OSSUserRepository extends JpaRepository<OSSUser, Long> {

	Optional<OSSUser> findByName(String name);

}
