package ercankara.hastane.controller;

import ercankara.hastane.entity.Recete;
import ercankara.hastane.service.ReceteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/receteler")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class ReceteController {

    @Autowired
    private ReceteService receteService;

    //  Tüm reçeteleri listele (Admin, Doktor, Baş Doktor)
    @PreAuthorize("hasAnyRole('ADMIN','BAS_DOKTOR','DOKTOR')")
    @GetMapping
    public List<Recete> getAllReceteler() {
        return receteService.getAllReceteler();
    }

    //  ID ile reçete getir
    @PreAuthorize("hasAnyRole('ADMIN','BAS_DOKTOR','DOKTOR')")
    @GetMapping("/{id}")
    public Recete getReceteById(@PathVariable Long id) {
        return receteService.getReceteById(id);
    }

    //  Yeni reçete oluştur (sadece Doktor)
    @PreAuthorize("hasRole('DOKTOR')")
    @PostMapping
    public ResponseEntity<Recete> createRecete(@RequestBody Map<String, Object> body) {
        Long muayeneId = Long.valueOf(body.get("muayeneId").toString());
        String ilacAdi = body.get("ilacAdi").toString();
        String doz = body.get("doz").toString();
        String aciklama = body.get("aciklama").toString();

        Recete recete = receteService.createRecete(muayeneId, ilacAdi, doz, aciklama);
        return ResponseEntity.ok(recete);
    }

    //  Reçete güncelle (sadece Doktor veya Admin)
    @PreAuthorize("hasAnyRole('DOKTOR','ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Recete> updateRecete(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        String ilacAdi = body.get("ilacAdi").toString();
        String doz = body.get("doz").toString();
        String aciklama = body.get("aciklama").toString();

        Recete updated = receteService.updateRecete(id, ilacAdi, doz, aciklama);
        return ResponseEntity.ok(updated);
    }

    //  Reçete sil (sadece Admin)
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRecete(@PathVariable Long id) {
        receteService.deleteRecete(id);
        return ResponseEntity.ok("Reçete silindi.");
    }
}
