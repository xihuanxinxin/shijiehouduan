package com.example.shijiehouduan.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * JWT工具类，用于生成和验证Token
 */
@Component
public class JwtUtil {

    // 密钥，实际应用中应该从配置中读取
    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    
    // Token有效期（毫秒），默认24小时
    private final long expiration = 86400000;

    /**
     * 从token中提取用户ID
     */
    public Integer getUserIdFromToken(String token) {
        return Integer.valueOf(getClaimFromToken(token, Claims::getSubject));
    }

    /**
     * 从token中提取用户名
     */
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, claims -> claims.get("username", String.class));
    }

    /**
     * 从token中提取角色类型
     */
    public String getRoleTypeFromToken(String token) {
        return getClaimFromToken(token, claims -> claims.get("roleType", String.class));
    }

    /**
     * 检查token是否过期
     */
    public Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    /**
     * 从token中提取过期时间
     */
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    /**
     * 从token中提取指定的claim
     */
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    /**
     * 从token中提取所有claim
     */
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 生成token
     */
    public String generateToken(Integer userId, String username, String roleType) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", username);
        claims.put("roleType", roleType);
        
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(String.valueOf(userId))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key)
                .compact();
    }

    /**
     * 验证token
     */
    public Boolean validateToken(String token) {
        try {
            return !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }
} 