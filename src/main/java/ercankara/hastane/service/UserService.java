package ercankara.hastane.service;

import ercankara.hastane.dto.LoginRequest;
import ercankara.hastane.dto.LoginResponse;
import ercankara.hastane.entity.User;
import ercankara.hastane.repository.UserRepository;
import ercankara.hastane.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
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

    // ✅ Kullanıcı giriş işlemi
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByKullaniciAdi(request.getKullaniciAdi())
                .orElseGet(() -> userRepository.findByEmail(request.getKullaniciAdi())
                        .orElseThrow(() -> new RuntimeException("User not found")));

        if (passwordEncoder.matches(request.getSifre(), user.getSifre())) {
            String token = jwtUtil.generateToken(user.getKullaniciAdi(), user.getRol());
            return new LoginResponse(token, user.getId(), user.getKullaniciAdi());
        } else {
            throw new RuntimeException("Invalid credentials");
        }
    }

    // ✅ Yeni kullanıcı oluşturma (Signup)
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
        user.setRol((request.getRol() == null || request.getRol().isEmpty()) ? "USER" : request.getRol().toUpperCase());
        user.setAktif(true);
        user.setOlusturulmaTarihi(LocalDateTime.now());
        userRepository.save(user);

        String token = jwtUtil.generateToken(user.getKullaniciAdi(), user.getRol());
        return new LoginResponse(token, user.getId(), user.getKullaniciAdi());
    }

    // ✅ Kullanıcıyı kullanıcı adına göre bul
    public User findByKullaniciAdi(String kullaniciAdi) {
        return userRepository.findByKullaniciAdi(kullaniciAdi)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // ✅ Yeni refresh token üret
    public String generateRefreshToken(String kullaniciAdi) {
        return jwtUtil.generateRefreshToken(kullaniciAdi);
    }

    // ✅ Refresh token ile access token yenile
    public String refreshAccessToken(String refreshToken) {
        if (jwtUtil.isTokenExpired(refreshToken)) {
            throw new RuntimeException("Refresh token expired");
        }
        String kullaniciAdi = jwtUtil.extractUsername(refreshToken);
        User user = findByKullaniciAdi(kullaniciAdi);
        return jwtUtil.generateToken(kullaniciAdi, user.getRol());
    }

    // ✅ Admin paneli için tüm kullanıcıları getir
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // ✅ Sadece DOKTOR rolündekileri getir
    public List<User> getAllDoktorUsers() {
        return userRepository.findAll()
                .stream()
                .filter(user -> "DOKTOR".equalsIgnoreCase(user.getRol()))
                .toList();
    }

    // ✅ Rol kontrolü ile ID’ye göre kullanıcı getir
    public User getUserByIdWithRoleCheck(Long id) {
        User user = getUserById(id);
        if (isBasDoktor() && !"DOKTOR".equalsIgnoreCase(user.getRol())) {
            throw new RuntimeException("Baş doktor sadece DOKTOR rolündeki kullanıcıları görebilir!");
        }
        return user;
    }

    // ✅ Rol kontrolü ile kullanıcı oluştur
    public User createUserWithRoleCheck(User user) {
        if (isBasDoktor() && !"DOKTOR".equalsIgnoreCase(user.getRol())) {
            throw new RuntimeException("Baş doktor sadece DOKTOR rolünde kullanıcı oluşturabilir!");
        }
        return createUser(user);
    }

    // ✅ Rol kontrolü ile kullanıcı güncelle
    public User updateUserWithRoleCheck(Long id, User updatedUser) {
        User existing = getUserById(id);
        if (isBasDoktor() && !"DOKTOR".equalsIgnoreCase(existing.getRol())) {
            throw new RuntimeException("Baş doktor sadece DOKTOR rolündeki kullanıcıları güncelleyebilir!");
        }

        existing.setKullaniciAdi(updatedUser.getKullaniciAdi());
        existing.setEmail(updatedUser.getEmail());
        existing.setRol(updatedUser.getRol());
        existing.setAktif(updatedUser.getAktif());
        existing.setOlusturulmaTarihi(updatedUser.getOlusturulmaTarihi());

        return userRepository.save(existing);
    }

    // ✅ Rol kontrolü ile kullanıcı sil
    public void deleteUserWithRoleCheck(Long id) {
        User existing = getUserById(id);
        if (isBasDoktor() && !"DOKTOR".equalsIgnoreCase(existing.getRol())) {
            throw new RuntimeException("Baş doktor sadece DOKTOR rolündeki kullanıcıları silebilir!");
        }
        userRepository.deleteById(id);
    }

    // ✅ ID ile kullanıcı getir (iç servis)
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // ✅ Yeni kullanıcı oluştur (iç servis)
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

    // ✅ Giriş yapan kişi Baş Doktor mu?
    private boolean isBasDoktor() {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                .contains(new SimpleGrantedAuthority("ROLE_BAS_DOKTOR"));
    }
}
