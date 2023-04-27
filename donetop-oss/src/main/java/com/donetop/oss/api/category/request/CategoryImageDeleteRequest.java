package com.donetop.oss.api.category.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;

@Getter @Setter
public class CategoryImageDeleteRequest {

	@Min(value = 1L)
	private long fileId;

}
