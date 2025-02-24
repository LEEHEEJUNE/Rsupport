package com.rspt.announcement.common.dto

import com.fasterxml.jackson.annotation.JsonInclude
import org.springframework.http.HttpStatus
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class responseDto {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    data class apiResponse<T:Any?>(
        var status: Int? = HttpStatus.OK.value(), // 상태 코드 (HTTP 상태 또는 사용자 정의 코드)
        var message: String? = "",       // 응답 메시지
        var result: T? = null,       // 성공 시 응답 데이터
        val timestamp: String =  getCurrentFormattedTimestamp() // 응답 시간
    ) {
        companion object {
            fun getCurrentFormattedTimestamp(): String {
                val dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(System.currentTimeMillis()), ZoneId.systemDefault())
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                return dateTime.format(formatter)
            }
        }
    }
}