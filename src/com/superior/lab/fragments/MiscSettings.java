/*
 * Copyright (C) 2023 SuperiorOS
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

package com.superior.lab.fragments;

import com.android.internal.logging.nano.MetricsProto;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.SystemProperties;
import android.os.UserHandle;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import androidx.preference.Preference.OnPreferenceChangeListener;
import android.provider.Settings;
import com.android.settings.R;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settingslib.search.SearchIndexable;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import com.android.settings.SettingsPreferenceFragment;
import com.superior.lab.fragments.SmartPixels;
import com.superior.lab.fragments.SmartCharging;
import com.android.internal.util.superior.systemUtils;

@SearchIndexable
public class MiscSettings extends SettingsPreferenceFragment implements
        OnPreferenceChangeListener {
    
    private static final String SMART_PIXELS = "smart_pixels";
    private static final String SMART_CHARGING = "smart_charging";
    private static final String SETTINGS_HEADER_IMAGE_RANDOM = "settings_header_image_random";
    
    private static final String SYS_GAMES_SPOOF = "persist.sys.pixelprops.games";
    private static final String SYS_PHOTOS_SPOOF = "persist.sys.pixelprops.gphotos";
    private static final String SYS_NETFLIX_SPOOF = "persist.sys.pixelprops.netflix";

    private Preference mSmartPixels;
    private Preference mSmartCharging;
    private Preference mSettingsHeaderImageRandom;
    private Preference mGcamSpoof;
    
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        addPreferencesFromResource(R.xml.superior_lab_misc);
        final PreferenceScreen prefScreen = getPreferenceScreen();
        
        mSmartCharging = (Preference) prefScreen.findPreference(SMART_CHARGING);
        boolean mSmartChargingSupported = res.getBoolean(
                com.android.internal.R.bool.config_smartChargingAvailable);
        if (!mSmartChargingSupported)
            prefScreen.removePreference(mSmartCharging);    
        
        mGcamSpoof = (Preference) prefScreen.findPreference("persist.sys.pixelprops.gcam");
        boolean isPixel6Series = SystemProperties.get("ro.product.brand").toLowerCase().contains("google") 
                        && SystemProperties.get("ro.product.manufacturer").toLowerCase().contains("google");
        if (!isPixel6Series) {
            prefScreen.removePreference(mGcamSpoof);
        }    
        
        mSettingsHeaderImageRandom = findPreference(SETTINGS_HEADER_IMAGE_RANDOM);
        mSettingsHeaderImageRandom.setOnPreferenceChangeListener(this);
        mSmartPixels = (Preference) prefScreen.findPreference(SMART_PIXELS);
        boolean mSmartPixelsSupported = getResources().getBoolean(
                com.android.internal.R.bool.config_supportSmartPixels);
        if (!mSmartPixelsSupported)
            prefScreen.removePreference(mSmartPixels);
    }
    
    public static void reset(Context mContext) {
        ContentResolver resolver = mContext.getContentResolver();
        SmartPixels.reset(mContext);
        SmartCharging.reset(mContext);
        SystemProperties.set(SYS_GAMES_SPOOF, "false");
        SystemProperties.set(SYS_PHOTOS_SPOOF, "true");
        SystemProperties.set(SYS_NETFLIX_SPOOF, "false");
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object objValue) {
    	Context mContext = getActivity().getApplicationContext();
	ContentResolver resolver = mContext.getContentResolver();
	if (preference == mSettingsHeaderImageRandom) {
            systemUtils.showSettingsRestartDialog(getContext());
            return true;
          }  
        return false;
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.SUPERIOR;
    }
    
    /**
     * For search
     */
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER =
            new BaseSearchIndexProvider(R.xml.superior_lab_misc) {
                @Override
                public List<String> getNonIndexableKeys(Context context) {
                    List<String> keys = super.getNonIndexableKeys(context);
                    
                    boolean mSmartChargingSupported = res.getBoolean(
                            com.android.internal.R.bool.config_smartChargingAvailable);
                    if (!mSmartChargingSupported)
                        keys.add(SMART_CHARGING);
                        

                    boolean mSmartPixelsSupported = context.getResources().getBoolean(
                            com.android.internal.R.bool.config_supportSmartPixels);
                    if (!mSmartPixelsSupported)
                        keys.add(SMART_PIXELS);

                    return keys;
                }
            };
}
