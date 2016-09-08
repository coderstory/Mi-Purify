package com.coderstory.Purify.utils.hosts;


import android.content.Context;

import com.coderstory.Purify.utils.root.SuHelper;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * 和hosts相关的操作
 * Created by cc on 2016/6/7.
 */
public class HostsHelper extends SuHelper {
    private String mcontent;
    private  Context mcontext=null;

    public HostsHelper(String mcontent,Context m) {
        this.mcontent = mcontent;
        this.mcontext=m;
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

        String path="/data/data/com.coderstory.Purify/files/hosts";

        FileOutputStream out=null;
        BufferedWriter writer;
        try {
            out=mcontext.openFileOutput("hosts", Context.MODE_PRIVATE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        writer=new BufferedWriter(new OutputStreamWriter(out));
        try {
            writer.write(mcontent);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        list.add(String.format("mv %s %s", path, "/etc/hosts"));
        list.add(String.format("chmod 755 %s", "/etc/hosts"));


        return list;
    }
}
