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
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    // ✅ Login
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByKullaniciAdi(request.getKullaniciAdi())
                .orElseGet(() -> userRepository.findByEmail(request.getKullaniciAdi())
                        .orElseThrow(() -> new RuntimeException("User not found")));

        if (!passwordEncoder.matches(request.getSifre(), user.getSifre())) {
            throw new RuntimeException("Invalid credentials");
        }

        String token = jwtUtil.generateToken(user.getKullaniciAdi(), user.getRol());
        return new LoginResponse(token, user.getId(), user.getKullaniciAdi());
    }

    // ✅ Signup
    public LoginResponse signup(LoginRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent())
            throw new RuntimeException("Email is already in use");
        if (userRepository.findByKullaniciAdi(request.getKullaniciAdi()).isPresent())
            throw new RuntimeException("Username is already in use");

        User user = new User();
        user.setKullaniciAdi(request.getKullaniciAdi());
        user.setSifre(passwordEncoder.encode(request.getSifre()));
        user.setEmail(request.getEmail());
        user.setRol("USER"); // ✅ her zaman USER olarak kaydolur
        user.setAktif(true);
        user.setOlusturulmaTarihi(LocalDateTime.now());

        userRepository.save(user);

        String token = jwtUtil.generateToken(user.getKullaniciAdi(), user.getRol());
        return new LoginResponse(token, user.getId(), user.getKullaniciAdi());
    }

    // ✅ Tüm kullanıcıları getir
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // ✅ Kullanıcı oluştur
    public User createUser(User user) {
        if (userRepository.findByKullaniciAdi(user.getKullaniciAdi()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }
        user.setSifre(passwordEncoder.encode(user.getSifre()));
        if (user.getRol() == null || user.getRol().isEmpty()) {
            user.setRol("USER");
        }
        user.setAktif(true);
        user.setOlusturulmaTarihi(LocalDateTime.now());
        return userRepository.save(user);
    }

    // ✅ Kullanıcı güncelle
    public User updateUser(Long id, User updatedUser) {
        User existing = getUserById(id);
        existing.setKullaniciAdi(updatedUser.getKullaniciAdi());
        existing.setEmail(updatedUser.getEmail());
        existing.setRol(updatedUser.getRol());
        existing.setAktif(updatedUser.getAktif());
        existing.setOlusturulmaTarihi(updatedUser.getOlusturulmaTarihi());
        return userRepository.save(existing);
    }

    // ✅ Kullanıcı sil
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    // ✅ Kullanıcı ID ile getir
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // ✅ Kullanıcı adıyla bul
    public User findByKullaniciAdi(String kullaniciAdi) {
        return userRepository.findByKullaniciAdi(kullaniciAdi)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // ✅ Refresh token
    public String generateRefreshToken(String kullaniciAdi) {
        return jwtUtil.generateRefreshToken(kullaniciAdi);
    }

    public String refreshAccessToken(String refreshToken) {
        if (jwtUtil.isTokenExpired(refreshToken)) {
            throw new RuntimeException("Refresh token expired");
        }
        String kullaniciAdi = jwtUtil.extractUsername(refreshToken);
        User user = findByKullaniciAdi(kullaniciAdi);
        return jwtUtil.generateToken(kullaniciAdi, user.getRol());
    }
}
