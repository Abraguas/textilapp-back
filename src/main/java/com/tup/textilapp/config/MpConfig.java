package com.tup.textilapp.config;

import com.mercadopago.MercadoPagoConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class MpConfig implements CommandLineRunner {
    @Value("${mercadopago.accesstoken}")
    private String token;

    @Override
    public void run(String... args) {
        MercadoPagoConfig.setAccessToken(token);
    }
}
