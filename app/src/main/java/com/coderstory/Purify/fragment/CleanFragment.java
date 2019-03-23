package com.coderstory.purify.fragment;


import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.widget.Button;
import android.widget.TextView;

import com.coderstory.purify.R;
import com.coderstory.purify.config.Misc;
import com.coderstory.purify.fragment.base.BaseFragment;
import com.coderstory.purify.utils.RuntimeUtil;
import com.coderstory.purify.utils.SnackBarUtils;
import com.coderstory.purify.utils.hostshelper.FileHelper;


import java.util.List;


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
            List<String> ret = RuntimeUtil.exec("find /data/data/ -type dir -name \"cache\"");
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

            sendMessageStr(getString(R.string.view_clean_complete, FileHelper.getReadableFileSize(totalSize)));
            hComplete.sendEmptyMessage(0);
            Misc.isProcessing = false;
        });
        th.start();
    }


    private CacheSize getSize(String path) {
        try {
            String result = RuntimeUtil.exec(String.format("du -s -k \"%s\"", path)).get(0);
            String sizeStr = result.substring(0, result.indexOf('\t')).trim();
            long size;
            size = Long.parseLong(sizeStr);
            return new CacheSize(sizeStr + "K", size);
        } catch (Exception e) {
            return new CacheSize(0 + "K", 0);
        }

    }

    private void deleteCache(String path) {
        RuntimeUtil.execSilent(String.format("rm -r \"%s\"", path));
    }

    private void deleteAnrLog() {
        RuntimeUtil.exec("rm -r /data/anr/*");
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