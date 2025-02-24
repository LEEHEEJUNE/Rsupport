package com.rspt.announcement.controller

import com.rspt.announcement.common.dto.responseDto
import com.rspt.announcement.dto.authDto
import com.rspt.announcement.security.jwtTokenProvider
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class authContorller(private val tokenProvider: jwtTokenProvider) {

    @PostMapping("/login")
    fun login(@RequestBody reqDto: authDto.login): ResponseEntity<responseDto.apiResponse<String>> {
        //id, pw 비교 생략
        val token = tokenProvider.createToken(reqDto.id)
        return ResponseEntity(responseDto.apiResponse(
            message = "Login successful",
            result = token
        ), HttpStatus.OK)
    }
}