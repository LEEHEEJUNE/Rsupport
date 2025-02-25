package com.rspt.announcement.controller

import com.rspt.announcement.common.dto.responseDto
import com.rspt.announcement.dto.anmtDto
import com.rspt.announcement.service.userService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/main")
class userController(
    private val userService: userService
) {
    /**
     * 공지사항 조회
     * */
    @GetMapping("/anmt/{anmtId}")
    fun getAnnouncementDetail(
        @PathVariable anmtId: Long,
    ) : ResponseEntity<responseDto.apiResponse<anmtDto.announcement>> {
        return ResponseEntity(userService.getAnnoucement(anmtId), HttpStatus.OK)
    }

}