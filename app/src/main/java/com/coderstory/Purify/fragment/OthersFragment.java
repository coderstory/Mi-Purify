package com.coderstory.purify.fragment;


import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Base64;
import android.widget.Switch;

import com.coderstory.purify.R;
import com.coderstory.purify.fragment.base.BaseFragment;
import com.coderstory.purify.utils.RuntimeUtil;



public class OthersFragment extends BaseFragment {
    public OthersFragment() {
    }

    @Override
    protected void setUpView() {

        $(R.id.enableBlockAD).setOnClickListener(v -> {
            getEditor().putBoolean("EnableBlockAD", ((Switch) v).isChecked());
            getEditor().apply();
            setCheck(((Switch) v).isChecked());

            if (((Switch) v).isChecked()) {
                new Thread(() -> {
                    String list[] = new String[]{"pm disable com.miui.systemAdSolution",
                            "pm disable com.miui.analytics",
                            "pm disable com.qualcomm.qti.seemp",
                            "pm disable com.xiaomi.ab",
                            "pm disable com.miLink"};
                    RuntimeUtil.execSilent(list);
                }).start();
            } else {
                new Thread(() -> {
                    String list[] = new String[]{"pm enable com.miui.systemAdSolution",
                            "pm enable com.miui.analytics",
                            "pm enable com.qualcomm.qti.seemp",
                            "pm enable com.xiaomi.ab",
                            "pm enable com.miLink"};
                    RuntimeUtil.execSilent(list);
                }).start();
            }
            sudoFixPermissions();
        });


        $(R.id.enableMMS).setOnClickListener(v -> {
            getEditor().putBoolean("EnableMMS", ((Switch) v).isChecked());
            getEditor().apply();
            sudoFixPermissions();

        });

        $(R.id.enableDownload).setOnClickListener(v -> {
            getEditor().putBoolean("EnableDownload", ((Switch) v).isChecked());
            getEditor().apply();
            sudoFixPermissions();

        });

        $(R.id.enabletheme).setOnClickListener(v -> {

            getEditor().putBoolean("EnableTheme", ((Switch) v).isChecked());
            getEditor().apply();
            sudoFixPermissions();
        });

        $(R.id.signatureCheck).setOnClickListener(v -> {
            getEditor().putBoolean("signatureCheck", ((Switch) v).isChecked());
            getEditor().apply();
            sudoFixPermissions();
        });

        $(R.id.comparisonSignature).setOnClickListener(v -> {
            getEditor().putBoolean("comparisonSignature", ((Switch) v).isChecked());
            getEditor().apply();
            sudoFixPermissions();
        });


        $(R.id.downgrade).setOnClickListener(v -> {
            getEditor().putBoolean("downgrade", ((Switch) v).isChecked());
            getEditor().apply();
            sudoFixPermissions();
        });

        $(R.id.hide_icon_label).setOnClickListener(v -> {
            getEditor().putBoolean("hide_icon_label", ((Switch) v).isChecked());
            getEditor().apply();
            sudoFixPermissions();
        });
        $(R.id.prevent_freeze_reverse).setOnClickListener(v -> {
            getEditor().putBoolean("prevent_freeze_reverse", ((Switch) v).isChecked());
            getEditor().apply();
            sudoFixPermissions();
        });

        if (getPrefs().getString("platform", "").equals("")) {
            // 读取平台签名并保存
            new Thread(() -> {
                try {
                    PackageInfo packageInfo = getMContext().getPackageManager().getPackageInfo("android", PackageManager.GET_SIGNATURES);
                    if (packageInfo.signatures[0] != null) {
                        String platform = new String(Base64.encode(packageInfo.signatures[0].toByteArray(), Base64.DEFAULT)).replaceAll("\n", "");
                        getEditor().putString("platform", platform);
                        getEditor().apply();
                        sudoFixPermissions();
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_others;
    }

    @Override
    protected void setUpData() {
        ((Switch) $(R.id.hide_icon_label)).setChecked(getPrefs().getBoolean("hide_icon_label", false));
        ((Switch) $(R.id.signatureCheck)).setChecked(getPrefs().getBoolean("signatureCheck", false));
        ((Switch) $(R.id.comparisonSignature)).setChecked(getPrefs().getBoolean("comparisonSignature", false));
        ((Switch) $(R.id.downgrade)).setChecked(getPrefs().getBoolean("downgrade", false));
        ((Switch) $(R.id.prevent_freeze_reverse)).setChecked(getPrefs().getBoolean("prevent_freeze_reverse", false));
        ((Switch) $(R.id.enableBlockAD)).setChecked(getPrefs().getBoolean("EnableBlockAD", false));
        ((Switch) $(R.id.enableMMS)).setChecked(getPrefs().getBoolean("EnableMMS", false));
        ((Switch) $(R.id.enableDownload)).setChecked(getPrefs().getBoolean("EnableDownload", false));
        ((Switch) $(R.id.enabletheme)).setChecked(getPrefs().getBoolean("EnableTheme", false));
        setCheck(getPrefs().getBoolean("EnableBlockAD", true));

    }

    private void setCheck(boolean type) {
        if (type) {
            $(R.id.enableMMS).setEnabled(true);
            $(R.id.enableDownload).setEnabled(true);
            $(R.id.enabletheme).setEnabled(true);
        } else {
            $(R.id.enableMMS).setEnabled(false);
            $(R.id.enableDownload).setEnabled(false);
            $(R.id.enabletheme).setEnabled(false);
        }

    }
}
