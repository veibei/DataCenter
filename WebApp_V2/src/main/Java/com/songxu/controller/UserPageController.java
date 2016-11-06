package com.songxu.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.songxu.aop.AopMethod;
import com.songxu.bean.LogBean;
import com.songxu.bean.LogInIp;
import com.songxu.bean.User;
import com.songxu.dao.DCOLDaoImpl;
import com.songxu.dao.UserPageDao;
import com.songxu.dao.UserPageDaoImpl;
import com.songxu.service.TokenService;
import com.songxu.util.ConstantValue;
import org.darkphoenixs.pool.socket.SocketConnectionPool;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by songxu on 2016/6/15.
 */
@Controller
@RequestMapping("/user")
public class UserPageController extends BaseController {
    @Resource
    private UserPageDao userPageDao;
    @Resource
    private TokenService tokenService;
    @Resource
    private SocketConnectionPool connectionPool;
    private int perPageCountlogInIp = 10;
    private Integer pageCountlogInIp = null;// 记录常量值 总的页数 仅在firstload的时候加载
    private List<LogInIp> logInIps;



    /**
     * 获取第一个分页
     *
     * @return
     */
    @AopMethod
    @RequestMapping(value = "/first", method = RequestMethod.GET)
    @ResponseBody
    public String firstLoad(String token) throws UnknownHostException, IOException {
        try {
            User user = tokenService.getUser(token);
            if (null == user) {
                return JSON.toJSONString(ConstantValue.ERROR);
            }
            logInIps = userPageDao.getAll(user);
            //计算总的页数
            int page = logInIps.size() / perPageCountlogInIp;
            if (logInIps.size() % perPageCountlogInIp != 0) {
                page++;
            }
            pageCountlogInIp = page;
            JSONObject resultObject = new JSONObject();
            @SuppressWarnings("unchecked")
            List<LogInIp> list = (new UserPageDaoImpl()).getSubPage(0,
                    perPageCountlogInIp, logInIps);
            List<Data> cvList=list.stream().map(logInIp -> {
                return new Data(logInIp.getIp(), logInIp.getLogTime().getTime());
            }).collect(Collectors.toList());

            // 处理下端显示的按钮
            List<Integer> listBtn = new ArrayList<Integer>();
            for (int i = 0; i < page; i++) {
                if (i + 1 > 10) {
                    break;
                }
                listBtn.add(i + 1);

            }
            resultObject.put("pageCount", pageCountlogInIp);// 总的页数
            resultObject.put("rowsCount", logInIps.size());// 总的记录数
            resultObject.put("rowData", cvList);// 页面显示的记录
            resultObject.put("currentPage", 1);// 当前页码
            resultObject.put("perPageCount", perPageCountlogInIp);// 每页的记录数量
            resultObject.put("listBtn", listBtn);// 显示页码
            return resultObject.toJSONString();
        } catch (Exception e) {
            return JSON.toJSONString(ConstantValue.ERROR);
        }
    }

    /**
     * 获取分页
     *
     * @param index
     * @return
     */
    @AopMethod
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    @ResponseBody
    public String indexPage(int index, String token) throws Exception {
        if (null == pageCountlogInIp || logInIps == null) {
            firstLoad(token);

        }
        List<LogInIp> list = (new UserPageDaoImpl()).getSubPage(index - 1,
                perPageCountlogInIp, logInIps);
        List<Data> cvList=list.stream().map(logInIp -> {
            return new Data(logInIp.getIp(), logInIp.getLogTime().getTime());
        }).collect(Collectors.toList());


        JSONObject resultObject = new JSONObject();
        resultObject.put("pageCount", pageCountlogInIp);// 总的页数
        resultObject.put("rowsCount", logInIps.size());// 总的记录数
        resultObject.put("rowData", cvList);// 页面显示的记录
        resultObject.put("currentPage", index);// 当前页码
        resultObject.put("perPageCount", perPageCountlogInIp);// 每页的记录数量
        if (pageCountlogInIp > 10) {
            List<Integer> listBtn = new ArrayList<Integer>();
            // 当前页处于按钮的中间
            if (index > 5) {
                if (index + 5 >= pageCountlogInIp) {
                    for (int i = pageCountlogInIp - 9; i <= pageCountlogInIp; i++) {
                        listBtn.add(i);
                    }
                } else {
                    for (int i = index - 5; i < index + 5; i++) {
                        listBtn.add(i);
                    }
                }
            } else {
                for (int i = 1; i < 11; i++) {
                    listBtn.add(i);
                }
            }
            resultObject.put("listBtn", listBtn);// 显示页码
        } else {
            // 什么都不做
        }
        return resultObject.toJSONString();
    }

    /**
     * 改变每页显示的条目数
     *
     * @param perPageCount
     * @return
     */
    @AopMethod
    @RequestMapping(value = "/change", method = RequestMethod.GET)
    @ResponseBody
    public String changePerPageCount(int perPageCount,String token) {
        perPageCountlogInIp = perPageCount;
        // 计算总的页数
        int rowsCount = logInIps.size();
        int page = rowsCount / perPageCountlogInIp;
        if (logInIps.size() % perPageCountlogInIp != 0) {
            page++;
        }
        pageCountlogInIp = page;
        @SuppressWarnings("unchecked")
        List<LogInIp> list = ( new UserPageDaoImpl()).getSubPage(0,
                perPageCountlogInIp, logInIps);
        List<Data> cvList=list.stream().map(logInIp -> {
            return new Data(logInIp.getIp(), logInIp.getLogTime().getTime());
        }).collect(Collectors.toList());

        // 处理下端显示的按钮
        List<Integer> listBtn = new ArrayList<Integer>();

        for (int i = 0; i < page; i++) {
            if (i + 1 > 10) {
                break;
            }
            listBtn.add(i + 1);
        }
        JSONObject resultObject = new JSONObject();
        resultObject.put("pageCount", pageCountlogInIp);// 总的页数
        resultObject.put("rowsCount", logInIps.size());// 总的记录数
        resultObject.put("rowData", cvList);// 页面显示的记录
        resultObject.put("currentPage", 1);// 当前页码
        resultObject.put("perPageCount", perPageCountlogInIp);// 每页的记录数量
        resultObject.put("listBtn", listBtn);// 显示页码
        return resultObject.toJSONString();
    }
}
class Data{
    public  Data(String ip,long time) {
        this.ip=ip;
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.time = simpleDateFormat.format(new Date(time));
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    private String ip;
    private String time;

}