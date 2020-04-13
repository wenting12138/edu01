package com.xc.auth;

import com.xc.ommon.client.XcServiceList;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Base64Utils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.util.Map;

@SpringBootTest(classes = UcenterAuthMain.class)
@RunWith(SpringRunner.class)
public class W {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @Test
    public void test(){

        // 从eureka获取地址
        ServiceInstance serviceInstance = loadBalancerClient.choose(XcServiceList.XC_SERVICE_UCENTER_AUTH);

        // http://host:port
        URI uri = serviceInstance.getUri();
        String authUrl = uri + "/auth/oauth/token";

        LinkedMultiValueMap<String, String> header = new LinkedMultiValueMap<>();

        header.add("Authorization", getHttpBasic("XcWebApp", "XcWebApp"));

        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        // 采用密码模式
        body.add("grant_type", "password");
        body.add("username", "itcast");
        body.add("password", "111111");
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(header,body);


        restTemplate.setErrorHandler(new DefaultResponseErrorHandler(){
            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
                if (response.getRawStatusCode() != 400 && response.getRawStatusCode() != 401) {
                    super.handleError(response);
                }
            }
        });

        // 请求
        ResponseEntity<Map> responseEntity = restTemplate.exchange(authUrl, HttpMethod.POST, entity, Map.class);

        Map map = responseEntity.getBody();

        System.out.println(map);

    }

    private String getHttpBasic(String clientId, String clientSecret){

        String s = clientId + ":" + clientSecret;

        byte[] encode = Base64Utils.encode(s.getBytes());

        return "Basic " + new String(encode);
    }

    @Test
    public void test5(){


        String s = "XcWebApp" + ":" + "XcWebApp";
        byte[] encode = Base64Utils.encode(s.getBytes());
        System.out.println( "Basic " + new String(encode));
    }

}
