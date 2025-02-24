package com.rspt.announcement.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Component
import java.util.Date
import java.security.Key

@Component
class jwtTokenProvider {
    private val key: Key = Keys.secretKeyFor(SignatureAlgorithm.HS256)
    private val validityInMilliseconds: Long = 3600000 // 1시간

    // 토큰 생성: id를 subject로 설정
    fun createToken(userId: String): String {
        val claims: Claims = Jwts.claims().setSubject(userId)
        val now = Date()
        val validity = Date(now.time + validityInMilliseconds)
        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(validity)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact()
    }

    // 토큰에서 Id 추출
    fun getId(token: String): String {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).body.subject
    }

    // 토큰 유효성 검증
    fun validateToken(token: String): Boolean {
        return try {
            val claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token)
            !claims.body.expiration.before(Date())
        } catch (e: Exception) {
            false
        }
    }
}