package ercankara.hastane.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "doktorlar")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Doktor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String adSoyad;

    @Column(nullable = false)
    private String uzmanlikAlani;

    @Column(nullable = false)
    private String telefon;

    // 🧑‍⚕️ Kullanıcı ile birebir ilişki
    @OneToOne
    @JoinColumn(name = "kullanici_id", referencedColumnName = "id")
    private User kullanici;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime olusturulmaTarihi;
}
