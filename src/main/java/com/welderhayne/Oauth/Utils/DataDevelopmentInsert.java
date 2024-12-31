package com.welderhayne.Oauth.Utils;

import com.welderhayne.Oauth.Enums.Permitions;
import com.welderhayne.Oauth.Models.Register;
import com.welderhayne.Oauth.Repositories.RegisterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
@Profile({"dev", "test"})
public class DataDevelopmentInsert {
    @Autowired
    private RegisterRepository registerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @EventListener
    public void handlerContextRefresh(ContextRefreshedEvent event) {
        Register register = new Register();
        LocalDateTime date = LocalDateTime.now();

        register.setIdKey(UUID.randomUUID().toString());
        register.setUsername("kswelder");
        register.setEmail("email@email.com");
        register.setSalt(BCrypt.gensalt());
        register.setPassword(passwordEncoder.encode(register.getSalt() + "5544"));
        register.setPermition(Permitions.ADMIN);
        register.setUpdatedAt(date);
        register.setCreatedAt(date);

        registerRepository.save(register);
    }
}
