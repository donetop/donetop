package com.donetop.common.api.category;

import com.donetop.dto.category.CategoryDTO;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class CategorySortRequest {

	@NotEmpty(message = "정렬할 데이터가 없습니다.")
	private List<CategoryDTO> categories = new ArrayList<>();

}
