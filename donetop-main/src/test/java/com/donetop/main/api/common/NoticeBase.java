package com.donetop.main.api.common;

import com.donetop.common.service.storage.LocalFileUtil;
import com.donetop.common.service.storage.Resource;
import com.donetop.common.service.storage.StorageService;
import com.donetop.domain.entity.folder.Folder;
import com.donetop.domain.entity.notice.Notice;
import com.donetop.repository.notice.NoticeRepository;
import org.junit.jupiter.api.AfterAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileSystemUtils;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class NoticeBase extends IntegrationBase {

	@Autowired
	protected NoticeRepository noticeRepository;

	@Autowired
	protected StorageService<Folder> storageService;

	@AfterAll
	void clearNoticeBase() throws Exception {
		noticeRepository.deleteAll();
		FileSystemUtils.deleteRecursively(Path.of(testStorage.getRoot()));
	}

	protected List<Notice> saveMultipleNoticesWithFiles() {
		final List<Notice> notices = new ArrayList<>();
		final List<Resource> jpgResources = LocalFileUtil
			.readResources(Path.of(testStorage.getSrc()))
			.stream().filter(resource -> resource.getOriginalFilename().endsWith("jpg"))
			.collect(toList());
		LocalDateTime now = LocalDateTime.now();
		for (int i = 0; i < 10; i++) {
			final Notice notice = noticeRepository.save(
				new Notice().toBuilder()
					.title("title" + i)
					.createTime(now)
					.build()
			);
			storageService.saveOrReplace(jpgResources, storageService.addNewFolderOrGet(notice));
			notices.add(noticeRepository.save(notice));
			now = now.plusDays(1L);
		}
		return notices;
	}

}
