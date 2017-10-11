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

package com.coderstory.Purify.utils.licensesdialog;


import com.coderstory.Purify.utils.licensesdialog.licenses.ApacheSoftwareLicense20;
import com.coderstory.Purify.utils.licensesdialog.licenses.GnuGeneralPublicLicense20;
import com.coderstory.Purify.utils.licensesdialog.licenses.License;

import java.util.HashMap;
import java.util.Map;

public final class LicenseResolver {

    private static final int INITIAL_LICENSES_COUNT = 4;
    private static final Map<String, License> sLicenses = new HashMap<String, License>(INITIAL_LICENSES_COUNT);

    static {
        registerDefaultLicenses();
    }

    private LicenseResolver() {
    }

    static void registerDefaultLicenses() {
        sLicenses.clear();
        registerLicense(new ApacheSoftwareLicense20());
        registerLicense(new GnuGeneralPublicLicense20());
    }

    /**
     * Register an additional license.
     *
     * @param license the license to register
     */
    public static void registerLicense(final License license) {
        sLicenses.put(license.getName(), license);
    }

    /**
     * Get a license by name
     *
     * @param license license name
     * @return License
     * @throws IllegalStateException when unknown license is requested
     */
    public static License read(final String license) {
        final String trimmedLicense = license.trim();
        if (sLicenses.containsKey(trimmedLicense)) {
            return sLicenses.get(trimmedLicense);
        } else {
            throw new IllegalStateException(String.format("no such license available: %s, did you forget to register it?", trimmedLicense));
        }
    }
}
