package com.welderhayne.Oauth.Controllers;

import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.welderhayne.Oauth.Dtos.AuthorizationResponse;
import com.welderhayne.Oauth.Models.Register;
import com.welderhayne.Oauth.Services.ClaimService;
import com.welderhayne.Oauth.Services.RegisterService;
import jakarta.validation.constraints.NotBlank;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/oauth2")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtEncoder jwtEncoder;

    @Autowired
    private JwtDecoder jwtDecoder;

    @Autowired
    private RegisterService registerService;

    @Autowired
    private ClaimService claimService;

    private String generateToken(Authentication authentication) {
        Instant now = Instant.now();
        long expiry = 3600;

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
    public ResponseEntity<String> login(
            @NotBlank @RequestParam("usernameOrEmail") String usernameOrEmail,
            @NotBlank @RequestParam("password") String password) {

        try {
            Register register = registerService.getRegister(usernameOrEmail);

            GrantedAuthority authority = new SimpleGrantedAuthority(register.getPermition().name());

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            register.getUsername(),
                            register.getSalt() + password,
                            List.of(authority)
                    ));

            String token = generateToken(authentication);

            return ResponseEntity.ok(token);
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

    @SneakyThrows
    @GetMapping("/authorities")
    public ResponseEntity<AuthorizationResponse> getAuthorities(@RequestHeader("Authorization") String authorization) {
        return ResponseEntity.ok(claimService.getClaims(authorization));
    }
}
