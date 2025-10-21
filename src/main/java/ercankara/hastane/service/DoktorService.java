package ercankara.hastane.service;

import ercankara.hastane.entity.Doktor;
import ercankara.hastane.repository.DoktorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DoktorService {

    @Autowired
    private DoktorRepository doktorRepository;

    // ✅ Tüm doktorları getir (BAS_DOKTOR ve DOKTOR görebilir)
    public List<Doktor> getAllDoktorlar() {
        return doktorRepository.findAll();
    }

    // ✅ ID ile doktor getir
    public Doktor getDoktorById(Long id) {
        return doktorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doktor bulunamadı"));
    }

    // ✅ Yeni doktor oluştur (sadece BAS_DOKTOR)
    public Doktor createDoktor(Doktor doktor) {
        checkBasDoktorYetkisi();
        doktor.setOlusturulmaTarihi(LocalDateTime.now());
        return doktorRepository.save(doktor);
    }

    // ✅ Doktor güncelle (sadece BAS_DOKTOR)
    public Doktor updateDoktor(Long id, Doktor updated) {
        checkBasDoktorYetkisi();
        Doktor doktor = getDoktorById(id);
        doktor.setAdSoyad(updated.getAdSoyad());
        doktor.setUzmanlikAlani(updated.getUzmanlikAlani());
        doktor.setTelefon(updated.getTelefon());
        doktor.setKullanici(updated.getKullanici());
        return doktorRepository.save(doktor);
    }

    // ✅ Doktor sil (sadece BAS_DOKTOR)
    public void deleteDoktor(Long id) {
        checkBasDoktorYetkisi();
        doktorRepository.deleteById(id);
    }

    // 📌 Yardımcı metot: Baş doktor yetkisi var mı kontrol et
    private void checkBasDoktorYetkisi() {
        boolean basDoktor = SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                .contains(new SimpleGrantedAuthority("ROLE_BAS_DOKTOR"));

        if (!basDoktor) {
            throw new RuntimeException("Bu işlemi sadece Baş Doktor yapabilir!");
        }
    }
}
