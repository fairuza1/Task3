package ercankara.hastane.service;

import ercankara.hastane.entity.Doktor;
import ercankara.hastane.repository.DoktorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DoktorService {

    @Autowired
    private DoktorRepository doktorRepository;

    // 📋 Tüm doktorları listele (Admin, Baş Doktor, Doktor)
    public List<Doktor> getAllDoktorlar() {
        return doktorRepository.findAll();
    }

    // 🔍 ID ile doktor getir
    public Doktor getDoktorById(Long id) {
        return doktorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doktor bulunamadı"));
    }

    // ➕ Yeni doktor oluştur (Admin ve Baş Doktor)
    public Doktor createDoktor(Doktor doktor) {
        checkAdminOrBasDoktorYetkisi();
        doktor.setOlusturulmaTarihi(LocalDateTime.now());
        return doktorRepository.save(doktor);
    }

    // ✏️ Doktor güncelle (Admin ve Baş Doktor)
    public Doktor updateDoktor(Long id, Doktor updated) {
        checkAdminOrBasDoktorYetkisi();
        Doktor doktor = getDoktorById(id);

        doktor.setAdSoyad(updated.getAdSoyad());
        doktor.setUzmanlikAlani(updated.getUzmanlikAlani());
        doktor.setTelefon(updated.getTelefon());
        doktor.setKullanici(updated.getKullanici());

        return doktorRepository.save(doktor);
    }

    // 🗑️ Doktor sil (Admin ve Baş Doktor)
    public void deleteDoktor(Long id) {
        checkAdminOrBasDoktorYetkisi();
        doktorRepository.deleteById(id);
    }

    // 📌 Yardımcı metot: Giriş yapan kişi Admin mi veya Baş Doktor mu?
    private void checkAdminOrBasDoktorYetkisi() {
        boolean admin = SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                .contains(new SimpleGrantedAuthority("ROLE_ADMIN"));

        boolean basDoktor = SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                .contains(new SimpleGrantedAuthority("ROLE_BAS_DOKTOR"));

        if (!admin && !basDoktor) {
            throw new RuntimeException("Bu işlemi sadece Admin veya Baş Doktor yapabilir!");
        }
    }
}
