package com.donetop.domain.entity.notice;

import com.donetop.domain.entity.folder.Folder;
import com.donetop.domain.interfaces.SingleFolderContainer;
import com.donetop.dto.notice.NoticeDTO;
import com.donetop.enums.folder.DomainType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(
	name = "tbNotice"
)
@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Notice implements SingleFolderContainer<Folder> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false, updatable = false)
	private long id;

	@Column(nullable = false, columnDefinition = "varchar(200) default ''")
	private String title;

	@Builder.Default
	@Column(nullable = false)
	private LocalDateTime createTime = LocalDateTime.now();

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@JoinColumn(name = "folderId")
	private Folder folder;

	public static Notice of(final String title) {
		return new Notice().toBuilder()
			.title(title)
			.build();
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
		return this.folder == null ? Folder.of(DomainType.NOTICE, root, this.id) : this.folder;
	}

	public NoticeDTO toDTO() {
		return NoticeDTO.builder()
			.id(this.id)
			.title(this.title)
			.createTime(this.createTime)
			.folder(this.folder == null ? null : this.folder.toDTO())
			.build();
	}
}
