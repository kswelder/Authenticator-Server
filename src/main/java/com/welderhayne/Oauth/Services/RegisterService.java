package com.welderhayne.Oauth.Services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.welderhayne.Oauth.Dtos.RegisterDto;
import com.welderhayne.Oauth.Enums.Permitions;
import com.welderhayne.Oauth.Exceptions.DuplicateData;
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
        if (registerRepository.existsByUsername(username)) throw new DuplicateData("Username alread exists");
        if (registerRepository.existsByEmail(email)) throw new DuplicateData("Email alread exists");

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

    public void updateUser(String id, String username, String email) {
        Register register = registerRepository.findRegisterByIdKey(id)
                .orElseThrow(() -> new RuntimeException("Register not found"));

        if (registerRepository.existsByUsername(username)) throw new EntityExistsException("Username alread exists");
        if (registerRepository.existsByEmail(email)) throw new EntityExistsException("Email alread exists");

        register.setUsername(username);
        register.setEmail(email);
        register.setUpdatedAt(LocalDateTime.now());

        registerRepository.save(register);
    }

    public void updatePasswordRegister(String id, String password) {
        Register register = registerRepository.findRegisterByIdKey(id)
                .orElseThrow(() -> new RuntimeException("Register not found"));

        register.setSalt(BCrypt.gensalt());
        register.setPassword(passwordEncoder.encode(register.getSalt() + password));
        register.setUpdatedAt(LocalDateTime.now());

        registerRepository.save(register);
    }

    public void updatePermition(String id, String perm) {
        Register register = registerRepository.findRegisterByIdKey(id)
                .orElseThrow(() -> new RuntimeException("Register not found"));

        register.setPermition(Permitions.valueOf(perm));

        registerRepository.save(register);
    }

    public RegisterDto findById(String id) {
        Register register = registerRepository.findRegisterByIdKey(id)
                .orElseThrow(() -> new RuntimeException("Register not found"));

        RegisterDto dto = objectMapper.convertValue(register, RegisterDto.class);

        dto.setId(register.getIdKey());

        return dto;
    }

    public RegisterDto findByUsername(String username) {
        Register register = registerRepository.findRegisterByUsername(username)
                .orElseThrow(() -> new RuntimeException("Register not found"));

        RegisterDto dto = objectMapper.convertValue(register, RegisterDto.class);

        dto.setId(register.getIdKey());

        return dto;
    }

    public RegisterDto findByEmail(String email) {
        Register register = registerRepository.findRegisterByEmail(email)
                .orElseThrow(() -> new RuntimeException("Register not found"));

        RegisterDto dto = objectMapper.convertValue(register, RegisterDto.class);

        dto.setId(register.getIdKey());

        return dto;
    }

    public List<RegisterDto> getAllRegisters() {
        return registerRepository.findAll()
                .stream()
                .map(item -> {
                    RegisterDto dto = objectMapper.convertValue(item, RegisterDto.class);
                    dto.setId(item.getIdKey());
                    return dto;
                })
                .toList();
    }

    public void deleteRegister(String id) {
        boolean deleted = registerRepository.deleteByIdKey(id);
        if (!deleted) throw new RuntimeException("Can't deleted id: "+ id);
        System.out.println(id + " deleted");
    }

    public Register getRegister(String usernameOrEmail) {
        return usernameOrEmail.contains("@") ?
                registerRepository.findRegisterByEmail(usernameOrEmail)
                        .orElseThrow(() -> new EntityExistsException("Register not found")):
                registerRepository.findRegisterByUsername(usernameOrEmail)
                        .orElseThrow(() -> new EntityExistsException("Register not found"));
    }
}
