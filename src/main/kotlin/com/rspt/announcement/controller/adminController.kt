package com.rspt.announcement.controller

import com.rspt.announcement.common.dto.responseDto
import com.rspt.announcement.dto.anmtDto
import com.rspt.announcement.service.adminService
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDateTime

@RestController
@RequestMapping("/bo")  //backoffice
class adminController(
    private val adminService: adminService
) {
    /**
     * 공지사항 리스트 조회
     * */
    @GetMapping("/anmts")
    fun getAnnouncements(
        @PageableDefault(page = 0, size = 20) pageable: Pageable
    ) : ResponseEntity<responseDto.apiResponse<anmtDto.responseList>>{
        val pageRequest = PageRequest.of(pageable.pageNumber, pageable.pageSize)
        return ResponseEntity(adminService.getAnnoucements(pageRequest), HttpStatus.OK)
    }

    /**
     * 공지사항 저장
     * */
    @PostMapping("/anmt", consumes = ["multipart/form-data"])
    fun saveAnnouncement(
        @RequestParam("title") title: String,
        @RequestParam("content") content: String,
        @RequestParam("start_date") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") startDate: LocalDateTime,
        @RequestParam("end_date") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") endDate: LocalDateTime,
        @RequestParam("files", required = false) files: List<MultipartFile>?,
    ): ResponseEntity<responseDto.apiResponse<Nothing>> {
        val authentication = SecurityContextHolder.getContext().authentication
        val writer = authentication?.name ?: "anonymous"

        return ResponseEntity(
            adminService.saveAnnouncement(
                anmtDto.requestSave(
                    title = title,
                    content = content,
                    writer = writer,
                    start_date = startDate,
                    end_date = endDate,
                    files = files
        )), HttpStatus.OK)
    }

    @DeleteMapping("/anmt/{anmtId}")
    fun deleteAnnouncement(@PathVariable anmtId: Long):ResponseEntity<responseDto.apiResponse<Nothing>> {
        return ResponseEntity(
            adminService.deleteAnnouncement(anmtId), HttpStatus.OK)
    }

    @PutMapping("/anmt/{anmtId}")
    fun updateAnnoucement(
        @PathVariable anmtId: Long,
        @RequestParam("title") title: String,
        @RequestParam("content") content: String,
        @RequestParam("start_date") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") startDate: LocalDateTime,
        @RequestParam("end_date") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") endDate: LocalDateTime,
        @RequestParam("files", required = false) files: List<MultipartFile>?
        ):ResponseEntity<responseDto.apiResponse<Nothing>>{
        return ResponseEntity(
            adminService.updateAnnoucement(
                anmtDto.requestSave(
                    id = anmtId,
                    title = title,
                    content = content,
                    start_date = startDate,
                    end_date = endDate,
                    files = files
                )), HttpStatus.OK)
    }
}