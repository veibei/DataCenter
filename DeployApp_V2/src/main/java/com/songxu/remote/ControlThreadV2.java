package com.songxu.remote;

import com.songxu.core.Server;
import org.apache.log4j.Logger;
import org.apache.mina.core.session.IoSession;

import java.io.*;
import java.net.Socket;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

public class ControlThreadV2 implements Callable<Integer> {

    private static Logger logger = Logger.getLogger(ControlThreadV2.class);
    private final Socket socket;
    private final Server server;
    private volatile boolean isrun = false;

    public ControlThreadV2(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
    }

    public void stop() {
        isrun = false;
    }

    @Override
    public Integer call() throws Exception {
        isrun = true;
        while (isrun) {
            try {
                InputStream inputStream = socket.getInputStream();
                if (inputStream.available() != 0) {
                    // 阻塞 等待inputstream
                    DataInputStream dataInputStream = new DataInputStream(inputStream);

                    String getInfo = dataInputStream.readUTF();

                    if (getInfo.equals("start")) {
                        /**
                         * 2015-10-27 新增记录远程控制ip和用户名功能
                         */
                        String requestInfo = dataInputStream.readUTF();
                        String[] userInfo = requestInfo.split("\\|");
                        // 启动服务
                        logger.info(userInfo[1] + "@" + userInfo[0]
                                + "request start server");
                        try {
                            server.startSever();
                            int count = 0;
                            //检验服务启动是否超时 50*100 毫秒
                            while (true) {
                                count++;
                                if (server.getStatus() == 1) {
                                    sendResult(true, socket.getOutputStream());
                                    break;
                                }
                                if (count > 100) {
                                    logger.info("service start timeout");
                                    server.stopSever();
                                    sendResult(false, socket.getOutputStream());
                                    break;

                                }
                                Thread.sleep(50);
                            }
                        } catch (Exception e) {
                            //如果在停止服务过程中出现错误。。。则报告错误
                            logger.error("start request error...");
                            sendResult(false, socket.getOutputStream());

                        }

                    } else if (getInfo.equals("stop")) {
                        String requestInfo = dataInputStream.readUTF();
                        String[] userInfo = requestInfo.split("\\|");
                        // 停止服务
                        logger.info(userInfo[1] + "@" + userInfo[0]
                                + "request stop server");
                        try {
                            server.stopSever();
                            int count = 0;
                            while (true) {
                                count++;
                                if (server.getStatus() == 0) {
                                    sendResult(true, socket.getOutputStream());
                                    break;
                                }
                                if (count > 100) {
                                    logger.info("service stop timeout");
                                    sendResult(false, socket.getOutputStream());
                                    break;

                                }
                                Thread.sleep(50);
                            }
                        } catch (Exception e) {
                            logger.error("stop request error");
                            sendResult(false, socket.getOutputStream());
                        }

                    } else if (getInfo.equals("close")) {
                        //远程主机关闭连接
                        sendResult(true, socket.getOutputStream());
                        break;
                    }
                    /**
                     * 获取DTU/Client在线连接情况
                     */
                    else if (getInfo.equals("DC")) {
                        sendDCOL(socket.getOutputStream());
                    } else {

                    }

                }
            } catch (IOException e) {
                // 远程控制器关闭  就关闭当前线程 释放资源
                // 所有的请求式的连接都是短连接 请求完毕即中断连接 释放掉资源
                //只有一个线程是长连接  获取状态的线程
                if (e.getMessage().equals("Software caused connection abort: socket write error")) {
                    logger.info("remote control socket closed" + socket.getRemoteSocketAddress().toString().replace("/", ""));
                    stop();
                } else {
                    e.printStackTrace();
                }

            }
        }
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.info("remote control thread stopped " + Thread.currentThread());
        return null;
    }

    /**
     * 发送结果
     *
     * @param result
     * @param outputStream
     * @throws IOException
     */
    private void sendResult(boolean result, OutputStream outputStream)
            throws IOException {
        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
        dataOutputStream.writeBoolean(result);
    }

    /**
     * 发送DTU/Client 在线连接情况
     *
     * @param outputStream
     * @throws IOException
     */
    private void sendDCOL(OutputStream outputStream) throws IOException {
        StringBuilder stringBuffer = new StringBuilder();

        ConcurrentHashMap<String, IoSession> clientPool = server.getClientPool();
        ConcurrentHashMap<String, IoSession> dTUPool = server.getDTUPool();

        Set<Entry<String, IoSession>> clientEntry = clientPool.entrySet();
        for (Entry<String, IoSession> entry : clientEntry) {
            stringBuffer.append(entry.getKey());
            stringBuffer.append(",");
            stringBuffer.append(entry.getValue().getRemoteAddress().toString().replace("/", ""));
            stringBuffer.append(",");
            double up = server.getRateUpCount().get(entry.getKey());
            double down = server.getRateDownCount().get(entry.getKey());
            stringBuffer.append(up);
            stringBuffer.append(",");//上行流量
            stringBuffer.append(down);//下行流量
            stringBuffer.append("|");
        }
        Set<Entry<String, IoSession>> dtuEntry = dTUPool.entrySet();
        for (Entry<String, IoSession> entry : dtuEntry) {
            stringBuffer.append(entry.getKey());
            stringBuffer.append(",");
            stringBuffer.append(entry.getValue().getRemoteAddress().toString().replace("/", ""));
            stringBuffer.append(",");
            double up = server.getRateUpCount().get(entry.getKey());
            double down = server.getRateDownCount().get(entry.getKey());
            stringBuffer.append(up);
            stringBuffer.append(",");//上行流量
            stringBuffer.append(down);//下行流量
            stringBuffer.append("|");
        }
        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
        dataOutputStream.writeUTF(stringBuffer.toString());


    }
}
