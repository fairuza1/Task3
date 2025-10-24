package ercankara.hastane.service;

import ercankara.hastane.entity.Doktor;
import ercankara.hastane.entity.Hasta;
import ercankara.hastane.repository.DoktorRepository;
import ercankara.hastane.repository.HastaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class HastaService {

    @Autowired
    private HastaRepository hastaRepository;

    @Autowired
    private DoktorRepository doktorRepository;


    public List<Hasta> getAllHastalar() {
        checkSekreterOrDoktorYetkisi();
        return hastaRepository.findAll();
    }


    public Hasta getHastaById(Long id) {
        checkSekreterOrDoktorYetkisi();
        return hastaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hasta bulunamadı"));
    }


    public Hasta createHasta(Hasta hasta) {
        checkSekreterYetkisi();

        if (hastaRepository.existsByTcKimlikNo(hasta.getTcKimlikNo())) {
            throw new RuntimeException("Bu TC kimlik numarasına sahip bir hasta zaten mevcut!");
        }


        if (hasta.getDoktor() != null && hasta.getDoktor().getId() != null) {
            Doktor doktor = doktorRepository.findById(hasta.getDoktor().getId())
                    .orElseThrow(() -> new RuntimeException("Doktor bulunamadı"));
            hasta.setDoktor(doktor);
        }

        hasta.setOlusturulmaTarihi(LocalDateTime.now());
        return hastaRepository.save(hasta);
    }


    public Hasta updateHasta(Long id, Hasta updated) {
        checkSekreterYetkisi();
        Hasta hasta = getHastaById(id);
        hasta.setAdSoyad(updated.getAdSoyad());
        hasta.setTcKimlikNo(updated.getTcKimlikNo());
        hasta.setDogumTarihi(updated.getDogumTarihi());
        hasta.setTelefon(updated.getTelefon());
        hasta.setAdres(updated.getAdres());


        if (updated.getDoktor() != null && updated.getDoktor().getId() != null) {
            Doktor doktor = doktorRepository.findById(updated.getDoktor().getId())
                    .orElseThrow(() -> new RuntimeException("Doktor bulunamadı"));
            hasta.setDoktor(doktor);
        }

        return hastaRepository.save(hasta);
    }

    public void deleteHasta(Long id) {
        checkSekreterYetkisi();
        hastaRepository.deleteById(id);
    }

    public Hasta assignDoctor(Long hastaId, Long doktorId) {
        checkSekreterYetkisi();
        Hasta hasta = getHastaById(hastaId);
        Doktor doktor = doktorRepository.findById(doktorId)
                .orElseThrow(() -> new RuntimeException("Doktor bulunamadı"));
        hasta.setDoktor(doktor);
        return hastaRepository.save(hasta);
    }

    private void checkSekreterYetkisi() {
        boolean yetkili = SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                .stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_SEKRETER") ||
                        a.getAuthority().equals("ROLE_ADMIN")); // ✅ 'ROLE_SECRETER' yanlış yazılmıştı
        if (!yetkili) {
            throw new RuntimeException("Bu işlemi sadece SEKRETER veya ADMIN yapabilir!");
        }
    }

    private void checkSekreterOrDoktorYetkisi() {
        boolean yetkili = SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                .stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_SEKRETER") ||
                        a.getAuthority().equals("ROLE_DOKTOR") ||
                        a.getAuthority().equals("ROLE_ADMIN"));
        if (!yetkili) {
            throw new RuntimeException("Bu işlemi sadece SEKRETER, DOKTOR veya ADMIN yapabilir!");
        }
    }

}
