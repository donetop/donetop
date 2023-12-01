package com.donetop.main.api.common;

import com.donetop.domain.entity.user.User;
import com.donetop.enums.user.RoleType;
import com.donetop.repository.user.UserRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterAll;
import org.springframework.beans.factory.annotation.Autowired;

import static com.donetop.main.api.form.FormAPIController.URI.LOGIN;

public class UserBase extends IntegrationBase {

	@Autowired
	protected UserRepository userRepository;

	@AfterAll
	void clearUserBase() {
		userRepository.deleteAll();
	}

	protected User saveUser(final String name, final RoleType roleType) {
		return userRepository.save(
			User.builder()
				.email(name + "@test.com")
				.name(name)
				.password("password").build().updateRoleType(roleType)
		);
	}

	protected User persistUser(final User user) {
		return userRepository.save(user);
	}

	protected Response doLoginWith(final User user) throws Exception {
		final JSONObject loginBody = new JSONObject();
		loginBody.put("username", user.getEmail());
		loginBody.put("password", user.getPassword());
		return RestAssured.given(this.spec).when()
			.contentType(ContentType.JSON)
			.body(loginBody.toString())
			.post(LOGIN);
	}

}
