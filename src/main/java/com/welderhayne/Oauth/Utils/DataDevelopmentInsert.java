package com.welderhayne.Oauth.Utils;

import com.welderhayne.Oauth.Dtos.RegisterDto;
import com.welderhayne.Oauth.Services.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Profile({"dev", "test"})
public class DataDevelopmentInsert {
    @Autowired
    private RegisterService registerService;

    @EventListener
    public void handlerContextRefresh(ContextRefreshedEvent event) {
        registerService.saveRegister("kswelder","email@email.com","5544");
        RegisterDto register = registerService.findByUsername("kswelder");
        registerService.updatePermition(register.getId(), "ADMIN");

        registerService.saveRegister("mrbucaki","mrbucaki370@email.com","5544");
        registerService.saveRegister("ElPepe","skolbeach@email.com","5544");
        registerService.saveRegister("ElonMusk","workaholic@email.com","5544");
        registerService.saveRegister("KekiusMaximus","wtfidoinghere@email.com","5544");
        registerService.saveRegister("JoeBiden","oldpresidentofamerica@email.com","5544");
    }
}
