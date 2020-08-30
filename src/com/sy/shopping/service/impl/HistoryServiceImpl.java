package com.sy.shopping.service.impl;

import com.sy.shopping.constant.BusinessConstant;
import com.sy.shopping.entity.User;
import com.sy.shopping.service.HistoryService;
import com.sy.shopping.service.UserService;
import com.sy.shopping.utils.HttpUtils;
import com.sy.shopping.utils.RedisUtils;

import java.util.*;

public class HistoryServiceImpl implements HistoryService {

    @Override
    public void mergeHistory(Integer userId) {
        //1.获取当前的临时记录
        String cookieTempHistory = HttpUtils.getCookie(BusinessConstant.COOKIE_TEMP_HISTORY_NAME);
        String tempKey = BusinessConstant.REDIS_TEMP_HISTORY_KEY_PREFIX + cookieTempHistory;
        List<String> tempList = new ArrayList<>();
        if (RedisUtils.exists(tempKey)) {
            tempList =
                    RedisUtils.lrange(tempKey,
                            BusinessConstant.REDIS_LIST_START_INDEX,
                            RedisUtils.llen(tempKey) - 1);
        }

        //2.获取当前登录用户的历史记录
        String userKey = BusinessConstant.REDIS_USER_HISTORY_KEY_PREFIX + userId;
        List<String> userList = new ArrayList<>();
        if (RedisUtils.exists(userKey)) {
            userList =
                    RedisUtils.lrange(userKey,
                            BusinessConstant.REDIS_LIST_START_INDEX,
                            RedisUtils.llen(userKey) - 1);
        }
        //3.两块历史记录合并在一起
        Set<String> historySet = new LinkedHashSet<>();
        historySet.addAll(tempList);
        historySet.addAll(userList);
        System.out.println(historySet.size());

        //4.将合并后的记录替换当前用户的历史记录
        if (historySet.size()!=0){
            System.out.println(123);
            RedisUtils.del(BusinessConstant.REDIS_USER_HISTORY_KEY_PREFIX + userId);
            RedisUtils.lpush(BusinessConstant.REDIS_USER_HISTORY_KEY_PREFIX + userId,
                    new ArrayList<>(historySet).toArray(new String[0]));
            //5.删除临时记录
            RedisUtils.del(BusinessConstant.REDIS_TEMP_HISTORY_KEY_PREFIX + cookieTempHistory);
        }


    }

    public static void main(String[] args) {
        new HistoryServiceImpl().mergeHistory(5);

    }
}
