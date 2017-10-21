package com.coderstory.Purify.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Looper;
import android.view.View;
import android.widget.Switch;

import com.coderstory.Purify.R;
import com.coderstory.Purify.utils.hostshelper.FileHelper;
import com.coderstory.Purify.utils.hostshelper.HostsHelper;

import com.coderstory.Purify.fragment.base.BaseFragment;


public class HostsFragment extends BaseFragment {

    private Dialog dialog;

    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_hosts;
    }

    @Override
    protected void setUpView() {

        $(R.id.enableHosts).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEditor().putBoolean("enableHosts", ((Switch) v).isChecked());
                getEditor().apply();
                setCheck(((Switch) v).isChecked());
                new MyTask().execute();
            }
        });

        $(R.id.enableMIUIHosts).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEditor().putBoolean("enableMIUIHosts", ((Switch) v).isChecked());
                getEditor().apply();
                new MyTask().execute();
            }
        });

        $(R.id.enableBlockAdsHosts).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEditor().putBoolean("enableBlockAdsHosts", ((Switch) v).isChecked());
                getEditor().apply();
                new MyTask().execute();
            }
        });
        $(R.id.enableGoogleHosts).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEditor().putBoolean("enableGoogleHosts", ((Switch) v).isChecked());
                getEditor().apply();
                new MyTask().execute();
            }
        });

        $(R.id.enableStore).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEditor().putBoolean("enableStore", ((Switch) v).isChecked());
                getEditor().apply();
                new MyTask().execute();
            }
        });
        $(R.id.enableupdater).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEditor().putBoolean("enableUpdater", ((Switch) v).isChecked());
                getEditor().apply();
                new MyTask().execute();
            }
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
    private boolean UpdateHosts() {
        boolean enableHostsSet = getPrefs().getBoolean("enableHosts", false); //1
        boolean enableMIUIHostsSet = getPrefs().getBoolean("enableMIUIHosts", false); //4
        boolean enableBlockAdsHostsSet = getPrefs().getBoolean("enableBlockAdsHosts", false); //4
        boolean enableGoogleHostsSet = getPrefs().getBoolean("enableGoogleHosts", false); //4
        boolean enableStoreSet = getPrefs().getBoolean("enableStore", false); //4
        boolean enableupdaterSet = getPrefs().getBoolean("enableUpdater", false); //4

        if (enableHostsSet) {
            FileHelper fh = new FileHelper();
            String HostsContext = fh.getFromAssets("none", getMContext());

            if (getPrefs().getBoolean("enableHosts", false)) { //如果未启用hosts
                if (enableBlockAdsHostsSet) {
                    HostsContext += fh.getFromAssets("hosts_noad", getMContext());
                }
                if (enableGoogleHostsSet) {
                    HostsContext += fh.getFromAssets("hosts_foreign", getMContext());
                }

                if (enableupdaterSet) {
                    HostsContext += fh.getFromAssets("hosts_noup", getMContext());
                }
                if (enableStoreSet) {
                    HostsContext += fh.getFromAssets("hosts_nostore", getMContext());
                }
                if (enableMIUIHostsSet) {
                    HostsContext += fh.getFromAssets("hosts_miui", getMContext());
                }
            }

            HostsHelper h = new HostsHelper(HostsContext, getMContext());
            return h.execute();

        } else {
            return true;
        }
    }

    private void showProgress() {
        if (dialog == null || !dialog.isShowing()) { //dialog未实例化 或者实例化了但没显示
            dialog = ProgressDialog.show(getActivity(), getString(R.string.Working), getString(R.string.Waiting));
            dialog.show();
        }
    }

    private void closeProgress() {
        if (!getActivity().isFinishing()) {
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
            UpdateHosts();
            return null;
        }
    }

}

