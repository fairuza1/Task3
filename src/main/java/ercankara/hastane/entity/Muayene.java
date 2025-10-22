package ercankara.hastane.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Muayene {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 👨‍⚕️ Muayeneyi yapan doktor
    @ManyToOne
    @JoinColumn(name = "doktor_id", nullable = false)
    private Doktor doktor;

    // 🧍‍♂️ Muayene edilen hasta
    @ManyToOne
    @JoinColumn(name = "hasta_id", nullable = false)
    private Hasta hasta;

    private LocalDateTime tarih;

    @Column(length = 1000)
    private String tani; // Doktorun tanısı

    // 🔗 Reçete (tekil ilişki)
    @OneToOne(mappedBy = "muayene", cascade = CascadeType.ALL)
    private Recete recete;
}
