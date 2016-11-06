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
    private Integer pageCountClient = null;// ��¼����ֵ �ܵ�ҳ�� ����firstload��ʱ�����
    private List<ClientBean> ClientBeans;
    @Resource
    private SocketConnectionPool connectionPool;

    /**
     * ��ȡ��һ����ҳ
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
        // �����¶���ʾ�İ�ť
        List<Integer> listBtn = new ArrayList<Integer>();

        for (int i = 0; i < page; i++) {
            if (i + 1 > 10) {
                break;
            }
            listBtn.add(i + 1);

        }
        JSONObject resultObject = new JSONObject();
        resultObject.put("pageCount", pageCountClient);// �ܵ�ҳ��
        resultObject.put("rowsCount", ClientBeans.size());// �ܵļ�¼��
        resultObject.put("rowData", list);// ҳ����ʾ�ļ�¼
        resultObject.put("currentPage", 1);// ��ǰҳ��
        resultObject.put("perPageCount", perPageCountClient);// ÿҳ�ļ�¼����
        resultObject.put("listBtn", listBtn);// ��ʾҳ��
        return resultObject.toJSONString();

    }

    /**
     * ��ȡ��ҳ
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
        resultObject.put("pageCount", pageCountClient);// �ܵ�ҳ��
        resultObject.put("rowsCount", ClientBeans.size());// �ܵļ�¼��
        resultObject.put("rowData", list);// ҳ����ʾ�ļ�¼
        resultObject.put("currentPage", index);// ��ǰҳ��
        resultObject.put("perPageCount", perPageCountClient);// ÿҳ�ļ�¼����
        if (pageCountClient > 10) {
            List<Integer> listBtn = new ArrayList<Integer>();
            // ��ǰҳ���ڰ�ť���м�
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
            resultObject.put("listBtn", listBtn);// ��ʾҳ��
        } else {
            // ʲô������
        }
        return resultObject.toJSONString();
    }

    /**
     * �ı�ÿҳ��ʾ����Ŀ��
     *
     * @param perPageCount
     * @return
     */
    @RequestMapping(value = "/change", method = RequestMethod.GET)
    @ResponseBody
    public String changePerPageCount(int perPageCount) {
        perPageCountClient = perPageCount;
        // �����ܵ�ҳ��
        int rowsCount = ClientBeans.size();
        int page = rowsCount / perPageCountClient;
        if (ClientBeans.size() % perPageCountClient != 0) {
            page++;
        }
        pageCountClient = page;
        @SuppressWarnings("unchecked")
        List<LogBean> list = ((DCOLDaoImpl) dcolDao).getSubPage(0,
                perPageCountClient, ClientBeans);
        // �����¶���ʾ�İ�ť
        List<Integer> listBtn = new ArrayList<Integer>();

        for (int i = 0; i < page; i++) {
            if (i + 1 > 10) {
                break;
            }
            listBtn.add(i + 1);
        }
        JSONObject resultObject = new JSONObject();
        resultObject.put("pageCount", pageCountClient);// �ܵ�ҳ��
        resultObject.put("rowsCount", ClientBeans.size());// �ܵļ�¼��
        resultObject.put("rowData", list);// ҳ����ʾ�ļ�¼
        resultObject.put("currentPage", 1);// ��ǰҳ��
        resultObject.put("perPageCount", perPageCountClient);// ÿҳ�ļ�¼����
        resultObject.put("listBtn", listBtn);// ��ʾҳ��
        return resultObject.toJSONString();
    }
}
