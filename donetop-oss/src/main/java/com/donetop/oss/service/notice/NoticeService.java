package com.donetop.oss.service.notice;

import com.donetop.dto.notice.NoticeDTO;
import com.donetop.oss.api.notice.request.NoticeCreateRequest;

import java.util.List;

public interface NoticeService {

	List<NoticeDTO> notices();

	long createNewNotice(NoticeCreateRequest request);

	long deleteNotice(long id);

}
