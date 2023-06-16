package com.donetop.collect.service.draft

import com.donetop.collect.api.draft.DraftCollectRequest
import com.donetop.collect.properties.ApplicationProperties
import com.donetop.collect.properties.Storage
import com.donetop.common.service.storage.StorageService
import com.donetop.domain.entity.draft.Draft
import com.donetop.domain.entity.folder.DraftFolder
import com.donetop.enums.folder.FolderType.DRAFT_WORK
import com.donetop.repository.draft.DraftDAO
import com.donetop.repository.draft.DraftRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.math.max

@Service
@Transactional
class DraftCollectService(
    private val draftRetrieveService: DraftRetrieveService,
    private val draftRepository: DraftRepository,
    private val draftDAO: DraftDAO,
    private val storageService: StorageService<DraftFolder>,
    private val applicationProperties: ApplicationProperties,
    private val storage: Storage = applicationProperties.storage
) {

	private val log = LoggerFactory.getLogger(this.javaClass)

	data class Result(val saveCount: Int, val updateCount: Int) {
		operator fun plus(other: Result): Result {
			return Result(saveCount + other.saveCount, updateCount + other.updateCount)
		}
	}

	fun collect(request: DraftCollectRequest): Result {
		if (applicationProperties.collectPassword != request.password) throw IllegalStateException("Password is wrong.")
		if (request.refreshCookie) draftRetrieveService.refreshCookieHeader()
		val actualEndPageNum = draftRetrieveService.getEndPageNum()
		val pageNumProgression = request.getPageNumProgression()
		val max = max(pageNumProgression.first, pageNumProgression.last)
		if (actualEndPageNum < max) throw IllegalStateException("Possible end page num is $actualEndPageNum.")
		var result = Result(0, 0)
		for (pageNum in pageNumProgression) {
			draftRetrieveService.getDraftDTOList(pageNum).forEach { result += saveOrUpdate(it) }
		}
		log.info("[COLLECT] $result")
		return result
	}

	private fun saveOrUpdate(draftDTO: DraftDTO): Result {
		val draftOpt = draftRepository.findById(draftDTO.id)
		var saveCount = 0; var updateCount = 0
		val fileProcessResult: Boolean
		if (draftOpt.isPresent) {
			log.info("[SAVE_OR_UPDATE] Draft(${draftDTO.id}) already exists. So it will be updated.")
			val draft = draftDTO.applyTo(draftOpt.get())
			fileProcessResult = processFiles(draft, draftDTO)
			updateCount++
		} else {
			log.info("[SAVE_OR_UPDATE] Draft(${draftDTO.id}) doesn't exist. So it will be saved.")
			val draft = draftDAO.saveAndGet(draftDTO.toEntity())
			fileProcessResult = processFiles(draft, draftDTO)
			saveCount++
		}
		if (fileProcessResult) storage.clearTmpDir()
		return Result(saveCount, updateCount)
	}

	private fun processFiles(draft: Draft, draftDTO: DraftDTO): Boolean {
		val files = draftDTO.fileMap.keys
		if (draft.hasFolder(DRAFT_WORK)) {
			val workFolder = storageService.addNewFolderOrGet(draft, DRAFT_WORK)
			val diff = files - workFolder.files
			if (diff.isNotEmpty()) {
				log.info("[SAVE_OR_UPDATE] There are new files($diff). So they will be added.")
				storageService.add(draftDTO.downloadResourcesAt(storage.tmp), workFolder)
				return true
			}
		} else {
			if (files.isNotEmpty()) {
				log.info("[SAVE_OR_UPDATE] There are new files($files). So they will be added.")
				storageService.add(draftDTO.downloadResourcesAt(storage.tmp), storageService.addNewFolderOrGet(draft, DRAFT_WORK))
				return true
			}
		}
		return false
	}

}