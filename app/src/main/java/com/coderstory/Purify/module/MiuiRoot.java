package com.coderstory.Purify.module;

import android.os.Bundle;
import android.os.Message;
import android.widget.Button;
import android.widget.TextView;

import com.coderstory.Purify.plugins.IModule;
import com.coderstory.Purify.utils.XposedHelper;

import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_InitPackageResources;
import de.robv.android.xposed.callbacks.XC_LayoutInflated;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * 去除root25秒等待
 * Created by cc on 2016/6/17.
 */
public class MiuiRoot extends XposedHelper implements IModule {
    public static TextView WarningText;
    public static Button accept;

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) {

        if (!prefs.getBoolean("root25", false) && !loadPackageParam.packageName.equals("com.miui.securitycenter")) {
            return;
        }

        //创建弹出ROOT警告activity的方法
        XposedHelpers.findAndHookMethod("com.miui.permcenter.root.RootApplyActivity", loadPackageParam.classLoader, "onCreate", Bundle.class, new XC_MethodHook() {
            protected void afterHookedMethod(MethodHookParam paramAnonymousMethodHookParam)
                    throws Throwable {
                if (MiuiRoot.accept == null) {
                    return;
                }
                //模拟用户点击确定 5次
                int i = 0;
                while (i < 5) {
                    MiuiRoot.accept.performClick();
                    i += 1;
                }
            }
        });
        //这个方法是修改每次点击确定时显示不同的警告文字的 在miui8上找不到方法 可能不存在 或者已经改名
        XposedHelpers.findAndHookMethod("com.miui.permcenter.root.c", loadPackageParam.classLoader, "handleMessage", Message.class, new XC_MethodReplacement() {
            protected Object replaceHookedMethod(MethodHookParam paramAnonymousMethodHookParam)
                    throws Throwable {
                return null;
            }
        });

        //这个方法是修改每次点击确定时显示不同的警告文字的 在miui8上找不到方法 可能不存在 或者已经改名
        XposedHelpers.findAndHookMethod("com.miui.permcenter.root.a", loadPackageParam.classLoader, "handleMessage", Message.class, new XC_MethodReplacement() {
            protected Object replaceHookedMethod(MethodHookParam paramAnonymousMethodHookParam)
                    throws Throwable {
                return null;
            }
        });
    }

    @Override
    public void initZygote(IXposedHookZygoteInit.StartupParam startupParam) {

    }


    @Override
    public void handleInitPackageResources(XC_InitPackageResources.InitPackageResourcesParam initPackageResourcesParam) {
        if (!prefs.getBoolean("root25", false) && !initPackageResourcesParam.packageName.equals("com.miui.securitycenter")) {
            return;
        }
        initPackageResourcesParam.res.hookLayout("com.miui.securitycenter", "layout", "pm_activity_root_apply", new XC_LayoutInflated() {
            public void handleLayoutInflated(LayoutInflatedParam paramAnonymousLayoutInflatedParam)
                    throws Throwable {
                MiuiRoot.accept = paramAnonymousLayoutInflatedParam.view.findViewById(paramAnonymousLayoutInflatedParam.res.getIdentifier("accept", "id", "com.miui.securitycenter"));
                MiuiRoot.WarningText = paramAnonymousLayoutInflatedParam.view.findViewById(paramAnonymousLayoutInflatedParam.res.getIdentifier("warning_info", "id", "com.miui.securitycenter"));
                if (MiuiRoot.WarningText != null) {
                    MiuiRoot.WarningText.setLines(6);
                }
            }
        });
    }
}
