package ercankara.hastane.controller;

import ercankara.hastane.entity.User;
import ercankara.hastane.repository.UserRepository;
import ercankara.hastane.repository.DoktorRepository;
import ercankara.hastane.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/kullanicilar")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DoktorRepository doktorRepository;

    // ✅ Sadece ADMIN tüm kullanıcıları görebilir
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    // ✅ Sadece ADMIN kullanıcı oluşturabilir
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        return ResponseEntity.ok(userService.createUser(user));
    }

    // ✅ Sadece ADMIN kullanıcı güncelleyebilir
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        return ResponseEntity.ok(userService.updateUser(id, updatedUser));
    }

    // ✅ Sadece ADMIN kullanıcı silebilir
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("Kullanıcı silindi");
    }

    // ✅ Sadece ADMIN kullanıcıyı ID ile görebilir
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    // 🆕 🔹 Rol bazlı kullanıcıları getir (örn: DOKTOR)
    @PreAuthorize("hasAnyRole('ADMIN','BAS_DOKTOR')")
    @GetMapping("/role/{rol}")
    public List<User> getUsersByRole(@PathVariable String rol) {
        // Role göre kullanıcıları bul
        List<User> users = userRepository.findByRolIgnoreCase(rol);

        // Eğer rol DOKTOR ise, zaten doktor tablosunda kayıtlı olanları filtrele
        if (rol.equalsIgnoreCase("DOKTOR")) {
            users = users.stream()
                    .filter(user -> !doktorRepository.existsByKullanici_Id(user.getId()))
                    .toList();
        }

        return users;
    }
}
