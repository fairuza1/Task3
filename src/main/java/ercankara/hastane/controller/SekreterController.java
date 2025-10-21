package ercankara.hastane.controller;

import ercankara.hastane.entity.Sekreter;
import ercankara.hastane.service.SekreterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sekreterler")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class SekreterController {

    @Autowired
    private SekreterService sekreterService;

    // 📋 Tüm sekreterleri listele
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<Sekreter> getAllSekreterler() {
        return sekreterService.getAllSekreterler();
    }

    // 🔍 ID ile sekreter getir
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public Sekreter getSekreterById(@PathVariable Long id) {
        return sekreterService.getSekreterById(id);
    }

    // ➕ Yeni sekreter oluştur
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Sekreter> createSekreter(@RequestBody Sekreter sekreter) {
        return ResponseEntity.ok(sekreterService.createSekreter(sekreter));
    }

    // ✏️ Sekreter güncelle
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Sekreter> updateSekreter(@PathVariable Long id, @RequestBody Sekreter sekreter) {
        return ResponseEntity.ok(sekreterService.updateSekreter(id, sekreter));
    }

    // 🗑️ Sekreter sil
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSekreter(@PathVariable Long id) {
        sekreterService.deleteSekreter(id);
        return ResponseEntity.ok("Sekreter silindi");
    }
}
