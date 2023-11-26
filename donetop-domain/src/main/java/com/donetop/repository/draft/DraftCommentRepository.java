package com.donetop.repository.draft;

import com.donetop.domain.entity.draft.DraftComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DraftCommentRepository extends JpaRepository<DraftComment, Long> {
}
