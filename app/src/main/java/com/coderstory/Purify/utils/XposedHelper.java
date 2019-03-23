package com.coderstory.purify.utils;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

import static com.coderstory.purify.config.Misc.ApplicationName;
import static com.coderstory.purify.config.Misc.SharedPreferencesName;

public class XposedHelper {
    protected XSharedPreferences prefs = new XSharedPreferences(ApplicationName, SharedPreferencesName);
    {
        prefs.makeWorldReadable();
        prefs.reload();
    }

    public static void findAndHookMethod(String p1, ClassLoader lpparam, String p2, Object... parameterTypesAndCallback) {
        XposedHelpers.findAndHookMethod(p1, lpparam, p2, parameterTypesAndCallback);
    }

    public static void hookAllConstructors(String p1, XC_MethodHook parameterTypesAndCallback) {

        Class packageParser = XposedHelpers.findClass(p1, null);
        XposedBridge.hookAllConstructors(packageParser, parameterTypesAndCallback);


    }

    protected static void findAndHookMethod(String p1, String p2, Object[] p3) throws ClassNotFoundException {
        XposedHelpers.findAndHookMethod(Class.forName(p1), p2, p3);

    }

    public static void hookAllMethods(String p1, ClassLoader lpparam, String methodName, XC_MethodHook parameterTypesAndCallback) {
        Class packageParser = XposedHelpers.findClass(p1, lpparam);
        XposedBridge.hookAllMethods(packageParser, methodName, parameterTypesAndCallback);

    }


    protected static Object getDrmResultSUCCESS() {
        Class<Enum> drmSuccess = (Class<Enum>) XposedHelpers.findClass("miui.drm.DrmManager.DrmResult", null);
        return Enum.valueOf(drmSuccess, "DRM_SUCCESS");
    }
}
