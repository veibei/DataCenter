package com.songxu.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.songxu.aop.AopMethod;
import com.songxu.bean.RemoteServerBean;
import com.songxu.bean.User;
import com.songxu.service.TokenService;
import com.songxu.util.ConstantValue;
import com.songxu.util.NetworkUtil;
import com.songxu.webservice.ConnectionRemoteSystem;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.Socket;

/**
 * indexPage controller
 */

@Controller
@RequestMapping("/main")
public class MainController extends BaseController {

    @Resource
    private TokenService tokenService;


    @AopMethod
    @RequestMapping(value = "/info", method = RequestMethod.GET)
    @ResponseBody
    public String getInfo(String token) {
        User user = tokenService.getUser(token);
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        ServletContext servletContext = request.getServletContext();
        RemoteServerBean remoteServerBean = (RemoteServerBean) servletContext.getAttribute("remoteServerBean");
        JSONObject reVal = new JSONObject();
        reVal.put("user", user.getUsername());
        reVal.put("remoteServerBean", remoteServerBean);
        return reVal.toJSONString();
    }

    @AopMethod
    @RequestMapping(value = "/start", method = RequestMethod.POST)
    @ResponseBody
    public String startServer(String token) throws IOException {
        ServletContext servletContext = request.getServletContext();
        //以下代码防止直接从浏览器访问
        RemoteServerBean bean = (RemoteServerBean) servletContext.getAttribute("remoteServerBean");
        if (bean.getStatus() == RemoteServerBean.Status_Running) {
            return JSON.toJSONString(ConstantValue.ERROR);
        }
        String ip = servletContext.getInitParameter("ip");
        int port = Integer.parseInt(servletContext.getInitParameter("port"));
        Socket socket;
        try {
            socket = new Socket(ip, port);
        } catch (Exception e) {
            return JSON.toJSONString("close");
        }

        ConnectionRemoteSystem connectionRemoteSystem = new ConnectionRemoteSystem(socket);
        /**
         * 2015-10-27 增加传递操作ip及用户名功能
         */
        User user = tokenService.getUser(token);
        String remoteAddr = NetworkUtil.getRemoteHost(request);
        if (connectionRemoteSystem.StartServer(remoteAddr, user.getUsername())) {
            connectionRemoteSystem.closeConnection();
            socket.close();
            return JSON.toJSONString("true");
        } else {
            connectionRemoteSystem.closeConnection();
            socket.close();
            return JSON.toJSONString("false");
        }

    }

    @AopMethod
    @RequestMapping(value = "/stop", method = RequestMethod.POST)
    @ResponseBody
    public String stopServer(String token) throws IOException {
        ServletContext servletContext = request.getServletContext();
        HttpSession session = request.getSession();
        //以下代码防止直接从浏览器访问action
        RemoteServerBean bean = (RemoteServerBean) servletContext.getAttribute("remoteServerBean");
        if (bean.getStatus() == RemoteServerBean.Status_Stopped) {
            return JSON.toJSONString(ConstantValue.ERROR);
        }
        String ip = servletContext.getInitParameter("ip");
        int port = Integer.parseInt(servletContext.getInitParameter("port"));
        Socket socket;
        try {
            socket = new Socket(ip, port);
        } catch (Exception e) {
            return JSON.toJSONString("close");
        }
        ConnectionRemoteSystem connectionRemoteSystem = new ConnectionRemoteSystem(socket);
        User user = tokenService.getUser(token);
        String remoteAddr = NetworkUtil.getRemoteHost(request);
        if (connectionRemoteSystem.StopServer(remoteAddr, user.getUsername())) {
            connectionRemoteSystem.closeConnection();
            socket.close();
            return JSON.toJSONString("true");
        } else {
            connectionRemoteSystem.closeConnection();
            socket.close();
            return JSON.toJSONString("false");
        }
    }
}