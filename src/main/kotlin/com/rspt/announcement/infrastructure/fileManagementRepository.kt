package com.rspt.announcement.infrastructure

import com.rspt.announcement.entity.FileManagement
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface fileManagementRepository:JpaRepository<FileManagement,Long> {
}