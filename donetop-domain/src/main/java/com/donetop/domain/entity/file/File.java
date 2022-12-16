package com.donetop.domain.entity.file;

import com.donetop.domain.entity.folder.Folder;
import com.donetop.dto.file.FileDTO;
import com.donetop.enums.file.Extension;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(
	name = "tbFile",
	uniqueConstraints = @UniqueConstraint(columnNames = {"name", "extension", "folderId"})
)
@Getter
@NoArgsConstructor
public class File {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false, updatable = false)
	private long id;

	@Column(nullable = false, columnDefinition = "varchar(128) default ''")
	private String name;

	@Enumerated(value = EnumType.STRING)
	@Column(nullable = false, columnDefinition = "varchar(10) default ''")
	private Extension extension;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "folderId", nullable = false)
	private Folder folder;

	@Builder
	public File(final String name, final Extension extension, final Folder folder) {
		this.name = name;
		this.extension = extension;
		this.folder = folder;
		folder.add(this);
	}

	public String getPath() {
		return addSlashIfAbsent(folder.getPath()) + fileNameWithExtension();
	}

	public String fileNameWithExtension() {
		return name + "." + extension.toString().toLowerCase();
	}

	private String addSlashIfAbsent(final String path) {
		return path.endsWith("/") ? path : path + "/";
	}

	public FileDTO toDTO() {
		final FileDTO fileDTO = new FileDTO();
		fileDTO.setFileName(fileNameWithExtension());
		fileDTO.setPath(getPath());
		return fileDTO;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		File file = (File) o;
		return name.equals(file.name) && extension == file.extension;
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, extension);
	}
}
