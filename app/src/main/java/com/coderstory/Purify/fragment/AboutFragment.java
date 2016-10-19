package com.coderstory.Purify.fragment;

import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.View;
import android.widget.TextView;

import com.coderstory.Purify.R;
import com.coderstory.Purify.utils.licensesdialog.LicensesDialog;
import com.coderstory.Purify.utils.licensesdialog.licenses.ApacheSoftwareLicense20;
import com.coderstory.Purify.utils.licensesdialog.licenses.GnuLesserGeneralPublicLicense21;
import com.coderstory.Purify.utils.licensesdialog.model.Notice;
import com.coderstory.Purify.utils.licensesdialog.model.Notices;

import ren.solid.library.fragment.base.BaseFragment;

/**
 * Created by _SOLID
 * Date:2016/3/30
 * Time:20:03
 */
public class AboutFragment extends BaseFragment {


    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_about;
    }

    @Override
    protected void setUpView() {
        TextView tv_content = $(R.id.tv_content);
        tv_content.setAutoLinkMask(Linkify.ALL);
        tv_content.setMovementMethod(LinkMovementMethod
                .getInstance());


        $(R.id.os).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Notices notices = new Notices();
                notices.addNotice(new Notice("ApacheSoftwareLicense", "", "", new ApacheSoftwareLicense20()));
                notices.addNotice(new Notice("GnuLesserGeneralPublicLicense2", "", "", new GnuLesserGeneralPublicLicense21()));

                new LicensesDialog.Builder(getMContext())
                        .setNotices(notices)
                        //.setIncludeOwnLicense(true)
                        .build()
                        .show();
            }
        });
    }
}
