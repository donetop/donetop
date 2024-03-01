package com.donetop.domain.entity.draft;

import com.donetop.domain.entity.folder.Folder;
import com.donetop.domain.interfaces.SingleFolderContainer;
import com.donetop.dto.draft.DraftCommentDTO;
import com.donetop.enums.folder.DomainType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(
	name = "tbDraftComment"
)
@DynamicUpdate
@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class DraftComment implements SingleFolderContainer<Folder> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false, updatable = false)
	private long id;

	@Column(nullable = false, columnDefinition = "varchar(1024) default ''")
	private String content;

	@Column(nullable = false, columnDefinition = "tinyint(1) default 0")
	private boolean checked;

	@Builder.Default
	@Column(nullable = false)
	private LocalDateTime createTime = LocalDateTime.now();

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "draftId")
	private Draft draft;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@JoinColumn(name = "folderId")
	private Folder folder;

	public static DraftComment of(final String content, final Draft draft) {
		final DraftComment draftComment = new DraftComment().toBuilder()
			.content(content)
			.draft(draft)
			.build();
		draft.getDraftComments().add(draftComment);
		return draftComment;
	}

	@Override
	public void addFolder(final Folder folder) {
		this.folder = folder;
	}

	@Override
	public boolean hasFolder() {
		return this.folder != null;
	}

	@Override
	public Folder getFolderOrNew(final String root) {
		return this.folder == null ? Folder.of(DomainType.DRAFT_COMMENT, root, this.id) : this.folder;
	}

	public boolean isNotChecked() {
		return !checked;
	}

	public void setChecked() {
		this.checked = true;
	}

	public DraftCommentDTO toDTO() {
		return DraftCommentDTO.builder()
			.id(this.id)
			.content(this.content)
			.createTime(this.createTime)
			.folder(this.folder == null ? null : this.folder.toDTO())
			.build();
	}
}
