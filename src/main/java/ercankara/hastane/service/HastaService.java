package ercankara.hastane.service;

import ercankara.hastane.entity.Doktor;
import ercankara.hastane.entity.Hasta;
import ercankara.hastane.repository.DoktorRepository;
import ercankara.hastane.repository.HastaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class HastaService {

    @Autowired
    private HastaRepository hastaRepository;

    @Autowired
    private DoktorRepository doktorRepository;

    // 📋 Tüm hastaları getir (sekreter ve doktor görebilir)
    public List<Hasta> getAllHastalar() {
        checkSekreterOrDoktorYetkisi();
        return hastaRepository.findAll();
    }

    // 🔍 ID ile hasta getir
    public Hasta getHastaById(Long id) {
        checkSekreterOrDoktorYetkisi();
        return hastaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hasta bulunamadı"));
    }

    // ➕ Yeni hasta oluştur (sadece SEKRETER)
    public Hasta createHasta(Hasta hasta) {
        checkSekreterYetkisi();

        if (hastaRepository.existsByTcKimlikNo(hasta.getTcKimlikNo())) {
            throw new RuntimeException("Bu TC kimlik numarasına sahip bir hasta zaten mevcut!");
        }

        // 📌 Doktor atanmışsa kontrol et ve ekle
        if (hasta.getDoktor() != null && hasta.getDoktor().getId() != null) {
            Doktor doktor = doktorRepository.findById(hasta.getDoktor().getId())
                    .orElseThrow(() -> new RuntimeException("Doktor bulunamadı"));
            hasta.setDoktor(doktor);
        }

        hasta.setOlusturulmaTarihi(LocalDateTime.now());
        return hastaRepository.save(hasta);
    }

    // ✏️ Hasta güncelle (sadece SEKRETER)
    public Hasta updateHasta(Long id, Hasta updated) {
        checkSekreterYetkisi();
        Hasta hasta = getHastaById(id);
        hasta.setAdSoyad(updated.getAdSoyad());
        hasta.setTcKimlikNo(updated.getTcKimlikNo());
        hasta.setDogumTarihi(updated.getDogumTarihi());
        hasta.setTelefon(updated.getTelefon());
        hasta.setAdres(updated.getAdres());

        // 📌 Doktor eşleştirmesini güncelle
        if (updated.getDoktor() != null && updated.getDoktor().getId() != null) {
            Doktor doktor = doktorRepository.findById(updated.getDoktor().getId())
                    .orElseThrow(() -> new RuntimeException("Doktor bulunamadı"));
            hasta.setDoktor(doktor);
        }

        return hastaRepository.save(hasta);
    }

    // 🗑️ Hasta sil (sadece SEKRETER)
    public void deleteHasta(Long id) {
        checkSekreterYetkisi();
        hastaRepository.deleteById(id);
    }

    // 📌 Hasta – Doktor eşleştirme
    public Hasta assignDoctor(Long hastaId, Long doktorId) {
        checkSekreterYetkisi();
        Hasta hasta = getHastaById(hastaId);
        Doktor doktor = doktorRepository.findById(doktorId)
                .orElseThrow(() -> new RuntimeException("Doktor bulunamadı"));
        hasta.setDoktor(doktor);
        return hastaRepository.save(hasta);
    }

    // 🔐 Sadece SEKRETER mi?
    private void checkSekreterYetkisi() {
        boolean isSekreter = SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                .contains(new SimpleGrantedAuthority("ROLE_SEKRETER"));
        if (!isSekreter) {
            throw new RuntimeException("Bu işlemi sadece SEKRETER yapabilir!");
        }
    }

    // 🔐 SEKRETER veya DOKTOR mu?
    private void checkSekreterOrDoktorYetkisi() {
        boolean yetkili = SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                .stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_SEKRETER") || a.getAuthority().equals("ROLE_DOKTOR"));
        if (!yetkili) {
            throw new RuntimeException("Bu işlemi sadece SEKRETER veya DOKTOR yapabilir!");
        }
    }
}
