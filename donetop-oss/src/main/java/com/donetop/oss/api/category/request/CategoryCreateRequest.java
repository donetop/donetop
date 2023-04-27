package com.donetop.oss.api.category.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter @Setter
public class CategoryCreateRequest {

	@NotEmpty(message = "카테고리 이름을 입력해주세요.")
	private String name;

	private long parentCategoryId;

	public boolean isParentCategoryCreateRequest() {
		return this.parentCategoryId == 0L;
	}

}
