package ercankara.hastane.entity;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

public class Hasta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String adSoyad;

    @Column(nullable = false, unique = true)
    private String tcKimlikNo;

    @Column(nullable = false)
    private LocalDateTime dogumTarihi;

    @Column(nullable = false)
    private String telefon;
}
