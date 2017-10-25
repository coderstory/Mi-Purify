package com.coderstory.Purify.fragment;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.coderstory.Purify.R;
import com.coderstory.Purify.adapter.AppInfo;
import com.coderstory.Purify.adapter.AppInfoAdapter;
import com.coderstory.Purify.fragment.base.BaseFragment;
import com.coderstory.Purify.view.PullToRefreshView;

import java.util.ArrayList;
import java.util.List;

import eu.chainfire.libsuperuser.Shell;

import static com.coderstory.Purify.config.Misc.ApplicationName;
import static com.coderstory.Purify.config.Misc.SharedPreferencesName;


public class HideAppFragment extends BaseFragment {
    public static final String PREFS_FOLDER = " /data/data/" + ApplicationName + "/shared_prefs\n";
    public static final String PREFS_FILE = " /data/data/" + ApplicationName + "/shared_prefs/" + SharedPreferencesName + ".xml\n";
    private static final String TAG = "AA";

    List<PackageInfo> packages = new ArrayList<>();
    AppInfoAdapter adapter = null;
    ListView listView = null;
    AppInfo appInfo = null;
    int mPosition = 0;
    View mView = null;
    PullToRefreshView mPullToRefreshView;
    List<String> hideAppList;
    private List<AppInfo> appInfoList = new ArrayList<>();
    private List<AppInfo> appInfoList2 = new ArrayList<>();
    private Dialog dialog;
    Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {
            ((ProgressDialog) dialog).setMessage(getString(R.string.refreshing_list));
            initData();
            adapter.notifyDataSetChanged();
            dialog.cancel();
            dialog = null;
            super.handleMessage(msg);
        }
    };

    private void initData() {
        String list = getPrefs().getString("Hide_App_List", "");
        hideAppList = new ArrayList<>();
        hideAppList.addAll(java.util.Arrays.asList(list.split(":")));
        packages = new ArrayList<>();
        packages = getContext().getPackageManager().getInstalledPackages(0);
        initFruit();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_hideapp_toolbar, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void initFruit() {
        appInfoList.clear();
        appInfoList2.clear();
        for (int i = 0; i < packages.size(); i++) {
            PackageInfo packageInfo = packages.get(i);
            Intent intent = getContext().getPackageManager().getLaunchIntentForPackage(packageInfo.packageName);
            // 过来掉没启动器图标的app
            if (intent != null) {
                if (!hideAppList.contains(packageInfo.applicationInfo.packageName)) {
                    AppInfo appInfo = new AppInfo(packageInfo.applicationInfo.loadLabel(getContext().getPackageManager()).toString(), packageInfo.applicationInfo.loadIcon(getContext().getPackageManager()), packageInfo.packageName, false, String.valueOf(packageInfo.versionName));
                    appInfoList.add(appInfo);
                } else {
                    AppInfo appInfo = new AppInfo(packageInfo.applicationInfo.loadLabel(getContext().getPackageManager()).toString(), packageInfo.applicationInfo.loadIcon(getContext().getPackageManager()), packageInfo.packageName, true, String.valueOf(packageInfo.versionName));
                    appInfoList2.add(appInfo);
                }
            }
        }
        appInfoList.addAll(appInfoList2);
    }

    private void showData() {
        adapter = new AppInfoAdapter(getContext(), R.layout.app_info_item, appInfoList);
        listView = getContentView().findViewById(R.id.listView);
        assert listView != null;
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mPosition = position;
                mView = view;
                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                dialog.setTitle(R.string.Tips_Title);
                String tipsText;
                String BtnText = getString(R.string.Btn_Sure);
                appInfo = appInfoList.get(mPosition);
                if (appInfo.getDisable()) {
                    tipsText = getString(R.string.sureAntiDisable) + appInfo.getName() + "的隐藏状态吗";
                } else {
                    tipsText = "你确定要隐藏" + appInfo.getName() + getString(R.string.sureDisableAfter);
                }
                dialog.setMessage(tipsText);
                dialog.setPositiveButton(BtnText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (appInfo.getDisable()) {
                            // 解除隐藏
                            String tmp = "";
                            for (String s : hideAppList) {
                                if (s.equals(appInfo.getPackageName())) {
                                    tmp = s;
                                }
                            }
                            hideAppList.remove(tmp);
                        } else {
                            // 隐藏
                            hideAppList.add(appInfo.getPackageName());
                        }
                        String value = "";
                        for (String s : hideAppList) {
                            value += s + ":";
                        }
                        value = value.substring(0, value.length() - 1);

                        getEditor().putString("Hide_App_List", value);
                        getEditor().apply();
                        sudoFixPermissions();

                        if (appInfo.getDisable()) {
                            appInfo.setDisable(false);
                            appInfoList.set(mPosition, appInfo);
                            mView.setBackgroundColor(getResources().getColor(R.color.colorPrimary)); //正常的颜色
                        } else {
                            appInfo.setDisable(true);
                            appInfoList.set(mPosition, appInfo);
                            mView.setBackgroundColor(Color.parseColor("#d0d7d7d7")); //冻结的颜色
                        }

                    }
                });
                dialog.setCancelable(true);
                dialog.setNegativeButton(R.string.Btn_Cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                dialog.show();
            }
        });
    }

    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_app_list;
    }

    @Override
    protected void init() {
        super.init();
        Toast.makeText(getActivity(), "点击应用切换 隐藏/显示 状态 \n          【重启桌面生效】", Toast.LENGTH_LONG).show();

        new MyTask().execute();

        mPullToRefreshView = getContentView().findViewById(R.id.pull_to_refresh);

        mPullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPullToRefreshView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        initData();
                        showData();
                        adapter.notifyDataSetChanged();
                        mPullToRefreshView.setRefreshing(false);
                    }
                }, 2000);
            }
        });
    }

    protected void showProgress() {
        if (dialog == null) {
            dialog = ProgressDialog.show(getContext(), getString(R.string.Tips_Title), getString(R.string.loadappinfo));
            dialog.show();
        }
    }

    //
    protected void closeProgress() {

        if (dialog != null) {
            dialog.cancel();
            dialog = null;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_restrathome) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
            dialog.setTitle("提示");
            String tipsText = "是否重启MIUI桌面应用当前设置?";
            dialog.setMessage(tipsText);
            dialog.setPositiveButton(getString(R.string.Btn_Sure), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    new Thread() {
                        @Override
                        public void run() {
                            Shell.SU.run("am force-stop com.miui.home");
                        }
                    }.start();
                }
            });
            dialog.setCancelable(true);
            dialog.setNegativeButton(R.string.Btn_Cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog mydialog = dialog.create();
            mydialog.show();
        }

        return false;
    }

    class MyTask extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {

            showProgress();
        }

        @Override
        protected void onPostExecute(String param) {
            showData();

            adapter.notifyDataSetChanged();
            closeProgress();
        }


        @Override
        protected String doInBackground(String... params) {

            if (Looper.myLooper() == null) {
                Looper.prepare();
            }
            initData();
            return null;
        }
    }

}

