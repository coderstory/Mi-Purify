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
import android.os.Environment;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.coderstory.Purify.R;
import com.coderstory.Purify.utils.Adapter.Application.AppInfo;
import com.coderstory.Purify.utils.Adapter.Application.AppInfoAdapter;
import com.coderstory.Purify.utils.DirManager;
import com.yalantis.phoenix.PullToRefreshView;

import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.List;

import ren.solid.library.fragment.base.BaseFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class BackupAppFragment extends BaseFragment {


    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_backupapp;
    }


    private View view;
    private List<AppInfo> appInfoList = new ArrayList<AppInfo>();
    private List<AppInfo> appInfoList2 = new ArrayList<AppInfo>();
    List<PackageInfo> packages = new ArrayList<PackageInfo>();
    AppInfoAdapter adapter = null;
    ListView listView = null;
    AppInfo appInfo = null;
    int mPosition = 0;
    View mView = null;
    com.yalantis.phoenix.PullToRefreshView mPullToRefreshView;
  final   String  path_backup=Environment.getExternalStorageDirectory().getPath() + "/MIUI Purify/backupAPP/";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_backupapp, container, false);
        return view;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        new MyTask().execute();
        mPullToRefreshView = (PullToRefreshView) getActivity().findViewById(R.id.pull_to_refresh);

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

    private void initFruit() {

        appInfoList = new ArrayList<>();
        PackageManager pm = getActivity().getPackageManager();
        DirManager.apkAll = DirManager.GetApkFileName(path_backup);
        packages = new ArrayList<>();

        appInfoList2.clear();
        for (String item : DirManager.apkAll
                ) {
            PackageInfo packageInfo = DirManager.loadAppInfo(item, getActivity());
            if (packageInfo != null) {
                ApplicationInfo appInfo = packageInfo.applicationInfo;
                //必须设置apk的路径 否则无法读取app的图标和名称
                appInfo.sourceDir = path_backup + item;
                appInfo.publicSourceDir = path_backup + item;
                AppInfo appInfos = new AppInfo(pm.getApplicationLabel(appInfo).toString(), pm.getApplicationIcon(appInfo), packageInfo.packageName, false, packageInfo.applicationInfo.sourceDir, packageInfo.versionName,packageInfo.versionCode);
                appInfoList2.add(appInfos);
            }
        }

        appInfoList.clear();
        packages = getContext().getPackageManager().getInstalledPackages(0);
        //packageInfo.applicationInfo.flags&ApplicationInfo.FLAG_SYSTEM)==0 表示是系统应用
        if (packages != null) {
            for (int i = 0; i < packages.size(); i++) {
                PackageInfo packageInfo = packages.get(i);
                if (packageInfo.applicationInfo.sourceDir.startsWith("/data/")) {
                    switch (isBackuped(packageInfo)) {
                        case 0:
                                AppInfo appInfo = new AppInfo(packageInfo.applicationInfo.loadLabel(getActivity().getPackageManager()).toString(), packageInfo.applicationInfo.loadIcon(getActivity().getPackageManager()), packageInfo.packageName, false, packageInfo.applicationInfo.sourceDir, packageInfo.versionName,packageInfo.versionCode);
                                appInfoList.add(appInfo);
                            break;
                        case 1:
                            break;
                        default:
                                AppInfo appInfo2 = new AppInfo(packageInfo.applicationInfo.loadLabel(getActivity().getPackageManager()).toString(), packageInfo.applicationInfo.loadIcon(getActivity().getPackageManager()), packageInfo.packageName, false, packageInfo.applicationInfo.sourceDir, packageInfo.versionName+"  有新版本未备份",packageInfo.versionCode);
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
       // DirManager.apkAll = DirManager.GetApkFileName(Environment.getExternalStorageDirectory().getPath() + "/MIUI Purify/backup/");
        if (DirManager.apkAll != null) {
            for (AppInfo element :appInfoList2)
            {
              //  if(packageInfo.packageName.equals("com.coderstory.Purify")) {
                    if ((packageInfo.packageName).equals(element.getPackageName())) {
                        if (packageInfo.versionCode > element.getVersionCode()) {
                            result = 2;
                        } else {
                            result = 1;
                        }
                        break;
                 //   }
                }
            }
        }
        return result;
    }

    private void initData() {
        packages.clear();
        //   Toast.makeText(getActivity(), R.string.isloading, Toast.LENGTH_LONG).show();
        packages = getActivity().getPackageManager().getInstalledPackages(0);
        initFruit();
    }

    private void showData() {
        adapter = new AppInfoAdapter(getActivity(), R.layout.app_info_item, R.color.disableApp, appInfoList);
        listView = (ListView) view.findViewById(R.id.listView);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                mPosition = position;
                mView = view;
                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                dialog.setTitle(R.string.Tips_Title);
                String tipsText = "";
                String BtnText = getString(R.string.Btn_Sure);
                appInfo = appInfoList.get(mPosition);
                tipsText = "你确定要备份" + appInfo.getName() + "吗？";

                dialog.setMessage(tipsText);
                dialog.setPositiveButton(BtnText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DirManager.needReload=true;
                        String commandText = "cp -f " + appInfo.getappdir() +" \""+path_backup + appInfo.getPackageName() + ".apk\"";
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
                            View rootView = LayoutInflater.from(getActivity()).inflate(R.layout.app_info_item, null);
                            appInfoList.remove(mPosition);
                            adapter.notifyDataSetChanged();
                        } catch (Exception e) {

                        } finally {
                            try {
                                if (os != null) {
                                    os.close();
                                }
                                process.destroy();
                            } catch (Exception e) {
                            }
                        }
                        DirManager.needReload=true;
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


    class MyTask extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            //getActivity().setProgressBarIndeterminateVisibility(true);
            showProgress();
        }

        @Override
        protected void onPostExecute(String param) {
            showData();
           // getActivity().setProgressBarIndeterminateVisibility(false);
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
            if(Looper.myLooper()==null) {
                Looper.prepare();
            }
            initData();
            return null;
        }
    }

    private Dialog dialog;

    protected void showProgress() {
        if (dialog == null) {
//		    dialog.setContentView(R.layout.progress_dialog);
            //    dialog.getWindow().setAttributes(params);
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

    public boolean isShowing() {
        if (dialog != null) {
            return dialog.isShowing();
        }
        return false;
    }

}
