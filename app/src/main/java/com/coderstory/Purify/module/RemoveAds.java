package com.coderstory.Purify.module;

import android.content.Context;

import com.coderstory.Purify.plugins.IModule;

import java.util.List;
import java.util.Map;

import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_InitPackageResources;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created by Baby Song on 2016/8/17.
 */

public class RemoveAds implements IModule {

    private static XC_LoadPackage.LoadPackageParam loadPackageParam;

    @Override
    public void handleInitPackageResources(XC_InitPackageResources.InitPackageResourcesParam resparam) {
    }

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) {
        loadPackageParam = lpparam;
        patchcode();
    }

    @Override
    public void initZygote(IXposedHookZygoteInit.StartupParam startupParam) {
    }

    private void patchcode() {

        XSharedPreferences prefs = new XSharedPreferences("com.coderstory.Purify", "UserSettings");
        prefs.makeWorldReadable();
        prefs.reload();

        //核心模块
        if (loadPackageParam.packageName.equals("com.miui.core")) {
            findAndHookMethod("miui.os.SystemProperties", loadPackageParam.classLoader, "get", String.class, String.class, new XC_MethodHook() {

                protected void afterHookedMethod(MethodHookParam paramAnonymousMethodHookParam)
                        throws Throwable {
                    if (paramAnonymousMethodHookParam.args[0].toString().equals("ro.product.mod_device")) {
                        paramAnonymousMethodHookParam.setResult("gemini_global");
                    }
                }
                protected void beforeHookedMethod(MethodHookParam paramAnonymousMethodHookParam)
                        throws Throwable {
                    if (paramAnonymousMethodHookParam.args[0].toString().equals("ro.product.mod_device")) {
                        paramAnonymousMethodHookParam.setResult("gemini_global");
                    }
                }
            });
//            try {
//                XposedHelpers.setStaticBooleanField(Class.forName("miui.os.SystemProperties.Build"), "IS_CM_CUSTOMIZATION_TEST", true);
//            } catch (ClassNotFoundException e) {
//                e.printStackTrace();
//            }
//
//
//            try {
//                XposedHelpers.setStaticBooleanField(Class.forName("com.miui.internal.util"),"IS_INTERNATIONAL_BUILD",true);
//            } catch (ClassNotFoundException e) {
//                e.printStackTrace();
//            }
            return;
        }

        //垃圾清理
        if (loadPackageParam.packageName.equals("com.miui.cleanmaster")) {

            if (prefs.getBoolean("enableSafeCenter", false)) {
                findAndHookMethod("com.miui.optimizecenter.result.DataModel", loadPackageParam.classLoader, "post", Map.class, new XC_MethodHook() {
                    protected void beforeHookedMethod(MethodHookParam paramAnonymousMethodHookParam)
                            throws Throwable {
                        paramAnonymousMethodHookParam.setResult("");
                    }
                });
                findAndHookMethod("com.miui.optimizecenter.config.MiStat", loadPackageParam.classLoader, "getChannel", new XC_MethodHook() {
                    protected void beforeHookedMethod(MethodHookParam paramAnonymousMethodHookParam)
                            throws Throwable {
                        paramAnonymousMethodHookParam.setResult("international");
                    }
                });
            }
        }

        //视频
//        if (loadPackageParam.packageName.equals("com.miui.video")) {
//            findAndHookMethod("com.miui.videoplayer.ads.DynamicAd", loadPackageParam.classLoader, "replaceList", List.class, String.class, new XC_MethodHook() {
//                protected void beforeHookedMethod(MethodHookParam paramAnonymousMethodHookParam)
//                        throws Throwable {
//                    paramAnonymousMethodHookParam.args[0] = null;
//                    paramAnonymousMethodHookParam.args[1] = null;
//                }
//            });
//        }

        //文件管理器
        if (loadPackageParam.packageName.equals("com.android.fileexplorer")) {

            if (prefs.getBoolean("enableFileManager", false)) {
                findAndHookMethod("com.android.fileexplorer.model.ConfigHelper", loadPackageParam.classLoader, "isAdEnable", Context.class, String.class, XC_MethodReplacement.returnConstant(false));
                findAndHookMethod("com.android.fileexplorer.model.ConfigHelper", loadPackageParam.classLoader, "supportAd", XC_MethodReplacement.returnConstant(false));
                findAndHookMethod("com.android.fileexplorer.model.ConfigHelper", loadPackageParam.classLoader, "ifAdShowByCloudForNetwork", Context.class, String.class, XC_MethodReplacement.returnConstant(false));
                findAndHookMethod("com.android.fileexplorer.model.ConfigHelper", loadPackageParam.classLoader, "getHomePageHotVideoTipSwitch", Context.class, XC_MethodReplacement.returnConstant(false));
                findAndHookMethod("com.android.fileexplorer.model.ConfigHelper", loadPackageParam.classLoader, "getHomePageHotVideoTopicUri", Context.class, XC_MethodReplacement.returnConstant(""));
                findAndHookMethod("com.android.fileexplorer.model.ConfigHelper", loadPackageParam.classLoader, "getAdStyleName", Context.class, String.class, XC_MethodReplacement.returnConstant(""));
                findAndHookMethod("com.android.fileexplorer.model.ConfigHelper", loadPackageParam.classLoader, "tryInit", Context.class, XC_MethodReplacement.returnConstant(false));
                findAndHookMethod("com.android.fileexplorer.model.ConfigHelper", loadPackageParam.classLoader, "isVideoEnable", Context.class, XC_MethodReplacement.returnConstant(false));
                findAndHookMethod("com.android.fileexplorer.model.ConfigHelper", loadPackageParam.classLoader, "isStickerEnable", Context.class, XC_MethodReplacement.returnConstant(false));
                findAndHookMethod("com.android.fileexplorer.util.XLUtil", loadPackageParam.classLoader, "isNetworkAvailable", Context.class, XC_MethodReplacement.returnConstant(false));
                findAndHookMethod("com.xunlei.adlibrary.XunleiADSdk", loadPackageParam.classLoader, "setup", Context.class, new XC_MethodReplacement() {
                    @Override
                    protected Object replaceHookedMethod(MethodHookParam methodHookParam) throws Throwable {
                        return null;
                    }
                });
                findAndHookMethod("com.xunlei.adlibrary.analytics.xunlei.AdStatHelper", loadPackageParam.classLoader, "init", Context.class, new XC_MethodReplacement() {
                    @Override
                    protected Object replaceHookedMethod(MethodHookParam methodHookParam) throws Throwable {
                        return null;
                    }
                });
                findAndHookMethod("com.android.fileexplorer.video.upload.VideoItemManager", loadPackageParam.classLoader, "initLoad", new XC_MethodReplacement() {
                    @Override
                    protected Object replaceHookedMethod(MethodHookParam methodHookParam) throws Throwable {
                        return null;
                    }
                });
            }
        }

        //音乐
        if (loadPackageParam.packageName.equals("com.miui.player")) {
            if (prefs.getBoolean("enableMusic", false)) {
                findAndHookMethod("com.miui.player.util.AdUtils", loadPackageParam.classLoader, "isAdEnable", XC_MethodReplacement.returnConstant(false));
                findAndHookMethod("com.miui.player.util.AdUtils", loadPackageParam.classLoader, "getPlayAd", XC_MethodReplacement.returnConstant(null));
                findAndHookMethod("com.miui.player.util.ExperimentsHelper", loadPackageParam.classLoader, "isAdEnable", XC_MethodReplacement.returnConstant(false));
            }
        }

        //下载管理
        if (loadPackageParam.packageName.equals("com.android.providers.downloads.ui")) {
            if (prefs.getBoolean("enableDownload", false)) {
                findAndHookMethod("com.android.providers.downloads.ui.recommend.config.ADConfig", loadPackageParam.classLoader, "OSSupportAD", new XC_MethodHook() {
                    protected void beforeHookedMethod(MethodHookParam paramAnonymousMethodHookParam)
                            throws Throwable {
                        paramAnonymousMethodHookParam.setResult(false);
                    }
                });
                XposedBridge.log("开始修改下载管理");
                findAndHookMethod("com.android.providers.downloads.ui.utils.BuildUtils", loadPackageParam.classLoader, "isCmTestBuilder", XC_MethodReplacement.returnConstant(true));
            }
        }


        //天气
        if (loadPackageParam.packageName.equals("com.miui.weather2")) {

            if (prefs.getBoolean("enableWeather", false)) {
                findAndHookMethod("com.miui.weather2.tools.ToolUtils", loadPackageParam.classLoader, "checkCommericalStatue", Context.class, new XC_MethodHook() {
                    protected void beforeHookedMethod(MethodHookParam paramAnonymousMethodHookParam)
                            throws Throwable {
                        paramAnonymousMethodHookParam.setResult(false);
                    }
                });
                findAndHookMethod("com.miui.weather2.tools.ToolUtils", loadPackageParam.classLoader, "canRequestCommercialInfo", Context.class, XC_MethodReplacement.returnConstant(false));
                findAndHookMethod("com.miui.weather2.tools.ToolUtils", loadPackageParam.classLoader, "checkCommericalStatue", Context.class, XC_MethodReplacement.returnConstant(false));
                return;
            }
        }

        //搜索框
        if (loadPackageParam.packageName.equals("com.android.quicksearchbox")) {

            if (prefs.getBoolean("enableHotKey", false)) {
                //新版本 v8
               findAndHookMethod("com.android.quicksearchbox.ui.LocalListView", loadPackageParam.classLoader, "updateHotQuery", List.class, int.class, new XC_MethodReplacement() {
                    @Override
                    protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
                        return null;
                    }
                });
                //旧版本 v7
                findAndHookMethod("com.android.quicksearchbox.util.HotWordsUtil", loadPackageParam.classLoader, "setHotQueryView", "com.android.quicksearchbox.ui.HotQueryView", new XC_MethodHook() {
                    protected void beforeHookedMethod(MethodHookParam paramAnonymousMethodHookParam)
                            throws Throwable {
                        paramAnonymousMethodHookParam.setResult(null);
                    }
                });
            }
        }

        // 短信
        if (loadPackageParam.packageName.equals("com.android.mms")) {
            if (prefs.getBoolean("enableMMS", false)) {
                XposedBridge.log("开始短信mod");
                findAndHookMethod("com.android.mms.ui.MessageUtils", loadPackageParam.classLoader, "isMessagingTemplateAllowed", Context.class, new XC_MethodHook() {
                    protected void beforeHookedMethod(MethodHookParam paramAnonymousMethodHookParam)
                            throws Throwable {
                        Context mc = (Context) paramAnonymousMethodHookParam.args[0];
                        XposedBridge.log("短信 当前类：" + mc.getClass().getName().toLowerCase());
                        if (mc.getClass().getName().toLowerCase().contains("app")) {
                            paramAnonymousMethodHookParam.setResult(false);
                            XposedBridge.log("返回false");
                        } else {
                            paramAnonymousMethodHookParam.setResult(true);
                            XposedBridge.log("返回true");
                        }
                    }
                });
                //显示短信功能按钮
                findAndHookMethod("com.android.mms.ui.SingleRecipientConversationActivity", loadPackageParam.classLoader, "showMenuMode", new XC_MethodReplacement() {
                    @Override
                    protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
                        return null;
                    }
                });

                //显示短信推荐信息
                findAndHookMethod("com.android.mms.util.MiStatSdkHelper", loadPackageParam.classLoader, "recordBottomMenuShown", String.class, new XC_MethodReplacement() {
                    @Override
                    protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
                        return null;
                    }
                });

            }
        }
    }

    private static void findAndHookMethod(String p1, ClassLoader lpparam, String p2, Object... parameterTypesAndCallback) {
        try {
            XposedHelpers.findAndHookMethod(p1, lpparam, p2, parameterTypesAndCallback);

        } catch (Throwable localString3) {
            XposedBridge.log(localString3.toString());
        }
    }

}
