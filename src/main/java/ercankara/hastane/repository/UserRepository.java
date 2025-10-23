package ercankara.hastane.repository;

import ercankara.hastane.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByKullaniciAdi(String kullaniciAdi);
    Optional<User> findByEmail(String email); // Bu satırı ekleyin
    // 🔹 Rol adına göre kullanıcıları getir (örnek: DOKTOR)
    List<User> findByRolIgnoreCase(String rol);
}
