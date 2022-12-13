package com.donetop.main.api.draft.request;

import com.donetop.domain.entity.draft.Draft;
import com.donetop.main.service.storage.LocalResource;
import com.donetop.main.service.storage.Resource;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter @Setter
public class DraftCreateRequest {

	@NotEmpty(message = "고객명을 입력해주세요.")
	private String customerName;

	@NotNull(message = "주소를 입력해주세요.")
	private String address;

	@Min(value = 1000L, message = "최소 가격은 1,000원입니다.")
	private long price;

	@NotNull(message = "메모를 입력해주세요.")
	private String memo;

	@NotEmpty(message = "비밀번호를 입력해주세요.")
	private String password;

	private List<MultipartFile> files = new ArrayList<>();

	public Draft toEntity() {
		return Draft.builder()
			.customerName(this.customerName)
			.address(this.address)
			.price(this.price)
			.memo(this.memo)
			.password(this.password).build();
	}

	public List<Resource> getResources() {
		return files.stream().map(LocalResource::new).collect(Collectors.toList());
	}
}
