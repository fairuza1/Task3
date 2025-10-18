package ercankara.hastane.service;

import ercankara.hastane.dto.LoginRequest;
import ercankara.hastane.dto.LoginResponse;
import ercankara.hastane.entity.User;
import ercankara.hastane.repository.UserRepository;
import ercankara.hastane.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    // Login metodu
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByKullaniciAdi(request.getKullaniciAdi())
                .orElseGet(() -> userRepository.findByEmail(request.getKullaniciAdi())
                        .orElseThrow(() -> new RuntimeException("User not found")));

        if (passwordEncoder.matches(request.getSifre(), user.getSifre())) {
            String token = jwtUtil.generateToken(user.getKullaniciAdi());
            return new LoginResponse(token, user.getId(), user.getKullaniciAdi());
        } else {
            throw new RuntimeException("Invalid credentials");
        }
    }

    // Signup metodu (düzeltilmiş)
    public LoginResponse signup(LoginRequest request) {
        // Email kontrolü
        if (request.getEmail() == null || request.getEmail().isEmpty()) {
            throw new RuntimeException("Email cannot be empty");
        }
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email is already in use");
        }

        // Username kontrolü
        if (request.getKullaniciAdi() == null || request.getKullaniciAdi().isEmpty()) {
            throw new RuntimeException("Username cannot be empty");
        }
        if (userRepository.findByKullaniciAdi(request.getKullaniciAdi()).isPresent()) {
            throw new RuntimeException("Username is already in use");
        }

        // Kullanıcı oluşturma
        User user = new User();
        user.setKullaniciAdi(request.getKullaniciAdi());
        user.setSifre(passwordEncoder.encode(request.getSifre()));
        user.setEmail(request.getEmail());
        user.setRol("USER");
        user.setAktif(true); // 🔥 BURASI EKLENDİ
        user.setOlusturulmaTarihi(LocalDateTime.now());
        userRepository.save(user);

        String token = jwtUtil.generateToken(user.getKullaniciAdi());
        return new LoginResponse(token, user.getId(), user.getKullaniciAdi());
    }

    // Kullanıcı adı ile kullanıcıyı bulur
    public User findByKullaniciAdi(String kullaniciAdi) {
        return userRepository.findByKullaniciAdi(kullaniciAdi)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // Yeni refresh token üretir
    public String generateRefreshToken(String kullaniciAdi) {
        return jwtUtil.generateRefreshToken(kullaniciAdi);
    }

    // Refresh token ile access token'ı yeniler
    public String refreshAccessToken(String refreshToken) {
        if (jwtUtil.isTokenExpired(refreshToken)) {
            throw new RuntimeException("Refresh token expired");
        }
        String kullaniciAdi = jwtUtil.extractUsername(refreshToken);
        return jwtUtil.generateToken(kullaniciAdi);
    }
}
