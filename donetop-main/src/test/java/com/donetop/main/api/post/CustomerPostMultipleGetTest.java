package com.donetop.main.api.post;

import com.donetop.domain.entity.post.CustomerPost;
import com.donetop.main.api.common.CustomerPostBase;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.List;

import static com.donetop.main.api.post.CustomerPostAPIController.URI.PLURAL;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.JsonFieldType.BOOLEAN;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

public class CustomerPostMultipleGetTest extends CustomerPostBase {

	final int totalNumberOfCustomerPosts = 100;

	List<CustomerPost> customerPosts;

	@BeforeAll
	void beforeAll() {
		customerPosts = saveMultipleCustomerPost(totalNumberOfCustomerPosts);
	}

	@Test
	void getMultiple_withInvalidPageValue_return400() {
	    // given
		final RequestSpecification given = RestAssured.given(this.spec);
		given.filter(
			document(
				"customer_post_multiple_get/getMultiple_withInvalidPageValue_return400"
			)
		);

		// when & then
		given
			.when()
				.param("page", -1)
				.get(PLURAL)
			.then()
				.statusCode(HttpStatus.OK.value());
	}

	@Test
	void getMultiple_withInvalidSizeValue_return400() {
		// given
		final RequestSpecification given = RestAssured.given(this.spec);
		given.filter(
			document(
				"customer_post_multiple_get/getMultiple_withInvalidSizeValue_return400"
			)
		);

		// when & then
		given
			.when()
				.param("size", -1)
				.get(PLURAL)
			.then()
				.statusCode(HttpStatus.OK.value());
	}

	@Test
	void getMultiple_withInvalidDirectionValue_return400() {
		// given
		final RequestSpecification given = RestAssured.given(this.spec);
		given.filter(
			document(
				"customer_post_multiple_get/getMultiple_withInvalidDirectionValue_return400"
			)
		);

		// when & then
		given
			.when()
				.param("direction", "asce")
				.get(PLURAL)
			.then()
				.statusCode(HttpStatus.OK.value());
	}

	@Test
	void getMultiple_withInvalidPropertyValue_return400() {
		// given
		final RequestSpecification given = RestAssured.given(this.spec);
		given.filter(
			document(
				"customer_post_multiple_get/getMultiple_withInvalidPropertyValue_return400"
			)
		);

		// when & then
		given
			.when()
				.param("property", "createTimeee")
				.get(PLURAL)
			.then()
				.statusCode(HttpStatus.OK.value());
	}

	@Test
	void getMultiple_withValidParams_return200() {
		// given
		final RequestSpecification given = RestAssured.given(this.spec);
		given.filter(
			document(
				"customer_post_multiple_get/getMultiple_withValidParams_return200",
				requestParameters(
					parameterWithName("page").description("The page to retrieve. Default is 0."),
					parameterWithName("size").description("The size to retrieve. Default is 20."),
					parameterWithName("sort").description("The property for sorting. Default is \"createTime,desc\".")
				),
				responseFields(
					fieldWithPath("status").type(STRING).description("Status value."),
					fieldWithPath("code").type(NUMBER).description("Status code."),
					fieldWithPath("data").type(OBJECT).description("Response data."),
					subsectionWithPath("data.content").type(ARRAY).description("Searched CustomerPosts. See the Get Single CustomerPost response body field description for details."),
					subsectionWithPath("data.pageable").type(OBJECT).description("Pageable information."),
					fieldWithPath("data.last").type(BOOLEAN).description("Last information."),
					fieldWithPath("data.totalElements").type(NUMBER).description("TotalElements information."),
					fieldWithPath("data.totalPages").type(NUMBER).description("TotalPages information."),
					fieldWithPath("data.size").type(NUMBER).description("Size information."),
					fieldWithPath("data.number").type(NUMBER).description("Number information."),
					subsectionWithPath("data.sort").type(OBJECT).description("Sort information."),
					fieldWithPath("data.first").type(BOOLEAN).description("First information."),
					fieldWithPath("data.numberOfElements").type(NUMBER).description("NumberOfElements information."),
					fieldWithPath("data.empty").type(BOOLEAN).description("Empty information.")
				)
			)
		);

		// when
		final Response response = given.when()
			.param("page", 1)
			.param("size", 20)
			.param("sort", "id,desc")
			.get(PLURAL);

		// then
		response.then()
			.statusCode(HttpStatus.OK.value())
			.body("data.content", hasSize(20))
			.body("data.first", is(false))
			.body("data.totalPages", is(5))
			.body("data.totalElements", is(totalNumberOfCustomerPosts));
	}

	@Test
	void getMultiple_withoutParams_return200() {
		// given
		final RequestSpecification given = RestAssured.given(this.spec);
		given.filter(
			document(
				"customer_post_multiple_get/getMultiple_withoutParams_return200"
			)
		);

		// when & then
		given
			.when()
				.get(PLURAL)
			.then()
				.statusCode(HttpStatus.OK.value())
				.body("data.content", hasSize(20))
				.body("data.first", is(true))
				.body("data.totalPages", is(5))
				.body("data.totalElements", is(totalNumberOfCustomerPosts));
	}

	@Test
	void getMultiple_withQueryDSLParams_return200() {
		// given
		final RequestSpecification given = RestAssured.given(this.spec);
		given.filter(
			document(
				"customer_post_multiple_get/getMultiple_withQueryDSLParams_return200"
			)
		);
		final String lastCustomerPostTitle = customerPosts.get(totalNumberOfCustomerPosts - 1).getTitle();

		// when
		final Response response = given.when()
			.param("title", lastCustomerPostTitle)
			.get(PLURAL);

		// then
		response.then()
			.statusCode(HttpStatus.OK.value())
			.body("data.content", hasSize(1))
			.body("data.first", is(true))
			.body("data.totalPages", is(1))
			.body("data.totalElements", is(1))
			.body("data.content[0].title", is(lastCustomerPostTitle));
	}

}
