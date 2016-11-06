package com.songxu.service;

import com.songxu.bean.User;
import com.songxu.util.ConstantValue;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ��¼service
 */
@Service("tokenService")
public class TokenService {

    private final Map<String, User> users = new ConcurrentHashMap<>();


    public String add(User user) {
        int id = user.getId();
        Long timeNow = System.currentTimeMillis();
        Long timeExp = timeNow + ConstantValue.OUTOFTIME;//����ʱ����ʮ����
        String token = Long.toHexString(timeExp) + "-" + id;
        users.put(user.getId() + "", user);
        return token.toUpperCase();
    }

    public User getUser(String token) {

        String strs[] = token.split("-");
        String exp = strs[0];
        String id = strs[1];
        long t = Long.parseLong(exp, 16);
        long t2 = System.currentTimeMillis();
        if (t < t2) {
            users.remove(id);
            return null;
        } else {
            return users.get(id);
        }

    }

    /**
     * �˳���¼
     * @param token
     */
    public void rmvUser(String token) {
        String strs[] = token.split("-");
        String id = strs[1];
        users.remove(id);
    }


}
