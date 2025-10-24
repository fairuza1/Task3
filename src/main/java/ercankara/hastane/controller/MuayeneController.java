package ercankara.hastane.controller;

import ercankara.hastane.entity.Muayene;
import ercankara.hastane.entity.Doktor;
import ercankara.hastane.repository.DoktorRepository;
import ercankara.hastane.service.MuayeneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.List;

@RestController
@RequestMapping("/api/muayeneler")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class MuayeneController {

    @Autowired
    private MuayeneService muayeneService;

    @Autowired
    private DoktorRepository doktorRepository; // 🔹 Eklendi

    // 📋 Tüm muayeneleri listele
    @PreAuthorize("hasAnyRole('ADMIN','BAS_DOKTOR','DOKTOR','SEKRETER')")
    @GetMapping
    public List<Muayene> getAllMuayeneler() {
        return muayeneService.getAllMuayeneler();
    }

    // 🔍 ID ile muayene getir
    @PreAuthorize("hasAnyRole('ADMIN','BAS_DOKTOR','DOKTOR','SEKRETER')")
    @GetMapping("/{id}")
    public Muayene getMuayeneById(@PathVariable Long id) {
        return muayeneService.getMuayeneById(id);
    }

    //  Yeni muayene oluştur
    @PreAuthorize("hasRole('DOKTOR')")
    @PostMapping
    public ResponseEntity<Muayene> createMuayene(@RequestBody Map<String, Object> body) {
        Long kullaniciId = Long.valueOf(body.get("doktorId").toString()); // 👈 Kullanıcı ID alıyoruz
        Long hastaId = Long.valueOf(body.get("hastaId").toString());
        String tani = body.get("tani").toString();

        return ResponseEntity.ok(muayeneService.createMuayene(kullaniciId, hastaId, tani));
    }

    //  Muayene güncelle
    @PreAuthorize("hasAnyRole('DOKTOR','ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Muayene> updateMuayene(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        String tani = body.get("tani").toString();
        return ResponseEntity.ok(muayeneService.updateMuayene(id, tani));
    }

    //  Muayene sil (sadece Admin)
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMuayene(@PathVariable Long id) {
        muayeneService.deleteMuayene(id);
        return ResponseEntity.ok("Muayene silindi.");
    }

    // ️ Belirli bir doktorun muayeneleri (kullanıcı ID’sine göre)
    @PreAuthorize("hasAnyRole('ADMIN','BAS_DOKTOR','DOKTOR')")
    @GetMapping("/doktor/{kullaniciId}")
    public List<Muayene> getMuayenelerByDoktor(@PathVariable Long kullaniciId) {
        // 🔎 Kullanıcı ID’den doktoru buluyoruz
        Doktor doktor = doktorRepository.findByKullaniciId(kullaniciId)
                .orElseThrow(() -> new RuntimeException("Doktor bulunamadı!"));
        // Ardından o doktora ait muayeneleri çekiyoruz
        return muayeneService.getMuayenelerByDoktor(doktor.getId());
    }

    // Belirli bir hastanın muayeneleri
    @PreAuthorize("hasAnyRole('ADMIN','BAS_DOKTOR','DOKTOR','SEKRETER')")
    @GetMapping("/hasta/{hastaId}")
    public List<Muayene> getMuayenelerByHasta(@PathVariable Long hastaId) {
        return muayeneService.getMuayenelerByHasta(hastaId);
    }
    @PreAuthorize("hasAnyRole('ADMIN','BAS_DOKTOR','DOKTOR','SEKRETER')")
    @GetMapping("/hasta/{hastaId}/receteler")
    public List<Muayene> getHastaReceteleri(@PathVariable Long hastaId) {
        return muayeneService.getMuayenelerByHasta(hastaId);
    }
}
