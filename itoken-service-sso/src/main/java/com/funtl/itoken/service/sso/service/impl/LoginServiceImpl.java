package com.funtl.itoken.service.sso.service.impl;

import com.funtl.itoken.common.domain.TbSysUser;
import com.funtl.itoken.common.utils.MapperUtils;
import com.funtl.itoken.service.sso.mapper.TbSysUserMapper;
import com.funtl.itoken.service.sso.service.LoginService;
import com.funtl.itoken.service.sso.service.consumer.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import tk.mybatis.mapper.entity.Example;

@Service
public class LoginServiceImpl implements LoginService {

    private static final Logger logger = LoggerFactory.getLogger(LoginServiceImpl.class);

    @Autowired
    private RedisService redisService;

    @Autowired
    private TbSysUserMapper tbSysUserMapper;

    @Override
    public TbSysUser login(String loginCode, String plantPassword) {
        TbSysUser tbSysUser = null;
        String json = redisService.get(loginCode);
        //用户信息不再缓存中.,从数据库取数据后放入缓存
        if(json == null) {
            Example example = new Example(TbSysUser.class);
            example.createCriteria().andEqualTo("loginCode",loginCode);
            tbSysUser = tbSysUserMapper.selectOneByExample(example);
            String password = DigestUtils.md5DigestAsHex(plantPassword.getBytes());
            System.out.println(tbSysUser);
            if(tbSysUser != null  && password.equals(tbSysUser.getPassword())){
                try {
                    redisService.put(loginCode, MapperUtils.obj2json(tbSysUser), 60*60*24);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return tbSysUser;
            }
            return null;

        }
        //缓存中有数据，直接从缓存取数据
        else {

            try {
                tbSysUser = MapperUtils.json2pojo(json, TbSysUser.class);
            } catch (Exception e) {
                logger.warn("触发了redis服务的熔断", e.getMessage());
            }

        }
        return tbSysUser;
    }
}
