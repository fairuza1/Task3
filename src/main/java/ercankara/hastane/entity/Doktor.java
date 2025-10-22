package ercankara.hastane.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

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

    // 📋 Doktorun yaptığı muayeneler (1:N)
    @JsonIgnore
    @OneToMany(mappedBy = "doktor", cascade = CascadeType.ALL)
    private List<Muayene> muayeneler;

}
