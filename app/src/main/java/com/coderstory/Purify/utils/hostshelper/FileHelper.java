package com.coderstory.Purify.utils.hostshelper;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class FileHelper {


    public static void rewriteFile(File file, String text) throws IOException {
        writeFileByWriter(file, text, false);
    }

    public static void rewriteFile(String path, String text) throws IOException {
        File f = new File(path);
        rewriteFile(f, text);
    }

    private static void writeFileByWriter(File file, String text, boolean append) throws IOException {
        FileWriter writer = new FileWriter(file, append);
        if (append) {
            writer.append(text);
        } else {
            writer.write(text);
        }
        writer.close();
    }

    public static List<String> readFile(File file) throws IOException {
        FileReader reader = new FileReader(file);
        BufferedReader bufferReader = new BufferedReader(reader);
        String line;
        List<String> fileText = new ArrayList<>();
        while ((line = bufferReader.readLine()) != null) {
            fileText.add(line);
        }
        bufferReader.close();
        reader.close();
        return fileText;
    }

    public static String getReadableFileSize(long size) {
        String[] units = new String[]{"K", "M", "G", "T", "P"};
        double nSize = size * 1L * 1.0f;
        double mod = 1024.0f;
        int i = 0;
        while (nSize >= mod) {
            nSize /= mod;
            i++;
        }
        DecimalFormat df = new DecimalFormat("#.##");
        return String.format("%s %s", df.format(nSize), units[i]);
    }

    /**
     * 从Assets中读取文本
     *
     * @param FileName 文件名
     * @param mContext context
     * @return 读取到的文本
     */
    public String getFromAssets(String FileName, Context mContext) {
        try {
            InputStreamReader inputReader = new InputStreamReader(mContext.getAssets().open(FileName), "utf-8");
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line;
            String Result = "";
            while ((line = bufReader.readLine()) != null)
                Result += line + "\n";
            return Result;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }


}
