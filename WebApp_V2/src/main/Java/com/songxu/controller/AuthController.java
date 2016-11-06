package com.songxu.controller;

import com.alibaba.fastjson.JSON;
import com.songxu.bean.User;
import com.songxu.dao.CheckInDao;
import com.songxu.service.TokenService;
import com.songxu.util.ConstantValue;
import com.songxu.util.NetworkUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by songxu on 2016/11/1.
 */
@Controller
@RequestMapping("/auth")
public class AuthController  extends BaseController{

    @Resource
    private CheckInDao checkInDao;
    @Resource
    private TokenService tokenService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public String login(String username, String password) throws IOException, ServletException {
        String ipAddress = NetworkUtil.getRemoteHost(request);
        if (username == null || password == null) {
            return JSON.toJSONString(ConstantValue.ERROR);
        }
        User user;
        if ((user = checkInDao.access(username, password, ipAddress)) != null) {
            String token=tokenService.add(user);
           /* request.getRequestDispatcher("/WEB-INF/index.html").forward(request, response);
            response.sendRedirect("/index.html");*/
            return  JSON.toJSONString(token);
        } else {
            return JSON.toJSONString(ConstantValue.ERROR);
        }
    }

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    @ResponseBody
    public String logout(String token) {
        tokenService.rmvUser(token);
        return JSON.toJSONString(ConstantValue.SUCCESS);
    }
}
