package com.welderhayne.Oauth.Controllers;

import com.welderhayne.Oauth.Dtos.RegisterDto;
import com.welderhayne.Oauth.Services.RegisterService;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private RegisterService registerService;

    @GetMapping("/all")
    public ResponseEntity<List<RegisterDto>> getAll() {
        return ResponseEntity.ok(registerService.getAllRegisters());
    }

    @GetMapping("/find/register/id/{id}")
    public ResponseEntity<RegisterDto> getRegisterById(@PathVariable("id") String id) {
        return ResponseEntity.ok(registerService.findById(id));
    }

    @GetMapping("/find/register/username/{username}")
    public ResponseEntity<RegisterDto> getRegisterByUsername(@PathVariable("username") String username) {
        return ResponseEntity.ok(registerService.findByUsername(username));
    }

    @GetMapping("/find/register/email/{email}")
    public ResponseEntity<RegisterDto> getRegisterByEmail(@PathVariable("email") String email) {
        return ResponseEntity.ok(registerService.findByEmail(email));
    }

    @PutMapping("/update/register")
    public ResponseEntity<Void> updateRegister(
            @NotBlank @RequestParam("id") String id,
            @NotBlank @RequestParam("username") String username,
            @NotBlank @RequestParam("email") String email
    ) {
        registerService.updateUser(id, username, email);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/update/password")
    public ResponseEntity<Void> updatePassword(
            @NotBlank @RequestParam("id") String id,
            @NotBlank @RequestParam("password") String password
    ) {
        registerService.updatePasswordRegister(id, password);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/update/permition")
    public ResponseEntity<Void> updatePermition(
            @NotBlank @RequestParam("id") String id,
            @NotBlank @RequestParam("perm") String perm
    ) {
        registerService.updatePermition(id, perm);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteRegister(@NotBlank @PathVariable("id") String id) {
        registerService.deleteRegister(id);
        return ResponseEntity.noContent().build();
    }
}
