package com.donetop.main.api.common;

import com.donetop.domain.entity.draft.Draft;
import com.donetop.domain.entity.folder.DraftFolder;
import com.donetop.domain.entity.user.User;
import com.donetop.enums.folder.FolderType;
import com.donetop.enums.user.RoleType;
import com.donetop.common.service.storage.LocalFileUtil;
import com.donetop.common.service.storage.Resource;
import com.donetop.common.service.storage.StorageService;
import com.donetop.repository.draft.DraftRepository;
import com.donetop.repository.user.UserRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileSystemUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.donetop.main.api.form.FormAPIController.URI.LOGIN;

public class DraftBase extends IntegrationBase {

	@Autowired
	protected DraftRepository draftRepository;

	@Autowired
	protected StorageService<DraftFolder> draftFolderStorageService;

	@Autowired
	protected UserRepository userRepository;

	@AfterAll
	void afterAll() throws IOException {
		draftRepository.deleteAll();
		userRepository.deleteAll();
		FileSystemUtils.deleteRecursively(Path.of(testStorage.getRoot()));
	}

	protected Draft saveSingleDraftWithoutFiles() {
		final LocalDateTime now = LocalDateTime.now();
		final Draft draft = new Draft().toBuilder()
			.customerName("jin")
			.companyName("jin's company")
			.inChargeName("hak")
			.email("jin@test.com")
			.categoryName("배너")
			.phoneNumber("010-0000-0000")
			.address("my address")
			.detailAddress("my detail address")
			.price(10000L)
			.memo("get test")
			.password("my password")
			.createTime(now)
			.updateTime(now).build();
		return draftRepository.save(draft);
	}

	protected void saveMultipleDraftWithoutFiles() {
		final List<Draft> drafts = new ArrayList<>();
		LocalDateTime now = LocalDateTime.now();
		for (int i = 0; i < 100; i++) {
			Draft draft = new Draft().toBuilder()
				.customerName("jin" + i)
				.companyName("jin's company")
				.email("jin@test.com")
				.categoryName("현수막")
				.phoneNumber("010-0000-0000")
				.price(1000 + i)
				.address("address" + i)
				.detailAddress("detail address" + i)
				.memo("memo" + i)
				.password("password" + i)
				.createTime(now)
				.updateTime(now).build();
			drafts.add(draft);
			now = now.plusDays(1L);
		}
		draftRepository.saveAll(drafts);
	}

	protected Draft saveSingleDraftWithFiles(final FolderType... folderTypes) {
		final List<Resource> resources = LocalFileUtil.readResources(Path.of(testStorage.getSrc()));
		final LocalDateTime now = LocalDateTime.now();
		final Draft draft = new Draft().toBuilder()
			.customerName("jin")
			.companyName("jin's company")
			.inChargeName("hak")
			.email("jin@test.com")
			.categoryName("배너")
			.phoneNumber("010-0000-0000")
			.address("my address")
			.detailAddress("my detail address")
			.price(10000L)
			.memo("get test")
			.password("my password")
			.createTime(now)
			.updateTime(now).build();
		draftRepository.save(draft);
		for (final FolderType folderType : folderTypes)
			draftFolderStorageService.saveOrReplace(resources, draftFolderStorageService.addNewFolderOrGet(draft, folderType));
		return draftRepository.save(draft);
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

	protected User saveUser(final String name, final RoleType roleType) {
		User normal = User.builder()
			.email(name + "@test.com")
			.name(name)
			.password("password").build().updateRoleType(roleType);
		return userRepository.save(normal);
	}

}
