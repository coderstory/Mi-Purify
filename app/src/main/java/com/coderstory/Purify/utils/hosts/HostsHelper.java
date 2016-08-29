package com.coderstory.Purify.utils.hosts;


import android.content.Context;

import com.coderstory.Purify.utils.root.SuHelper;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Map;

/**
 * 和hosts相关的操作
 * Created by cc on 2016/6/7.
 */
public class HostsHelper extends SuHelper {

    private Context mContext = null; //Context
    private Map<String, String> HostsConfig; //hosts的用户配置

    public HostsHelper(Context mContext, Map<String, String> mMap) {
        this.mContext = mContext;
        this.HostsConfig = mMap;
    }

    /**
     * 构造需要root下执行的命令组
     *
     * @return 构造好的命令组
     * @throws UnsupportedEncodingException
     */
    @Override
    protected ArrayList<String> getCommandsToExecute() throws UnsupportedEncodingException {
        ArrayList<String> list = new ArrayList<>();
        list.add("mount -o rw,remount /system");
        FileHelper fh = new FileHelper();
        String content = fh.getFromAssets("none", mContext);
        list.add("echo '" + content + "' > /system/etc/hosts");//清空

        if (HostsConfig.get("enableHosts").equals("True")) {
            if (HostsConfig.get("enableBlockAdsHosts").equals("True")) {
                content = fh.getFromAssets("hosts_noad", mContext);
                list.add("echo '" + content + "' >> /system/etc/hosts");//移除广告
            }
            if (HostsConfig.get("enableGoogleHosts").equals("True")) {

                content = fh.getFromAssets("hosts", mContext);
                list.add("echo '" + content + "' >> /system/etc/hosts");//谷歌hosts


                content = fh.getFromAssets("google", mContext);
                list.add("echo '" + content + "' >> /system/etc/hosts");//谷歌hosts

            }
            if (HostsConfig.get("enableMIUIHosts").equals("True")) {
                content = fh.getFromAssets("hosts_miui", mContext);
                list.add("echo '" + content + "' >> /system/etc/hosts"); //屏蔽商店 音乐 视频
            }

            if (HostsConfig.get("enableStore").equals("True")) {
                content = fh.getFromAssets("hosts_nostore", mContext);
                list.add("echo '" + content + "' >> /system/etc/hosts"); //屏蔽商店 音乐 视频
            }

            if (HostsConfig.get("enableupdater").equals("True")) {
                content = fh.getFromAssets("hosts_noup", mContext);
                list.add("echo '" + content + "' >> /system/etc/hosts"); //屏蔽商店 音乐 视频
            }
        }

        return list;
    }
}
