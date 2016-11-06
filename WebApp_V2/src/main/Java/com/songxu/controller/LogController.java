package com.songxu.controller;

import com.alibaba.fastjson.JSONObject;
import com.songxu.bean.LogBean;
import com.songxu.dao.LogQueryDao;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 日志Controller
 * Created by songxu on 2016/6/14
 */
@Controller
@RequestMapping("/log")
public class LogController {
    @Resource
    private LogQueryDao logDaoQuery;
    private int perPageCount = 10;
    private Integer pageCount = null;// 记录常量值 总的页数 仅在firstload的时候加载
    private Integer rowsCount=null;
    /**
     * 获取第一个分页
     *
     * @return
     */
    @RequestMapping(value = "/first", method = RequestMethod.GET)
    @ResponseBody
    public String firstLoad() {
        // 计算总的页数
        this.rowsCount = logDaoQuery.getPages(10);
        int page = rowsCount / perPageCount;
        if (page % perPageCount != 0) {
            page++;
        }
        pageCount = page;
        List<LogBean> list = logDaoQuery.getSubPage(0, perPageCount);
        // 处理下端显示的按钮
        List<Integer> listBtn = new ArrayList<Integer>();

        for (int i = 0; i < page; i++) {
            if (i + 1 > 10) {
                break;
            }
            listBtn.add(i + 1);

        }

        JSONObject resultObject = new JSONObject();
        resultObject.put("pageCount", pageCount);// 总的页数
        resultObject.put("rowsCount", rowsCount);// 总的记录数
        resultObject.put("rowData", list);// 当前页需要记录
        resultObject.put("currentPage", 1);// 当前页码
        resultObject.put("perPageCount", perPageCount);// 每页的记录数
        resultObject.put("listBtn", listBtn);// 下端显示的页码
        return resultObject.toJSONString();
    }

    /**
     * 获取分页
     *
     * @param index
     * @return
     */
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    @ResponseBody
    public String indexPage(int index) {
        if (null == pageCount) {
            firstLoad();
        }
        List<LogBean> list = logDaoQuery.getSubPage(index - 1, perPageCount);
        JSONObject resultObject = new JSONObject();
        resultObject.put("pageCount", pageCount);// 总的页数
        resultObject.put("rowsCount", rowsCount);// 总的记录数
        resultObject.put("perPageCount", perPageCount);// 每页的记录数
        resultObject.put("rowData", list);// 记录
        resultObject.put("currentPage", index);// 当前页

        // int pageCount=(int) session.getAttribute("pageCount");
        if (pageCount > 10) {
            List<Integer> listBtn = new ArrayList<Integer>();
            // 当前页处于按钮的中间
            if (index > 5) {
                if (index + 5 >= pageCount) {
                    for (int i = pageCount - 9; i <= pageCount; i++) {
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
     * @param perPageCount
     * @return
     */
    @RequestMapping(value = "/change", method = RequestMethod.GET)
    @ResponseBody
    public String changePerPageCount(int perPageCount) {
        this.perPageCount = perPageCount;
        // 计算总的页数
        int rowsCount = logDaoQuery.getPages(10);
        int page = rowsCount / perPageCount;
        if (page % perPageCount != 0) {
            page++;
        }
        pageCount = page;
        List<LogBean> list = logDaoQuery.getSubPage(0, perPageCount);
        // 处理下端显示的按钮
        List<Integer> listBtn = new ArrayList<Integer>();
        for (int i = 0; i < page; i++) {
            if (i + 1 > 10) {
                break;
            }
            listBtn.add(i + 1);
        }
        JSONObject resultObject = new JSONObject();
        resultObject.put("pageCount", page);// 总的页数
        resultObject.put("rowsCount", rowsCount);// 总的记录数
        resultObject.put("rowData", list);// 当前页需要记录
        resultObject.put("currentPage", 1);// 当前页码
        resultObject.put("perPageCount", perPageCount);// 每页的记录数
        resultObject.put("listBtn", listBtn);// 下端显示的页码
        return resultObject.toJSONString();
    }

}
