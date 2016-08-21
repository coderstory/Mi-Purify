package com.coderstory.Purify.module;

import android.os.Bundle;
import android.os.Message;
import android.widget.Button;
import android.widget.TextView;

import com.coderstory.Purify.plugins.IModule;

import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_InitPackageResources;
import de.robv.android.xposed.callbacks.XC_LayoutInflated;
import de.robv.android.xposed.callbacks.XC_LoadPackage;


public class miuiroot implements IModule {
    public static TextView WarningText;
    public static Button accept;

    @Override
    public void handleInitPackageResources(XC_InitPackageResources.InitPackageResourcesParam resparam) {
        XSharedPreferences prefs = new XSharedPreferences("com.coderstory.Purify", "UserSettings");
        prefs.makeWorldReadable();
        prefs.reload();
        if (!prefs.getBoolean("root", false)) {
            return;
        }
        if (!resparam.packageName.equals("com.miui.securitycenter")) {
            return;
        }
        resparam.res.hookLayout("com.miui.securitycenter", "layout", "pm_activity_root_apply", new XC_LayoutInflated() {
            public void handleLayoutInflated(XC_LayoutInflated.LayoutInflatedParam paramAnonymousLayoutInflatedParam)
                    throws Throwable {
                miuiroot.accept = (Button) paramAnonymousLayoutInflatedParam.view.findViewById(paramAnonymousLayoutInflatedParam.res.getIdentifier("accept", "id", "com.miui.securitycenter"));
                miuiroot.WarningText = (TextView) paramAnonymousLayoutInflatedParam.view.findViewById(paramAnonymousLayoutInflatedParam.res.getIdentifier("warning_info", "id", "com.miui.securitycenter"));
                if (miuiroot.WarningText != null) {
                    miuiroot.WarningText.setLines(6);
                }
            }
        });
    }

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) {
        XSharedPreferences prefs = new XSharedPreferences("com.coderstory.Purify", "UserSettings");
        prefs.makeWorldReadable();
        prefs.reload();
        if (!prefs.getBoolean("root", false)) {
            return;
        }

        if (!lpparam.packageName.equals("com.miui.securitycenter")) {
            return;
        }
        //创建弹出ROOT警告activity的方法
        XposedHelpers.findAndHookMethod("com.miui.permcenter.root.RootApplyActivity", lpparam.classLoader, "onCreate", Bundle.class, new XC_MethodHook() {
            protected void afterHookedMethod(MethodHookParam paramAnonymousMethodHookParam)
                    throws Throwable {
                // XposedHelpers.setIntField(paramAnonymousMethodHookParam.thisObject, "TK", 5);
                if (miuiroot.accept == null) {
                    return;
                }
                //模拟用户点击确定 5次
                int i = 0;
                while (i < 5) {
                    miuiroot.accept.performClick();
                    i += 1;
                }
            }
        });
        //这个方法是修改每次点击确定时显示不同的警告文字的 在miui8上找不到方法 可能不存在 或者已经改名
        XposedHelpers.findAndHookMethod("com.miui.permcenter.root.c", lpparam.classLoader, "handleMessage", Message.class, new XC_MethodReplacement() {
            protected Object replaceHookedMethod(MethodHookParam paramAnonymousMethodHookParam)
                    throws Throwable {
                return null;
            }
        });

        //这个方法是修改每次点击确定时显示不同的警告文字的 在miui8上找不到方法 可能不存在 或者已经改名
        XposedHelpers.findAndHookMethod("com.miui.permcenter.root.a", lpparam.classLoader, "handleMessage", Message.class, new XC_MethodReplacement() {
            protected Object replaceHookedMethod(MethodHookParam paramAnonymousMethodHookParam)
                    throws Throwable {
                return null;
            }
        });
    }

    @Override
    public void initZygote(IXposedHookZygoteInit.StartupParam startupParam) {

    }
}
