package com.coderstory.Purify.Update;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * app更新
 */
class HttpHelper {
    /**
     * 调用接口
     *
     * @param UpdateUrl 更新url地址
     * @return
     */

    public String RequestUrl(String UpdateUrl) {
        String rvalue = "";
        try {
            //声明URL
            URL url = new URL(UpdateUrl);
            //打开连接
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //设置连接方式
            conn.setRequestMethod("POST");
            //获取返回值
            InputStream inStream = conn.getInputStream();
            //流转化为字符串
            rvalue = streamToStr(inStream);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            UpdateConfig.errorMsg = "Oops...软件更新服务器似乎挂掉了..";
            // e.printStackTrace();
        }
        return rvalue;
    }

    /**
     * 把流对象转换成字符串对象
     */
    private String streamToStr(InputStream is) {
        try {
            // 定义字节数组输出流对象
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            // 定义读取的长度
            int len;
            // 定义读取的缓冲区
            byte buffer[] = new byte[1024];
            // 按照定义的缓冲区进行循环读取，直到读取完毕为止
            while ((len = is.read(buffer)) != -1) {
                // 根据读取的长度写入到字节数组输出流对象中
                os.write(buffer, 0, len);
            }
            // 关闭流
            is.close();
            os.close();
            // 把读取的字节数组输出流对象转换成字节数组
            byte data[] = os.toByteArray();
            // 按照指定的编码进行转换成字符串(此编码要与服务端的编码一致就不会出现乱码问题了，android默认的编码为UTF-8)
            return new String(data, "UTF-8");
        } catch (IOException e) {
            UpdateConfig.errorMsg = "更新服务器访问失败，请稍后再试。。。";
            e.printStackTrace();
            return null;
        }
    }
}
