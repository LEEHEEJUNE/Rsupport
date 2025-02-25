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
     * 공지사항 삭제
     * 1.첨부파일 삭제(DB 삭제, 로컬 삭제)
     * 2.공지사항 삭제(DB 삭제)
     * */
    @Transactional
    fun deleteAnnouncement(anmtId:Long):responseDto.apiResponse<Nothing>{
        val result = responseDto.apiResponse(result = null)

        try{
            val anmtEntity = announcementRepository.getReferenceById(anmtId)

            val files = anmtEntity.files!!

            if(files.size > 0) {
                files.forEach{ file ->
                    val fileEntity = fileManagementRepository.getReferenceById(file)
                    val deleteFile = File(fileEntity.path)
                    if(deleteFile.exists()){
                        log.info("Deleted file ${fileEntity.path}")
                        deleteFile.delete()
                    }
                    fileManagementRepository.delete(fileEntity)
                }
            }

            announcementRepository.delete(anmtEntity)

            result.message = "delete successful"
        }catch(e:Exception){
            log.error("delete announcement failed with error: ", e)
            throw e
        }

        return result
    }

    /**
     * 공지사항 저장
     * 제목, 내용, 공지 시작일시, 공지 종료일시, 첨부파일 (여러개)
     * 1. 첨부파일 저장(DB 저장, 로컬 저장)
     * 2. 공지사항 저장(DB 저장)
     * */
    @Transactional
    fun saveAnnouncement(requestDto: anmtDto.requestSave) : responseDto.apiResponse<Nothing>{
        val result = responseDto.apiResponse(result = null)

        try{
            //1.첨부파일 저장
            val fileIds: MutableList<Long> = mutableListOf()
            isUploadDirectory() //파일 저장 디렉토리

            requestDto.files?.let { fileList ->
                fileList.forEach { file ->
                    val originalName = file.originalFilename    //원래 파일명

                    val currentTimestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
                    val uniqueFileName = "${originalName}_$currentTimestamp" //저장 될 파일명
                    val storagePath = System.getProperty("user.dir") + File.separator + "uploads"+File.separator+ "$uniqueFileName"
                    val destinationFile = File(storagePath)

                    file.transferTo(destinationFile)    //파일저장

                    val fileEntity = fileManagement.create(
                        originalName = file.originalFilename!!,
                        storedName = "${originalName}_$currentTimestamp",
                        path = storagePath,
                        size = file.size.toInt(),
                        type = file.contentType ?: "unknown"
                    )
                    val fileId = fileManagementRepository.save(fileEntity).id
                    fileIds.add(fileId!!)
                }
            }
            //2.공지사항 저장
            val announcementEntity = announcement.create(
                title= requestDto.title!!,
                content = requestDto.content!!,
                viewCount = 0,
                writer = requestDto.writer!!,
                startDate = requestDto.start_date!!,
                endDate = requestDto.end_date!!,
                files = fileIds
            )

            announcementRepository.save(announcementEntity)
            result.message = "save successful"
        }catch(e:Exception){
            log.error("Announcement was saving exception: {}", e.message)
            throw e
        }
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