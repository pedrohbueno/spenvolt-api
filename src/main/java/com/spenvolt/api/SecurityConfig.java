package com.spenvolt.api;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {

        http.csrf(csrf -> csrf.disable());

        http.authorizeHttpRequests(auth ->
                auth.anyRequest().authenticated()
        );

        http.httpBasic(Customizer.withDefaults());

        return http.build();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        // Usado para gerar e validar hashes de senhas.
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtEncoder jwtEncoder(
            @Value("${JWT_SECRET}") String secret) {

        // A chave do .env deve estar codificada em Base64.
        byte[] keyBytes = secret.getBytes(java.nio.charset.StandardCharsets.UTF_8);

        // HS256 exige uma chave de pelo menos 256 bits.
        if (keyBytes.length < 32) {
            throw new IllegalArgumentException(
                    "JWT_SECRET precisa ter pelo menos 32 bytes."
            );
        }

        SecretKey key = new SecretKeySpec(keyBytes, "HmacSHA256");

        return new NimbusJwtEncoder(new ImmutableSecret<>(key));
    }
}