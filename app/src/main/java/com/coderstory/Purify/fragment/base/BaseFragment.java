package com.coderstory.Purify.fragment.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.coderstory.Purify.utils.RuntimeUtil;

import java.io.File;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import static com.coderstory.Purify.config.Misc.ApplicationName;
import static com.coderstory.Purify.config.Misc.SharedPreferencesName;


/**
 * Created by _SOLID
 * Date:2016/3/30
 * Time:11:30
 */
public abstract class BaseFragment extends Fragment {

    public static final String PREFS_FOLDER = " /data/data/" + ApplicationName + "/shared_prefs\n";
    public static final String PREFS_FILE = " /data/data/" + ApplicationName + "/shared_prefs/" + SharedPreferencesName + ".xml\n";
    private static SharedPreferences prefs;
    private static SharedPreferences.Editor editor;
    private View mContentView;
    private Context mContext;
    private ProgressDialog mProgressDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContentView = inflater.inflate(setLayoutResourceID(), container, false);//setContentView(inflater, container);
        mContext = getContext();
        mProgressDialog = new ProgressDialog(getMContext());
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
            editor = getPrefs().edit();
        }
        return editor;

    }

    protected SharedPreferences getPrefs() {
        prefs = getContext().getSharedPreferences("UserSettings", Context.MODE_PRIVATE);
        return prefs;
    }

    protected void init() {
    }

    protected void setUpView() {
    }

    protected <T extends View> T $(int id) {
        return (T) mContentView.findViewById(id);
    }

    // protected abstract View setContentView(LayoutInflater inflater, ViewGroup container);

    protected View getContentView() {
        return mContentView;
    }

    public Context getMContext() {
        return mContext;
    }

    protected ProgressDialog getProgressDialog() {
        return mProgressDialog;
    }

    protected void sudoFixPermissions() {
        new Thread(() -> {
            File pkgFolder = new File("/data/data/" + ApplicationName);
            if (pkgFolder.exists()) {
                pkgFolder.setExecutable(true, false);
                pkgFolder.setReadable(true, false);
            }
            RuntimeUtil.exec("chmod  755 " + PREFS_FOLDER);
            // Set preferences file permissions to be world readable
            RuntimeUtil.exec("chmod  644 " + PREFS_FILE);
        }).start();
    }
}
