package com.donetop.main.api.common;

import com.donetop.domain.entity.draft.Draft;
import com.donetop.domain.entity.draft.DraftComment;
import com.donetop.domain.entity.folder.DraftFolder;
import com.donetop.enums.folder.FolderType;
import com.donetop.common.service.storage.LocalFileUtil;
import com.donetop.common.service.storage.Resource;
import com.donetop.common.service.storage.StorageService;
import com.donetop.repository.draft.DraftCommentRepository;
import com.donetop.repository.draft.DraftRepository;
import org.junit.jupiter.api.AfterAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileSystemUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DraftBase extends UserBase {

	@Autowired
	protected DraftRepository draftRepository;

	@Autowired
	protected DraftCommentRepository draftCommentRepository;

	@Autowired
	protected StorageService<DraftFolder> draftFolderStorageService;

	@AfterAll
	void clearDraftBase() throws IOException {
		draftRepository.deleteAll();
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

	protected List<Draft> saveMultipleDraftWithoutFiles(final int size) {
		final List<Draft> drafts = new ArrayList<>();
		LocalDateTime now = LocalDateTime.now();
		for (int i = 0; i < size; i++) {
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
		return draftRepository.saveAll(drafts);
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

	protected Draft saveSingleDraftWithComments(final int commentCount) {
		final Draft draft = saveSingleDraftWithoutFiles();
		for (int i = 1; i <= commentCount; i++) {
			final DraftComment draftComment = DraftComment.of("my content" + i, draft);
			draftCommentRepository.save(draftComment);
		}
		return draft;
	}

}
