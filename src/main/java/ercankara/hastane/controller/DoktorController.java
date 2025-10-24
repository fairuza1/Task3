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

    // 📋 Tüm doktorları listele (Admin, Baş Doktor, Doktor)
    @PreAuthorize("hasAnyRole('ADMIN','BAS_DOKTOR','DOKTOR','SEKRETER')")
    @GetMapping
    public List<Doktor> getAllDoktorlar() {
        return doktorService.getAllDoktorlar();
    }

    //  ID ile doktor getir
    @PreAuthorize("hasAnyRole('ADMIN','BAS_DOKTOR','DOKTOR','SEKRETER')")
    @GetMapping("/{id}")
    public Doktor getDoktorById(@PathVariable Long id) {
        return doktorService.getDoktorById(id);
    }

    // Yeni doktor oluştur
    @PreAuthorize("hasAnyRole('ADMIN','BAS_DOKTOR')")
    @PostMapping
    public ResponseEntity<Doktor> createDoktor(@RequestBody Doktor doktor) {
        return ResponseEntity.ok(doktorService.createDoktor(doktor));
    }

    //  Doktor güncelle
    @PreAuthorize("hasAnyRole('ADMIN','BAS_DOKTOR')")
    @PutMapping("/{id}")
    public ResponseEntity<Doktor> updateDoktor(@PathVariable Long id, @RequestBody Doktor doktor) {
        return ResponseEntity.ok(doktorService.updateDoktor(id, doktor));
    }

    //  Doktor sil
    @PreAuthorize("hasAnyRole('ADMIN','BAS_DOKTOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDoktor(@PathVariable Long id) {
        doktorService.deleteDoktor(id);
        return ResponseEntity.ok("Doktor silindi");
    }
}
