package com.coderstory.Purify.fragment;

import android.view.View;
import android.widget.Switch;

import com.coderstory.Purify.R;

import ren.solid.library.fragment.base.BaseFragment;

import static com.coderstory.Purify.config.FunctionModule.enableBlockAD;
import static com.coderstory.Purify.config.FunctionModule.enableBlockADBasic;
import static com.coderstory.Purify.config.FunctionModule.enableDownload;
import static com.coderstory.Purify.config.FunctionModule.enableFileManager;
import static com.coderstory.Purify.config.FunctionModule.enableMMS;
import static com.coderstory.Purify.config.FunctionModule.enableMusic;
import static com.coderstory.Purify.config.FunctionModule.enableSafeCenter;
import static com.coderstory.Purify.config.FunctionModule.enableWeather;
import static com.coderstory.Purify.config.FunctionModule.enableMIUIVedio;
import static com.coderstory.Purify.config.FunctionModule.enableTheme;
import static com.coderstory.Purify.config.packageNameEntries.analytics_packageName;
import static com.coderstory.Purify.config.packageNameEntries.miLink_packageName;
import static com.coderstory.Purify.config.packageNameEntries.seemp_packageName;
import static com.coderstory.Purify.config.packageNameEntries.systemAdSolution_packageName;
import static com.coderstory.Purify.utils.roothelper.ShellUtils.execCommand;


public class BlockADSFragment extends BaseFragment {

    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_block_ad;
    }

    @Override
    protected void setUpView() {
        $(R.id.enableBlockAD).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEditor().putBoolean(enableBlockAD, ((Switch) v).isChecked());
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
                }
            }
        });

        $(R.id.enableBlockADBasic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEditor().putBoolean(enableBlockADBasic, ((Switch) v).isChecked());
                getEditor().apply();
            }
        });

        $(R.id.enableMMS).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEditor().putBoolean(enableMMS, ((Switch) v).isChecked());
                getEditor().apply();
            }
        });
        $(R.id.enableWeather).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEditor().putBoolean(enableWeather, ((Switch) v).isChecked());
                getEditor().apply();
            }
        });
        $(R.id.enableFileManager).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEditor().putBoolean(enableFileManager, ((Switch) v).isChecked());
                getEditor().apply();
            }
        });

        $(R.id.enableDownload).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEditor().putBoolean(enableDownload, ((Switch) v).isChecked());
                getEditor().apply();
            }
        });

        $(R.id.enableSafeCenter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEditor().putBoolean(enableSafeCenter, ((Switch) v).isChecked());
                getEditor().apply();
            }
        });

        $(R.id.enableMusic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEditor().putBoolean(enableMusic, ((Switch) v).isChecked());
                getEditor().apply();
            }
        });

        $(R.id.enablevideo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEditor().putBoolean(enableMIUIVedio, ((Switch) v).isChecked());
                getEditor().apply();
            }
        });


        $(R.id.enabletheme).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEditor().putBoolean(enableTheme, ((Switch) v).isChecked());
                getEditor().apply();
            }
        });

    }

    @Override
    protected void setUpData() {
        ((Switch) $(R.id.enabletheme)).setChecked(getPrefs().getBoolean(enableTheme, false));
        ((Switch) $(R.id.enableBlockAD)).setChecked(getPrefs().getBoolean(enableBlockAD, false));
        ((Switch) $(R.id.enableBlockADBasic)).setChecked(getPrefs().getBoolean(enableBlockADBasic, false));
        ((Switch) $(R.id.enableMMS)).setChecked(getPrefs().getBoolean(enableMMS, false));
        ((Switch) $(R.id.enableWeather)).setChecked(getPrefs().getBoolean(enableWeather, false));
        ((Switch) $(R.id.enableFileManager)).setChecked(getPrefs().getBoolean(enableFileManager, false));
        ((Switch) $(R.id.enableDownload)).setChecked(getPrefs().getBoolean(enableDownload, false));
        ((Switch) $(R.id.enableSafeCenter)).setChecked(getPrefs().getBoolean(enableSafeCenter, false));
        ((Switch) $(R.id.enableMusic)).setChecked(getPrefs().getBoolean(enableMusic, false));
        ((Switch) $(R.id.enablevideo)).setChecked(getPrefs().getBoolean(enableMIUIVedio, false));
        setCheck(getPrefs().getBoolean(enableBlockAD, false));
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
            $(R.id.enablevideo).setEnabled(true);
            $(R.id.enabletheme).setEnabled(true);
        } else {
            $(R.id.enableBlockADBasic).setEnabled(false);
            $(R.id.enableMMS).setEnabled(false);
            $(R.id.enableWeather).setEnabled(false);
            $(R.id.enableFileManager).setEnabled(false);
            $(R.id.enableDownload).setEnabled(false);
            $(R.id.enableSafeCenter).setEnabled(false);
            $(R.id.enableMusic).setEnabled(false);
            $(R.id.enablevideo).setEnabled(false);
            $(R.id.enabletheme).setEnabled(false);
        }

    }

}
