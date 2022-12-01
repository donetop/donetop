package com.donetop.main.api.draft.request;

import com.donetop.domain.entity.draft.Draft;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

@Getter @Setter
@NoArgsConstructor
public class DraftCreateRequest {

	@NotEmpty(message = "Customer name shouldn't be empty.")
	private String customerName;

	private String address = "";

	@Min(value = 1000L, message = "The minimum price is 1,000.")
	private long price;

	private String memo = "";

	@Builder
	public DraftCreateRequest(final String customerName, final String address, final long price, final String memo) {
		this.customerName = customerName;
		this.address = address;
		this.price = price;
		this.memo = memo;
	}

	public Draft toEntity() {
		return Draft.builder()
			.customerName(this.customerName)
			.address(this.address)
			.price(this.price)
			.memo(this.memo).build();
	}
}
