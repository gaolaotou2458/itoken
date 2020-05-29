package com.funtl.itoken.service.sso.service.consumer.fallback;


import com.funtl.itoken.common.hystrix.Fallback;
import com.funtl.itoken.service.sso.service.consumer.RedisService;
import org.springframework.stereotype.Component;

@Component
public class RedisServiceFallback implements RedisService{

    @Override
    public String put(String key, String value, long seconds) {

        return null;
        //return Fallback.badGateway();
    }

    @Override
    public String get(String key) {

        return null;
        //return Fallback.badGateway();
    }
}
