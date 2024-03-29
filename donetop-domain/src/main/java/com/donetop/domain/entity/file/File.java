package com.donetop.domain.entity.file;

import com.donetop.domain.entity.folder.Folder;
import com.donetop.dto.file.FileDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

@Entity
@Table(
	name = "tbFile",
	uniqueConstraints = @UniqueConstraint(columnNames = {"name", "mimeType", "folderId"})
)
@Getter
@NoArgsConstructor
public class File implements Comparable<File> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false, updatable = false)
	private long id;

	@Column(nullable = false, columnDefinition = "varchar(200) default ''")
	private String name;

	@Column(nullable = false, columnDefinition = "varchar(100) default ''")
	private String mimeType;

	@Column(nullable = false, columnDefinition = "bigint(20) default 0")
	private long size;

	@Column(nullable = false, columnDefinition = "int(11) default 0")
	private int sequence;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "folderId", nullable = false)
	private Folder folder;

	@Builder
	public File(final String name, final long size, final Folder folder) {
		this.name = name;
		this.size = size;
		try {
			this.mimeType = Files.probeContentType(Path.of(name));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		this.folder = folder;
		folder.add(this);
	}

	public String getPath() {
		return addSlashIfAbsent(this.folder.getPath()) + this.name;
	}

	private String addSlashIfAbsent(final String path) {
		return path.endsWith("/") ? path : path + "/";
	}

	public File setSequence(final int sequence) {
		this.sequence = sequence;
		return this;
	}

	public void decreaseSequence() {
		if (this.sequence > 1) this.sequence--;
	}

	public FileDTO toDTO() {
		final FileDTO fileDTO = new FileDTO();
		fileDTO.setId(this.id);
		fileDTO.setName(this.name);
		fileDTO.setMimeType(this.mimeType);
		fileDTO.setPath(getPath());
		fileDTO.setSize(this.size);
		fileDTO.setSequence(this.sequence);
		return fileDTO;
	}

	@Override
	public int compareTo(final File o) {
		if (this.sequence == o.sequence) return Long.compare(this.id, o.id);
		return Integer.compare(this.sequence, o.sequence);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		File file = (File) o;
		return name.equals(file.name) && mimeType.equals(file.mimeType);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, mimeType);
	}

	@Override
	public String toString() {
		return "File{" +
			"name='" + name + '\'' +
			", mimeType='" + mimeType + '\'' +
			", size=" + size +
			", sequence=" + sequence +
			'}';
	}
}
