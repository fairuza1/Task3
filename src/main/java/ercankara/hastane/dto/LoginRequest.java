package ercankara.hastane.dto;

import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern; // <-- DOĞRU IMPORT
import lombok.Data;

@Data
public class LoginRequest {

    @Size(min = 4, max = 255)
    private String kullaniciAdi;

    @Size(min = 8, max = 255)
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$",
            message = "Password must contain at least one uppercase letter, one lowercase letter, and one digit.")
    private String sifre;

    private String email;

}
