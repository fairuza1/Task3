package ercankara.hastane.controller;

import ercankara.hastane.entity.Hasta;
import ercankara.hastane.service.HastaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hastalar")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class HastaController {

    @Autowired
    private HastaService hastaService;

    // 📋 Tüm hastaları listele (sekreter ve doktor)
    @PreAuthorize("hasAnyRole('SEKRETER','DOKTOR')")
    @GetMapping
    public List<Hasta> getAllHastalar() {
        return hastaService.getAllHastalar();
    }

    // 🔍 ID ile hasta getir
    @PreAuthorize("hasAnyRole('SEKRETER','DOKTOR')")
    @GetMapping("/{id}")
    public Hasta getHastaById(@PathVariable Long id) {
        return hastaService.getHastaById(id);
    }

    // ➕ Yeni hasta oluştur (sadece SEKRETER)
    @PreAuthorize("hasRole('SEKRETER')")
    @PostMapping
    public ResponseEntity<Hasta> createHasta(@RequestBody Hasta hasta) {
        return ResponseEntity.ok(hastaService.createHasta(hasta));
    }

    // ✏️ Hasta güncelle (sadece SEKRETER)
    @PreAuthorize("hasRole('SEKRETER')")
    @PutMapping("/{id}")
    public ResponseEntity<Hasta> updateHasta(@PathVariable Long id, @RequestBody Hasta hasta) {
        return ResponseEntity.ok(hastaService.updateHasta(id, hasta));
    }

    // 🗑️ Hasta sil (sadece SEKRETER)
    @PreAuthorize("hasRole('SEKRETER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteHasta(@PathVariable Long id) {
        hastaService.deleteHasta(id);
        return ResponseEntity.ok("Hasta silindi");
    }

    // 👩‍⚕️ Hasta – Doktor eşleştir
    @PreAuthorize("hasRole('SEKRETER')")
    @PutMapping("/{hastaId}/doktor/{doktorId}")
    public ResponseEntity<Hasta> assignDoctor(@PathVariable Long hastaId, @PathVariable Long doktorId) {
        return ResponseEntity.ok(hastaService.assignDoctor(hastaId, doktorId));
    }
}
