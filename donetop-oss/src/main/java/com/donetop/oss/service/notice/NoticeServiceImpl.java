package com.donetop.oss.service.notice;

import com.donetop.common.service.storage.Resource;
import com.donetop.common.service.storage.StorageService;
import com.donetop.domain.entity.folder.Folder;
import com.donetop.domain.entity.notice.Notice;
import com.donetop.dto.notice.NoticeDTO;
import com.donetop.oss.api.notice.request.NoticeCreateRequest;
import com.donetop.repository.notice.NoticeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

import static com.donetop.common.api.Message.UNKNOWN_NOTICE_WITH_ARGUMENTS;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService {

	private final NoticeRepository noticeRepository;

	private final StorageService<Folder> storageService;

	@Override
	public List<NoticeDTO> notices() {
		return noticeRepository.findAll()
			.stream().map(Notice::toDTO)
			.sorted(comparing(NoticeDTO::getCreateTime).reversed())
			.collect(toList());
	}

	@Override
	public long createNewNotice(final NoticeCreateRequest request) {
		final Notice newNotice = noticeRepository.save(Notice.of(request.getTitle()));
		final List<Resource> resources = request.getResources();
		if (!resources.isEmpty()) {
			storageService.saveOrReplace(resources, storageService.addNewFolderOrGet(newNotice));
		}
		log.info("[CREATE] noticeId: {}", newNotice.getId());
		return newNotice.getId();
	}

	@Override
	public long deleteNotice(final long id) {
		final Notice notice = getOrThrow(id);
		if (notice.hasFolder()) storageService.delete(notice.getFolder());
		noticeRepository.delete(notice);
		log.info("[DELETE] noticeId: {}", notice.getId());
		return id;
	}

	private Notice getOrThrow(final long noticeId) {
		return noticeRepository.findById(noticeId)
			.orElseThrow(() -> new IllegalStateException(String.format(UNKNOWN_NOTICE_WITH_ARGUMENTS, noticeId)));
	}
}
