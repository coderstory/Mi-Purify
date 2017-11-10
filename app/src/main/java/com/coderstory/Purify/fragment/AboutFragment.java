package com.coderstory.Purify.fragment;

import android.didikee.donate.AlipayDonate;
import android.didikee.donate.WeiXinDonate;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.widget.TextView;
import android.widget.Toast;

import com.coderstory.Purify.R;
import com.coderstory.Purify.fragment.base.BaseFragment;
import com.coderstory.Purify.utils.licensesdialog.LicensesDialog;
import com.coderstory.Purify.utils.licensesdialog.licenses.ApacheSoftwareLicense20;
import com.coderstory.Purify.utils.licensesdialog.licenses.GnuGeneralPublicLicense20;
import com.coderstory.Purify.utils.licensesdialog.model.Notice;
import com.coderstory.Purify.utils.licensesdialog.model.Notices;

import java.io.File;
import java.io.InputStream;

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

        $(R.id.os).setOnClickListener(v -> {
            final Notices notices = new Notices();
            notices.addNotice(new Notice("ApacheSoftwareLicense", "", "", new ApacheSoftwareLicense20()));
            notices.addNotice(new Notice("GnuGeneralPublicLicense", "", "", new GnuGeneralPublicLicense20()));

            new LicensesDialog.Builder(getMContext())
                    .setNotices(notices)
                    .build()
                    .show();
        });

        $(R.id.alipay).setOnClickListener(view ->
                donateAlipay("aex087445gnaa6gawjaohe8")
        );
        $(R.id.wechat).setOnClickListener(view ->
                // donateWeixin()
                Toast.makeText(getMContext(), "暂不支持微信捐赠通道(#腊鸡微信)", Toast.LENGTH_LONG).show()
        );
    }

    /* 支付宝支付
     * @param payCode 收款码后面的字符串；例如：收款二维码里面的字符串为 https://qr.alipay.com/stx00187oxldjvyo3ofaw60 ，则
     *payCode = stx00187oxldjvyo3ofaw60
     *注：不区分大小写
     */
    private void donateAlipay(String payCode) {
        boolean hasInstalledAlipayClient = AlipayDonate.hasInstalledAlipayClient(getMContext());
        if (hasInstalledAlipayClient) {
            AlipayDonate.startAlipayClient(getActivity(), payCode);
        }
    }

    /**
     * 需要提前准备好 微信收款码 照片，可通过微信客户端生成
     */
    private void donateWeixin() {
        InputStream weixinQrIs = getResources().openRawResource(R.raw.wechat);
        String qrPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "AndroidDonateSample" + File.separator +
                "didikee_weixin.png";
        WeiXinDonate.saveDonateQrImage2SDCard(qrPath, BitmapFactory.decodeStream(weixinQrIs));
        WeiXinDonate.donateViaWeiXin(getActivity(), qrPath);
    }
}
