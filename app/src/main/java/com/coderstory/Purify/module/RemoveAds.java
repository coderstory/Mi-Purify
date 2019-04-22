package com.coderstory.purify.module;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.coderstory.purify.plugins.IModule;
import com.coderstory.purify.utils.XposedHelper;

import org.json.JSONObject;

import java.util.Arrays;

import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static de.robv.android.xposed.XposedHelpers.findClass;


public class RemoveAds extends XposedHelper implements IModule {

    @Override
    public void initZygote(IXposedHookZygoteInit.StartupParam startupParam) {
    }

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) {


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


        //下载管理
        if (loadPackageParam.packageName.equals("com.android.providers.downloads.ui")) {
            if (prefs.getBoolean("EnableDownload", false)) {
                findAndHookMethod("com.android.providers.downloads.ui.utils.CloudConfigHelper", loadPackageParam.classLoader, "getAdButtonType", XC_MethodReplacement.returnConstant(0));
                findAndHookMethod("com.android.providers.downloads.ui.utils.CloudConfigHelper", loadPackageParam.classLoader, "isShouldShowAppSubject", XC_MethodReplacement.returnConstant(false));
                findAndHookMethod("com.android.providers.downloads.ui.utils.CloudConfigHelper", loadPackageParam.classLoader, "isShouldShowExtraAd", XC_MethodReplacement.returnConstant(false));
                findAndHookMethod("com.android.providers.downloads.ui.utils.CloudConfigHelper", loadPackageParam.classLoader, "isShouldShowRecommendInfo", XC_MethodReplacement.returnConstant(false));
                findAndHookMethod("com.android.providers.downloads.ui.utils.CloudConfigHelper", loadPackageParam.classLoader, "isStableShowActivateNotify", XC_MethodReplacement.returnConstant(false));
                findAndHookMethod("com.android.providers.downloads.ui.utils.CloudConfigHelper", loadPackageParam.classLoader, "supportRank", XC_MethodReplacement.returnConstant(false));
            }
        }

        // 短信
        if (loadPackageParam.packageName.equals("com.android.mms")) {
            if (prefs.getBoolean("EnableMMS", false)) {
                findAndHookMethod("com.android.mms.util.SmartMessageUtils", loadPackageParam.classLoader, "isMessagingTemplateAllowed", Context.class, XC_MethodReplacement.returnConstant(true));
                findAndHookMethod("com.android.mms.ui.SingleRecipientConversationActivity", loadPackageParam.classLoader, "showMenuMode", boolean.class, XC_MethodReplacement.returnConstant(null));
            }
        }

        if (loadPackageParam.packageName.equals("com.miui.player")) {
            XposedHelper.findAndHookMethod("com.miui.player.display.model.MediaData", loadPackageParam.classLoader, "toAdvertisment", XC_MethodReplacement.returnConstant(null));
            XposedHelper.hookAllMethods("com.miui.player.display.view.cell.TopNewsAdListItemCell", loadPackageParam.classLoader, "onBindItem", new XC_MethodHook() {
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    ((LinearLayout) param.thisObject).setVisibility(View.GONE);
                }
            });
            XposedHelper.hookAllMethods("com.miui.player.display.view.cell.AdRecommendImageItemCell", loadPackageParam.classLoader, "setAdInfo", new XC_MethodHook() {
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    ((LinearLayout) param.thisObject).setVisibility(View.GONE);
                }
            });
        }

        if (loadPackageParam.packageName.equals("com.android.thememanager")) {
            if (this.prefs.getBoolean("EnableTheme", false)) {
                XposedHelper.findAndHookMethod("com.android.thememanager.b.b.h", loadPackageParam.classLoader, "a", JSONObject.class, new XC_MethodReplacement() {
                    protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
                        if (RemoveAds.this.isAd(((JSONObject)param.args[0]).getString("type"))) {
                            return null;
                        }
                        return XposedBridge.invokeOriginalMethod(param.method, param.thisObject, param.args);
                    }
                });
                return;
            }
            XposedHelper.findAndHookMethod("com.market.sdk.k", loadPackageParam.classLoader, "isSupported", XC_MethodReplacement.returnConstant(false));
            Class<?> clsHybridView = findClass("miui.hybrid.HybridView", loadPackageParam.classLoader);
            if ( clsHybridView != null) {
                XposedHelper.findAndHookMethod("com.android.thememanager.h5.ThemeHybridFragment$b", loadPackageParam.classLoader, "shouldInterceptRequest", clsHybridView, String.class, new XC_MethodHook() {
                    public void beforeHookedMethod(MethodHookParam param) {
                        if (((String)param.args[1]).contains("AdCenter")) {
                            param.args[1] = "http://127.0.0.1/";
                        }
                    }
                });
            }
        }

    }

    private boolean isAd(String s) {
        return Arrays.asList(new String[]{"SHOPWINDOW", "SHOPWINDOWNEW", "PURCHASE", "ADBANNER"}).contains(s.toUpperCase());
    }


}


