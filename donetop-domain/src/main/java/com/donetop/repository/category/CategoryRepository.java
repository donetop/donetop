package com.donetop.repository.category;

import com.donetop.domain.entity.category.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
	Optional<Category> findByName(String name);

	@Query("select c from Category c where c.parent is null")
	List<Category> parentCategories();

	@Query("select c from Category c where c.parent is null and c.sequence > :sequence")
	List<Category> greaterSequenceParentCategories(@Param("sequence") int sequence);

	@Query("select c from Category c where c.parent.id = :parentId and c.sequence > :sequence")
	List<Category> greaterSequenceSubCategories(@Param("parentId") long parentId, @Param("sequence") int sequence);
}
