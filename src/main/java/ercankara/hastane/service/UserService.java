package ercankara.hastane.service;

import ercankara.hastane.dto.LoginRequest;
import ercankara.hastane.dto.LoginResponse;
import ercankara.hastane.entity.User;
import ercankara.hastane.repository.UserRepository;
import ercankara.hastane.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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
        User user = userRepository.findByUsername(request.getUsername())
                .orElseGet(() -> userRepository.findByEmail(request.getUsername())
                        .orElseThrow(() -> new RuntimeException("User not found")));

        if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            String token = jwtUtil.generateToken(user.getUsername());
            return new LoginResponse(token, user.getId(), user.getUsername());
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
        if (request.getUsername() == null || request.getUsername().isEmpty()) {
            throw new RuntimeException("Username cannot be empty");
        }
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Username is already in use");
        }

        // Kullanıcı oluşturma
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        userRepository.save(user);

        String token = jwtUtil.generateToken(user.getUsername());
        return new LoginResponse(token, user.getId(), user.getUsername());
    }

    // Kullanıcı adı ile kullanıcıyı bulur
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // Yeni refresh token üretir
    public String generateRefreshToken(String username) {
        return jwtUtil.generateRefreshToken(username);
    }

    // Refresh token ile access token'ı yeniler
    public String refreshAccessToken(String refreshToken) {
        if (jwtUtil.isTokenExpired(refreshToken)) {
            throw new RuntimeException("Refresh token expired");
        }
        String username = jwtUtil.extractUsername(refreshToken);
        return jwtUtil.generateToken(username);
    }
}
