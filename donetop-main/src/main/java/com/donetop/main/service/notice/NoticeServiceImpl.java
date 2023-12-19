package com.donetop.main.service.notice;

import com.donetop.domain.entity.notice.Notice;
import com.donetop.dto.notice.NoticeDTO;
import com.donetop.repository.notice.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

@Service
@Transactional
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService {

	private final NoticeRepository noticeRepository;

	@Override
	public List<NoticeDTO> notices() {
		return noticeRepository.findAll()
			.stream().map(Notice::toDTO)
			.sorted(comparing(NoticeDTO::getCreateTime).reversed())
			.collect(toList());
	}

}
