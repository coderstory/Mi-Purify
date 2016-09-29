package com.coderstory.Purify.module;

import android.content.Context;
import android.content.Intent;

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


public class RemoveAds implements IModule {

    private static XC_LoadPackage.LoadPackageParam loadPackageParam;

    @Override
    public void handleInitPackageResources(XC_InitPackageResources.InitPackageResourcesParam resparam) {
        XSharedPreferences prefs = new XSharedPreferences("com.coderstory.Purify", "UserSettings");
        prefs.makeWorldReadable();
        prefs.reload();
        if (resparam.packageName.equals("com.miui.cleanmaster")) {
            if (prefs.getBoolean("enableSafeCenter", false)) {
                if (resparam.packageName.equals("com.miui.cleanmaster")) {
                    resparam.res.setReplacement(resparam.packageName, "string", "no_network", "");
                }
            }
        }

    }

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) {
        loadPackageParam = lpparam;
        try {
            patchcode();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initZygote(IXposedHookZygoteInit.StartupParam startupParam) {
    }

    private void patchcode() throws ClassNotFoundException {

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
        if (loadPackageParam.packageName.equals("com.miui.video")) {
            if (prefs.getBoolean("enablemiuividio", false)) {
                findAndHookMethod("com.video.ui.view.AdView", loadPackageParam.classLoader, "getAdsBlock", Context.class, XC_MethodReplacement.returnConstant(null));
                Class<?> clsCallback = Class.forName("com.video.ui.idata.SyncServiceHelper$Callback");
                if (clsCallback != null) {
                    findAndHookMethod("com.video.ui.idata.SyncServiceHelper", loadPackageParam.classLoader, "fetchAds", Context.class, clsCallback, XC_MethodReplacement.returnConstant(null));
                }
                findAndHookMethod("com.video.ui.idata.iDataORM", loadPackageParam.classLoader, "getBooleanValue", Context.class, String.class, boolean.class, new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        String key = (String) param.args[1];
                        if (key.equals("debug_mode") || key.equals("show_first_ads") || key.equals("ads_show_homekey") || key.equals("startup_ads_loop") || key.equals("app_upgrade_splash")) {
                            param.setResult(false);
                        }
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        String key = (String) param.args[1];
                        if (key.equals("debug_mode") || key.equals("show_first_ads") || key.equals("ads_show_homekey") || key.equals("startup_ads_loop") || key.equals("app_upgrade_splash")) {
                            param.setResult(false);
                        }
                    }
                });
                findAndHookMethod("com.video.ui.idata.iDataORM", loadPackageParam.classLoader, "getStringValue", Context.class, String.class, String.class, new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        String key = (String) param.args[1];
                        if (key.equals("startup_ads")) {
                            param.setResult(null);
                        }
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        String key = (String) param.args[1];
                        if (key.equals("startup_ads")) {
                            param.setResult(null);
                        }
                    }
                });
            }
        }


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
                findAndHookMethod("com.miui.player.util.AdUtils", loadPackageParam.classLoader, "getPlayAd", "esponse.Listener<Result>", "Response.ErrorListener", XC_MethodReplacement.returnConstant(null));
                findAndHookMethod("com.miui.player.util.ExperimentsHelper", loadPackageParam.classLoader, "isAdEnabled", XC_MethodReplacement.returnConstant(false));
                //findAndHookMethod("com.miui.player.util.Configuration", loadPackageParam.classLoader, "isCmCustomization", XC_MethodReplacement.returnConstant(true));
                findAndHookMethod("com.miui.player.util.Configuration", loadPackageParam.classLoader, "isCmTest", XC_MethodReplacement.returnConstant(true));
                findAndHookMethod("com.miui.player.util.Configuration", loadPackageParam.classLoader, "isCpLogoVisiable", XC_MethodReplacement.returnConstant(false));

                findAndHookMethod("com.miui.optimizecenter.result.DataModel", loadPackageParam.classLoader, "post", Map.class, XC_MethodReplacement.returnConstant(""));
                findAndHookMethod("com.miui.optimizecenter.config.MiStat", loadPackageParam.classLoader, "getChannel", XC_MethodReplacement.returnConstant("international"));
                Class<?> clsAdImageView = Class.forName("com.miui.optimizecenter.widget.AdImageView");
                Class<?> clsAdvertisement = Class.forName("com.miui.optimizecenter.result.Advertisement");
                if (clsAdImageView != null && clsAdvertisement != null) {
                    findAndHookMethod("com.miui.optimizecenter.result.CleanResultActivity", loadPackageParam.classLoader, "startAdCountdown", clsAdImageView, clsAdvertisement, XC_MethodReplacement.returnConstant(null));
                    findAndHookMethod("com.miui.optimizecenter.result.CleanResultActivity", loadPackageParam.classLoader, "addAdvertisementEvent", String.class, clsAdvertisement, XC_MethodReplacement.returnConstant(null));
                }
                Class<?> clsAdInfo = Class.forName("com.xiaomi.music.online.model.AdInfo");
                if (clsAdInfo != null) {
                    findAndHookMethod("com.miui.player.util.AdForbidManager", loadPackageParam.classLoader, "recordAdInfo", clsAdInfo, int.class, XC_MethodReplacement.returnConstant(null));
                    findAndHookMethod("com.miui.player.util.AdForbidManager", loadPackageParam.classLoader, "addForbidInfo", String.class, XC_MethodReplacement.returnConstant(null));
                    findAndHookMethod("com.miui.player.util.AdForbidManager", loadPackageParam.classLoader, "isForbidden", String.class, XC_MethodReplacement.returnConstant(true));
                }
                final Class<?> clsDisplayItem = Class.forName("com.miui.player.display.model.DisplayItem");
                findAndHookMethod("com.miui.player.display.view.LocalDynamicList", loadPackageParam.classLoader, "createHeader", new XC_MethodReplacement() {
                    @Override
                    protected Object replaceHookedMethod(MethodHookParam methodHookParam) throws Throwable {
                        return clsDisplayItem.newInstance();
                    }
                });

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

        //个性主题
        if (loadPackageParam.packageName.equals("com.android.thememanager")) {
            if (prefs.getBoolean("enabletheme", false)) {
                findAndHookMethod("com.android.thememanager.controller.online.OnlineJSONDataParser", loadPackageParam.classLoader, "parseAdInfo", String.class, XC_MethodReplacement.returnConstant(null));
               // findAndHookMethod("com.android.thememanager.util.UIHelper", loadPackageParam.classLoader, "supportOnlineContent", String.class, XC_MethodReplacement.returnConstant(false));

//                findAndHookMethod("com.android.thememanager.view.AdBannerView", loadPackageParam.classLoader, "AdBannerView", Context.class, new XC_MethodReplacement() {
//                    @Override
//                    protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
//                        return null;
//                    }
//                });

                findAndHookMethod("com.android.thememanager.view.AdBannerView", loadPackageParam.classLoader, "init", new XC_MethodReplacement() {
                    @Override
                    protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
                        return null;
                    }
                });

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
        //日历
        if (loadPackageParam.packageName.equals("com.android.calendar")) {
            if (prefs.getBoolean("enablecalendar", false)) {
                findAndHookMethod("com.miui.calendar.ad.AdUtils", loadPackageParam.classLoader, "canShowBrandAd", Context.class, XC_MethodReplacement.returnConstant(false));
                findAndHookMethod("com.miui.calendar.ad.AdService", loadPackageParam.classLoader, "onHandleIntent", Intent.class, XC_MethodReplacement.returnConstant(null));
                findAndHookMethod("com.miui.calendar.card.single.local.SummarySingleCard", loadPackageParam.classLoader, "needShowAdBanner", XC_MethodReplacement.returnConstant(false));
                findAndHookMethod("com.miui.calendar.card.single.custom.ad.AdSingleCard", loadPackageParam.classLoader, "needDisplay", XC_MethodReplacement.returnConstant(false));
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

    private static void findAndHookConstructor(String p1, ClassLoader lpparam, String p2, Object... parameterTypesAndCallback) {
        try {
            XposedHelpers.findAndHookConstructor(p1, lpparam, p2, parameterTypesAndCallback);

        } catch (Throwable localString3) {
            XposedBridge.log(localString3.toString());
        }
    }

}
