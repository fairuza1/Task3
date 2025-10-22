package ercankara.hastane.repository;

import ercankara.hastane.entity.Muayene;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MuayeneRepository extends JpaRepository<Muayene, Long> {
    List<Muayene> findByDoktorId(Long doktorId);
    List<Muayene> findByHastaId(Long hastaId);
}
