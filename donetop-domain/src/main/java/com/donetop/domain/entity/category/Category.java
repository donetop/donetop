package com.donetop.domain.entity.category;

import com.donetop.domain.entity.folder.Folder;
import com.donetop.dto.category.CategoryDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Entity
@Table(
	name = "tbCategory",
	uniqueConstraints = {
		@UniqueConstraint(columnNames = {"name"})
	}
)
@DynamicUpdate
@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Category implements Comparable<Category> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false, updatable = false)
	private long id;

	@Column(nullable = false, columnDefinition = "varchar(128) default ''")
	private String name;

	@Column(nullable = false, columnDefinition = "int(11) default 0")
	private int sequence;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parentId")
	private Category parent;

	@OneToMany(mappedBy = "parent")
	private final List<Category> subCategories = new ArrayList<>();

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@JoinColumn(name = "folderId")
	private Folder folder;

	public static Category of(final String name, final int sequence) {
		return Category.builder()
			.name(name)
			.sequence(sequence)
			.build();
	}

	public Category setParent(final Category parent) {
		this.parent = parent;
		return this;
	}

	public Category setSequence(final int sequence) {
		this.sequence = sequence;
		return this;
	}

	public boolean isParent() {
		return this.parent == null;
	}

	public CategoryDTO toDTO() {
		return CategoryDTO.builder()
			.id(this.id)
			.name(this.name)
			.sequence(this.sequence)
			.subCategories(this.subCategories.stream().sorted().map(Category::toDTO).collect(toList()))
			.folder(this.folder == null ? null : this.folder.toDTO())
			.build();
	}

	@Override
	public int compareTo(final Category o) {
		return Integer.compare(this.sequence, o.sequence);
	}
}
