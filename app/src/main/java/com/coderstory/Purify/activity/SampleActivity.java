/*
 * Copyright 2013 Philip Schiffer
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.coderstory.Purify.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.coderstory.Purify.R;
import com.coderstory.Purify.utils.licensesdialog.LicensesDialog;
import com.coderstory.Purify.utils.licensesdialog.LicensesDialogFragment;
import com.coderstory.Purify.utils.licensesdialog.licenses.ApacheSoftwareLicense20;
import com.coderstory.Purify.utils.licensesdialog.licenses.GnuLesserGeneralPublicLicense21;
import com.coderstory.Purify.utils.licensesdialog.licenses.License;
import com.coderstory.Purify.utils.licensesdialog.model.Notice;
import com.coderstory.Purify.utils.licensesdialog.model.Notices;


public class SampleActivity extends FragmentActivity {

    // ==========================================================================================================================
    // Android Lifecycle
    // ==========================================================================================================================

    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);
    }

    // ==========================================================================================================================
    // Public API
    // ==========================================================================================================================

    public void onSingleClick(final View view) {
        final String name = "LicensesDialog";
        final String url = "http://psdev.de";
        final String copyright = "Copyright 2013 Philip Schiffer <admin@psdev.de>";
        final License license = new ApacheSoftwareLicense20();
        final Notice notice = new Notice(name, url, copyright, license);
        new LicensesDialog.Builder(this)
            .setNotices(notice)
            .build()
            .show();
    }

    public void onMultipleClick(final View view) {
        new LicensesDialog.Builder(this)
            .setNotices(R.raw.notices)
            .build()
            .show();
    }

    public void onMultipleIncludeOwnClick(final View view) {
        new LicensesDialog.Builder(this)
            .setNotices(R.raw.notices)
            .setIncludeOwnLicense(true)
            .build()
            .show();
    }

    public void onMultipleProgrammaticClick(final View view) {
        final Notices notices = new Notices();
        notices.addNotice(new Notice("Test 1", "http://example.org", "Example Person", new ApacheSoftwareLicense20()));
        notices.addNotice(new Notice("Test 2", "http://example.org", "Example Person 2", new GnuLesserGeneralPublicLicense21()));

        new LicensesDialog.Builder(this)
            .setNotices(notices)
            .setIncludeOwnLicense(true)
            .build()
            .show();
    }

    public void onSingleFragmentClick(final View view) {
        final String name = "LicensesDialog";
        final String url = "http://psdev.de";
        final String copyright = "Copyright 2013 Philip Schiffer <admin@psdev.de>";
        final License license = new ApacheSoftwareLicense20();
        final Notice notice = new Notice(name, url, copyright, license);

        final LicensesDialogFragment fragment = new LicensesDialogFragment.Builder(this)
            .setNotice(notice)
            .setIncludeOwnLicense(false)
            .build();

        fragment.show(getSupportFragmentManager(), null);
    }

    public void onMultipleFragmentClick(final View view) throws Exception {
        final LicensesDialogFragment fragment = new LicensesDialogFragment.Builder(this)
            .setNotices(R.raw.notices)
            .build();

        fragment.show(getSupportFragmentManager(), null);
    }

    public void onMultipleIncludeOwnFragmentClick(final View view) throws Exception {
        final LicensesDialogFragment fragment = new LicensesDialogFragment.Builder(this)
            .setNotices(R.raw.notices)
            .setShowFullLicenseText(false)
            .setIncludeOwnLicense(true)
            .build();

        fragment.show(getSupportFragmentManager(), null);
    }

    public void onMultipleProgrammaticFragmentClick(final View view) {
        final Notices notices = new Notices();
        notices.addNotice(new Notice("Test 1", "http://example.org", "Example Person", new ApacheSoftwareLicense20()));
        notices.addNotice(new Notice("Test 2", "http://example.org", "Example Person 2", new GnuLesserGeneralPublicLicense21()));

        final LicensesDialogFragment fragment = new LicensesDialogFragment.Builder(this)
            .setNotices(notices)
            .setShowFullLicenseText(false)
            .setIncludeOwnLicense(true)
            .build();

        fragment.show(getSupportFragmentManager(), null);
    }

    public void onCustomThemeClick(final View view) {
        new LicensesDialog.Builder(this)
            .setNotices(R.raw.notices)
            .setIncludeOwnLicense(true)
            //.setThemeResourceId(R.style.custom_theme)
           // .setDividerColorId(R.color.custom_divider_color)
            .build()
            .show();
    }

    public void onCustomThemeFragmentClick(final View view) throws Exception {
        final LicensesDialogFragment fragment = new LicensesDialogFragment.Builder(this)
            .setNotices(R.raw.notices)
            .setShowFullLicenseText(false)
            .setIncludeOwnLicense(true)
           // .setThemeResourceId(R.style.custom_theme)
            //.setDividerColorRes(R.color.custom_divider_color)
            .build();

        fragment.show(getSupportFragmentManager(), null);
    }
}