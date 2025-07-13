package com.transportcompany.transport_app.service

import com.transportcompany.transport_app.exception.InvalidJwtTokenException
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.security.Key
import java.util.*
import java.util.function.Function

@Service
class JwtService(
    @Value("\${my.security.secretkey}")
    private var SECRET_KEY: String
) {


    fun extractUsername(token: String?): String? {
        return extractClaim(token, Claims::getSubject)
    }

    fun <T> extractClaim(token: String?, claimsResolver: Function<Claims, T>): T {
        val claims: Claims = extractAllClaims(token)
        return claimsResolver.apply(claims)
    }

    fun extractAuthorities(token: String): List<GrantedAuthority> {
        val roles = extractClaim(token) { claims ->
            (claims["authorities"] as? List<*>)?.mapNotNull { it as? String } ?: emptyList()
        }
        return roles.map { SimpleGrantedAuthority(it) }
    }


    fun isTokenValid(token: String) {
        try{
            if(isTokenExpired(token)){
                throw InvalidJwtTokenException("Токен истёк")
            }
        } catch (e:Exception) {
            throw InvalidJwtTokenException("Ошибка при разборе токена: ${e.message}")
        }
    }

    private fun isTokenExpired(token: String?): Boolean {
        return extractExpiration(token).before(Date())
    }

    private fun extractExpiration(token: String?): Date {
        return extractClaim(token, Claims::getExpiration)
    }

    private fun extractAllClaims(token: String?): Claims {
        return Jwts.parserBuilder()
            .setSigningKey(getSignInKey())
            .build()
            .parseClaimsJws(token)
            .body
    }

    private fun getSignInKey(): Key {
        val keyBytes: ByteArray = Decoders.BASE64.decode(SECRET_KEY)
        return Keys.hmacShaKeyFor(keyBytes)
    }

    fun extractAccountId(token: String?): String? {
        return extractClaim(token) { claims ->
            claims["accountId"] as? String
        }
    }
}

