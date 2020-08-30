package com.sy.shopping.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class SMSUtils {
    private SMSUtils() {

    }

    /**
     * 通过短信平台发送短信到指定的手机号
     *
     * @param mobile 向哪个手机号发送
     * @param validateCode 要发送的验证码
     */
    public static void sendValidateCode(String mobile, String validateCode) {
        try {
            //创建连接
            URL url = new URL("http://api01.monyun.cn:7901/sms/v2/std/single_send");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("POST");
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.connect();
            // POST请求
            DataOutputStream out = new
                    DataOutputStream(connection.getOutputStream());
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("apikey", "6118dcfccd0057367cdaecf1fcd63ecf");
            jsonObject.put("mobile", mobile);
            jsonObject.put("content", URLEncoder.encode("验证码：" + validateCode + "，打死都不要告诉别人哦！", "gbk"));
            out.writeBytes(jsonObject.toJSONString());
            out.flush();
            out.close();
            // 读取响应
            BufferedReader reader = new BufferedReader(new
                    InputStreamReader(connection.getInputStream()));
            String lines;
            StringBuffer sb = new StringBuffer("");
            while ((lines = reader.readLine()) != null) {
                sb.append(lines);
            }
            JSONObject result = JSON.parseObject(sb.toString());
            System.out.println(result);
            reader.close();
            // 断开连接
            connection.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
