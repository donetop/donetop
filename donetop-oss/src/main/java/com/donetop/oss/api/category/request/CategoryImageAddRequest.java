package com.donetop.oss.api.category.request;

import com.donetop.common.service.storage.LocalResource;
import com.donetop.common.service.storage.Resource;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Getter @Setter
public class CategoryImageAddRequest {

	@Size(min = 1, max = 1)
	private List<MultipartFile> files = new ArrayList<>();

	public List<Resource> getResources() {
		return files.stream().map(LocalResource::new).distinct().collect(toList());
	}

}
