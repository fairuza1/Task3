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

    // ✅ Kullanıcı giriş işlemi
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

    // ✅ Kullanıcı kayıt işlemi
    public LoginResponse signup(LoginRequest request) {
        if (request.getEmail() == null || request.getEmail().isEmpty()) {
            throw new RuntimeException("Email cannot be empty");
        }
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email is already in use");
        }

        if (request.getKullaniciAdi() == null || request.getKullaniciAdi().isEmpty()) {
            throw new RuntimeException("Username cannot be empty");
        }
        if (userRepository.findByKullaniciAdi(request.getKullaniciAdi()).isPresent()) {
            throw new RuntimeException("Username is already in use");
        }

        User user = new User();
        user.setKullaniciAdi(request.getKullaniciAdi());
        user.setSifre(passwordEncoder.encode(request.getSifre()));
        user.setEmail(request.getEmail());

        // ✅ Rolü dışarıdan al, eğer null ise USER yap
        user.setRol((request.getRol() == null || request.getRol().isEmpty()) ? "USER" : request.getRol().toUpperCase());

        user.setAktif(true);
        user.setOlusturulmaTarihi(LocalDateTime.now());
        userRepository.save(user);

        String token = jwtUtil.generateToken(user.getKullaniciAdi());
        return new LoginResponse(token, user.getId(), user.getKullaniciAdi());
    }

    public User findByKullaniciAdi(String kullaniciAdi) {
        return userRepository.findByKullaniciAdi(kullaniciAdi)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public String generateRefreshToken(String kullaniciAdi) {
        return jwtUtil.generateRefreshToken(kullaniciAdi);
    }

    public String refreshAccessToken(String refreshToken) {
        if (jwtUtil.isTokenExpired(refreshToken)) {
            throw new RuntimeException("Refresh token expired");
        }
        String kullaniciAdi = jwtUtil.extractUsername(refreshToken);
        return jwtUtil.generateToken(kullaniciAdi);
    }
}
