package ercankara.hastane.service;

import ercankara.hastane.entity.Muayene;
import ercankara.hastane.entity.Recete;
import ercankara.hastane.repository.MuayeneRepository;
import ercankara.hastane.repository.ReceteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReceteService {

    @Autowired
    private ReceteRepository receteRepository;

    @Autowired
    private MuayeneRepository muayeneRepository;

    // 📋 Tüm reçeteleri getir (Admin, Doktor, Baş Doktor)
    public List<Recete> getAllReceteler() {
        checkYetkili();
        return receteRepository.findAll();
    }

    // 🔍 ID ile reçete getir
    public Recete getReceteById(Long id) {
        checkYetkili();
        return receteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reçete bulunamadı!"));
    }

    // ➕ Reçete oluştur (sadece Doktor)
    public Recete createRecete(Long muayeneId, String ilacAdi, String doz, String aciklama) {
        checkDoktorYetkisi();
        Muayene muayene = muayeneRepository.findById(muayeneId)
                .orElseThrow(() -> new RuntimeException("Muayene bulunamadı!"));

        Recete recete = Recete.builder()
                .muayene(muayene)
                .ilacAdi(ilacAdi)
                .doz(doz)
                .aciklama(aciklama)
                .build();

        return receteRepository.save(recete);
    }

    // ✏️ Reçete güncelle (sadece Doktor veya Admin)
    public Recete updateRecete(Long id, String ilacAdi, String doz, String aciklama) {
        checkDoktorOrAdmin();
        Recete recete = getReceteById(id);
        recete.setIlacAdi(ilacAdi);
        recete.setDoz(doz);
        recete.setAciklama(aciklama);
        return receteRepository.save(recete);
    }

    // 🗑️ Reçete sil (sadece Admin)
    public void deleteRecete(Long id) {
        checkAdminYetkisi();
        receteRepository.deleteById(id);
    }

    // 🔐 Yetki kontrolleri
    private void checkYetkili() {
        boolean yetkili = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .anyMatch(a ->
                        a.getAuthority().equals("ROLE_ADMIN") ||
                                a.getAuthority().equals("ROLE_BAS_DOKTOR") ||
                                a.getAuthority().equals("ROLE_DOKTOR")
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
