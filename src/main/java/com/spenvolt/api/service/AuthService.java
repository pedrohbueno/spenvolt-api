package com.spenvolt.api.service;

import com.spenvolt.api.dto.UserRequestDTO;
import com.spenvolt.api.model.User;
import com.spenvolt.api.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtEncoder jwtEncoder;

    public String register(UserRequestDTO request) {
        if (userRepository.findByEmail(request.getEmail()) != null) {
            throw new IllegalArgumentException("Email já cadastrado");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));

        userRepository.save(user);

        return "Usuário cadastrado com sucesso";
    }

    public String login(String email, String password) {
        User user = userRepository.findByEmail(email);

        if (user == null ||
                !passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new BadCredentialsException("Email ou senha inválidos");
        }

        Instant now = Instant.now();

        JwtClaimsSet.Builder claimsBuilder = JwtClaimsSet.builder();
        claimsBuilder.issuer("spenvolt-api");
        claimsBuilder.issuedAt(now);
        claimsBuilder.expiresAt(now.plusSeconds(3600));
        claimsBuilder.subject(user.getEmail());
        JwtClaimsSet claims = claimsBuilder.build();

        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();

        return jwtEncoder.encode(
                JwtEncoderParameters.from(header, claims)
        ).getTokenValue();
    }

    public User getAuthenticatedUser() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        // Aceita somente autenticação JWT validada pelo Spring Security.
        if (!(authentication instanceof JwtAuthenticationToken jwtAuth)) {
            throw new AuthenticationCredentialsNotFoundException(
                    "Usuário não autenticado"
            );
        }

        // O subject do seu JWT contém o email.
        String email = jwtAuth.getToken().getSubject();

        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new AuthenticationCredentialsNotFoundException(
                    "Usuário autenticado não encontrado"
            );
        }

        return user;
    }
}