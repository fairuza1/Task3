package ercankara.hastane.repository;

import ercankara.hastane.entity.Recete;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReceteRepository extends JpaRepository<Recete, Long> {
    Recete findByMuayeneId(Long muayeneId);
}
