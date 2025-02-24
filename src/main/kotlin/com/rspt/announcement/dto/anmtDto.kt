package com.rspt.announcement.dto

import org.springframework.web.multipart.MultipartFile
import java.time.LocalDateTime

class anmtDto {
    data class requestSave(
        var title:String? = null,   //제목
        var content:String? = null,   //내용
        var writer:String? = null,  //작성자
        var start_date: LocalDateTime? = null,  //공지 시작
        var end_date: LocalDateTime? = null,    //공지 마감
        var files:List<MultipartFile>? = null   //첨부파일 리스트
    )

}