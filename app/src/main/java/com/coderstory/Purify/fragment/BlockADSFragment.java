package com.coderstory.Purify.fragment;

import android.view.View;
import android.widget.Switch;

import com.coderstory.Purify.R;
import com.coderstory.Purify.fragment.base.BaseFragment;

import eu.chainfire.libsuperuser.Shell;


public class BlockADSFragment extends BaseFragment {

    @Override
    protected void init() {
        super.init();
    }

    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_block_ad;
    }

    @Override
    protected void setUpView() {
        $(R.id.enableBlockAD).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEditor().putBoolean("EnableBlockAD", ((Switch) v).isChecked());
                getEditor().apply();
                setCheck(((Switch) v).isChecked());

                ((Switch) $(R.id.enableBlockADBasic)).setChecked(((Switch) v).isChecked());
                getEditor().putBoolean("EnableBlockADBasic", ((Switch) v).isChecked());
                getEditor().apply();

                if (((Switch) v).isChecked()) {
                    new Thread(() -> {
                        String list[] = new String[]{"pm disable com.miui.systemAdSolution",
                                "pm disable com.miui.analytics",
                                "pm disable com.qualcomm.qti.seemp",
                                "pm disable com.xiaomi.ab",
                                "pm disable com.miLink"};
                        Shell.SU.run(list);
                    }).start();
                } else {
                    new Thread(() -> {
                        String list[] = new String[]{"pm enable com.miui.systemAdSolution",
                                "pm enable com.miui.analytics",
                                "pm enable com.qualcomm.qti.seemp",
                                "pm enable com.xiaomi.ab",
                                "pm enable com.miLink"};
                        Shell.SU.run(list);
                    }).start();
                }
                sudoFixPermissions();
            }
        });

        $(R.id.enableBlockADBasic).setOnClickListener(v -> {
            getEditor().putBoolean("EnableBlockADBasic", ((Switch) v).isChecked());
            getEditor().apply();
            sudoFixPermissions();

        });

        $(R.id.enableMMS).setOnClickListener(v -> {
            getEditor().putBoolean("EnableMMS", ((Switch) v).isChecked());
            getEditor().apply();
            sudoFixPermissions();

        });
        $(R.id.enableWeather).setOnClickListener(v -> {
            getEditor().putBoolean("EnableWeather", ((Switch) v).isChecked());
            getEditor().apply();
            sudoFixPermissions();

        });
        $(R.id.enableFileManager).setOnClickListener(v -> {
            getEditor().putBoolean("EnableFileManager", ((Switch) v).isChecked());
            getEditor().apply();
            sudoFixPermissions();

        });

        $(R.id.enableDownload).setOnClickListener(v -> {
            getEditor().putBoolean("EnableDownload", ((Switch) v).isChecked());
            getEditor().apply();
            sudoFixPermissions();

        });

        $(R.id.enableSafeCenter).setOnClickListener(v -> {
            getEditor().putBoolean("EnableSafeCenter", ((Switch) v).isChecked());
            getEditor().apply();
            sudoFixPermissions();

        });

        $(R.id.enableMusic).setOnClickListener(v -> {
            getEditor().putBoolean("EnableMusic", ((Switch) v).isChecked());
            getEditor().apply();
            sudoFixPermissions();

        });

        $(R.id.enabletheme).setOnClickListener(v -> {

            getEditor().putBoolean("EnableTheme", ((Switch) v).isChecked());
            getEditor().apply();
            sudoFixPermissions();
        });

    }

    @Override
    protected void setUpData() {
        ((Switch) $(R.id.enableBlockAD)).setChecked(getPrefs().getBoolean("EnableBlockAD", false));
        ((Switch) $(R.id.enableBlockADBasic)).setChecked(getPrefs().getBoolean("EnableBlockADBasic", false));
        ((Switch) $(R.id.enableMMS)).setChecked(getPrefs().getBoolean("EnableMMS", false));
        ((Switch) $(R.id.enableWeather)).setChecked(getPrefs().getBoolean("EnableWeather", false));
        ((Switch) $(R.id.enableFileManager)).setChecked(getPrefs().getBoolean("EnableFileManager", false));
        ((Switch) $(R.id.enableDownload)).setChecked(getPrefs().getBoolean("EnableDownload", false));
        ((Switch) $(R.id.enableSafeCenter)).setChecked(getPrefs().getBoolean("EnableSafeCenter", false));
        ((Switch) $(R.id.enableMusic)).setChecked(getPrefs().getBoolean("EnableMusic", false));
        ((Switch) $(R.id.enabletheme)).setChecked(getPrefs().getBoolean("EnableTheme", false));
        setCheck(getPrefs().getBoolean("EnableBlockAD", true));

    }

    private void setCheck(boolean type) {
        if (type) {
            $(R.id.enableBlockADBasic).setEnabled(true);
            $(R.id.enableMMS).setEnabled(true);
            $(R.id.enableWeather).setEnabled(true);
            $(R.id.enableFileManager).setEnabled(true);
            $(R.id.enableDownload).setEnabled(true);
            $(R.id.enableSafeCenter).setEnabled(true);
            $(R.id.enableMusic).setEnabled(true);

            $(R.id.enabletheme).setEnabled(true);
        } else {
            $(R.id.enableBlockADBasic).setEnabled(false);
            $(R.id.enableMMS).setEnabled(false);
            $(R.id.enableWeather).setEnabled(false);
            $(R.id.enableFileManager).setEnabled(false);
            $(R.id.enableDownload).setEnabled(false);
            $(R.id.enableSafeCenter).setEnabled(false);
            $(R.id.enableMusic).setEnabled(false);
            $(R.id.enabletheme).setEnabled(false);
        }

    }

}

