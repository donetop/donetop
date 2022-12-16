package com.donetop.domain.entity.folder;

import com.donetop.domain.entity.file.File;
import com.donetop.dto.folder.FolderDTO;
import com.donetop.enums.folder.FolderType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "tbFolder")
@Getter
@NoArgsConstructor
public class Folder {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false, updatable = false)
	private long id;

	@Column(nullable = false, columnDefinition = "varchar(512) default ''")
	private String path;

	@Enumerated(value = EnumType.STRING)
	@Column(nullable = false, columnDefinition = "varchar(10) default ''")
	private FolderType folderType;

	@OneToMany(mappedBy = "folder", cascade = CascadeType.REMOVE)
	private final Set<File> files = new HashSet<>();

	@Builder
	public Folder(final String path, final FolderType folderType) {
		this.path = path;
		this.folderType = folderType;
	}

	public static Folder of(final FolderType folderType, final String root, final long id) {
		return Folder.builder()
			.folderType(folderType)
			.path(folderType.buildPathFrom(root, id))
			.build();
	}

	public void add(final File... files) {
		this.files.addAll(List.of(files));
	}

	public void deleteAllFiles() {
		this.files.clear();
	}

	public FolderDTO toDTO() {
		final FolderDTO folderDTO = new FolderDTO();
		folderDTO.setPath(this.path);
		folderDTO.setFiles(this.files.stream().map(File::toDTO).collect(Collectors.toList()));
		return folderDTO;
	}

}
