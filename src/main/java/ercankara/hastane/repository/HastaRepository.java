package ercankara.hastane.repository;

import ercankara.hastane.entity.Hasta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HastaRepository extends JpaRepository<Hasta, Long> {
    boolean existsByTcKimlikNo(String tcKimlikNo);
}
