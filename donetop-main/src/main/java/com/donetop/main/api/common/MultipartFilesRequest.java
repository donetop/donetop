package com.donetop.main.api.common;

import com.donetop.main.service.storage.LocalResource;
import com.donetop.main.service.storage.Resource;
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
		return files.stream().map(LocalResource::new).collect(Collectors.toList());
	}

}
