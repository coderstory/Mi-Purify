package com.coderstory.Purify.fragment;


import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.coderstory.Purify.R;
import com.coderstory.Purify.config.Misc;
import com.coderstory.Purify.fragment.base.BaseFragment;
import com.coderstory.Purify.utils.SnackBarUtils;
import com.coderstory.Purify.utils.hostshelper.FileHelper;

import java.util.List;

import eu.chainfire.libsuperuser.Shell;

public class CleanFragment extends BaseFragment {
    Thread th;
    private TextView tvClean = null;
    @SuppressLint("HandlerLeak")
    private Handler hInfo = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            tvClean.append((String) msg.obj);
            super.handleMessage(msg);
        }
    };
    @SuppressLint("HandlerLeak")
    private Handler hComplete = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ((Button) $(R.id.button)).setText(R.string.starting_clean);
            $(R.id.button).setEnabled(true);
            SnackBarUtils.makeShort($(R.id.button), getString(R.string.clean_success)).info();
        }
    };

    public CleanFragment() {
    }

    @Override
    protected void setUpView() {
        super.setUpView();
        $(R.id.button).setOnClickListener(view -> threadClean());
    }

    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_clean;
    }

    private void sendMessageStr(String str) {
        Message msg = new Message();
        msg.obj = str;
        hInfo.sendMessage(msg);
    }

    public void threadClean() {
        tvClean = $(R.id.tvClean);
        ((Button) $(R.id.button)).setText(R.string.cleaning);
        tvClean.append(getString(R.string.view_start_clean));
        $(R.id.button).setEnabled(false);
        th = new Thread(() -> {
            long totalSize = 0L; // K
            Misc.isProcessing = true;
            List<String> ret = Shell.SU.run("find /data/data/ -type dir -name \"cache\"");
            CacheSize cs;
            for (String s : ret) {
                cs = getSize(s);
                if (cs.size > 16) { // clean only above 16K
                    deleteCache(s);
                    sendMessageStr(getString(R.string.view_clean_cache, s, cs.sizeReadable));
                    totalSize += cs.size;

                }
            }
            // clean anr log
            CacheSize anrSize = getSize("/data/anr/");
            deleteAnrLog();
            sendMessageStr(getString(R.string.view_clean_anr, anrSize.sizeReadable));
            totalSize += anrSize.size;

            // clean art
            totalSize += deleteRemainArtCache();
            sendMessageStr(getString(R.string.view_clean_complete, FileHelper.getReadableFileSize(totalSize)));
            hComplete.sendEmptyMessage(0);
            Misc.isProcessing = false;
        });
        th.start();
    }


    private CacheSize getSize(String path) {
        try {
            String result = Shell.SU.run(String.format("du -s -k \"%s\"", path)).get(0);
            String sizeStr = result.substring(0, result.indexOf('\t')).trim();
            long size;
            size = Long.parseLong(sizeStr);
            return new CacheSize(sizeStr + "K", size);
        } catch (Exception e) {
            return new CacheSize(0 + "K", 0);
        }

    }

    private void deleteCache(String path) {
        Shell.SU.run(String.format("rm -r \"%s\"", path));
    }

    private void deleteAnrLog() {
        Shell.SU.run("rm -r /data/anr/*");
    }

    private long deleteRemainArtCache() {

        List<String> listInstalled = Shell.SU.run("ls /data/app");
        List<String> listAllInstalled = Shell.SU.run("pm list packages");
        List<String> listArm = Shell.SU.run("ls /data/dalvik-cache/arm");
        List<String> listArm64 = Shell.SU.run("ls /data/dalvik-cache/arm64");
        List<String> listProfile = Shell.SU.run("ls /data/dalvik-cache/profiles");
        long totalSize = 0L;
        String tmpPath;
        CacheSize size;
        for (String s : listArm) {
            if (!s.trim().equals("")) {
                if (!isCachedAppInstalled(listInstalled, s)) {
                    tmpPath = "/data/dalvik-cache/arm/" + s;
                    size = getSize(tmpPath);
                    deleteCache(tmpPath);
                    sendMessageStr(getString(R.string.view_clean_art_remain, s, size.sizeReadable));
                    totalSize += size.size;

                }
            }
        }

        for (String s : listArm64) {
            if (!s.trim().equals("")) {
                if (!isCachedAppInstalled(listInstalled, s)) {
                    tmpPath = "/data/dalvik-cache/arm64/" + s;
                    size = getSize(tmpPath);
                    deleteCache(tmpPath);
                    sendMessageStr(getString(R.string.view_clean_art_remain, s, size.sizeReadable));
                    totalSize += size.size;

                }
            }
        }

        for (String s : listProfile) {
            if (!s.trim().equals("")) {
                if (!isProfileInstalled(listAllInstalled, s)) {
                    tmpPath = "/data/dalvik-cache/profiles/" + s;
                    size = getSize(tmpPath);
                    deleteCache(tmpPath);
                    sendMessageStr(getString(R.string.view_clean_art_remain, s, size.sizeReadable));
                    totalSize += size.size;
                }
            }
        }
        return totalSize;
    }

    private boolean isCachedAppInstalled(List<String> oriList, String app) {
        if (app.startsWith("system") || app.startsWith("data@dalvik-cache")) {
            return true;
        }
        String newAppPath = app.replace("data@app@", "");
        try {
            newAppPath = newAppPath.substring(0, newAppPath.indexOf("@"));
        } catch (Exception e) {
            Log.e("MIUI", "isCachedAppInstalled: " + e.getMessage());
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

    private boolean isProfileInstalled(List<String> oriList, String app) {
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
