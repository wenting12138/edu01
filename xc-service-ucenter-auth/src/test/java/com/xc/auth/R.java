package com.xc.auth;

import com.alibaba.fastjson.JSON;
import com.netflix.discovery.converters.Auto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@SpringBootTest(classes = UcenterAuthMain.class)
@RunWith(SpringRunner.class)
public class R {


    @Autowired
    private StringRedisTemplate redisTemplate;

    @Test
    public void test() {

        String key = "jwt";
        Map<String, String> map = new HashMap<>();
        map.put("jwt","jwt");
        map.put("refresh", "refreshjwt");


        // 查询key的过期时间
        Long expire = redisTemplate.getExpire(key, TimeUnit.SECONDS);

        redisTemplate.boundValueOps(key).set(JSON.toJSONString(map),30, TimeUnit.SECONDS);

        String s = redisTemplate.opsForValue().get(key);
        System.out.println(s);

    }
}
