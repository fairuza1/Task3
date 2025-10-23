package ercankara.hastane.config;

import ercankara.hastane.filter.JwtRequestFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtRequestFilter jwtRequestFilter;

    public SecurityConfig(JwtRequestFilter jwtRequestFilter) {
        this.jwtRequestFilter = jwtRequestFilter;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> auth
                        // 🔓 Auth endpoint'leri herkese açık
                        .requestMatchers(
                                "/api/auth/login",
                                "/api/auth/signup",
                                "/api/auth/refresh",
                                "/api/auth/validate-token",
                                "/api/auth/me"
                        ).permitAll()

                        // 👤 Kullanıcı işlemleri
                        .requestMatchers("/api/kullanicilar/**")
                        .hasAnyRole("ADMIN", "BAS_DOKTOR")

                        // 👨‍⚕️ Doktor işlemleri
                        .requestMatchers("/api/doktorlar/**")
                        .hasAnyRole("ADMIN", "BAS_DOKTOR", "DOKTOR", "SEKRETER")

                        // 🩺 Hasta işlemleri
                        .requestMatchers("/api/hastalar/**")
                        .hasAnyRole("SEKRETER", "DOKTOR","ADMIN","BAS_DOKTOR")

                        // 💊 Reçete işlemleri
                        .requestMatchers("/api/receteler/**")
                        .hasAnyRole("DOKTOR", "ADMIN", "BAS_DOKTOR")

                        // ⚕️ Muayene işlemleri
                        .requestMatchers("/api/muayeneler/**")
                        .hasAnyRole("DOKTOR", "ADMIN", "BAS_DOKTOR", "SEKRETER")

                        // 🌐 Diğer tüm istekler JWT doğrulaması ister
                        .anyRequest().authenticated()
                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public org.springframework.web.filter.CorsFilter corsFilter() {
        return new org.springframework.web.filter.CorsFilter(corsConfigurationSource());
    }

    private UrlBasedCorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOrigins(List.of("http://localhost:5173"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
