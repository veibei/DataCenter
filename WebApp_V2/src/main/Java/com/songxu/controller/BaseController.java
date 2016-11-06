package com.songxu.controller;

import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by songxu on 2016/11/3.
 */
public class BaseController {

    protected HttpServletResponse response;
    protected HttpServletRequest request;

    @ModelAttribute
    public void setReqAndRes(HttpServletRequest request, HttpServletResponse response){
        this.request = request;
        this.response = response;
    }
}
