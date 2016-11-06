package com.songxu.multithread;

import com.songxu.bean.LogBean;
import com.songxu.dao.LogDao;
import org.apache.log4j.Logger;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.websocket.RemoteEndpoint.Basic;
import javax.websocket.Session;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.BlockingQueue;

/**
 * 2015-11-3 新增
 * 日志推送线程纳入线程池管理
 *
 * @author songxu
 * @version 2.0
 */
public class PushLogInfoV2 implements CoreHandlerV2<Integer> {
    private Logger logger = Logger.getLogger(PushLogInfoV2.class);
    /**
     * servlet上下文
     */
    private boolean isRun = false;
    private boolean isStop = true;
    private final LogDao logDao;
    private final ServletContext servletContext;
    /*
     * 用于上一次的日志记录数，如果没有发生变化，则不向服务器推送
     */
    @SuppressWarnings("unused")
    private int lastCount = 0;

    public PushLogInfoV2(ServletContext servletContext) {
        logDao = WebApplicationContextUtils.getWebApplicationContext(
                servletContext).getBean("logDao", LogDao.class);
        this.servletContext = servletContext;
    }

    @Override
    public Integer call() throws Exception {
        isRun = true;
        while (isRun) {
            @SuppressWarnings("unchecked")
            BlockingQueue<Session> sessions = (BlockingQueue<Session>) servletContext
                    .getAttribute("webSocketSessions");
            // 只有当存在websocket会话时，才需要推送消息
            if (sessions.size() > 0) {
                List<LogBean> logBeans = logDao.getLog();
                if (logBeans == null) {
                    logger.error("logDao查询出错");
                    continue;
                }
                /*
                 * if(logBeans.size()==lastCount)//如果没有新的记录加入 则不再推送消息 {
				 * continue; }
				 */
                else {
                    lastCount = logBeans.size();
                    Iterator<Session> iterator = sessions.iterator();
                    String msg = getMsg(logBeans);
                    while (iterator.hasNext()) {
                        Session session = iterator.next();
                        try {
                            Basic basic = session.getBasicRemote();
                            synchronized (basic) {
                                basic.sendText(msg);
                            }

                        } catch (IOException e) {
                            logger.error("推送消息出错");
                        }
                    }

                }
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public void stop() {
        isRun = false;
    }

    @Override
    public boolean getIfStop() {
        return isStop;
    }

    private String getMsg(List<LogBean> logBeans) {
        StringBuilder result = new StringBuilder();
        result.append("l");// 插入标识符
        // 如果记录小于10条 则全部推送
        if (logBeans.size() < 10) {
            for (LogBean logBean : logBeans) {
                result.append(logBean.getLog_date());
                result.append("|");
                result.append(logBean.getLog_level());
                result.append("|");
                result.append(logBean.getMessage());
                result.append(";");
            }
        } else {
            for (int i = logBeans.size() - 10; i < logBeans.size(); i++) {
                LogBean logBean = logBeans.get(i);
                result.append(logBean.getLog_date());
                result.append("|");
                result.append(logBean.getLog_level());
                result.append("|");
                result.append(logBean.getMessage());
                result.append(";");
            }
        }
        return result.toString();
    }

}
