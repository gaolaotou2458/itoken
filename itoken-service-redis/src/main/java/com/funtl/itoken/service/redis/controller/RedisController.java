package com.funtl.itoken.service.redis.controller;

import com.funtl.itoken.service.redis.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RedisController {

    private static final String RESULT_OK = "ok";
    private static final String RESULT_NOT_OK = "not_ok";
    @Autowired
    private RedisService redisService;

    @RequestMapping(value = "put", method = RequestMethod.POST)
    public String put(String key, String value, long seconds) {
        redisService.set(key, value, seconds);
        System.out.println("设置缓存key" + key  + "value:" + value);
        return RESULT_OK;
    }

    @RequestMapping(value = "get", method = RequestMethod.GET)
    public String get(String key) {
        String json = null;

        Object obj = redisService.get(key);

        if (obj != null) {
            json = (String) redisService.get(key);
            System.out.println(json);
            return json;
        }
        return null;

    }
}