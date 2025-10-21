package ercankara.hastane.service;

import ercankara.hastane.entity.Sekreter;
import ercankara.hastane.repository.SekreterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SekreterService {

    @Autowired
    private SekreterRepository sekreterRepository;

    // 📋 Tüm sekreterleri listele
    public List<Sekreter> getAllSekreterler() {
        checkAdminYetkisi();
        return sekreterRepository.findAll();
    }

    // 🔍 ID ile sekreter getir
    public Sekreter getSekreterById(Long id) {
        checkAdminYetkisi();
        return sekreterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sekreter bulunamadı"));
    }

    // ➕ Yeni sekreter oluştur
    public Sekreter createSekreter(Sekreter sekreter) {
        checkAdminYetkisi();
        sekreter.setOlusturulmaTarihi(LocalDateTime.now());
        return sekreterRepository.save(sekreter);
    }

    // ✏️ Sekreter güncelle
    public Sekreter updateSekreter(Long id, Sekreter updated) {
        checkAdminYetkisi();
        Sekreter existing = getSekreterById(id);
        existing.setAdSoyad(updated.getAdSoyad());
        existing.setTelefon(updated.getTelefon());
        existing.setEmail(updated.getEmail());
        existing.setKullanici(updated.getKullanici());
        return sekreterRepository.save(existing);
    }

    // 🗑️ Sekreter sil
    public void deleteSekreter(Long id) {
        checkAdminYetkisi();
        sekreterRepository.deleteById(id);
    }

    // 🔐 Sadece ADMIN yetkili mi?
    private void checkAdminYetkisi() {
        boolean isAdmin = SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                .contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
        if (!isAdmin) {
            throw new RuntimeException("Bu işlemi yalnızca ADMIN yapabilir!");
        }
    }
}
