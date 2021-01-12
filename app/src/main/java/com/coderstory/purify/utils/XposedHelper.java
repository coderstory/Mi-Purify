package com.coderstory.purify.utils;

import com.coderstory.purify.BuildConfig;
import com.coderstory.purify.config.Misc;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

import static com.coderstory.purify.config.Misc.ApplicationName;

public class XposedHelper  {


    protected XSharedPreferences prefs;

    {
        if (android.os.Build.VERSION.SDK_INT == 30) {
            prefs = new XSharedPreferences(BuildConfig.APPLICATION_ID, Misc.SharedPreferencesName);
        } else {
            prefs = new XSharedPreferences(new File("/data/user_de/0/" + ApplicationName + "/shared_prefs/" + Misc.SharedPreferencesName + ".xml"));
        }
    }

    public static Class findClass(String classpatch, ClassLoader classLoader) {
        try {
            return XposedHelpers.findClass(classpatch, classLoader);
        } catch (XposedHelpers.ClassNotFoundError error) {
            XposedBridge.log(error.getMessage());
        }
        return null;
    }


    public static void findAndHookMethod(String p1, ClassLoader lpparam, String p2, Object... parameterTypesAndCallback) {
        try {
            XposedHelpers.findAndHookMethod(p1, lpparam, p2, parameterTypesAndCallback);

        } catch (Throwable error) {
            XposedBridge.log(error);
        }
    }

    public static void findAndHookMethod(Class<?> p1, String p2, Object... parameterTypesAndCallback) {
        try {
            XposedHelpers.findAndHookMethod(p1, p2, parameterTypesAndCallback);
        } catch (Throwable error) {
            XposedBridge.log(error);
        }
    }

    public static void hookAllConstructors(String p1, ClassLoader classLoader, XC_MethodHook parameterTypesAndCallback) {
        try {
            Class packageParser = XposedHelpers.findClass(p1, classLoader);
            XposedBridge.hookAllConstructors(packageParser, parameterTypesAndCallback);

        } catch (Throwable error) {
            XposedBridge.log(error);
        }
    }

    protected static void findAndHookMethod(String p1, String p2, Object[] p3) {
        try {
            XposedHelpers.findAndHookMethod(Class.forName(p1), p2, p3);
        } catch (Throwable error) {
            XposedBridge.log(error);
        }
    }

    public static int hookAllMethods(String p1, ClassLoader lpparam, String methodName, XC_MethodHook parameterTypesAndCallback) {
        try {
            Class packageParser = XposedHelpers.findClass(p1, lpparam);
            return XposedBridge.hookAllMethods(packageParser, methodName, parameterTypesAndCallback).size();
        } catch (Throwable error) {
            XposedBridge.log(error);
            return 0;
        }
    }

    public static Set<XC_MethodHook.Unhook> hookAllMethods(Class<?> hookClass, String methodName, XC_MethodHook callback) {
        try {
            return XposedBridge.hookAllMethods(hookClass, methodName, callback);
        } catch (Throwable error) {
            XposedBridge.log(error.getMessage());
        }
        return null;
    }

    public static void writeFile(File file, File file1) {
        FileInputStream fileInputStream;
        FileOutputStream fileOutputStream;
        BufferedInputStream bufferedInputStream;
        BufferedOutputStream bufferedOutputStream;
        try {
            fileInputStream = new FileInputStream(file);
            fileOutputStream = new FileOutputStream(file1);
            if (!file1.getParentFile().exists()) {
                file1.getParentFile().mkdirs();
            }
            bufferedInputStream = new BufferedInputStream(fileInputStream);
            bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
            byte[] bytes = new byte[5120];
            while (true) {
                int read = bufferedInputStream.read(bytes);
                if (read == -1) {
                    break;
                }

                bufferedOutputStream.write(bytes, 0, read);
            }
            bufferedOutputStream.flush();
            bufferedInputStream.close();
            bufferedOutputStream.close();
            fileOutputStream.close();
            fileInputStream.close();

        } catch (IOException error) {
            XposedBridge.log(error);
        }
    }

    public  void hookAllConstructors(String p1, XC_MethodHook parameterTypesAndCallback) {
        try {
            Class packageParser = findClass(p1, null);
            hookAllConstructors(packageParser, parameterTypesAndCallback);
        } catch (Exception e) {
            if (BuildConfig.DEBUG)
                XposedBridge.log(e);
        }
    }
    public static Class<?> findClassWithOutLog(String className, ClassLoader classLoader) {
        try {
            return Class.forName(className,false,classLoader);
        } catch (Exception e) {
            // 忽略
        }
        return null;
    }



    protected Set<XC_MethodHook.Unhook> hookAllConstructors(Class<?> hookClass, XC_MethodHook callback) {
        try {
            return XposedBridge.hookAllConstructors(hookClass, callback);
        } catch (Throwable error) {
            XposedBridge.log(error.getMessage());
            return new HashSet<>();
        }
    }

    protected Class findClassWithoutLog(String classpatch, ClassLoader classLoader) {
        try {
            return XposedHelpers.findClass(classpatch, classLoader);
        } catch (XposedHelpers.ClassNotFoundError error) {
            error.printStackTrace();
        }
        return null;
    }


    @SuppressWarnings("unchecked")
    protected static Object getDrmResultSUCCESS() {
        Class<Enum> drmSuccess = (Class<Enum>) XposedHelpers.findClass("miui.drm.DrmManager.DrmResult", null);
        return Enum.valueOf(drmSuccess, "DRM_SUCCESS");
    }
}
