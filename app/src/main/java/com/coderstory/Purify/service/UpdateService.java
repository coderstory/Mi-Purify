package com.coderstory.Purify.service;


import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import com.coderstory.Purify.R;
import com.coderstory.Purify.utils.checkupdate.UpdateConfig;
import com.coderstory.Purify.activity.MainActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class UpdateService extends Service {

    // 下载状态
    private final static int DOWNLOAD_COMPLETE = 0;
    private final static int DOWNLOAD_FAIL = 1;
    // 文件存储
    private File updateDir = null;
    private File updateFile = null;
    // 通知栏
    private NotificationManager updateNotificationManager = null;
    private Notification updateNotification = null;
    // 通知栏跳转Intent
    private Intent updateIntent = null;
    private PendingIntent updatePendingIntent = null;
    /***
     * 创建通知栏
     */
    // RemoteViews contentView;
    // 这样的下载代码很多，我就不做过多的说明
    private int downloadCount = 0;
    private int currentSize = 0;
    private long totalSize = 0;
    private int updateTotalSize = 0;
    @SuppressLint("HandlerLeak")
    private Handler updateHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DOWNLOAD_COMPLETE:
                    // 点击安装PendingIntent
                    Uri uri = Uri.fromFile(updateFile);
                    Intent installIntent = new Intent(Intent.ACTION_VIEW);
                    installIntent.setDataAndType(uri,
                            "application/vnd.android.package-archive");
                    updatePendingIntent = PendingIntent.getActivity(
                            UpdateService.this, 0, installIntent, 0);
                    updateNotification.defaults = Notification.DEFAULT_SOUND;// 铃声提醒
                    // 设置通知栏显示内容
                    updateNotification = new Notification.Builder(UpdateService.this)
                            .setAutoCancel(true)
                            .setContentTitle(getString(R.string.app_name))
                            .setContentText(getString(R.string.Download_Success))
                            .setContentIntent(updatePendingIntent)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setWhen(System.currentTimeMillis())
                            .build();
                    updateNotificationManager.notify(0, updateNotification);
                    break;
                case DOWNLOAD_FAIL:
                    // 下载失败
                    updateNotification = new Notification.Builder(UpdateService.this)
                            .setAutoCancel(true)
                            .setContentTitle(getString(R.string.app_name))
                            .setContentText(getString(R.string.Download_Fail))
                            .setContentIntent(updatePendingIntent)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setWhen(System.currentTimeMillis())
                            .build();
                    updateNotificationManager.notify(0, updateNotification);
                    break;
            }
            stopService(updateIntent);
        }
    };

    // 在onStartCommand()方法中准备相关的下载工作：
    @SuppressWarnings("deprecation")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 获取传值
        updateTotalSize = 0;
        currentSize = 0;
        int titleId = intent.getIntExtra("titleId", 0);
        // 创建文件
        if (android.os.Environment.MEDIA_MOUNTED.equals(android.os.Environment
                .getExternalStorageState())) {
            updateDir = new File(Environment.getExternalStorageDirectory(),
                    UpdateConfig.saveFileName);
            updateFile = new File(updateDir.getPath(), getResources()
                    .getString(titleId) + ".apk");

        }
        this.updateNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // 设置下载过程中，点击通知栏，回到主界面
        updateIntent = new Intent(this, MainActivity.class);
        updatePendingIntent = PendingIntent.getActivity(this, 0, updateIntent,
                0);
        // 设置通知栏显示内容
        updateNotification = new Notification.Builder(this)
                .setAutoCancel(true)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(getString(R.string.Downloading))
                .setContentIntent(updatePendingIntent)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setWhen(System.currentTimeMillis())
                .build();
        // public void setLatestEventInfo(Context context, CharSequence contentTitle, CharSequence contentText, PendingIntent contentIntent)
        // 发出通知
        updateNotificationManager.notify(0, updateNotification);
        // 开启一个新的线程下载，如果使用Service同步下载，会导致ANR问题，Service本身也会阻塞
        new Thread(new updateRunnable()).start();// 这个是下载的重点，是下载的过程
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    private long downloadUpdateFile(String downloadUrl, File saveFile)
            throws Exception {
        HttpURLConnection httpConnection = null;
        InputStream is = null;
        FileOutputStream fos = null;
        try {
            URL url = new URL(downloadUrl);
            httpConnection = (HttpURLConnection) url.openConnection();
            httpConnection
                    .setRequestProperty("User-Agent", "PacificHttpClient");
            if (currentSize > 0) {
                httpConnection.setRequestProperty("RANGE", "bytes="
                        + currentSize + "-");
            }
            httpConnection.setConnectTimeout(10000);
            httpConnection.setReadTimeout(20000);
            updateTotalSize = httpConnection.getContentLength();
            if (httpConnection.getResponseCode() == 404) {
                throw new Exception("fail!");
            }
            is = httpConnection.getInputStream();
            fos = new FileOutputStream(saveFile, false);
            byte buffer[] = new byte[4096];
            int readsize;
            while ((readsize = is.read(buffer)) > 0) {
                fos.write(buffer, 0, readsize);
                totalSize += readsize;
                // 为了防止频繁的通知导致应用吃紧，百分比增加10才通知一次
                if ((downloadCount == 0)
                        || (int) (totalSize * 100 / updateTotalSize) - 10 > downloadCount) {
                    downloadCount += 10;
                    updateNotification = new Notification.Builder(UpdateService.this)
                            .setAutoCancel(true)
                            .setContentTitle(getString(R.string.Downloading))
                            .setContentText((int) totalSize * 100 / updateTotalSize
                                    + "%")
                            .setContentIntent(updatePendingIntent)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setWhen(System.currentTimeMillis())
                            .build();
                    /***
                     * 在这里我们用自定的view来显示Notification
                     */
                    //   updateNotification.contentView = new RemoteViews(
                    //           getPackageName(), R.layout.notification_item);
//                    updateNotification.contentView.setTextViewText(
//                            R.id.notificationTitle, "正在下载");
//                    updateNotification.contentView.setProgressBar(
//                            R.id.notificationProgress, 100, downloadCount, false);
                    updateNotificationManager.notify(0, updateNotification);
                }
            }
        } finally {
            if (httpConnection != null) {
                httpConnection.disconnect();
            }
            if (is != null) {
                is.close();
            }
            if (fos != null) {
                fos.close();
            }
        }
        return totalSize;
    }

    private class updateRunnable implements Runnable {
        Message message = updateHandler.obtainMessage();

        public void run() {
            message.what = DOWNLOAD_COMPLETE;
            try {
                // 增加权限<USES-PERMISSION
                // android:name="android.permission.WRITE_EXTERNAL_STORAGE">;
                if (!updateDir.exists()) {
                    updateDir.mkdirs();
                }
                //重新创建一遍 删除之前的旧数据 比如下载失败的不完整文件
                if (updateFile.exists()) {
                    updateFile.delete();
                    // new ToastMessageTask().execute(getString(R.string.Delete_OldFile_Fail));
                }
                updateFile.createNewFile();
                //  new ToastMessageTask().execute(getString(R.string.Create_DownlodFile_Fail));

                // 下载函数
                // 增加权限<USES-PERMISSION
                // android:name="android.permission.INTERNET">;
                long downloadSize = downloadUpdateFile(
                        UpdateConfig.URL,
                        updateFile);
                if (downloadSize > 0) {
                    // 下载成功
                    updateHandler.sendMessage(message);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                message.what = DOWNLOAD_FAIL;
                // 下载失败
                updateHandler.sendMessage(message);
            }
        }
    }


    // A class that will run Toast messages in the main GUI context
//    private class ToastMessageTask extends AsyncTask<String, String, String> {
//        String toastMessage;
//
//        @Override
//        protected String doInBackground(String... params) {
//            toastMessage = params[0];
//            return toastMessage;
//        }
//
//
//        // This is executed in the context of the main GUI thread
//        protected void onPostExecute(String result) {
//            Toast toast = Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT);
//            toast.show();
//        }
//    }


}
