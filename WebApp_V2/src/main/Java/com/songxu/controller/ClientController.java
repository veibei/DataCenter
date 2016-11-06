package com.songxu.controller;

import com.alibaba.fastjson.JSONObject;
import com.songxu.bean.ClientBean;
import com.songxu.bean.LogBean;
import com.songxu.dao.DCOLDao;
import com.songxu.dao.DCOLDaoImpl;
import com.songxu.webservice.ConnectionRemoteSystem;
import org.darkphoenixs.pool.socket.SocketConnectionPool;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by songxu on 2016/6/15.
 */
@Controller
@RequestMapping("/client")
public class ClientController {
    private DCOLDao dcolDao;
    private int perPageCountClient = 10;
    private Integer pageCountClient = null;// 记录常量值 总的页数 仅在firstload的时候加载
    private List<ClientBean> ClientBeans;
    @Resource
    private SocketConnectionPool connectionPool;

    /**
     * 获取第一个分页
     *
     * @return
     */
    @RequestMapping(value = "/first", method = RequestMethod.GET)
    @ResponseBody
    public String firstLoad() throws UnknownHostException, IOException {
        Socket socket = connectionPool.getConnection();
        ConnectionRemoteSystem connectionRemoteSystem = new ConnectionRemoteSystem(
                socket);

        String result = connectionRemoteSystem.getRemoteDCData();
        this.dcolDao = new DCOLDaoImpl(result);
        connectionPool.returnConnection(socket);
        ClientBeans = dcolDao.getClientBeans();
        int page = ClientBeans.size() / perPageCountClient;
        if (ClientBeans.size() % perPageCountClient != 0) {
            page++;
        }
        pageCountClient = page;

        @SuppressWarnings("unchecked")
        List<LogBean> list = ((DCOLDaoImpl) dcolDao).getSubPage(0,
                perPageCountClient, ClientBeans);
        // 处理下端显示的按钮
        List<Integer> listBtn = new ArrayList<Integer>();

        for (int i = 0; i < page; i++) {
            if (i + 1 > 10) {
                break;
            }
            listBtn.add(i + 1);

        }
        JSONObject resultObject = new JSONObject();
        resultObject.put("pageCount", pageCountClient);// 总的页数
        resultObject.put("rowsCount", ClientBeans.size());// 总的记录数
        resultObject.put("rowData", list);// 页面显示的记录
        resultObject.put("currentPage", 1);// 当前页码
        resultObject.put("perPageCount", perPageCountClient);// 每页的记录数量
        resultObject.put("listBtn", listBtn);// 显示页码
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
    public String indexPage(int index) throws Exception {
        if (null == pageCountClient || ClientBeans == null) {
            firstLoad();

        }
        List<LogBean> list = ((DCOLDaoImpl) dcolDao).getSubPage(index - 1,
                perPageCountClient, ClientBeans);
        JSONObject resultObject = new JSONObject();
        resultObject.put("pageCount", pageCountClient);// 总的页数
        resultObject.put("rowsCount", ClientBeans.size());// 总的记录数
        resultObject.put("rowData", list);// 页面显示的记录
        resultObject.put("currentPage", index);// 当前页码
        resultObject.put("perPageCount", perPageCountClient);// 每页的记录数量
        if (pageCountClient > 10) {
            List<Integer> listBtn = new ArrayList<Integer>();
            // 当前页处于按钮的中间
            if (index > 5) {
                if (index + 5 >= pageCountClient) {
                    for (int i = pageCountClient - 9; i <= pageCountClient; i++) {
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
    @RequestMapping(value = "/change", method = RequestMethod.GET)
    @ResponseBody
    public String changePerPageCount(int perPageCount) {
        perPageCountClient = perPageCount;
        // 计算总的页数
        int rowsCount = ClientBeans.size();
        int page = rowsCount / perPageCountClient;
        if (ClientBeans.size() % perPageCountClient != 0) {
            page++;
        }
        pageCountClient = page;
        @SuppressWarnings("unchecked")
        List<LogBean> list = ((DCOLDaoImpl) dcolDao).getSubPage(0,
                perPageCountClient, ClientBeans);
        // 处理下端显示的按钮
        List<Integer> listBtn = new ArrayList<Integer>();

        for (int i = 0; i < page; i++) {
            if (i + 1 > 10) {
                break;
            }
            listBtn.add(i + 1);
        }
        JSONObject resultObject = new JSONObject();
        resultObject.put("pageCount", pageCountClient);// 总的页数
        resultObject.put("rowsCount", ClientBeans.size());// 总的记录数
        resultObject.put("rowData", list);// 页面显示的记录
        resultObject.put("currentPage", 1);// 当前页码
        resultObject.put("perPageCount", perPageCountClient);// 每页的记录数量
        resultObject.put("listBtn", listBtn);// 显示页码
        return resultObject.toJSONString();
    }
}
