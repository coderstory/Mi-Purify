package com.coderstory.purify.module;

import com.coderstory.purify.plugins.IModule;
import com.coderstory.purify.utils.XposedHelper;

import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class test extends XposedHelper implements IModule {
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) {
        if ("me.weishu.corepatch".equals(lpparam.packageName)) {

            hookAllMethods("me.weishu.corepatch.MainActivity", lpparam.classLoader, "onCreate", new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                    String[] aa = new String[]{
                            "IAAcAzkG", "IgMeCic+Ggw3QVlW", "JwYBBDINETweV0JYXzo="


                    };
                    for (String s : aa) {
                        Class<?> aaaa = Class.forName("me.weishu.corepatch.f");
                        XposedBridge.log(s + "" + XposedHelpers.callMethod(aaaa, "a", s).toString());
                    }

                }
            });

            hookAllMethods("me.weishu.corepatch.f", lpparam.classLoader, "a", new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    XposedBridge.log(param.args[0] + " " + param.getResult());
                }
            });

        }
    }

    @Override
    public void initZygote(IXposedHookZygoteInit.StartupParam startupParam) {

    }

    public static String a(String str) {
        String  a = aaa();
        XposedBridge.log(str+"   "+ a);
        return a;
    }

    public static String aaa(){
        return  "xxxx";
    }

}
