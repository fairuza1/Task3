package ercankara.hastane.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
@Entity
@Data
@Table(name = "doktors")
public class Doktor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String adSoyad;

    @Column(nullable = false)
    private String uzmanlikAlani;

    @Column(nullable = false)
    private String telefon;

    @Column(nullable = false)
    private Long kullaniciId;




}
