package com.welderhayne.Oauth.Utils;

import com.welderhayne.Oauth.Models.Register;
import com.welderhayne.Oauth.Repositories.RegisterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private RegisterRepository registerRepository;

    @Override
    public UserDetails loadUserByUsername(String emailOrUsername) throws UsernameNotFoundException {
        Register register = emailOrUsername.contains("@") ?
                registerRepository.findRegisterByEmail(emailOrUsername)
                        .orElseThrow(() -> new RuntimeException("Client not found")) :
                registerRepository.findRegisterByUsername(emailOrUsername)
                        .orElseThrow(() -> new RuntimeException("Client not found"));

        UserDetails userDetails = User.builder()
                .username(register.getUsername())
                .authorities(register.getPermition().toString())
                .password(register.getPassword())
                .build();

        return userDetails;
    }
}
