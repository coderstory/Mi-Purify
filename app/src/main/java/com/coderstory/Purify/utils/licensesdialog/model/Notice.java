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

package com.coderstory.Purify.utils.licensesdialog.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.coderstory.Purify.utils.licensesdialog.licenses.License;

public class Notice implements Parcelable {

    public static Creator<Notice> CREATOR = new Creator<Notice>() {
        public Notice createFromParcel(final Parcel source) {
            return new Notice(source);
        }

        public Notice[] newArray(final int size) {
            return new Notice[size];
        }
    };
    private String mName;
    private String mUrl;
    private String mCopyright;

    //
    private License mLicense;

    public Notice() {
    }

    // Setter / Getter

    public Notice(final String name, final String url, final String copyright, final License license) {
        mName = name;
        mUrl = url;
        mCopyright = copyright;
        mLicense = license;
    }

    private Notice(final Parcel in) {
        mName = in.readString();
        mUrl = in.readString();
        mCopyright = in.readString();
        mLicense = (License) in.readSerializable();
    }

    public String getName() {
        return mName;
    }

    public void setName(final String name) {
        mName = name;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(final String url) {
        mUrl = url;
    }

    public String getCopyright() {
        return mCopyright;
    }

    public void setCopyright(final String copyright) {
        mCopyright = copyright;
    }

    // Parcelable

    public License getLicense() {
        return mLicense;
    }

    public void setLicense(final License license) {
        mLicense = license;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeString(mName);
        dest.writeString(mUrl);
        dest.writeString(mCopyright);
        dest.writeSerializable(mLicense);
    }
}
