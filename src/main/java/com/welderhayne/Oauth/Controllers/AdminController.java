package com.welderhayne.Oauth.Controllers;

import com.welderhayne.Oauth.Dtos.RegisterDto;
import com.welderhayne.Oauth.Services.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
