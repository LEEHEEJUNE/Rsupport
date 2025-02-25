package com.rspt.announcement.entity



import com.rspt.announcement.common.entity.Auditing
import jakarta.persistence.*

@Entity
@Table(name = "file_management")
class FileManagement() : Auditing(){
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null    //파일 ID

    @Column(length = 255, nullable = false)
    var original_name: String = ""  //기존 파일 명

    @Column(length = 255, nullable = false)
    var stored_name: String = ""  //저장 파일 명

    @Column(length = 500)
    var path: String? = null   //저장 경로

    @Column
    var size: Int? = null   //파일 사이즈

    @Column(length = 100)
    var type: String? = null   //파일 타입

    companion object {
        fun create(
            originalName: String,
            storedName: String,
            path: String,
            size: Int,
            type: String
        ): FileManagement {
            return FileManagement().apply {
                this.original_name = originalName
                this.stored_name = storedName
                this.path = path
                this.size = size
                this.type = type
            }
        }
    }
}