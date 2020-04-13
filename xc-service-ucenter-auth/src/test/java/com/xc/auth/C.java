package com.xc.auth;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = UcenterAuthMain.class)
@RunWith(SpringRunner.class)
public class C {


    @Test
    public void test(){

        // 加密, 随机进行加盐
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        String pass = "111111";
        for (int i = 0; i < 10; i++) {
            String encode = encoder.encode(pass);
            System.out.println(encode);
        }

    }


}
