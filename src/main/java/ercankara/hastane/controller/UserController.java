package ercankara.hastane.controller;

import ercankara.hastane.entity.User;
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

    // ✅ Admin tüm kullanıcıları görebilir
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    // ✅ Baş doktor sadece DOKTOR rolündekileri görebilir
    @PreAuthorize("hasAnyRole('ADMIN', 'BAS_DOKTOR')")
    @GetMapping("/doktorlar")
    public List<User> getAllDoktorUsers() {
        return userService.getAllDoktorUsers();
    }

    // ✅ ID ile kullanıcı getir (Admin her kullanıcıyı, Baş Doktor sadece DOKTOR rolündekileri)
    @PreAuthorize("hasAnyRole('ADMIN','BAS_DOKTOR')")
    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.getUserByIdWithRoleCheck(id);
    }

    // ✅ Yeni kullanıcı oluştur
    // - Admin: tüm roller
    // - Baş Doktor: sadece DOKTOR rolü
    @PreAuthorize("hasAnyRole('ADMIN','BAS_DOKTOR')")
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        return ResponseEntity.ok(userService.createUserWithRoleCheck(user));
    }

    // ✅ Kullanıcı güncelle
    // - Admin: tüm kullanıcılar
    // - Baş Doktor: sadece DOKTOR rolündekiler
    @PreAuthorize("hasAnyRole('ADMIN','BAS_DOKTOR')")
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        return ResponseEntity.ok(userService.updateUserWithRoleCheck(id, updatedUser));
    }

    // ✅ Kullanıcı sil
    // - Admin: tüm kullanıcılar
    // - Baş Doktor: sadece DOKTOR rolündekiler
    @PreAuthorize("hasAnyRole('ADMIN','BAS_DOKTOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        userService.deleteUserWithRoleCheck(id);
        return ResponseEntity.ok("Kullanıcı silindi");
    }
}
