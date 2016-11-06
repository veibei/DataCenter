package com.songxu.core;

import com.songxu.bean.RemoteServerBean;
import com.songxu.memcached.MemecachedOperate;
import com.songxu.remote.RemoteConnector;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.net.InetSocketAddress;
import java.util.Scanner;

/**
 * Main方法类 程序入口
 *
 * @author songxu
 */
public class MainThread {
    private static Logger logger = Logger.getLogger(MainThread.class);
    private static ApplicationContext applicationContext;
    private static Server server;
    private static MemecachedOperate MemecachedOperate;
    private static RemoteConnector remoteConnector;

    public static void main(String[] args) throws Throwable {
        if (args.length < 1) {
            throw new RuntimeException("please start with conf location param!");
        }
        PropertyConfigurator.configure(args[0] + "log4j.properties");//配置log4J
        String[] configPath = {"file:" + args[0] + "application.xml"};//此处需添加file:
        applicationContext = new ClassPathXmlApplicationContext(configPath);

        server = (Server) applicationContext.getBean("server", Server.class);
        MemecachedOperate = (MemecachedOperate) applicationContext.getBean("memcachedOperate", MemecachedOperate.class);
        remoteConnector = (RemoteConnector) applicationContext.getBean("remoteSystem");

        /**
         * 2015-11-12新增 控制台可以修改一些参数
         * 控制系统
         */
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {

                remoteConnector.acceptConnection();
            }
        });
        thread.start();


        String strRead = "";
        Scanner scanner = new Scanner(System.in);
        System.out.println("please enter your command:");
        while (!(strRead = scanner.nextLine()).equals("exit")) {
            if (strRead.startsWith("set")) {
                String[] argsStrings = strRead.split(":");
                if (argsStrings.length < 3) {
                    continue;
                } else {
                    try {
                        String ip = argsStrings[1];
                        int port = new Integer(argsStrings[2]);
                        server.getAcceptor().setDefaultLocalAddress(new InetSocketAddress(ip, port));
                        RemoteServerBean bean = server.getRemoteServerBean();
                        bean.setHostIP(ip);
                        bean.setPort(port);
                        MemecachedOperate.update("server.serverBean", bean);
                        logger.info("modified ip:" + ip + ":" + port + " from console");

                    } catch (Exception e) {
                        continue;
                    }


                }
            } else if (strRead.equals("start")) {
                server.startSever();
                logger.info("Data Center start from console");
            } else if (strRead.equals("stop")) {
                server.stopSever();
                logger.info("Data Center stop from console");
            }
            System.out.println("please enter your command：");
        }
        server.getExecutorService().shutdown();
        remoteConnector.stop();
        logger.info("main thread exit,please wait...");
        System.gc();
        System.exit(0);
    }
}
