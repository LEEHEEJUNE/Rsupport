package com.rspt.announcement.service

import com.rspt.announcement.common.dto.responseDto
import com.rspt.announcement.dto.anmtDto
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class adminService(
    private val log: Logger = LoggerFactory.getLogger(adminService::class.java)
) {

    @Transactional
    fun save(requestSaveDto: anmtDto.requestSaveDto) : responseDto.apiResponse<Nothing>{
        val result = responseDto.apiResponse(result = null)
        log.info("save")

        return result
    }
}