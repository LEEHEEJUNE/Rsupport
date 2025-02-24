package com.rspt.announcement.controller

import com.rspt.announcement.dto.anmtDto
import com.rspt.announcement.service.adminService
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDateTime

@RestController
@RequestMapping("/bo")
class adminController(
    private val adminService: adminService
) {
    @PostMapping("/anmt", consumes = ["multipart/form-data"])
    fun saveAnnouncement(
        @RequestParam("title") title: String,
        @RequestParam("content") content: String,
        @RequestParam("start_date") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") startDate: LocalDateTime,
        @RequestParam("end_date") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") endDate: LocalDateTime,
        @RequestParam("files", required = false) files: List<MultipartFile>?
    ): ResponseEntity<Any?> {
        // 서비스 로직에서 title, content, 파일 리스트를 처리합니다.
        adminService.save(anmtDto.requestSaveDto(
            title = title,
            content = content,
            start_date = startDate,
            end_date = endDate,
            files = files
        ))
        return ResponseEntity("", HttpStatus.OK)
    }
}