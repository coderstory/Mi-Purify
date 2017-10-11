package com.coderstory.Purify.utils.checkupdate;

/**
 * APP在线功能的检测配置信息
 */

public class UpdateConfig {
    /* 下载包安装路径 */
    public static final String saveFileName = "/Mi Kit/";
    public static String URL = "";//app的下载地址
    public static String Version = "";//最新版的versionCode
    //版本信息
    static int localVersion = 0;
    static int serverVersion = 0;
    static String Info = ""; //更新内容
    static String errorMsg = ""; //错误提示
    static String UpdateServer = "http://blog.coderstory.cn/info";
}
