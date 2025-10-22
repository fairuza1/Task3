package ercankara.hastane.service;

import ercankara.hastane.entity.Doktor;
import ercankara.hastane.entity.Hasta;
import ercankara.hastane.entity.Muayene;
import ercankara.hastane.repository.DoktorRepository;
import ercankara.hastane.repository.HastaRepository;
import ercankara.hastane.repository.MuayeneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MuayeneService {

    @Autowired
    private MuayeneRepository muayeneRepository;

    @Autowired
    private DoktorRepository doktorRepository;

    @Autowired
    private HastaRepository hastaRepository;

    // 📋 Tüm muayeneleri listele (Admin, Baş Doktor, Doktor, Sekreter)
    public List<Muayene> getAllMuayeneler() {
        checkYetkili();
        return muayeneRepository.findAll();
    }

    // 🔍 ID ile muayene getir
    public Muayene getMuayeneById(Long id) {
        checkYetkili();
        return muayeneRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Muayene bulunamadı!"));
    }

    // ➕ Yeni muayene oluştur (sadece Doktor)
    public Muayene createMuayene(Long kullaniciId, Long hastaId, String tani) {
        checkDoktorYetkisi();

        // 👇 Artık doktoru kullanıcı ID'sinden buluyoruz
        Doktor doktor = doktorRepository.findByKullaniciId(kullaniciId)
                .orElseThrow(() -> new RuntimeException("Doktor bulunamadı!"));

        Hasta hasta = hastaRepository.findById(hastaId)
                .orElseThrow(() -> new RuntimeException("Hasta bulunamadı!"));

        Muayene muayene = Muayene.builder()
                .doktor(doktor)
                .hasta(hasta)
                .tarih(LocalDateTime.now())
                .tani(tani)
                .build();

        return muayeneRepository.save(muayene);
    }

    // ✏️ Muayene güncelle (sadece Doktor veya Admin)
    public Muayene updateMuayene(Long id, String tani) {
        checkDoktorOrAdmin();
        Muayene muayene = getMuayeneById(id);
        muayene.setTani(tani);
        return muayeneRepository.save(muayene);
    }

    // 🗑️ Muayene sil (sadece Admin)
    public void deleteMuayene(Long id) {
        checkAdminYetkisi();
        muayeneRepository.deleteById(id);
    }

    // 👨‍⚕️ Doktora göre filtreleme
    public List<Muayene> getMuayenelerByDoktor(Long doktorId) {
        checkYetkili();
        return muayeneRepository.findByDoktorId(doktorId);
    }

    // 🧍 Hastaya göre filtreleme
    public List<Muayene> getMuayenelerByHasta(Long hastaId) {
        checkYetkili();
        return muayeneRepository.findByHastaId(hastaId);
    }

    // 🔐 Yetki kontrolleri
    private void checkYetkili() {
        boolean yetkili = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .anyMatch(a ->
                        a.getAuthority().equals("ROLE_ADMIN") ||
                                a.getAuthority().equals("ROLE_BAS_DOKTOR") ||
                                a.getAuthority().equals("ROLE_DOKTOR") ||
                                a.getAuthority().equals("ROLE_SEKRETER")
                );
        if (!yetkili) throw new RuntimeException("Bu işlemi yapmaya yetkiniz yok!");
    }

    private void checkDoktorYetkisi() {
        boolean isDoktor = SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                .contains(new SimpleGrantedAuthority("ROLE_DOKTOR"));
        if (!isDoktor) throw new RuntimeException("Bu işlemi sadece DOKTOR yapabilir!");
    }

    private void checkDoktorOrAdmin() {
        boolean isDoktorOrAdmin = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .anyMatch(a ->
                        a.getAuthority().equals("ROLE_DOKTOR") ||
                                a.getAuthority().equals("ROLE_ADMIN")
                );
        if (!isDoktorOrAdmin)
            throw new RuntimeException("Bu işlemi sadece Doktor veya Admin yapabilir!");
    }

    private void checkAdminYetkisi() {
        boolean isAdmin = SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                .contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
        if (!isAdmin) throw new RuntimeException("Bu işlemi sadece ADMIN yapabilir!");
    }
}
