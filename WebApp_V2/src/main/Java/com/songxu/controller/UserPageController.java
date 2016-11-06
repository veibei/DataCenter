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
    private Integer pageCountlogInIp = null;// ��¼����ֵ �ܵ�ҳ�� ����firstload��ʱ�����
    private List<LogInIp> logInIps;



    /**
     * ��ȡ��һ����ҳ
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
            //�����ܵ�ҳ��
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

            // �����¶���ʾ�İ�ť
            List<Integer> listBtn = new ArrayList<Integer>();
            for (int i = 0; i < page; i++) {
                if (i + 1 > 10) {
                    break;
                }
                listBtn.add(i + 1);

            }
            resultObject.put("pageCount", pageCountlogInIp);// �ܵ�ҳ��
            resultObject.put("rowsCount", logInIps.size());// �ܵļ�¼��
            resultObject.put("rowData", cvList);// ҳ����ʾ�ļ�¼
            resultObject.put("currentPage", 1);// ��ǰҳ��
            resultObject.put("perPageCount", perPageCountlogInIp);// ÿҳ�ļ�¼����
            resultObject.put("listBtn", listBtn);// ��ʾҳ��
            return resultObject.toJSONString();
        } catch (Exception e) {
            return JSON.toJSONString(ConstantValue.ERROR);
        }
    }

    /**
     * ��ȡ��ҳ
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
        resultObject.put("pageCount", pageCountlogInIp);// �ܵ�ҳ��
        resultObject.put("rowsCount", logInIps.size());// �ܵļ�¼��
        resultObject.put("rowData", cvList);// ҳ����ʾ�ļ�¼
        resultObject.put("currentPage", index);// ��ǰҳ��
        resultObject.put("perPageCount", perPageCountlogInIp);// ÿҳ�ļ�¼����
        if (pageCountlogInIp > 10) {
            List<Integer> listBtn = new ArrayList<Integer>();
            // ��ǰҳ���ڰ�ť���м�
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
    @AopMethod
    @RequestMapping(value = "/change", method = RequestMethod.GET)
    @ResponseBody
    public String changePerPageCount(int perPageCount,String token) {
        perPageCountlogInIp = perPageCount;
        // �����ܵ�ҳ��
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

        // �����¶���ʾ�İ�ť
        List<Integer> listBtn = new ArrayList<Integer>();

        for (int i = 0; i < page; i++) {
            if (i + 1 > 10) {
                break;
            }
            listBtn.add(i + 1);
        }
        JSONObject resultObject = new JSONObject();
        resultObject.put("pageCount", pageCountlogInIp);// �ܵ�ҳ��
        resultObject.put("rowsCount", logInIps.size());// �ܵļ�¼��
        resultObject.put("rowData", cvList);// ҳ����ʾ�ļ�¼
        resultObject.put("currentPage", 1);// ��ǰҳ��
        resultObject.put("perPageCount", perPageCountlogInIp);// ÿҳ�ļ�¼����
        resultObject.put("listBtn", listBtn);// ��ʾҳ��
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