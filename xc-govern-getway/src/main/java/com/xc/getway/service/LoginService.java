package com.xc.getway.service;

import com.xc.utils.CookieUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class LoginService {

    //     // 从请求头获取jwt令牌
    public String getJwt2Header(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (StringUtils.isEmpty(header)) {
            return null;
        }
        if (!header.startsWith("Bearer ")) {
            return null;
        }
        return header.substring(7);

    }

    // 从cookie获取身份令牌
    public String getToken2Cookie(HttpServletRequest request) {
        Map<String, String> cookie = CookieUtil.readCookie(request, "uid");
        if (cookie != null && cookie.get("uid") != null) return cookie.get("uid");
        return null;
    }

    @Autowired
    private StringRedisTemplate redisTemplate;

    // 从redis取出jwt令牌
    public long getExpire(String token) {

        String key = "user_token:";
        Long expireTime = redisTemplate.getExpire(key + token, TimeUnit.SECONDS);

        return expireTime;
    }

}
