package com.coderstory.Purify.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.coderstory.Purify.R;
import com.coderstory.Purify.adapter.AppInfo;
import com.coderstory.Purify.adapter.AppInfoAdapter;
import com.coderstory.Purify.fragment.base.BaseFragment;
import com.coderstory.Purify.utils.LoadApkInfo;
import com.coderstory.Purify.view.PullToRefreshView;

import java.io.DataOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.coderstory.Purify.config.Misc.BackPath;


public class RestoreAppFragment extends BaseFragment {

    List<PackageInfo> packages = new ArrayList<>();
    AppInfoAdapter adapter = null;
    ListView listView = null;
    AppInfo appInfo = null;
    int mPosition = 0;
    View mView = null;
    PullToRefreshView mPullToRefreshView;
    private View view;
    private List<AppInfo> appInfoList = new ArrayList<>();
    private Context context;
    private Dialog dialog;

    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_app_list;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_app_list, container, false);
        context = getActivity();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
        new MyTask().execute();
        if (getActivity() == null) {
            return;
        }
        mPullToRefreshView = getActivity().findViewById(R.id.pull_to_refresh);
        mPullToRefreshView.setOnRefreshListener(() -> mPullToRefreshView.postDelayed(() -> {
            initData();
            showData();
            adapter.notifyDataSetChanged();
            mPullToRefreshView.setRefreshing(false);
        }, 2000));
    }

    private void initData() {
        appInfoList = new ArrayList<>();
        if (getActivity() == null) {
            return;
        }
        PackageManager pm = getActivity().getPackageManager();
        LoadApkInfo.apkAll = LoadApkInfo.GetApkFileName(BackPath);
        packages = new ArrayList<>();

        for (String item : LoadApkInfo.apkAll
                ) {
            PackageInfo packageInfo = LoadApkInfo.loadAppInfo(item, getActivity());
            if (packageInfo != null) {
                ApplicationInfo appInfo = packageInfo.applicationInfo;
                //必须设置apk的路径 否则无法读取app的图标和名称
                appInfo.sourceDir = BackPath + item;
                appInfo.publicSourceDir = BackPath + item;
                AppInfo appInfos = new AppInfo(pm.getApplicationLabel(appInfo).toString(), pm.getApplicationIcon(appInfo), packageInfo.packageName, false, packageInfo.applicationInfo.sourceDir, packageInfo.versionName, packageInfo.versionCode);
                appInfoList.add(appInfos);
            }
        }
    }

    private void showData() {
        adapter = new AppInfoAdapter(getActivity(), R.layout.app_info_item, appInfoList);
        listView = view.findViewById(R.id.listView);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener((parent, view, position, id) -> {

            mPosition = position;
            mView = view;
            AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
            dialog.setTitle(R.string.Tips_Title);
            String tipsText;
            String BtnText = getString(R.string.Btn_Sure);
            appInfo = appInfoList.get(mPosition);
            tipsText = "你确定要安装" + appInfo.getName() + "吗？";
            dialog.setMessage(tipsText);
            dialog.setPositiveButton(BtnText, (dialog12, which) -> {

                if (getPrefs().getBoolean("installType", false)) {
                    String commandText = "pm install  \"" + BackPath + appInfo.getPackageName() + ".apk\"";
                    Log.e("cc", commandText);
                    Process process = null;
                    DataOutputStream os = null;
                    try {
                        process = Runtime.getRuntime().exec("su"); //切换到root帐号
                        os = new DataOutputStream(process.getOutputStream());
                        os.writeBytes(commandText + "&\n");
                        os.writeBytes("exit\n");
                        os.flush();
                        process.waitFor();
                    } catch (Exception ignored) {

                    } finally {
                        try {
                            if (os != null) {
                                os.close();
                            }
                            assert process != null;
                            process.destroy();
                        } catch (Exception ignored) {
                        }
                    }
                    closeProgress();
                    Toast.makeText(context, "正在后台安装！", Toast.LENGTH_SHORT).show();
                } else {
                    installApkFile(BackPath + appInfo.getPackageName() + ".apk");
                }


            });
            dialog.setCancelable(true);
            dialog.setNegativeButton(R.string.Btn_Cancel, (dialog1, which) -> dialog1.cancel());
            dialog.show();
        });

    }

    protected void showProgress() {
        if (dialog == null) {
            dialog = ProgressDialog.show(getActivity(), getString(R.string.Tips_Title), getString(R.string.loadappinfo));
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

    public void installApkFile(String filePath) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(context, "com.coderstory.Purify.fileprovider", new File(filePath));
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(new File(filePath)), "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        startActivity(intent);
    }

    @SuppressLint("StaticFieldLeak")
    public class MyTask extends AsyncTask<String, Integer, String> {

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
