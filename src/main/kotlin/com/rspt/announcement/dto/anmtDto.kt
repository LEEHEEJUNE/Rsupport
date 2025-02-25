package com.rspt.announcement.dto

import com.fasterxml.jackson.annotation.JsonInclude
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDateTime

class anmtDto {
    data class requestSave(
        var id:Long? = null, //공지사항 Id
        var title:String? = null,   //제목
        var content:String? = null,   //내용
        var writer:String? = null,  //작성자
        var start_date: LocalDateTime? = null,  //공지 시작
        var end_date: LocalDateTime? = null,    //공지 마감
        var files:List<MultipartFile>? = null   //첨부파일 리스트
    )

    @JsonInclude(JsonInclude.Include.NON_NULL)
    data class announcement(
        var id:Long? = null, //공지사항 Id
        var title:String? = null,   //제목
        var content:String? = null,   //내용
        var writer:String? = null,  //작성자
        var start_date: String? = null,  //공지 시작
        var end_date: String? = null,    //공지 마감
        var files:MutableList<fileDto.file>? = mutableListOf(),
        var view_count:Int? = null,  //조회수
        var create_at: String? = null   //등록일
    )

    data class responseList(
        var total:Long? =0, //전체 리스트 수
        var announcements: MutableList<announcement> = mutableListOf()
    )
}