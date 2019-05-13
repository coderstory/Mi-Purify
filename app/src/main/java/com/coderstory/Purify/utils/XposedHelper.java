package com.coderstory.purify.utils;

import java.lang.ref.WeakReference;
import java.util.Set;

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
        try {
            XposedHelpers.findAndHookMethod(p1, lpparam, p2, parameterTypesAndCallback);
        } catch (Exception e) {
            XposedBridge.log(e);
        }
    }

    public static void hookAllConstructors(String p1, XC_MethodHook parameterTypesAndCallback) {
        try {
        Class packageParser = XposedHelpers.findClass(p1, null);
            hookAllConstructors(packageParser, parameterTypesAndCallback);
        } catch (Exception e) {
            XposedBridge.log(e);
        }
    }

    private static Set<XC_MethodHook.Unhook> hookAllConstructors(Class<?> hookClass, XC_MethodHook callback) {
        try {
            return XposedBridge.hookAllConstructors(hookClass, callback);
        } catch (Exception e) {
            XposedBridge.log(e);
            return null;
        }

    }

    protected static void findAndHookMethod(String p1, String p2, Object[] p3) throws ClassNotFoundException {
        try {
            XposedHelpers.findAndHookMethod(Class.forName(p1), p2, p3);
        } catch (Exception e) {
            XposedBridge.log(e);
        }
    }

    public static void hookAllMethods(String p1, ClassLoader lpparam, String methodName, XC_MethodHook parameterTypesAndCallback) {
        try {
            Class packageParser = XposedHelpers.findClass(p1, lpparam);
            XposedBridge.hookAllMethods(packageParser, methodName, parameterTypesAndCallback);
        } catch (Exception e) {
            XposedBridge.log(e);
        }

    }

    public void hookAllMethods(Class packageManagerServiceUtils, String verifySignatures, XC_MethodHook methodHook) {
        try {
            XposedBridge.hookAllMethods(packageManagerServiceUtils, verifySignatures, methodHook);
        } catch (Exception e) {
            XposedBridge.log(e);
        }
    }

    protected static Object getDrmResultSUCCESS() {
        Class<Enum> drmSuccess = (Class<Enum>) XposedHelpers.findClass("miui.drm.DrmManager.DrmResult", null);
        return Enum.valueOf(drmSuccess, "DRM_SUCCESS");
    }
}
