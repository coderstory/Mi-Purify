package com.coderstory.Purify.fragment;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.net.Uri;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.coderstory.Purify.R;
import com.coderstory.Purify.utils.SnackBarUtils;

import java.io.File;

import static android.content.Context.CLIPBOARD_SERVICE;
import static com.coderstory.Purify.config.Misc.MyBlogUrl;

public class BlogFragment extends WebViewFragment {
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_webview_toolbar, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public String getLoadUrl() {
        return MyBlogUrl;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_copy) {
            ClipboardManager myClipboard;
            if (getActivity() != null) {
                myClipboard = (ClipboardManager) getActivity().getSystemService(CLIPBOARD_SERVICE);
                ClipData myClip;
                String text = mWebView.getUrl();
                myClip = ClipData.newPlainText("text", text);
                if (myClipboard!=null) {
                    myClipboard.setPrimaryClip(myClip);
                    SnackBarUtils.makeLong(getView(), getString(R.string.cp_url_success)).show();
                }
            }
        } else if (item.getItemId() == R.id.action_share) {
            shareMsg(getString(R.string.share_url), mWebView.getTitle(), mWebView.getUrl());
        }
        return false;
    }

    public void shareMsg(String activityTitle, String msgTitle, String msgText) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain"); // 纯文本
        intent.putExtra(Intent.EXTRA_SUBJECT, msgTitle);
        intent.putExtra(Intent.EXTRA_TEXT, msgText);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(Intent.createChooser(intent, activityTitle));
    }
}
