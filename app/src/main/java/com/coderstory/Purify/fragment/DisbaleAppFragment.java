package com.coderstory.Purify.fragment;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
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
import com.coderstory.Purify.utils.roothelper.SuHelper;
import com.yalantis.phoenix.PullToRefreshView;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import ren.solid.library.fragment.base.BaseFragment;
import ren.solid.library.utils.SnackBarUtils;

import static com.coderstory.Purify.config.Misc.BackUpFileName;
import static ren.solid.library.utils.FileUtils.readFile;

/**
 * A simple {@link Fragment} subclass.
 */
public class DisbaleAppFragment extends BaseFragment {
    // private Context mContext=null;
    List<PackageInfo> packages = new ArrayList<>();
    AppInfoAdapter adapter = null;
    ListView listView = null;
    AppInfo appInfo = null;
    int mposition = 0;
    View mview = null;
    com.yalantis.phoenix.PullToRefreshView mPullToRefreshView;
    AlertDialog mydialog;
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
        packages = new ArrayList<>();
        packages = getContext().getPackageManager().getInstalledPackages(0);
        initFruit();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_disableapp_toolbar, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void initFruit() {
        //packageInfo.applicationInfo.flags&ApplicationInfo.FLAG_SYSTEM)==0 表示是系统应用
        appInfoList.clear();
        appInfoList2.clear();
        for (int i = 0; i < packages.size(); i++) {
            PackageInfo packageInfo = packages.get(i);
            if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) > 0) {
                if (packageInfo.applicationInfo.enabled) {
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
        listView = (ListView) getContentView().findViewById(R.id.listView);
        assert listView != null;
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mposition = position;
                mview = view;
                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                dialog.setTitle(R.string.Tips_Title);
                String tipsText;
                String BtnText = getString(R.string.Btn_Sure);
                appInfo = appInfoList.get(mposition);
                if (appInfo.getDisable()) {
                    tipsText = getString(R.string.sureAntiDisable) + appInfo.getName() + getString(R.string.sureAntiDisableAfter);
                } else {
                    tipsText = getString(R.string.sureDisable) + appInfo.getName() + getString(R.string.sureDisableAfter);
                }
                dialog.setMessage(tipsText);
                dialog.setPositiveButton(BtnText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String commandText = (!appInfo.getDisable() ? "pm disable " : "pm enable ") + appInfo.getPackageName();
                        Log.e("cc", commandText);
                        Process process = null;
                        DataOutputStream os = null;
                        try {
                            process = Runtime.getRuntime().exec("su"); //切换到root帐号
                            os = new DataOutputStream(process.getOutputStream());
                            os.writeBytes(commandText + "\n");
                            os.writeBytes("exit\n");
                            os.flush();
                            process.waitFor();
                            if (appInfo.getDisable()) {
                                appInfo.setDisable(false);
                                appInfoList.set(mposition, appInfo);
                                mview.setBackgroundColor(getResources().getColor(R.color.colorPrimary)); //正常的颜色
                            } else {
                                appInfo.setDisable(true);
                                appInfoList.set(mposition, appInfo);
                                mview.setBackgroundColor(Color.parseColor("#d0d7d7d7")); //冻结的颜色
                            }
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
        return R.layout.fragment_disbale_app;
    }

    @Override
    protected void init() {
        super.init();
        Toast.makeText(getActivity(), R.string.disableapptips, Toast.LENGTH_LONG).show();

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
        if (item.getItemId() == R.id.action_backupList) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
            dialog.setTitle(R.string.backup_list);
            String tipsText = getString(R.string.tips_sure_backuplist);
            dialog.setMessage(tipsText);
            dialog.setPositiveButton(getString(R.string.Btn_Sure), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    satrtBackuop();
                }
            });
            dialog.setCancelable(true);
            dialog.setNegativeButton(R.string.Btn_Cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            mydialog = dialog.create();
            mydialog.show();

        } else if (item.getItemId() == R.id.action_restoreList) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
            dialog.setTitle(R.string.tips_sure_restore_settings);
            String tipsText = getString(R.string.restore_set);
            dialog.setMessage(tipsText);
            dialog.setPositiveButton(getString(R.string.Btn_Sure), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    restoreList();
                }
            });
            dialog.setCancelable(true);
            dialog.setNegativeButton(R.string.Btn_Cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            mydialog = dialog.create();
            mydialog.show();
        }

        return false;
    }


    private void restoreList() {
        File dir = new File(BackUpFileName);
        String fileName = "userList";
        String content = "";
        if (!dir.exists()) {
            SnackBarUtils.makeShort($(R.id.listView), getString(R.string.not_fond_backup_list_file)).danger();
            return;
        }
        try {
            content = readFile(BackUpFileName + fileName, null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (content.isEmpty()) {
            SnackBarUtils.makeShort($(R.id.listView), getString(R.string.not_fond_backup_list)).danger();
            return;
        }

        final String[] list = content.split("\n");

        dialog = ProgressDialog.show(getContext(), getString(R.string.tips), getString(R.string.restoreing));
        dialog.show();

        new Thread(new Runnable() {
            public void run() {
                disableHelp dh = new disableHelp(list);
                dh.execute();
                myHandler.sendMessage(new Message());
            }
        }).start();

    }

    private void satrtBackuop() {
        StringBuilder SB = new StringBuilder("#已备份的系统APP冻结列表#\n");

        //遍历数据源
        for (AppInfo info : appInfoList) {
            if (info.getDisable()) { //判断是否被冻结
                SB.append(info.getPackageName() + "\n");
            }
        }

        File dir = new File(BackUpFileName);
        String fileName = "userList";
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                SnackBarUtils.makeShort($(R.id.listView), getString(R.string.tips_backup_error)).show();
                return;
            }
        }
        FileOutputStream fos;
        String result = "";
        try {
            fos = new FileOutputStream(BackUpFileName + fileName);
            fos.write(SB.toString().getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
            result = e.getMessage();
        }
        if (result.equals("")) {
            SnackBarUtils.makeShort($(R.id.listView), getString(R.string.tips_backup_success)).show();
        } else {
            SnackBarUtils.makeShort($(R.id.listView), getString(R.string.tips_backup_error) + result).show();
        }
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

    class disableHelp extends SuHelper {
        String[] list;

        disableHelp(String[] list) {
            this.list = list;
        }

        @Override
        protected ArrayList<String> getCommandsToExecute() throws UnsupportedEncodingException {
            ArrayList<String> mylist = new ArrayList<>();

            for (String item : list) {
                mylist.add("pm disable " + item);
            }
            return mylist;
        }

    }
}

