package com.rspt.announcement

import com.rspt.announcement.dto.authDto.login
import com.rspt.announcement.common.dto.responseDto
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.*

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthApiIntegrationTest(@Autowired val restTemplate: TestRestTemplate) {
    @Test
    fun `POST auth login returns token`() {
        // 요청 헤더 설정: JSON 타입
        val headers = HttpHeaders().apply {
            contentType = MediaType.APPLICATION_JSON
        }
        // 요청 바디 생성: id와 pw 포함 (비교 로직은 생략)
        val loginRequest = login(id = "testUser", pw = "dummyPass")
        val requestEntity = HttpEntity(loginRequest, headers)

        // /auth/login 엔드포인트 호출하여 응답 받기
        val response: ResponseEntity<responseDto.apiResponse<String>> =
            restTemplate.exchange(
                "/auth/login",
                HttpMethod.POST,
                requestEntity,
                object : ParameterizedTypeReference<responseDto.apiResponse<String>>() {}
            )

        // 응답 상태가 성공인지, 결과로 JWT 토큰이 반환되는지 확인
        println("JWT Token: ${response.body?.result}")
        assertNotNull(response.body?.result, "JWT token should not be null")
    }

}