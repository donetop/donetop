package com.donetop.batch.service.draft

import com.donetop.batch.properties.ApplicationProperties
import com.donetop.batch.properties.Storage
import com.donetop.common.service.storage.StorageService
import com.donetop.repository.draft.DraftRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class DraftCollectService(
	private val draftRetrieveService: DraftRetrieveService,
	private val draftRepository: DraftRepository,
	private val storageService: StorageService,
	private val applicationProperties: ApplicationProperties,
	private val storage: Storage = applicationProperties.storage
) {

	private val log = LoggerFactory.getLogger(this.javaClass)

	fun collect() {
		draftRetrieveService.refreshCookieHeader()
		val endPageNum = draftRetrieveService.getEndPageNum()
		for (page in 1 .. endPageNum) {
			draftRetrieveService.getDraftDTOList(page).forEach { saveOrUpdate(it) }
		}
	}

	private fun saveOrUpdate(draftDTO: DraftDTO) {
		val draftOpt = draftRepository.findById(draftDTO.id)
		if (draftOpt.isPresent) {
			log.info("[SAVE_OR_UPDATE] Draft(${draftDTO.id}) already exists. So it will be updated.")
			val draft = draftDTO.applyTo(draftOpt.get())
			val files = draftDTO.fileMap.keys
			if (draft.hasFolder()) {
				if (files != draft.folder.files) storageService.saveOrReplace(draftDTO.getResources(storage.tmp), storageService.addNewFolderOrGet(draft))
			} else {
				if (files.isNotEmpty()) storageService.saveOrReplace(draftDTO.getResources(storage.tmp), storageService.addNewFolderOrGet(draft))
			}
		} else {
			log.info("[SAVE_OR_UPDATE] Draft(${draftDTO.id}) doesn't exist. So it will be saved.")
			val draft = draftRepository.save(draftDTO.toEntity())
			val resources = draftDTO.getResources(storage.tmp)
			if (resources.isNotEmpty()) storageService.saveOrReplace(resources, storageService.addNewFolderOrGet(draft))
		}
		storage.clearTmpDir()
	}

}