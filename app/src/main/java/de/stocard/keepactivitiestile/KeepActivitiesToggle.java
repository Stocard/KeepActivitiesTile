/*
 * Copyright 2016 Stocard GmbH.
 * Copyright 2016 Google Inc.
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

package de.stocard.keepactivitiestile;

import android.content.ContentResolver;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

/**
 * A helper class for working with the system setting for "Don't keep activities".
 * <p>
 * Note that this requires a system level permission, so consumers <b>must</b> run this
 * <code>adb</code> command to use.
 * <p>
 * <code>adb shell pm grant de.stocard.keepactivitiestile android.permission.WRITE_SECURE_SETTINGS</code>
 */
class KeepActivitiesToggle {

    private static final String TAG                  = "KeepActivitiesToggle";
    private static final int    KEEP_ACTIVITIES      = 0;
    private static final int    DONT_KEEP_ACTIVITIES = 1;

    private KeepActivitiesToggle() {
    }

    static
    @DrawableRes
    int getIcon(boolean keepActivities) {
        if (keepActivities) {
            return R.drawable.ic_keep_activities_yes;
        } else {
            return R.drawable.ic_keep_activities_no;
        }
    }

    static boolean getKeepActivities(@NonNull ContentResolver contentResolver) {
        boolean keepActivities = true;
        try {
            keepActivities = Settings.Global.getInt(contentResolver, Settings.Global.ALWAYS_FINISH_ACTIVITIES) == 0;
        } catch (Settings.SettingNotFoundException e) {
            Log.e(TAG, "Could not read Always Finish Activities setting", e);
        }
        return keepActivities;
    }

    static
    @StringRes
    int getLabel(boolean keepActivities) {
        if (keepActivities) {
            return R.string.keep_activities_yes;
        } else {
            return R.string.keep_activities_no;
        }
    }

    static boolean setKeepActivities(@NonNull Context context, boolean keepActivities) {

        try {
            setKeepActivitiesInActivityManager(keepActivities);
            storeSetting(context, keepActivities);
            return true;
        } catch (SecurityException | NoSuchMethodException | IllegalAccessException | InvocationTargetException | ClassNotFoundException e) {
            String message = context.getString(R.string.permission_required_toast);
            Toast.makeText(context.getApplicationContext(), message, Toast.LENGTH_LONG).show();
            Log.e(TAG, message, e);
            return false;
        }
    }

    private static void setKeepActivitiesInActivityManager(
            boolean keepActivities) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        // Due to restrictions related to hidden APIs, need to emulate the line below
        // using reflection:
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            // ActivityManagerNative.getDefault().setAlwaysFinish(keepActivities);
            final Class classActivityManagerNative = Class.forName("android.app.ActivityManagerNative");
            final Method methodGetDefault = classActivityManagerNative.getMethod("getDefault");
            final Method methodSetAlwaysFinish = classActivityManagerNative.getMethod("setAlwaysFinish", new Class[]{boolean.class});
            final Object objectInstance = methodGetDefault.invoke(null);
            methodSetAlwaysFinish.invoke(objectInstance, new Object[]{!keepActivities});
        } else {
            // ActivityManager.getService().setAlwaysFinish(keepActivities);
            final Class classActivityManager = Class.forName("android.app.ActivityManager");
            final Method methodGetService = classActivityManager.getMethod("getService");
            final Object serviceInstance = methodGetService.invoke(null);
            final Method methodSetAlwaysFinish = serviceInstance.getClass().getMethod("setAlwaysFinish", new Class[]{boolean.class});
            methodSetAlwaysFinish.invoke(serviceInstance, new Object[]{!keepActivities});
        }
    }

    private static void storeSetting(@NonNull Context context, boolean keepActivities) {
        // https://developer.android.com/reference/android/provider/Settings.Global.html#ALWAYS_FINISH_ACTIVITIES
        // If not 0, the activity manager will aggressively finish activities and processes as soon as they are no longer needed.
        int value = keepActivities ? KEEP_ACTIVITIES : DONT_KEEP_ACTIVITIES;
        Settings.Global.putInt(
                context.getContentResolver(), Settings.Global.ALWAYS_FINISH_ACTIVITIES, value);
    }
}
