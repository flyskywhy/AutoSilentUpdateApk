/*
 * Copyright (C) 2006 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package android.content.pm;

import android.content.Intent;
import android.net.Uri;
import android.util.AndroidException;

/**
 * Class for retrieving various kinds of information related to the application
 * packages that are currently installed on the device.
 *
 * You can find this class through {@link android.content.Context#getPackageManager}.
 */
public abstract class PackageManager {

    /**
     * This exception is thrown when a given package, application, or component
     * name can not be found.
     */
    public static class NameNotFoundException extends AndroidException {
        public NameNotFoundException() {
        }

        public NameNotFoundException(String name) {
            super(name);
        }
    }

    /**
     * Flag parameter for {@link #installPackage} to indicate that you want to replace an already
     * installed package, if one exists.
     * @hide
     */
    public static final int INSTALL_REPLACE_EXISTING = 0x00000002;


    /**
     * Retrieve overall information about an application package that is
     * installed on the system.
     * <p>
     * Throws {@link NameNotFoundException} if a package with the given name can
     * not be found on the system.
     *
     * @param packageName The full name (i.e. com.google.apps.contacts) of the
     *            desired package.
     * @param flags Additional option flags. Use any combination of
     *            {@link #GET_ACTIVITIES}, {@link #GET_GIDS},
     *            {@link #GET_CONFIGURATIONS}, {@link #GET_INSTRUMENTATION},
     *            {@link #GET_PERMISSIONS}, {@link #GET_PROVIDERS},
     *            {@link #GET_RECEIVERS}, {@link #GET_SERVICES},
     *            {@link #GET_SIGNATURES}, {@link #GET_UNINSTALLED_PACKAGES} to
     *            modify the data returned.
     * @return Returns a PackageInfo object containing information about the
     *         package. If flag GET_UNINSTALLED_PACKAGES is set and if the
     *         package is not found in the list of installed applications, the
     *         package information is retrieved from the list of uninstalled
     *         applications(which includes installed applications as well as
     *         applications with data directory ie applications which had been
     *         deleted with DONT_DELTE_DATA flag set).
     * @see #GET_ACTIVITIES
     * @see #GET_GIDS
     * @see #GET_CONFIGURATIONS
     * @see #GET_INSTRUMENTATION
     * @see #GET_PERMISSIONS
     * @see #GET_PROVIDERS
     * @see #GET_RECEIVERS
     * @see #GET_SERVICES
     * @see #GET_SIGNATURES
     * @see #GET_UNINSTALLED_PACKAGES
     */

    public abstract PackageInfo getPackageInfo(String packageName, int flags)
            throws NameNotFoundException;
    /**
     * Return a "good" intent to launch a front-door activity in a package,
     * for use for example to implement an "open" button when browsing through
     * packages.  The current implementation will look first for a main
     * activity in the category {@link android.content.Intent#CATEGORY_INFO}, next for a
     * main activity in the category {@link android.content.Intent#CATEGORY_LAUNCHER}, or return
     * null if neither are found.
     *
     * <p>Throws {@link NameNotFoundException} if a package with the given
     * name can not be found on the system.
     *
     * @param packageName The name of the package to inspect.
     *
     * @return Returns either a fully-qualified Intent that can be used to
     * launch the main activity in the package, or null if the package does
     * not contain such an activity.
     */
    public abstract Intent getLaunchIntentForPackage(String packageName);

    /**
     * @hide
     *
     * Install a package. Since this may take a little while, the result will
     * be posted back to the given observer.  An installation will fail if the calling context
     * lacks the {@link android.Manifest.permission#INSTALL_PACKAGES} permission, if the
     * package named in the package file's manifest is already installed, or if there's no space
     * available on the device.
     *
     * @param packageURI The location of the package file to install.  This can be a 'file:' or a
     * 'content:' URI.
     * @param observer An observer callback to get notified when the package installation is
     * complete. {@link android.content.pm.IPackageInstallObserver#packageInstalled(String, int)} will be
     * called when that happens.  observer may be null to indicate that no callback is desired.
     * @param flags - possible values: {@link #INSTALL_FORWARD_LOCK},
     * {@link #INSTALL_REPLACE_EXISTING}, {@link #INSTALL_ALLOW_TEST}.
     * @param installerPackageName Optional package name of the application that is performing the
     * installation. This identifies which market the package came from.
     */
    public abstract void installPackage(
            Uri packageURI, IPackageInstallObserver observer, int flags,
            String installerPackageName);
}