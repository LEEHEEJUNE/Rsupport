package com.rspt.announcement.service

import com.rspt.announcement.common.dto.responseDto
import com.rspt.announcement.dto.anmtDto
import com.rspt.announcement.entity.announcement
import com.rspt.announcement.entity.fileManagement
import com.rspt.announcement.infrastructure.announcementRepository
import com.rspt.announcement.infrastructure.fileManagementRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Service
class adminService(
    private val log: Logger = LoggerFactory.getLogger(adminService::class.java),
    private val fileManagementRepository: fileManagementRepository,
    private val announcementRepository: announcementRepository
) {
    /**
     * 공지사항 저장
     * 제목, 내용, 공지 시작일시, 공지 종료일시, 첨부파일 (여러개)
     * 1. 첨부파일 저장
     * 2. 공지사항 저장
     * */
    @Transactional
    fun saveAnnouncement(requestDto: anmtDto.requestSave) : responseDto.apiResponse<Nothing>{
        val result = responseDto.apiResponse(result = null)

        try{
            //1.첨부파일 저장
            var fileIds: MutableList<Long> = mutableListOf()
            isUploadDirectory() //파일 저장 디렉토리

            requestDto.files?.let { fileList ->
                fileList.forEach { file ->
                    val originalName = file.originalFilename    //원래 파일명

                    val currentTimestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
                    val uniqueFileName = "${originalName}_$currentTimestamp" //저장 될 파일명
                    val storagePath = System.getProperty("user.dir") + File.separator + "uploads"+File.separator+ "$uniqueFileName"
                    val destinationFile = File(storagePath)

                    file.transferTo(destinationFile)    //파일저장

                    var fileEntity = fileManagement.create(
                        originalName = file.originalFilename!!,
                        storedName = "${originalName}_$currentTimestamp",
                        path = storagePath,
                        size = file.size.toInt(),
                        type = file.contentType ?: "unknown"
                    )
                    var fileId = fileManagementRepository.save(fileEntity).id
                    fileIds.add(fileId!!)
                }
            }
            //2.공지사항 저장
            var announcementEntity = announcement.create(
                title= requestDto.title!!,
                content = requestDto.content!!,
                viewCount = 0,
                writer = requestDto.writer!!,
                startDate = requestDto.start_date!!,
                endDate = requestDto.end_date!!,
                files = fileIds
            )

            announcementRepository.save(announcementEntity)

        }catch(e:Exception){
            log.error("Announcement was saving exception: {}", e.message)
            throw e
        }
        result.message = "save successful"
        return result
    }

    fun isUploadDirectory(){
        val uploadDir = "uploads"
        val uploadDirectory = File(uploadDir)
        if (!uploadDirectory.exists()) {
            uploadDirectory.mkdirs()
        }
    }
}