package com.rspt.announcement.service

import com.rspt.announcement.common.dto.responseDto
import com.rspt.announcement.dto.anmtDto
import com.rspt.announcement.dto.fileDto
import com.rspt.announcement.infrastructure.announcementRepository
import jakarta.transaction.Transactional
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.format.DateTimeFormatter

@Service
class userService(
    private val log: Logger = LoggerFactory.getLogger(userService::class.java),
    private val announcementRepository: announcementRepository
) {
    /**
     * 공지사항 조회
     * ● 공지사항 조회시 응답은 다음과 같다.
     * - 제목, 내용, 등록일시, 조회수, 작성자
     *
     * 조회시 조회수는 증가한다.(증가 된 조회수가 조회된다)
     * */
    @Transactional
    fun getAnnoucement(anmtId:Long): responseDto.apiResponse<anmtDto.announcement>{
        val resultDto = responseDto.apiResponse(result = anmtDto.announcement())

        try{
            val anmtEntity = announcementRepository.getReferenceById(anmtId)

            anmtEntity.view_count = (anmtEntity.view_count ?: 0) + 1

            resultDto.result!!.title = anmtEntity.title //제목
            resultDto.result!!.content = anmtEntity.content //내용
            resultDto.result!!.create_at = anmtEntity.created_at?.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))  //등록일시
            resultDto.result!!.view_count = anmtEntity.view_count
            resultDto.result!!.writer = anmtEntity.writer

            resultDto.message="Successful retrieval"
        }catch(e:Exception){
            log.error("Announcement detail API Error: {}", e.message)
        }

        return resultDto
    }
}