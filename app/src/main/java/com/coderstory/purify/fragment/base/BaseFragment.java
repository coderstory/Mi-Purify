package com.coderstory.purify.fragment.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.coderstory.purify.config.Misc;
import com.coderstory.purify.utils.Utils;
import com.topjohnwu.superuser.Shell;

import java.io.File;

import static com.coderstory.purify.config.Misc.ApplicationName;


/**
 * Created by _SOLID
 * Date:2016/3/30
 * Time:11:30
 */
public abstract class BaseFragment extends Fragment {

    public static final String PREFS_FOLDER = " /data/user_de/0/" + ApplicationName + "/shared_prefs\n";
    public static final String PREFS_FILE = " /data/user_de/0/" + ApplicationName + "/shared_prefs/" + Misc.SharedPreferencesName + ".xml\n";
    private static final String TAG = "BaseFragment";
    private static SharedPreferences prefs;
    private static SharedPreferences.Editor editor;
    private View mContentView;
    private Context mContext;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContentView = inflater.inflate(setLayoutResourceID(), container, false);//setContentView(inflater, container);
        mContext = getContext();
        ProgressDialog mProgressDialog = new ProgressDialog(getMContext());
        mProgressDialog.setCanceledOnTouchOutside(false);
        setHasOptionsMenu(true);
        init();
        setUpView();
        setUpData();
        getPrefs();
        return mContentView;
    }

    protected abstract int setLayoutResourceID();

    protected void setUpData() {
    }

    protected SharedPreferences.Editor getEditor() {
        if (editor == null) {
            editor = prefs.edit();
        }
        return editor;

    }


    protected SharedPreferences getPrefs() {
        prefs = Utils.getMySharedPreferences(getMContext().getApplicationContext(), "/data/user_de/0/" + ApplicationName + "/shared_prefs/", Misc.SharedPreferencesName);
        return prefs;
    }

    public void fix() {
        getEditor().commit();
        sudoFixPermissions();
    }

    protected void sudoFixPermissions() {
        if (Build.VERSION.SDK_INT < 30) {
            new Thread(() -> {
                File pkgFolder = new File("/data/user_de/0/" + ApplicationName);
                if (pkgFolder.exists()) {
                    pkgFolder.setExecutable(true, false);
                    pkgFolder.setReadable(true, false);
                }
                Shell.su("chmod  755 " + PREFS_FOLDER).exec();
                // Set preferences file permissions to be world readable
                Shell.su("chmod  644 " + PREFS_FILE).exec();
            }).start();
        }
    }

    protected void init() {
    }

    protected void setUpView() {
    }

    protected <T extends View> T $(int id) {
        return (T) mContentView.findViewById(id);
    }


    protected View getContentView() {
        return mContentView;
    }

    public Context getMContext() {
        return mContext;
    }
}
