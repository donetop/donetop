package com.donetop.common.api;

import com.donetop.common.service.storage.LocalResource;
import com.donetop.common.service.storage.Resource;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter @Setter
public class MultipartFilesRequest {

	private List<MultipartFile> files = new ArrayList<>();

	public List<Resource> getResources() {
		return files.stream().map(LocalResource::new).distinct().collect(Collectors.toList());
	}

}
