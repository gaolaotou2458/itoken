package com.funtl.itoken.web.admin.interceptor;

import com.funtl.itoken.common.domain.TbSysUser;
import com.funtl.itoken.common.utils.CookieUtils;
import com.funtl.itoken.common.utils.MapperUtils;
import com.funtl.itoken.web.admin.service.RedisService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 通过拦截器实现登录拦截
 */

public class WebAdminInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisService redisService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = CookieUtils.getCookieValue(request, "token");
        // token为空一定没有登录
        if(StringUtils.isBlank(token)){
            //可以写导配置文件,用@value注入进来
            response.sendRedirect("http://localhost:8503/login?url=http://localhost:8601");
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HttpSession session = request.getSession();
        TbSysUser tbSysUser = (TbSysUser) session.getAttribute("tbSysUser");
        // 已登录状态
        if(tbSysUser != null) {
            if(modelAndView != null) {
                modelAndView.addObject("tbSysUser" , tbSysUser);
            }
        }
        // 未登录状态
        else {
            String token = CookieUtils.getCookieValue(request, "token");
            //可能刚某个点token失效了,所以要判断
            if(StringUtils.isNotBlank(token)){
               String loginCode =  redisService.get(token);
               if(StringUtils.isNotBlank(loginCode)){
                   String json = redisService.get(loginCode);
                   if(StringUtils.isNotBlank(json)) {
                       //已登录状态
                       tbSysUser = MapperUtils.json2pojo(json, TbSysUser.class);
                       if(modelAndView != null) {
                           modelAndView.addObject("tbSysUser" , tbSysUser);
                       }
                       //创建局部会话
                       request.getSession().setAttribute("tbSysUser", tbSysUser);
                   }
               }
            }
        }

        //二次确认是否有用户信息
        if(tbSysUser == null) {
            response.sendRedirect("http://localhost:8503/login?url=http://localhost:8601");
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
