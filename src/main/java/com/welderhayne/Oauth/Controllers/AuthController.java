package com.welderhayne.Oauth.Controllers;

import com.welderhayne.Oauth.Models.Register;
import com.welderhayne.Oauth.Services.RegisterService;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping("/oauth2")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtEncoder jwtEncoder;

    @Autowired
    private RegisterService registerService;

    private String generateToken(Authentication authentication) {
        Instant now = Instant.now();
        long expiry = 3600; // Token válido por 1 hora

        String token = jwtEncoder.encode(JwtEncoderParameters.from(JwtClaimsSet.builder()
                .issuer("Oauth2-Server-Authenticator")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiry))
                .subject(authentication.getName())
                .claim("roles", authentication.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .toList())
                .build())).getTokenValue();

        return token;
    }

    @PostMapping("/token")
    public ResponseEntity<Map<String, String>> login(
            @NotBlank @RequestParam("emailOrUsername") String emailOrUsername,
            @NotBlank @RequestParam("password") String password) {

        try {
            Register register = registerService.getRegister(emailOrUsername);

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(register.getUsername(), register.getSalt() + password)
            );

            String token = generateToken(authentication);

            System.out.printf("Token: " + token);
            return ResponseEntity.ok(Map.of("access_token", token));
        }
        catch (RuntimeException error) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @NotBlank @RequestParam("username") String username,
            @NotBlank @RequestParam("email") String email,
            @NotBlank @RequestParam("password") String password) {
        registerService.saveRegister(username, email, password);

        return ResponseEntity.noContent().build();
    }
}
