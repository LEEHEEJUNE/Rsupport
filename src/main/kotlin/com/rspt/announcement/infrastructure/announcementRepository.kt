package com.rspt.announcement.infrastructure

import com.rspt.announcement.entity.announcement
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface announcementRepository: JpaRepository<announcement, Long> {
}