package com.coderstory.Purify.module;

import android.content.pm.Signature;
import android.util.Base64;

import com.coderstory.Purify.plugins.IModule;
import com.coderstory.Purify.utils.XposedHelper;

import java.lang.reflect.Field;

import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;


public class CorePatch extends XposedHelper implements IModule {


    public void initZygote(IXposedHookZygoteInit.StartupParam paramStartupParam) {

        findAndHookMethod("java.security.MessageDigest", null, "isEqual", byte[].class, byte[].class, new XC_MethodHook() {
            protected void beforeHookedMethod(MethodHookParam methodHookParam)
                    throws Throwable {
                prefs.reload();
                if (prefs.getBoolean("authcreak", true)) {
                    methodHookParam.setResult(true);
                }
            }
        });

        hookAllMethods("com.android.org.conscrypt.OpenSSLSignature", null, "engineVerify", new XC_MethodHook() {
            protected void beforeHookedMethod(MethodHookParam paramAnonymousMethodHookParam)
                    throws Throwable {
                prefs.reload();
                if (prefs.getBoolean("authcreak", true)) {
                    paramAnonymousMethodHookParam.setResult(true);
                }
            }
        });

        final Class packageClass = XposedHelpers.findClass("android.content.pm.PackageParser.Package", null);


        hookAllMethods("android.content.pm.PackageParser", null, "getApkSigningVersion", XC_MethodReplacement.returnConstant(1));

        hookAllConstructors("android.util.jar.StrictJarVerifier", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {

                prefs.reload();
                if (prefs.getBoolean("authcreak", true)) {
                    param.args[3] = false;
                }
            }
        });

        hookAllConstructors("android.util.apk.ApkSignatureSchemeV2Verifier", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                Object packageInfoLite = param.thisObject;
                prefs.reload();
                if (prefs.getBoolean("authcreak", true)) {
                    Field field = packageClass.getField(" SF_ATTRIBUTE_ANDROID_APK_SIGNED_ID");
                    field.setAccessible(true);
                    field.set(packageInfoLite, 9999);
                }
            }
        });
        hookAllConstructors("android.util.apk.ApkSignatureSchemeV3Verifier", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                Object packageInfoLite = param.thisObject;
                prefs.reload();
                if (prefs.getBoolean("authcreak", true)) {
                    Field field = packageClass.getField(" SF_ATTRIBUTE_ANDROID_APK_SIGNED_ID");
                    field.setAccessible(true);
                    field.set(packageInfoLite, 9999);
                }
            }
        });
    }

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam paramLoadPackageParam) {

        if (("android".equals(paramLoadPackageParam.packageName)) && (paramLoadPackageParam.processName.equals("android"))) {

            final Class packageClass = XposedHelpers.findClass("android.content.pm.PackageParser.Package", paramLoadPackageParam.classLoader);

            hookAllMethods("com.android.server.pm.PackageManagerService", paramLoadPackageParam.classLoader, "checkDowngrade", new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam methodHookParam) throws Throwable {
                    super.beforeHookedMethod(methodHookParam);
                    Object packageInfoLite = methodHookParam.args[0];
                    prefs.reload();
                    if (prefs.getBoolean("downgrade", true)) {
                        Field field = packageClass.getField("mVersionCodeMajor");
                        field.setAccessible(true);
                        field.set(packageInfoLite, -1);
                    }
                }
            });

            hookAllMethods("com.android.server.pm.PackageManagerService", paramLoadPackageParam.classLoader, "verifySignaturesLP", new XC_MethodHook() {

                protected void beforeHookedMethod(MethodHookParam methodHookParam) throws Throwable {
                    prefs.reload();
                    if (prefs.getBoolean("authcreak", true)) {
                        methodHookParam.setResult(true);
                    }
                }
            });

            hookAllMethods("com.android.server.pm. PackageManagerServiceUtils", paramLoadPackageParam.classLoader, "compareSignatures", new XC_MethodHook() {
                protected void beforeHookedMethod(MethodHookParam methodHookParam) throws Throwable {
                    prefs.reload();
                    if (prefs.getBoolean("zipauthcreak", false)) {

                        String platform = prefs.getString("platform", "DEFAULT");

                        Signature[] signatures = (Signature[]) methodHookParam.args[0];
                        if (signatures != null && signatures.length > 0) {
                            for (Signature signature : signatures) {
                                if (new String(Base64.encode(signature.toByteArray(), Base64.DEFAULT)).replaceAll("\n", "").equals(platform)) {
                                    return;
                                }
                            }
                        }

                        signatures = (Signature[]) methodHookParam.args[1];
                        if (signatures != null && signatures.length > 0) {
                            for (Signature signature : signatures) {
                                if (new String(Base64.encode(signature.toByteArray(), Base64.DEFAULT)).replaceAll("\n", "").equals(platform)) {
                                    return;
                                }
                            }
                        }
                        methodHookParam.setResult(0);
                    }
                }
            });

            hookAllMethods("com.android.server.pm.PackageManagerService", paramLoadPackageParam.classLoader, "compareSignaturesCompat", new XC_MethodHook() {
                protected void beforeHookedMethod(XC_MethodHook.MethodHookParam paramAnonymousMethodHookParam) {
                    prefs.reload();
                    if (prefs.getBoolean("authcreak", true)) {
                        paramAnonymousMethodHookParam.setResult(0);
                    }
                }
            });
            hookAllMethods("com.android.server.pm.PackageManagerService", paramLoadPackageParam.classLoader, "compareSignaturesRecover", new XC_MethodHook() {
                protected void beforeHookedMethod(XC_MethodHook.MethodHookParam paramAnonymousMethodHookParam) {
                    prefs.reload();
                    if (prefs.getBoolean("authcreak", true)) {
                        paramAnonymousMethodHookParam.setResult(0);
                    }
                }
            });
        }
    }
}
