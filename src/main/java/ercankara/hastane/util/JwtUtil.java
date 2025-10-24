package ercankara.hastane.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.*;

@Component
public class JwtUtil {

    // Sabit ve güvenli SECRET_KEY (32+ byte olmalı)
    private static final String SECRET = "bu-cok-uzun-ve-sabit-bir-secret-key-32-byte-uzunlugunda-olmali!!!";
    private final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(SECRET.getBytes());

    // Kullanıcı adını token'dan çıkar
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    //  Rol bilgisini token'dan çıkar

    public List<String> extractRoles(String token) {
        final Claims claims = extractAllClaims(token);
        return claims.get("roles", List.class);
    }

    // Token'ın son geçerlilik tarihini al
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    //  Herhangi bir claim'i çıkar
    public <T> T extractClaim(String token, java.util.function.Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    //Token içindeki tüm claim'leri çözümle
    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            throw new RuntimeException("Invalid JWT token", e);
        }
    }

    //  Token süresi dolmuş mu?
    public Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    //  Refresh token oluştur (30 gün geçerli)
    public String generateRefreshToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 30))
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    //  Access token oluştur (rol bilgisiyle birlikte)
    public String generateToken(String kullaniciAdi, String role) {
        Map<String, Object> claims = new HashMap<>();
        // ✅ Burada rolü ROLE_ ile başlatarak liste şeklinde ekliyoruz
        claims.put("roles", Collections.singletonList("ROLE_" + role));
        return createToken(claims, kullaniciAdi);
    }

    // Token oluşturma işlemi
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24)) // 1 gün
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    // ✅ Token geçerli mi kontrol et
    public Boolean validateToken(String token, String username) {
        final String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }
}
