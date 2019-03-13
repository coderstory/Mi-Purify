package com.coderstory.Purify.config;


public class Misc {
    public static final String BasePath = "/sdcard/MIUI_Purify";
    public static final String BackPath = BasePath + "/Backup/";
    public static final String CrashFilePath = BasePath + "/CrashLog/";
    public static final String MyBlogUrl = "https://blog.coderstory.cn";
    public static final String ApplicationName = "com.coderstory.Purify";
    public static final String SharedPreferencesName = "UserSettings";
    public static final String HostFileTmpName = "/hosts";
    public static boolean isProcessing = false;

    public static boolean isEnable() {
       /* Date currentTime = new Date();// 当前时间
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String nowtime = formatter.format(currentTime);

        Calendar now = Calendar.getInstance();
        Calendar c1 = Calendar.getInstance();
        String t1 = "2018-02-28 08:30:00";
        try {
            now.setTime(formatter.parse(nowtime));
            c1.setTime(formatter.parse(t1));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return now.compareTo(c1) < 0;// 比开始时间小，未开始*/
        return true;
    }
}
