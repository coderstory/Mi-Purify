package com.coderstory.Purify.fragment;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

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
import androidx.annotation.Nullable;
import static com.coderstory.Purify.config.Misc.BackPath;

/**
 * A simple {@link Fragment} subclass.
 */
public class BackupAppFragment extends BaseFragment {


    List<PackageInfo> packages = new ArrayList<>();
    AppInfoAdapter adapter = null;
    ListView listView = null;
    AppInfo appInfo = null;
    int mPosition = 0;
    View mView = null;
    PullToRefreshView mPullToRefreshView;
    private View view;
    private List<AppInfo> appInfoList = new ArrayList<>();
    private List<AppInfo> appInfoList2 = new ArrayList<>();
    private Dialog dialog;

    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_app_list;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //创建备份目录

      File  file = new File(BackPath);
        if (!file.exists()) {
            if (!file.mkdirs()){
                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                dialog.setTitle("提示");
                String tipsText = "备份应用需要读取存储权限,但目前似乎没有给?";
                dialog.setMessage(tipsText);
                dialog.setPositiveButton(getString(R.string.Btn_Sure), (dialog12, which) -> new Thread(() -> {
                    System.exit(0);
                }).start());
                dialog.setCancelable(false);
                AlertDialog mydialog = dialog.create();
                mydialog.show();
            }

        }
        view = inflater.inflate(R.layout.fragment_app_list, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        new MyTask().execute();
        mPullToRefreshView = getActivity().findViewById(R.id.pull_to_refresh);

        mPullToRefreshView.setOnRefreshListener(() -> mPullToRefreshView.postDelayed(() -> {
            initData();
            showData();
            adapter.notifyDataSetChanged();
            mPullToRefreshView.setRefreshing(false);
        }, 2000));
    }

    private void initFruit() {

        appInfoList = new ArrayList<>();
        PackageManager pm = getActivity().getPackageManager();
        LoadApkInfo.apkAll = LoadApkInfo.GetApkFileName(BackPath);
        packages = new ArrayList<>();

        appInfoList2.clear();
        for (String item : LoadApkInfo.apkAll
                ) {
            PackageInfo packageInfo = LoadApkInfo.loadAppInfo(item, getActivity());
            if (packageInfo != null) {
                ApplicationInfo appInfo = packageInfo.applicationInfo;
                //必须设置apk的路径 否则无法读取app的图标和名称
                appInfo.sourceDir = BackPath + item;
                appInfo.publicSourceDir = BackPath + item;
                AppInfo appInfos = new AppInfo(pm.getApplicationLabel(appInfo).toString(), pm.getApplicationIcon(appInfo), packageInfo.packageName, false, packageInfo.applicationInfo.sourceDir, packageInfo.versionName, packageInfo.versionCode);
                appInfoList2.add(appInfos);
            }
        }

        appInfoList.clear();
        packages = getContext().getPackageManager().getInstalledPackages(0);
        if (packages != null) {
            for (int i = 0; i < packages.size(); i++) {
                PackageInfo packageInfo = packages.get(i);
                if (packageInfo.applicationInfo.sourceDir.startsWith("/data/")) {
                    switch (isBackuped(packageInfo)) {
                        case 0:
                            AppInfo appInfo = new AppInfo(packageInfo.applicationInfo.loadLabel(getActivity().getPackageManager()).toString(), packageInfo.applicationInfo.loadIcon(getActivity().getPackageManager()), packageInfo.packageName, false, packageInfo.applicationInfo.sourceDir, packageInfo.versionName, packageInfo.versionCode);
                            appInfoList.add(appInfo);
                            break;
                        case 1:
                            break;
                        default:
                            AppInfo appInfo2 = new AppInfo(packageInfo.applicationInfo.loadLabel(getActivity().getPackageManager()).toString(), packageInfo.applicationInfo.loadIcon(getActivity().getPackageManager()), packageInfo.packageName, false, packageInfo.applicationInfo.sourceDir, packageInfo.versionName + "  有新版本未备份", packageInfo.versionCode);
                            appInfoList.add(appInfo2);
                            break;
                    }
                }
            }
        }
    }

    //1 已备份  0未备份 2 有新的版本未备份
    private int isBackuped(PackageInfo packageInfo) {
        int result = 0;
        if (LoadApkInfo.apkAll != null) {
            for (AppInfo element : appInfoList2) {
                if ((packageInfo.packageName).equals(element.getPackageName())) {
                    if (packageInfo.versionCode > element.getVersionCode()) {
                        result = 2;
                    } else {
                        result = 1;
                    }
                    break;
                }
            }
        }
        return result;
    }

    private void initData() {
        packages.clear();
        packages = getActivity().getPackageManager().getInstalledPackages(0);
        initFruit();
    }

    private void showData() {
        adapter = new AppInfoAdapter(getActivity(), R.layout.app_info_item, appInfoList);
        listView = view.findViewById(R.id.listView);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                mPosition = position;
                mView = view;
                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                dialog.setTitle(R.string.Tips_Title);
                String tipsText;
                String BtnText = getString(R.string.Btn_Sure);
                appInfo = appInfoList.get(mPosition);
                tipsText = "你确定要备份" + appInfo.getName() + "吗？";

                dialog.setMessage(tipsText);
                dialog.setPositiveButton(BtnText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        LoadApkInfo.needReload = true;
                        String commandText = "cp -f " + appInfo.getAppDir() + " \"" + BackPath + appInfo.getPackageName() + ".apk\"";
                        Process process = null;
                        DataOutputStream os = null;
                        try {
                            String cmd = commandText;
                            process = Runtime.getRuntime().exec("su"); //切换到root帐号
                            os = new DataOutputStream(process.getOutputStream());
                            os.writeBytes(cmd + "\n");
                            os.writeBytes("exit\n");
                            os.flush();
                            process.waitFor();
                            appInfoList.remove(mPosition);
                            adapter.notifyDataSetChanged();
                        } catch (Exception e) {
                            Log.e("", e.getMessage());
                        } finally {
                            try {
                                if (os != null) {
                                    os.close();
                                }
                                assert process != null;
                                process.destroy();
                            } catch (Exception e) {
                            }
                        }
                        LoadApkInfo.needReload = true;
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

    protected void showProgress() {
        if (dialog == null) {
            dialog = ProgressDialog.show(getActivity(), getString(R.string.Tips_Title), getString(R.string.loadappinfo));
            dialog.show();
        }
    }

    protected void closeProgress() {
        if (dialog != null) {
            dialog.cancel();
            dialog = null;
        }
    }

    private class MyTask extends AsyncTask<String, Integer, String> {

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
        protected void onCancelled() {
            // TODO Auto-generated method stub
            super.onCancelled();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            // TODO Auto-generated method stub
            super.onProgressUpdate(values);
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
