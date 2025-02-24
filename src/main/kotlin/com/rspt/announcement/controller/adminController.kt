package com.rspt.announcement.controller

import com.rspt.announcement.common.dto.responseDto
import com.rspt.announcement.dto.anmtDto
import com.rspt.announcement.service.adminService
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
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
}