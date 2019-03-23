package com.coderstory.purify.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Looper;
import android.widget.Switch;

import com.coderstory.purify.R;
import com.coderstory.purify.fragment.base.BaseFragment;
import com.coderstory.purify.utils.RuntimeUtil;
import com.coderstory.purify.utils.hostshelper.FileHelper;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

import static com.coderstory.purify.config.Misc.HostFileTmpName;


public class HostsFragment extends BaseFragment {

    private Dialog dialog;

    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_hosts;
    }

    @Override
    protected void setUpView() {

        $(R.id.enableHosts).setOnClickListener(v -> {
            getEditor().putBoolean("enableHosts", ((Switch) v).isChecked());
            getEditor().apply();
            sudoFixPermissions();
            setCheck(((Switch) v).isChecked());
            new MyTask().execute();
        });

        $(R.id.enableMIUIHosts).setOnClickListener(v -> {
            getEditor().putBoolean("enableMIUIHosts", ((Switch) v).isChecked());
            getEditor().apply();
            sudoFixPermissions();
            new MyTask().execute();
        });

        $(R.id.enableBlockAdsHosts).setOnClickListener(v -> {
            getEditor().putBoolean("enableBlockAdsHosts", ((Switch) v).isChecked());
            getEditor().apply();
            sudoFixPermissions();
            new MyTask().execute();
        });
        $(R.id.enableGoogleHosts).setOnClickListener(v -> {
            getEditor().putBoolean("enableGoogleHosts", ((Switch) v).isChecked());
            getEditor().apply();
            sudoFixPermissions();
            new MyTask().execute();
        });

        $(R.id.enableStore).setOnClickListener(v -> {
            getEditor().putBoolean("enableStore", ((Switch) v).isChecked());
            getEditor().apply();
            sudoFixPermissions();
            new MyTask().execute();
        });
        $(R.id.enableupdater).setOnClickListener(v -> {
            getEditor().putBoolean("enableUpdater", ((Switch) v).isChecked());
            getEditor().apply();
            sudoFixPermissions();
            new MyTask().execute();
        });
    }

    @Override
    protected void setUpData() {
        ((Switch) $(R.id.enableHosts)).setChecked(getPrefs().getBoolean("enableHosts", false));
        ((Switch) $(R.id.enableMIUIHosts)).setChecked(getPrefs().getBoolean("enableMIUIHosts", false));
        ((Switch) $(R.id.enableBlockAdsHosts)).setChecked(getPrefs().getBoolean("enableBlockAdsHosts", false));
        ((Switch) $(R.id.enableGoogleHosts)).setChecked(getPrefs().getBoolean("enableGoogleHosts", false));
        ((Switch) $(R.id.enableStore)).setChecked(getPrefs().getBoolean("enableStore", false));
        ((Switch) $(R.id.enableupdater)).setChecked(getPrefs().getBoolean("enableUpdater", false));
        setCheck(getPrefs().getBoolean("enableHosts", false));
    }

    //因为hosts修改比较慢 所以改成异步的
    //更新hosts操作
    private void UpdateHosts() throws UnsupportedEncodingException {
        boolean enableHostsSet = getPrefs().getBoolean("enableHosts", false); //1
        boolean enableMIUIHostsSet = getPrefs().getBoolean("enableMIUIHosts", false); //4
        boolean enableBlockAdsHostsSet = getPrefs().getBoolean("enableBlockAdsHosts", false); //4
        boolean enableGoogleHostsSet = getPrefs().getBoolean("enableGoogleHosts", false); //4
        boolean enableStoreSet = getPrefs().getBoolean("enableStore", false); //4
        boolean enableupdaterSet = getPrefs().getBoolean("enableUpdater", false); //4

        if (enableHostsSet) {
            FileHelper fh = new FileHelper();
            String HostsContext = fh.getFromAssets("hosts_default", getMContext());

            if (getPrefs().getBoolean("enableHosts", false)) { //如果未启用hosts

                // 国内广告
                if (enableBlockAdsHostsSet) {
                    HostsContext += fh.getFromAssets("hosts_noad", getMContext());
                }
                //  国外一些网站 不含google
                if (enableGoogleHostsSet) {
                    HostsContext += fh.getFromAssets("hosts_foreign", getMContext());
                }
                // 屏蔽在线更新
                if (enableupdaterSet) {
                    HostsContext += "\n127.0.0.1 update.miui.com\n";
                }
                // 屏蔽应用商店 游戏 等
                if (enableStoreSet) {
                    HostsContext += fh.getFromAssets("hosts_nostore", getMContext());
                }
                // 屏蔽miui广告
                if (enableMIUIHostsSet) {
                    HostsContext += fh.getFromAssets("hosts_miui", getMContext());
                }
            }

            RuntimeUtil.execSilent(getCommandsToExecute(HostsContext));

        }
    }

    protected String[] getCommandsToExecute(String context) throws UnsupportedEncodingException {
        String[] list = new String[3];
        list[0] = "mount -o rw,remount /system";

        String path = getMContext().getFilesDir().getPath() + HostFileTmpName;

        FileOutputStream out = null;
        BufferedWriter writer;
        try {
            out = getMContext().openFileOutput("hosts", Context.MODE_PRIVATE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (out != null) {
            writer = new BufferedWriter(new OutputStreamWriter(out));
            try {
                writer.write(context);
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        list[1] = String.format("mv %s %s", path, "/etc/hosts");
        list[2] = String.format("chmod 755 %s", "/system/etc/hosts");
        return list;
    }

    private void showProgress() {
        if (dialog == null || !dialog.isShowing()) { //dialog未实例化 或者实例化了但没显示
            dialog = ProgressDialog.show(getActivity(), getString(R.string.Working), getString(R.string.Waiting));
            dialog.show();
        }
    }

    private void closeProgress() {
        if (getActivity() != null && !getActivity().isFinishing()) {
            dialog.cancel();
        }
    }

    private void setCheck(boolean type) {

        if (type) {
            $(R.id.enableMIUIHosts).setEnabled(true);
            $(R.id.enableBlockAdsHosts).setEnabled(true);
            $(R.id.enableGoogleHosts).setEnabled(true);
            $(R.id.enableStore).setEnabled(true);
            $(R.id.enableupdater).setEnabled(true);
        } else {
            $(R.id.enableMIUIHosts).setEnabled(false);
            $(R.id.enableBlockAdsHosts).setEnabled(false);
            $(R.id.enableGoogleHosts).setEnabled(false);
            $(R.id.enableStore).setEnabled(false);
            $(R.id.enableupdater).setEnabled(false);
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class MyTask extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {
            showProgress();
        }

        @Override
        protected void onPostExecute(String param) {
            closeProgress();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... params) {
            if (Looper.myLooper() == null) {
                Looper.prepare();
            }
            try {
                UpdateHosts();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}

