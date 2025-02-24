package com.rspt.announcement.common.entity

import jakarta.persistence.*
import org.hibernate.envers.Audited
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDateTime

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
@Audited
abstract class Auditing {
    @CreatedDate
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    var createdAt: LocalDateTime? = null    //생성일

    @LastModifiedDate
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    var updatedAt: LocalDateTime? = null    //수정일
}