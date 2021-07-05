package com.akarinti.preapproved.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class BeanCollection {
    @Bean
    public PasswordEncoder passwordEncoder() {
//        PasswordEncoder defaultEncoder = new StandardPasswordEncoder("nostra");
        String encodingId = "bcrypt";
        Map<String, PasswordEncoder> encoders = new HashMap<>();
        encoders.put("bcrypt", new BCryptPasswordEncoder());
        encoders.put("sha256", new StandardPasswordEncoder("nostra"));
        encoders.put(null, new StandardPasswordEncoder("nostra"));
        DelegatingPasswordEncoder passwordEncoder = new DelegatingPasswordEncoder(encodingId, encoders);
//        passwordEncoder.setDefaultPasswordEncoderForMatches(defaultEncoder);

        return passwordEncoder;
    }
}
