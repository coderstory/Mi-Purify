package com.coderstory.Purify.fragment;

import android.view.View;
import android.widget.Switch;
import android.widget.Toast;

import com.coderstory.Purify.R;

import ren.solid.library.fragment.base.BaseFragment;


import static com.coderstory.Purify.utils.roothelper.ShellUtils.execCommand;


public class BlockADSFragment extends BaseFragment {

    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_block_ad;
    }

    @Override
    protected void setUpView() {

        execCommand("pm disable com.miui.systemAdSolution", true);
        execCommand("pm disable com.miui.analytics", true);
        execCommand("pm disable com.qualcomm.qti.seemp", true);
        execCommand("pm disable com.xiaomi.ab", true);
        execCommand("pm disable com.miLink", true);

        $(R.id.enableBlockAD).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*getEditor().putBoolean(enableBlockAD, ((Switch) v).isChecked());
                getEditor().apply();
                setCheck(((Switch) v).isChecked());

                ((Switch) $(R.id.enableBlockADBasic)).setChecked(((Switch) v).isChecked());
                getEditor().putBoolean(enableBlockADBasic, ((Switch) v).isChecked());
                getEditor().apply();

                if (((Switch) v).isChecked()) {
                    new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            execCommand("pm disable " + systemAdSolution_packageName, true);
                            execCommand("pm disable " + analytics_packageName, true);
                            execCommand("pm disable " + seemp_packageName, true);
                            execCommand("pm disable " + miLink_packageName, true);
                        }
                    }.start();
                } else {
                    new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            execCommand("pm enable " + systemAdSolution_packageName, true);
                            execCommand("pm enable " + analytics_packageName, true);
                            execCommand("pm enable " + seemp_packageName, true);
                            execCommand("pm enable " + miLink_packageName, true);
                        }
                    }.start();
                }*/
                ((Switch) v).setChecked(true);
                Toast.makeText(getMContext(), "测试版本,暂时无法关闭", Toast.LENGTH_LONG).show();
            }
        });

        $(R.id.enableBlockADBasic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //   getEditor().putBoolean(enableBlockADBasic, ((Switch) v).isChecked());
                //   getEditor().apply();
                ((Switch) v).setChecked(true);
                Toast.makeText(getMContext(), "测试版本,暂时无法关闭", Toast.LENGTH_LONG).show();
            }
        });

        $(R.id.enableMMS).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //   getEditor().putBoolean(enableMMS, ((Switch) v).isChecked());
                //   getEditor().apply();
                ((Switch) v).setChecked(true);
                Toast.makeText(getMContext(), "测试版本,暂时无法关闭", Toast.LENGTH_LONG).show();
            }
        });
        $(R.id.enableWeather).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //   getEditor().putBoolean(enableWeather, ((Switch) v).isChecked());
                //   getEditor().apply();
                ((Switch) v).setChecked(true);
                Toast.makeText(getMContext(), "测试版本,暂时无法关闭", Toast.LENGTH_LONG).show();
            }
        });
        $(R.id.enableFileManager).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  getEditor().putBoolean(enableFileManager, ((Switch) v).isChecked());
                //   getEditor().apply();
                ((Switch) v).setChecked(true);
                Toast.makeText(getMContext(), "测试版本,暂时无法关闭", Toast.LENGTH_LONG).show();
            }
        });

        $(R.id.enableDownload).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  getEditor().putBoolean(enableDownload, ((Switch) v).isChecked());
                // getEditor().apply();
                ((Switch) v).setChecked(true);
                Toast.makeText(getMContext(), "测试版本,暂时无法关闭", Toast.LENGTH_LONG).show();
            }
        });

        $(R.id.enableSafeCenter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // getEditor().putBoolean(enableSafeCenter, ((Switch) v).isChecked());
                // getEditor().apply();
                ((Switch) v).setChecked(true);
                Toast.makeText(getMContext(), "测试版本,暂时无法关闭", Toast.LENGTH_LONG).show();
            }
        });

        $(R.id.enableMusic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // getEditor().putBoolean(enableMusic, ((Switch) v).isChecked());
                // getEditor().apply();+
                ((Switch) v).setChecked(true);
                Toast.makeText(getMContext(), "测试版本,暂时无法关闭", Toast.LENGTH_LONG).show();
            }
        });

        $(R.id.enabletheme).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Switch) v).setChecked(true);
                Toast.makeText(getMContext(), "测试版本,暂时无法关闭", Toast.LENGTH_LONG).show();
                //getEditor().putBoolean(enableTheme, ((Switch) v).isChecked());
                //  getEditor().apply();
            }
        });

    }

    @Override
    protected void setUpData() {
        ((Switch) $(R.id.enabletheme)).setChecked(getPrefs().getBoolean("enableTheme", true));
        ((Switch) $(R.id.enableBlockAD)).setChecked(getPrefs().getBoolean("EnableBlockAD", true));
        ((Switch) $(R.id.enableBlockADBasic)).setChecked(getPrefs().getBoolean("EnableBlockADBasic", true));
        ((Switch) $(R.id.enableMMS)).setChecked(getPrefs().getBoolean("EnableMMS", true));
        ((Switch) $(R.id.enableWeather)).setChecked(getPrefs().getBoolean("EnableWeather", true));
        ((Switch) $(R.id.enableFileManager)).setChecked(getPrefs().getBoolean("enableFileManager", true));
        ((Switch) $(R.id.enableDownload)).setChecked(getPrefs().getBoolean("enableDownload", true));
        ((Switch) $(R.id.enableSafeCenter)).setChecked(getPrefs().getBoolean("enableSafeCenter", true));
        ((Switch) $(R.id.enableMusic)).setChecked(getPrefs().getBoolean("enableMusic", true));
        setCheck(getPrefs().getBoolean("EnableBlockAD", true));

       /* $(R.id.enableBlockAD).setClickable(false);
        $(R.id.enableBlockADBasic).setClickable(false);
        $(R.id.enableMMS).setClickable(false);
        $(R.id.enableWeather).setClickable(false);
        $(R.id.enableFileManager).setClickable(false);
        $(R.id.enableDownload).setClickable(false);
        $(R.id.enableSafeCenter).setClickable(false);
        $(R.id.enableMusic).setClickable(false);
        $(R.id.enabletheme).setClickable(false);*/

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
