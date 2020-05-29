package com.funtl.itoken.service.sso.controller;

import com.funtl.itoken.common.domain.TbSysUser;
import com.funtl.itoken.common.utils.CookieUtils;
import com.funtl.itoken.common.utils.MapperUtils;
import com.funtl.itoken.service.sso.service.LoginService;
import com.funtl.itoken.service.sso.service.consumer.RedisService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Controller
public class LoginController {

    @Autowired
    private RedisService redisService;

    @Autowired
    private LoginService loginService;

    /**
     * 跳转登陆页面
     * @return
     */
    @RequestMapping(value="login", method = RequestMethod.GET)
    public String login(
            @RequestParam(required = false) String url,
            HttpServletRequest request,
            Model model){
        String token = CookieUtils.getCookieValue(request, "token");
        // token 不为空可能已登陆
        if(StringUtils.isNotBlank(token)){
            String loginCode = redisService.get(token);
            if(StringUtils.isNotBlank(loginCode)){
                String json = redisService.get(loginCode);
                if(StringUtils.isNotBlank(json)){
                    try {
                        TbSysUser tbSysUser = MapperUtils.json2pojo(json, TbSysUser.class);
                        //已经登陆
                        if(tbSysUser != null){
                            if(StringUtils.isNotBlank(url)) {

                                return "redirect:" + url;
                            }
                        }
                        //将登陆信息传到登录页
                        model.addAttribute("tbSysUser", tbSysUser);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        if(StringUtils.isNotBlank(url)) {
            model.addAttribute("url", url);
        }
        return "login";
    }

    /**
     * 登陆业务
     * @param loginCode
     * @param password
     * @return
     */

    @RequestMapping(value = "login", method = RequestMethod.POST)
    public String login(
            String loginCode,
            String password,
            @RequestParam(required = false) String url,
            HttpServletRequest request,
            HttpServletResponse response,
            RedirectAttributes redirectAttributes) {
        TbSysUser tbSysUser = loginService.login(loginCode, password);
        //登陆失败
        if(tbSysUser == null) {
            redirectAttributes.addFlashAttribute("message", "用户名或密码错误，请重新输入");
        }
        //登陆成功
        else {
            String  token = UUID.randomUUID().toString();
            String result = redisService.put(token, loginCode, 60 * 60 * 24);
            //将token放入缓存
            if(StringUtils.isNotBlank(result) && "ok".equals(result)) {
                CookieUtils.setCookie(request, response, "token", token, 60 * 60 * 24);
                if(StringUtils.isNotBlank(url)) {
                    return "redirect:" + url;
                }
            }
            //熔断处理
            else {
                redirectAttributes.addFlashAttribute("message", "服务器异常，请稍后再试");
            }
        }
        //重定向到上面的方法
        return "redirect:/login";
    }

    /**
     * 单点注销
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "logout", method = RequestMethod.GET)
    public String logout(HttpServletRequest request, HttpServletResponse response, @RequestParam(required = false) String url,Model model){
        try {
            System.out.println("测试退出");
            CookieUtils.deleteCookie(request, response , "token");

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (StringUtils.isNotBlank(url)) {
            return "redirect:/login?url=" + url;
        } else {
            return "redirect:/login";
        }
    }
}
