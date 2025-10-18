package ercankara.hastane.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;
import java.util.List;


@Entity
@Data
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String kullaniciAdi;

    @Column(nullable = false)
    private String sifre;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String rol;

    @Column(nullable = false)
    private Boolean aktif ;

    @Column(nullable = false)
    private  LocalDateTime olusturulmaTarihi;
}
