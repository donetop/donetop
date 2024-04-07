package com.donetop.dto.category;

import com.donetop.dto.folder.FolderDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@JsonInclude(NON_NULL)
public class CategoryDTO {

	private long id;

	private String name;

	private int sequence;

	private boolean exposed;

	private List<CategoryDTO> subCategories;

	private FolderDTO folder;

}
