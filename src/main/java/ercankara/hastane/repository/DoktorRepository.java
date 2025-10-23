package ercankara.hastane.repository;

import ercankara.hastane.entity.Doktor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DoktorRepository extends JpaRepository<Doktor, Long> {
    Optional<Doktor> findByKullaniciId(Long kullaniciId);
    // 🔹 Bu satırı ekle: Kullanıcı zaten doktor mu kontrolü
    boolean existsByKullanici_Id(Long kullaniciId);
}
