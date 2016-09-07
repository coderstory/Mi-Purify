package com.coderstory.Purify.utils.hosts;


import android.os.Environment;

import com.coderstory.Purify.utils.root.SuHelper;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * 和hosts相关的操作
 * Created by cc on 2016/6/7.
 */
public class HostsHelper extends SuHelper {
    private String mcontent;

    public HostsHelper(String mcontent) {
        this.mcontent = mcontent;
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

        String tmpDir = Environment.getExternalStorageDirectory().getAbsolutePath();
        File fDir = new File(tmpDir, ".tmp");
        if (!fDir.exists()) {
            fDir.mkdirs();
        }
        String tmpFile = new File(fDir, "hosts").getAbsolutePath();

        try {
            FileHelper.rewriteFile(tmpFile, mcontent);
        } catch (IOException e) {
            e.printStackTrace();
        }
        list.add(String.format("cp %s %s", tmpFile, "/etc/hosts"));
        list.add(String.format("chmod 755 %s", "/etc/hosts"));


        return list;
    }
}
