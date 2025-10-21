package ercankara.hastane.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Sekreter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String adSoyad;
    private String telefon;
    private String email;

    @OneToOne
    @JoinColumn(name = "kullanici_id")
    private User kullanici; // user tablosu ile birebir ilişki

    private LocalDateTime olusturulmaTarihi;
}
