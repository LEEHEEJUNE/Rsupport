package com.rspt.announcement.dto

class fileDto {
    data class file(
        val id: Long,
        val originalName: String,
        val storedName: String,
        val path: String,   //임시 로컬 위치.. nas, aws s3 등 필요
        val fileSize: Long,
        val fileType: String
    )
}