package com.hnu.fk.utils;

import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 说明:
 *
 * @author WaveLee
 * 日期: 2018/8/8
 */
public class IpToAddressUtil {

    public static String getAddresses(String ip) {
        String content = "ip=" + ip;
        String encodingString = "utf-8";
        // 这里调用淘宝API
        String urlStr = "http://ip.taobao.com/service/getIpInfo.php";
        String returnStr = getResult(urlStr, content, encodingString);
        if (returnStr != null) {
            // 处理返回的json信息
            JSONObject json = new JSONObject(returnStr).getJSONObject("data");
            //国家
            String country = json.getString("country");
            //地区
            String area = json.getString("area");
            //省份
            String region = json.getString("region");
            //城市
            String city = json.getString("city");
            //区/县
            String county = json.getString("county");
            //互联网服务提供商
            String isp = json.getString("isp");

            String address = country + "/";
            address += region + "/";
            address += city + "/";
            address += county;
            if(country.isEmpty()){
                return "无效IP";
            }
            return address;
        }
        return null;
    }
    /**
     * @param urlStr
     *            请求的地址
     * @param content
     *            请求的参数 格式为：name=xxx&pwd=xxx
     * @param encoding
     *            服务器端请求编码。如GBK,UTF-8等
     * @return
     */
    private static String getResult(String urlStr, String content, String encoding) {
        URL url = null;
        HttpURLConnection connection = null;
        try {
            url = new URL(urlStr);
            connection = (HttpURLConnection) url.openConnection();// 新建连接实例
            connection.setConnectTimeout(2000);// 设置连接超时时间，单位毫秒
            connection.setReadTimeout(2000);// 设置读取数据超时时间，单位毫秒
            connection.setDoOutput(true);// 是否打开输出流 true|false
            connection.setDoInput(true);// 是否打开输入流true|false
            connection.setRequestMethod("POST");// 提交方法POST|GET
            connection.setUseCaches(false);// 是否缓存true|false
            connection.connect();// 打开连接端口
            DataOutputStream out = new DataOutputStream(connection
                    .getOutputStream());// 打开输出流往对端服务器写数据
            out.writeBytes(content);// 写数据,也就是提交你的表单 name=xxx&pwd=xxx
            out.flush();// 刷新
            out.close();// 关闭输出流
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    connection.getInputStream(), encoding));// 往对端写完数据对端服务器返回数据
            // ,以BufferedReader流来读取
            StringBuffer buffer = new StringBuffer();
            String line = "";
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            reader.close();
            return buffer.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();// 关闭连接
            }
        }
        return null;
    }

    // 测试
  /*  public static void main(String[] args) {
        // 参数ip
        String ip = "175.9.29.244";
        String address = IpToAddressUtil.getAddresses(ip);
        System.out.println(address);
    }*/
}