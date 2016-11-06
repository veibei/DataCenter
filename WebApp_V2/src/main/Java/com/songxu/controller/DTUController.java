package com.songxu.controller;

import com.alibaba.fastjson.JSONObject;
import com.songxu.bean.DTUBean;
import com.songxu.bean.LogBean;
import com.songxu.dao.DCOLDao;
import com.songxu.dao.DCOLDaoImpl;
import com.songxu.webservice.ConnectionRemoteSystem;
import org.apache.struts2.ServletActionContext;
import org.darkphoenixs.pool.socket.SocketConnectionPool;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by songxu on 2016/6/14.
 */
@Controller
@RequestMapping("/dtu")
public class DTUController {
    private DCOLDao dcolDao;
    @Resource
    private SocketConnectionPool connectionPool;
    private int perPageCountDTU = 10;
    private Integer pageCountDTU = null;// ��¼����ֵ �ܵ�ҳ�� ����firstload��ʱ�����
    private List<DTUBean> dtuBeans;
    /**
     * ��ȡ��һ����ҳ
     *
     * @return
     */
    @RequestMapping(value = "/first", method = RequestMethod.GET)
    @ResponseBody
    public String firstLoad() throws IOException {
        Socket socket=connectionPool.getConnection();
        ConnectionRemoteSystem connectionRemoteSystem = new ConnectionRemoteSystem(
            socket    );

        String result = connectionRemoteSystem.getRemoteDCData();
        this.dcolDao = new DCOLDaoImpl(result);
//        connectionRemoteSystem.closeConnection();
        connectionPool.returnConnection(socket);
        dtuBeans = dcolDao.getDtuBeans();
        int page = dtuBeans.size() / perPageCountDTU;
        if (dtuBeans.size() % perPageCountDTU != 0) {
            page++;
        }
        pageCountDTU = page;
        List<LogBean> list = ((DCOLDaoImpl) dcolDao).getSubPage(0,
                perPageCountDTU, dtuBeans);

        // �����¶���ʾ�İ�ť
        List<Integer> listBtn = new ArrayList<Integer>();

        for (int i = 0; i < page; i++) {
            if (i + 1 > 10) {
                break;
            }
            listBtn.add(i + 1);
        }
        JSONObject resultObject=new JSONObject();
        resultObject.put("pageCount", pageCountDTU);// �ܵ�ҳ��
        resultObject.put("rowsCount", dtuBeans.size());// �ܵļ�¼��
        resultObject.put("rowData", list);// ҳ����ʾ�ļ�¼
        resultObject.put("currentPage", 1);// ��ǰҳ��
        resultObject.put("perPageCount", perPageCountDTU);// ÿҳ�ļ�¼����
        resultObject.put("listBtn", listBtn);// ��ʾҳ��
        return  resultObject.toJSONString();
    }
    /**
     * ��ȡ��ҳ
     *
     * @param index
     * @return
     */
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    @ResponseBody
    public String indexPage(int index) throws IOException {
        if (null == pageCountDTU || dtuBeans == null) {
            firstLoad();
        }
        List<LogBean> list = ((DCOLDaoImpl) dcolDao).getSubPage(index - 1,
                perPageCountDTU, dtuBeans);
        JSONObject resultObject=new JSONObject();
        resultObject.put("pageCount", pageCountDTU);// �ܵ�ҳ��
        resultObject.put("rowsCount", dtuBeans.size());// �ܵļ�¼��
        resultObject.put("rowData", list);// ҳ����ʾ�ļ�¼
        resultObject.put("currentPage", 1);// ��ǰҳ��
        resultObject.put("perPageCount", perPageCountDTU);// ÿҳ�ļ�¼����
        if (pageCountDTU > 10) {
            List<Integer> listBtn = new ArrayList<Integer>();
            // ��ǰҳ���ڰ�ť���м�
            if (index > 5) {
                if (index + 5 >= pageCountDTU) {
                    for (int i = pageCountDTU - 9; i <= pageCountDTU; i++) {
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
        return  resultObject.toJSONString();
    }
    /**
     * �ı�ÿҳ��ʾ����Ŀ��
     * @param perPageCount
     * @return
     */
    @RequestMapping(value = "/change", method = RequestMethod.GET)
    @ResponseBody
    public String changePerPageCount(int perPageCount) {
        perPageCountDTU=perPageCount;
        // �����ܵ�ҳ��
        int rowsCount = dtuBeans.size();
        int page = rowsCount / perPageCountDTU;
        if (dtuBeans.size() % perPageCountDTU != 0) {
            page++;
        }
        pageCountDTU = page;
        @SuppressWarnings("unchecked")
        List<LogBean> list = ((DCOLDaoImpl) dcolDao).getSubPage(0,
                perPageCountDTU, dtuBeans);
        // �����¶���ʾ�İ�ť
        List<Integer> listBtn = new ArrayList<Integer>();

        for (int i = 0; i < page; i++) {
            if (i + 1 > 10) {
                break;
            }
            listBtn.add(i + 1);
        }
        JSONObject resultObject=new JSONObject();
        resultObject.put("pageCount", pageCountDTU);// �ܵ�ҳ��
        resultObject.put("rowsCount", dtuBeans.size());// �ܵļ�¼��
        resultObject.put("rowData", list);// ҳ����ʾ�ļ�¼
        resultObject.put("currentPage", 1);// ��ǰҳ��
        resultObject.put("perPageCount", perPageCountDTU);// ÿҳ�ļ�¼����
        resultObject.put("listBtn", listBtn);// ��ʾҳ��
        return  resultObject.toJSONString();
    }


}
