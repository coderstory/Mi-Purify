package com.coderstory.Purify.utils.hosts;

import android.content.Context;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class FileHelper {


    public static int S_IRU = 400;
    public static int S_IRG = 40;
    public static int S_IRO = 4;
    public static int S_IWU = 200;
    public static int S_IWG = 20;
    public static int S_IWO = 2;
    public static int S_IXU = 100;
    public static int S_IXG = 10;
    public static int S_IXO = 1;
    public static int S_IRWU = S_IRU | S_IWU;
    public static int S_IRWG = S_IRG | S_IWG;
    public static int S_IRWO = S_IRO | S_IWO;
    public static int S_IRXU = S_IRU | S_IXU;
    public static int S_IRXG = S_IRG | S_IXG;
    public static int S_IRXO = S_IRO | S_IXO;
    public static int S_IRWXU = S_IRU | S_IWU | S_IXU;
    public static int S_IRWXG = S_IRG | S_IWG | S_IXG;
    public static int S_IRWXO = S_IRO | S_IWO | S_IXO;

    /**
     * 从Assets中读取文本
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




}
