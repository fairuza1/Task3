package ercankara.hastane.repository;

import ercankara.hastane.entity.Sekreter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SekreterRepository extends JpaRepository<Sekreter, Long> {
}
