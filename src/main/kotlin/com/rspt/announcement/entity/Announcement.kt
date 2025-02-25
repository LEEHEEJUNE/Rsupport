package com.rspt.announcement.entity

import com.rspt.announcement.common.converter.JsonLongListConverter
import com.rspt.announcement.common.entity.Auditing
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "announcement")
class Announcement() : Auditing(){
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null    //공지사항 ID

    @Column(length = 255)
    var title: String? = null   //제목

    @Column(length = 255)
    var content: String? = null   //내용

    @Column
    var view_count: Int? = null   //조회수

    @Column(length = 100)
    var writer: String? = null   //작성자

    @Column
    var start_date: LocalDateTime? = null   // 공지 시작일

    @Column
    var end_date: LocalDateTime? = null   // 공지 종료일

    @Column
    @Convert(converter = JsonLongListConverter::class)
    var files: MutableList<Long>?  = null  // 첨부 파일 리스트

    companion object {
        fun create(
            title: String,
            content: String,
            viewCount: Int,
            writer: String,
            startDate: LocalDateTime,
            endDate: LocalDateTime,
            files: MutableList<Long>? = null
        ): Announcement {
            return Announcement().apply {
                this.title = title
                this.content = content
                this.view_count = viewCount
                this.writer = writer
                this.start_date = startDate
                this.end_date = endDate
                this.files = files
            }
        }
    }
}