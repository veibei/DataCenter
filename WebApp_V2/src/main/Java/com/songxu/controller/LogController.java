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
 * ��־Controller
 * Created by songxu on 2016/6/14
 */
@Controller
@RequestMapping("/log")
public class LogController {
    @Resource
    private LogQueryDao logDaoQuery;
    private int perPageCount = 10;
    private Integer pageCount = null;// ��¼����ֵ �ܵ�ҳ�� ����firstload��ʱ�����
    private Integer rowsCount=null;
    /**
     * ��ȡ��һ����ҳ
     *
     * @return
     */
    @RequestMapping(value = "/first", method = RequestMethod.GET)
    @ResponseBody
    public String firstLoad() {
        // �����ܵ�ҳ��
        this.rowsCount = logDaoQuery.getPages(10);
        int page = rowsCount / perPageCount;
        if (page % perPageCount != 0) {
            page++;
        }
        pageCount = page;
        List<LogBean> list = logDaoQuery.getSubPage(0, perPageCount);
        // �����¶���ʾ�İ�ť
        List<Integer> listBtn = new ArrayList<Integer>();

        for (int i = 0; i < page; i++) {
            if (i + 1 > 10) {
                break;
            }
            listBtn.add(i + 1);

        }

        JSONObject resultObject = new JSONObject();
        resultObject.put("pageCount", pageCount);// �ܵ�ҳ��
        resultObject.put("rowsCount", rowsCount);// �ܵļ�¼��
        resultObject.put("rowData", list);// ��ǰҳ��Ҫ��¼
        resultObject.put("currentPage", 1);// ��ǰҳ��
        resultObject.put("perPageCount", perPageCount);// ÿҳ�ļ�¼��
        resultObject.put("listBtn", listBtn);// �¶���ʾ��ҳ��
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
    public String indexPage(int index) {
        if (null == pageCount) {
            firstLoad();
        }
        List<LogBean> list = logDaoQuery.getSubPage(index - 1, perPageCount);
        JSONObject resultObject = new JSONObject();
        resultObject.put("pageCount", pageCount);// �ܵ�ҳ��
        resultObject.put("rowsCount", rowsCount);// �ܵļ�¼��
        resultObject.put("perPageCount", perPageCount);// ÿҳ�ļ�¼��
        resultObject.put("rowData", list);// ��¼
        resultObject.put("currentPage", index);// ��ǰҳ

        // int pageCount=(int) session.getAttribute("pageCount");
        if (pageCount > 10) {
            List<Integer> listBtn = new ArrayList<Integer>();
            // ��ǰҳ���ڰ�ť���м�
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
            resultObject.put("listBtn", listBtn);// ��ʾҳ��
        } else {
            // ʲô������
        }
        return resultObject.toJSONString();
    }

    /**
     * �ı�ÿҳ��ʾ����Ŀ��
     * @param perPageCount
     * @return
     */
    @RequestMapping(value = "/change", method = RequestMethod.GET)
    @ResponseBody
    public String changePerPageCount(int perPageCount) {
        this.perPageCount = perPageCount;
        // �����ܵ�ҳ��
        int rowsCount = logDaoQuery.getPages(10);
        int page = rowsCount / perPageCount;
        if (page % perPageCount != 0) {
            page++;
        }
        pageCount = page;
        List<LogBean> list = logDaoQuery.getSubPage(0, perPageCount);
        // �����¶���ʾ�İ�ť
        List<Integer> listBtn = new ArrayList<Integer>();
        for (int i = 0; i < page; i++) {
            if (i + 1 > 10) {
                break;
            }
            listBtn.add(i + 1);
        }
        JSONObject resultObject = new JSONObject();
        resultObject.put("pageCount", page);// �ܵ�ҳ��
        resultObject.put("rowsCount", rowsCount);// �ܵļ�¼��
        resultObject.put("rowData", list);// ��ǰҳ��Ҫ��¼
        resultObject.put("currentPage", 1);// ��ǰҳ��
        resultObject.put("perPageCount", perPageCount);// ÿҳ�ļ�¼��
        resultObject.put("listBtn", listBtn);// �¶���ʾ��ҳ��
        return resultObject.toJSONString();
    }

}
