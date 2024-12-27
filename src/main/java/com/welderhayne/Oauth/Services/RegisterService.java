package com.welderhayne.Oauth.Services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.welderhayne.Oauth.Dtos.RegisterDto;
import com.welderhayne.Oauth.Enums.Permitions;
import com.welderhayne.Oauth.Models.Register;
import com.welderhayne.Oauth.Repositories.RegisterRepository;
import jakarta.persistence.EntityExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class RegisterService {
    @Autowired
    private RegisterRepository registerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    public void saveRegister(String username, String email, String password) {
        LocalDateTime localDateTime = LocalDateTime.now();
        Register newRegister = new Register();

        String idKey = UUID.randomUUID().toString();
        String salt = BCrypt.gensalt();

        newRegister.setIdKey(idKey);
        newRegister.setUsername(username);
        newRegister.setEmail(email);
        newRegister.setPassword(passwordEncoder.encode(salt + password));
        newRegister.setSalt(salt);
        newRegister.setPermition(Permitions.USER);
        newRegister.setCreatedAt(localDateTime);
        newRegister.setUpdatedAt(localDateTime);

        registerRepository.save(newRegister);
    }

    public RegisterDto findById(int id) {
        Register register = registerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Register not found"));

        return objectMapper.convertValue(register, RegisterDto.class);
    }

    public RegisterDto findByUsername(String username) {
        Register register = registerRepository.findRegisterByUsername(username)
                .orElseThrow(() -> new RuntimeException("Register not found"));

        return objectMapper.convertValue(register, RegisterDto.class);
    }

    public RegisterDto findByEmail(String email) {
        Register register = registerRepository.findRegisterByEmail(email)
                .orElseThrow(() -> new RuntimeException("Register not found"));

        return objectMapper.convertValue(register, RegisterDto.class);
    }

    public List<RegisterDto> getAllRegisters() {
        return registerRepository.findAll()
                .stream()
                .map(item -> objectMapper.convertValue(item, RegisterDto.class))
                .toList();
    }

    public void deleteRegister(int id) {
        registerRepository.deleteById(id);
    }

    public String getSalt(String usernameOrEmail) {
        Register register = usernameOrEmail.contains("@") ?
                registerRepository.findRegisterByEmail(usernameOrEmail)
                        .orElseThrow(() -> new RuntimeException("Client not found")) :
                registerRepository.findRegisterByUsername(usernameOrEmail)
                        .orElseThrow(() -> new RuntimeException("Client not found"));

        return register.getSalt();
    }

    public Register getRegister(String usernameOrEmail) {
        return usernameOrEmail.contains("@") ?
                registerRepository.findRegisterByEmail(usernameOrEmail)
                        .orElseThrow(() -> new EntityExistsException("Register not found")):
                registerRepository.findRegisterByUsername(usernameOrEmail)
                        .orElseThrow(() -> new EntityExistsException("Register not found"));
    }
}
