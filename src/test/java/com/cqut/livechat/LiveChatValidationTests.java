package com.cqut.livechat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
class LiveChatValidationTests {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    void contextLoads() {
    }

    @Test
    void demo_password_generation() {
        String encode = passwordEncoder.encode("123456");
        System.out.println(encode);
        // $2a$10$yPTiPyvfhGIENHLEOfCaE.B1DQc1rHCATWwEdkvU52L6vFp5Rrklq
    }

}
