package com.coderstory.Purify.fragment;


import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;

import com.coderstory.Purify.R;
import com.coderstory.Purify.fragment.base.BaseFragment;


public class DonationFragment extends BaseFragment {


    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_donation;
    }

    @Override
    protected void setUpView() {
        super.setUpView();
        $(R.id.imageView).setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(getString(R.string.alipay_url)));
            startActivity(intent);
        });
    }
}
