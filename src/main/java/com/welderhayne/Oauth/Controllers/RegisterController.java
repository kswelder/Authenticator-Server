package com.welderhayne.Oauth.Controllers;

import com.welderhayne.Oauth.Dtos.AuthorizationResponse;
import com.welderhayne.Oauth.Dtos.RegisterDto;
import com.welderhayne.Oauth.Models.Register;
import com.welderhayne.Oauth.Services.ClaimService;
import com.welderhayne.Oauth.Services.RegisterService;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class RegisterController {
    @Autowired
    private RegisterService registerService;

    @Autowired
    private ClaimService claimService;

    @GetMapping("/register")
    public ResponseEntity<RegisterDto> getRegister(@RequestHeader("Authorization") String authorization) {
        AuthorizationResponse authClaim = claimService.getClaims(authorization);

        return ResponseEntity.ok(registerService.findByUsername(authClaim.getUsername()));
    }

    @PutMapping("/update/register")
    public ResponseEntity<Void> updateRegister(
            @RequestHeader("Authorization") String authorization,
            @NotBlank @RequestParam("username") String username,
            @NotBlank @RequestParam("email") String email
    ) {
        AuthorizationResponse authClaim = claimService.getClaims(authorization);

        Register register = registerService.getRegister(authClaim.getUsername());

        registerService.updateUser(register.getIdKey(), username, email);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/update/password")
    public ResponseEntity<Void> updatePassword(
            @RequestHeader("Authorization") String authorization,
            @NotBlank @RequestParam("password") String password
    ) {
        AuthorizationResponse authClaim = claimService.getClaims(authorization);

        Register register = registerService.getRegister(authClaim.getUsername());

        registerService.updatePasswordRegister(register.getIdKey(), password);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteRegister(@RequestHeader("Authorization") String authorization) {
        AuthorizationResponse authClaim = claimService.getClaims(authorization);

        RegisterDto register = registerService.findByUsername(authClaim.getUsername());

        registerService.deleteRegister(register.getId());

        return ResponseEntity.noContent().build();
    }
}
