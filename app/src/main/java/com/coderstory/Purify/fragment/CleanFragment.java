package com.coderstory.Purify.fragment;


import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.coderstory.Purify.R;
import com.coderstory.Purify.utils.MyConfig;
import com.coderstory.Purify.utils.hosts.FileHelper;
import com.coderstory.Purify.utils.root.CommandResult;
import com.coderstory.Purify.utils.root.RootUtils;

import ren.solid.library.fragment.base.BaseFragment;
import ren.solid.library.utils.SnackBarUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class CleanFragment extends BaseFragment {
    private TextView tvClean = null;


    public CleanFragment() {
        // Required empty public constructor
    }

    @Override
    protected void setUpView() {
        super.setUpView();
        $(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                threadClean();
            }
        });
    }

    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_clean;
    }

    private Handler hInfo = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            tvClean.append((String) msg.obj);
            super.handleMessage(msg);

        }
    };

    private Handler hComplete = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ((Button) $(R.id.button)).setText("开始清理");
            $(R.id.button).setEnabled(true);
            SnackBarUtils.makeShort($(R.id.button), "应用清理完成！").info();
        }
    };

    private void sendMessageStr(String str) {
        Message msg = new Message();
        msg.obj = str;
        hInfo.sendMessage(msg);
    }

    Thread th;
    public void threadClean() {
        tvClean = $(R.id.tvClean);
        ((Button) $(R.id.button)).setText("正在清理中...");
        tvClean.append(getString(R.string.view_start_clean));
        $(R.id.button).setEnabled(false);
        th = new Thread(new Runnable() {
            @Override
            public void run() {
                long totalSize = 0L; // K
                MyConfig.isProcessing=true;
                // clean app cache
                CommandResult ret = RootUtils.runCommand("find /data/data/ -type dir -name \"cache\"", true);
                String[] items = ret.result.split("\n");
                CacheSize cs;
                for (String s : items) {
                    cs = getSize(s);
                    if (cs != null && cs.size > 16) { // clean only above 16K
                        if (deleteCache(s)) {
                            sendMessageStr(getString(R.string.view_clean_cache, s, cs.sizeReadable));
                            totalSize += cs.size;
                        }
                    }
                }
                // clean anr log
                CacheSize anrSize = getSize("/data/anr/");
                if (deleteAnrLog()) {
                    sendMessageStr(getString(R.string.view_clean_anr, anrSize.sizeReadable));
                    totalSize += anrSize.size;
                }
                // clean art
                totalSize += deleteRemainArtCache();
                sendMessageStr(getString(R.string.view_clean_complete, FileHelper.getReadableFileSize(totalSize)));
                hComplete.sendEmptyMessage(0);
                MyConfig.isProcessing=false;
            }
        });
        th.start();
    }





    private CacheSize getSize(String path) {

        try {
            CommandResult ret = RootUtils.runCommand(String.format("du -s -k \"%s\"", path), true);
            String sizeStr = ret.result.substring(0, ret.result.indexOf('\t')).trim();
            long size = 0L;
            size = Long.parseLong(sizeStr);
            return new CacheSize(sizeStr + "K", size);
        } catch (Exception e) {
            return null;
        }

    }

    private boolean deleteCache(String path) {
        CommandResult ret = RootUtils.runCommand(String.format("rm -r \"%s\"", path), true);
        return ret.error.equals("");
    }

    private boolean deleteAnrLog() {
        CommandResult ret = RootUtils.runCommand("rm -r /data/anr/*", true);
        return ret.error.equals("");
    }

    private long deleteRemainArtCache() {

        CommandResult list = RootUtils.runCommand("ls /data/app", true);
        String[] listInstalled = list.result.split("\n");
        CommandResult listAll = RootUtils.runCommand("pm list packages", true);
        String[] listAllInstalled = listAll.result.split("\n");
        CommandResult retArm = RootUtils.runCommand("ls /data/dalvik-cache/arm", true);
        CommandResult retArm64 = RootUtils.runCommand("ls /data/dalvik-cache/arm64", true);
        CommandResult retProfile = RootUtils.runCommand("ls /data/dalvik-cache/profiles", true);
        String[] listArm = retArm.result.split("\n");
        String[] listArm64 = retArm64.result.split("\n");
        String[] listProfile = retProfile.result.split("\n");
        long totalSize = 0L;
        String tmpPath;
        CacheSize size;
        for (String s : listArm) {
            if (!s.trim().equals("")) {
                if (!isCachedAppInstalled(listInstalled, s)) {
                    tmpPath = "/data/dalvik-cache/arm/" + s;
                    size = getSize(tmpPath);
                    if (deleteCache(tmpPath)) {
                        sendMessageStr(getString(R.string.view_clean_art_remain, s, size.sizeReadable));
                        totalSize += size.size;
                    }
                }
            }
        }

        for (String s : listArm64) {
            if (!s.trim().equals("")) {
                if (!isCachedAppInstalled(listInstalled, s)) {
                    tmpPath = "/data/dalvik-cache/arm64/" + s;
                    size = getSize(tmpPath);
                    if (deleteCache(tmpPath)) {
                        sendMessageStr(getString(R.string.view_clean_art_remain, s, size.sizeReadable));
                        totalSize += size.size;
                    }
                }
            }
        }

        for (String s : listProfile) {
            if (!s.trim().equals("")) {
                if (!isProfileInstalled(listAllInstalled, s)) {
                    tmpPath = "/data/dalvik-cache/profiles/" + s;
                    size = getSize(tmpPath);
                    if (deleteCache(tmpPath)) {
                        sendMessageStr(getString(R.string.view_clean_art_remain, s, size.sizeReadable));
                        totalSize += size.size;
                    }
                }
            }
        }
        return totalSize;
    }

    private boolean isCachedAppInstalled(String[] oriList, String app) {
        if (app.startsWith("system") || app.startsWith("data@dalvik-cache")) {
            // do not delete anything about system
            return true;
        }
        String newAppPath = app.replace("data@app@", "");
        try {
            newAppPath = newAppPath.substring(0, newAppPath.indexOf("@"));
        }
        catch (Exception e){
            return  false;
        }
        boolean ret = false;
        for (String s : oriList) {
            if (s.equals(newAppPath)) {
                ret = true;
                break;
            }
        }
        return ret;
    }

    private boolean isProfileInstalled(String[] oriList, String app) {
        boolean ret = false;
        for (String s : oriList) {
            if (s.contains(app)) {
                ret = true;
                break;
            }
        }
        return ret;
    }

    private class CacheSize {
        String sizeReadable = "";
        long size = 0L;

        CacheSize(String sr, long s) {
            sizeReadable = sr;
            size = s;
        }
    }

}
