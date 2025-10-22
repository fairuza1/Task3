package ercankara.hastane.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Recete {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 🩺 Her reçete bir muayeneye bağlı
    @OneToOne
    @JoinColumn(name = "muayene_id", nullable = false)
    private Muayene muayene;

    private String ilacAdi;
    private String doz;
    private String aciklama;
}
