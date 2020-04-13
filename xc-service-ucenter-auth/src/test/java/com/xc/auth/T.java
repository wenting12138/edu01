package com.xc.auth;


import com.alibaba.fastjson.JSON;
import com.netflix.client.ssl.ClientSslSocketFactoryException;
import com.netflix.http4.ssl.KeyStoreAwareSocketFactory;
import com.xc.auth.UcenterAuthMain;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaSigner;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.security.rsa.crypto.KeyStoreKeyFactory;
import org.springframework.test.context.junit4.SpringRunner;

import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.interfaces.RSAPrivateKey;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest(classes = UcenterAuthMain.class)
@RunWith(SpringRunner.class)
public class T {


    @Test
    public void test() throws ClientSslSocketFactoryException, NoSuchAlgorithmException {

        // 创建jwt令牌
        String keystorepath = "xc.keystore";
        // 密钥库密码
        String keystorepass = "xuechengkeystore";

        // 密钥别名
        String alias = "xckey";
        // 密钥密码
        String keypassword = "xuecheng";
        // 密钥库文件路径
        ClassPathResource classPathResource = new ClassPathResource(keystorepath);
        // 密钥工厂
        KeyStoreKeyFactory factory = new KeyStoreKeyFactory(classPathResource, keystorepass.toCharArray());

        // 密钥对 (公钥和私钥)
        KeyPair keyPair = factory.getKeyPair(alias, keypassword.toCharArray());

        // 获取私钥
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

        // jwt令牌的内容
        Map<String, Object> map = new HashMap<>();
        map.put("name", "wenting");
        String content = JSON.toJSONString(map);
        // 生成jwt令牌
        Jwt jwt = JwtHelper.encode(content, new RsaSigner(privateKey));

        String jwtStr = jwt.getEncoded();

        System.out.println(jwtStr);
    }


    // 校验jwt
    @Test
    public void test01(){

        String publickey = "-----BEGIN PUBLIC KEY-----MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAnASXh9oSvLRLxk901HANYM6KcYMzX8vFPnH/To2R+SrUVw1O9rEX6m1+rIaMzrEKPm12qPjVq3HMXDbRdUaJEXsB7NgGrAhepYAdJnYMizdltLdGsbfyjITUCOvzZ/QgM1M4INPMD+Ce859xse06jnOkCUzinZmasxrmgNV3Db1GtpyHIiGVUY0lSO1Frr9m5dpemylaT0BV3UwTQWVW9ljm6yR3dBncOdDENumT5tGbaDVyClV0FEB1XdSKd7VjiDCDbUAUbDTG1fm3K9sx7kO1uMGElbXLgMfboJ963HEJcU01km7BmFntqI5liyKheX+HBUCD4zbYNPw236U+7QIDAQAB-----END PUBLIC KEY-----";
        String jwt = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJuYW1lIjoid2VudGluZyJ9.RObNssLZcl709MtCYWYIcwR9T2_w1-4rV6V0YHLF4clLzdJ2inylkeHcxkJIwXhDLMDmfDh32ifdiPJ4bm08eeKf7WLB9TKIeYZEDpQbYzM5-dy96wulHyWOagXeNHTrkz64dcup-uCQKnJe0cRFBimIWfoiBpHXyCn3mhEw1ByPeGN80W6JbV6fJGeo5u3agItrELM2KeODG0wWh-kx_p94jxm1Lu8vaAwqKY_Mn4_XRl3_8dn5eEBKOBJG6de5N-9meTYS5kTaA5ztUQJlveb6RaHSipcO2CNeyrYvNpZ5gkLanytbHk8z_2_y5JkEEx-xSJ8Ryx-XnjPWqN-85Q";

        Jwt verify = JwtHelper.decodeAndVerify(jwt, new RsaVerifier(publickey));

        // 拿到自定义的内容
        String claims = verify.getClaims();
        System.out.println(claims);
    }
}
