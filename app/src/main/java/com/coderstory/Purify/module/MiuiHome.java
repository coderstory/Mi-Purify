package com.coderstory.Purify.module;

import android.content.Context;
import android.view.View;

import com.coderstory.Purify.plugins.IModule;
import com.coderstory.Purify.utils.XposedHelper;

import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.callbacks.XC_LoadPackage;


public class MiuiHome extends XposedHelper implements IModule {


    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) {
        if (lpparam.packageName.equals("com.miui.home") && prefs.getBoolean("hide_icon_label", false)) {
            findAndHookMethod("com.miui.home.launcher.ShortcutIcon", lpparam.classLoader, "onFinishInflate", new XC_MethodHook() {
                protected void afterHookedMethod(MethodHookParam arg10) throws Throwable {
                    View view = (View) arg10.thisObject;
                    View v2 = view.findViewById(view.getContext().getResources().getIdentifier("icon_title", "id", "com.miui.home"));
                    v2.setVisibility(View.GONE);
                }
            });

            findAndHookMethod("com.miui.home.launcher.FolderInfo", lpparam.classLoader, "getTitle", Context.class, XC_MethodReplacement.returnConstant(""));
        }
    }

    @Override
    public void initZygote(IXposedHookZygoteInit.StartupParam startupParam) {
    }
}
