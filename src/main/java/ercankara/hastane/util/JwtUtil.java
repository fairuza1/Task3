package ercankara.hastane.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    // ✅ Sabit, değişmeyen SECRET_KEY tanımı
    private static final String SECRET = "bu-cok-uzun-ve-sabit-bir-secret-key-32-byte-uzunlugunda-olmali!!!";
    private final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(SECRET.getBytes());

    // JWT'den username al
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // JWT'den expiration al
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Herhangi bir claim'i al
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // JWT'den tüm claims al
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

    // Token süresi dolmuş mu?
    public Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Refresh token oluştur
    public String generateRefreshToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 30)) // 30 gün
                .signWith(SECRET_KEY)
                .compact();
    }

    // Access token oluştur
    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username);
    }

    // Token oluşturma metodu
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24)) // 1 gün
                .signWith(SECRET_KEY)
                .compact();
    }

    // Token geçerli mi?
    public Boolean validateToken(String token, String username) {
        final String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }
}
