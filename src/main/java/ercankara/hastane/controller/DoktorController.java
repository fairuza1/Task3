package ercankara.hastane.controller;

import ercankara.hastane.entity.Doktor;
import ercankara.hastane.service.DoktorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doktorlar")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class DoktorController {

    @Autowired
    private DoktorService doktorService;

    // 📋 Tüm doktorları listele (BAS_DOKTOR ve DOKTOR)
    @PreAuthorize("hasAnyRole('ADMIN','BAS_DOKTOR','DOKTOR')")
    @GetMapping
    public List<Doktor> getAllDoktorlar() {
        return doktorService.getAllDoktorlar();
    }

    // 🔍 ID ile doktor getir (BAS_DOKTOR ve DOKTOR)
    @PreAuthorize("hasAnyRole('ADMIN','BAS_DOKTOR','DOKTOR')")
    @GetMapping("/{id}")
    public Doktor getDoktorById(@PathVariable Long id) {
        return doktorService.getDoktorById(id);
    }

    // ➕ Yeni doktor oluştur (sadece BAS_DOKTOR)
    @PreAuthorize("hasRole('BAS_DOKTOR')")
    @PostMapping
    public ResponseEntity<Doktor> createDoktor(@RequestBody Doktor doktor) {
        return ResponseEntity.ok(doktorService.createDoktor(doktor));
    }

    // ✏️ Doktor güncelle (sadece BAS_DOKTOR)
    @PreAuthorize("hasRole('BAS_DOKTOR')")
    @PutMapping("/{id}")
    public ResponseEntity<Doktor> updateDoktor(@PathVariable Long id, @RequestBody Doktor doktor) {
        return ResponseEntity.ok(doktorService.updateDoktor(id, doktor));
    }

    // 🗑️ Doktor sil (sadece BAS_DOKTOR)
    @PreAuthorize("hasRole('BAS_DOKTOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDoktor(@PathVariable Long id) {
        doktorService.deleteDoktor(id);
        return ResponseEntity.ok("Doktor silindi");
    }
}
