/*
 *  Copyright (C) 2016 The Dirty Unicorns Project
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.superior.lab.fragments;

import static android.os.UserHandle.USER_CURRENT;

import android.content.ContentResolver;
import android.content.res.Resources;
import android.content.om.IOverlayManager;
import android.content.Context;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.UserHandle;
import android.os.Vibrator;
import androidx.preference.PreferenceCategory;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import androidx.preference.Preference.OnPreferenceChangeListener;
import androidx.preference.SwitchPreference;
import android.provider.Settings;

import com.android.settings.R;

import com.android.settings.SettingsPreferenceFragment;
import com.superior.support.preferences.SystemSettingListPreference;

import com.android.internal.logging.nano.MetricsProto;

public class ButtonSettings extends SettingsPreferenceFragment implements
        Preference.OnPreferenceChangeListener{
        
       private static final String VOLUMEBAR_STYLES = "VOLUME_BAR_STYLES";

    private static final String VOLUMEBAR_OVERLAY_STYLE1 = "com.custom.overlay.systemui.volume1";
    private static final String VOLUMEBAR_OVERLAY_STYLE2 = "com.custom.overlay.systemui.volume2"; 
    private static final String VOLUMEBAR_OVERLAY_STYLE3 = "com.custom.overlay.systemui.volume3";        
    private static final String VOLUMEBAR_OVERLAY_STYLE4 = "com.custom.overlay.systemui.volume4"; 
    private static final String VOLUMEBAR_OVERLAY_STYLE5 = "com.custom.overlay.systemui.volume5";
    
    private SystemSettingListPreference CustomVolumeStyle;
    private IOverlayManager mOverlayService; 
    private Context mContext;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        addPreferencesFromResource(R.xml.superior_lab_button);

        final PreferenceScreen prefScreen = getPreferenceScreen();
        mContext = getActivity();
        final PreferenceScreen screen = getPreferenceScreen();

        mOverlayService = IOverlayManager.Stub
                .asInterface(ServiceManager.getService(Context.OVERLAY_SERVICE));
                
        CustomVolumeStyle = (SystemSettingListPreference) findPreference("VOLUME_BAR_STYLES");
        int CuVolumeStyle = Settings.System.getIntForUser(getContentResolver(),
                "VOLUME_BAR_STYLES", 0, UserHandle.USER_CURRENT);
        int valueIndexvol = CustomVolumeStyle.findIndexOfValue(String.valueOf(CuVolumeStyle));
        CustomVolumeStyle.setValueIndex(valueIndexvol >= 0 ? valueIndexvol : 0);
        CustomVolumeStyle.setSummary(CustomVolumeStyle.getEntry());
        CustomVolumeStyle.setOnPreferenceChangeListener(this);
    }

    public boolean onPreferenceChange(Preference preference, Object newValue) {
        ContentResolver resolver = getActivity().getContentResolver();
        
        if (preference == CustomVolumeStyle) {
            int VolumeStyle = Integer.valueOf((String) newValue);
            Settings.System.putIntForUser(getContentResolver(),
                    "VOLUME_BAR_STYLES", VolumeStyle, UserHandle.USER_CURRENT);
            CustomVolumeStyle.setSummary(CustomVolumeStyle.getEntries()[VolumeStyle]);
                if (VolumeStyle == 0) {
                   try {
                      mOverlayService.setEnabled(VOLUMEBAR_OVERLAY_STYLE1, false, USER_CURRENT);
                      mOverlayService.setEnabled(VOLUMEBAR_OVERLAY_STYLE2, false, USER_CURRENT);
                      mOverlayService.setEnabled(VOLUMEBAR_OVERLAY_STYLE3, false, USER_CURRENT);
                      mOverlayService.setEnabled(VOLUMEBAR_OVERLAY_STYLE4, false, USER_CURRENT);
                      mOverlayService.setEnabled(VOLUMEBAR_OVERLAY_STYLE5, false, USER_CURRENT);     
                   } catch (RemoteException re) {
                      throw re.rethrowFromSystemServer();
                   }
               } else if (VolumeStyle == 1) {
                   try {
                      mOverlayService.setEnabledExclusiveInCategory(VOLUMEBAR_OVERLAY_STYLE1, USER_CURRENT);   
                   } catch (RemoteException re) {
                      throw re.rethrowFromSystemServer();
                   }
               } else if (VolumeStyle == 2) {
                   try {
                      mOverlayService.setEnabledExclusiveInCategory(VOLUMEBAR_OVERLAY_STYLE2, USER_CURRENT);   
                   } catch (RemoteException re) {
                      throw re.rethrowFromSystemServer();
                   }
                } else if (VolumeStyle == 3) {
                   try {
                      mOverlayService.setEnabledExclusiveInCategory(VOLUMEBAR_OVERLAY_STYLE3, USER_CURRENT);     
                   } catch (RemoteException re) {
                      throw re.rethrowFromSystemServer();
                   }
                } else if (VolumeStyle == 4) {
                   try {
                      mOverlayService.setEnabledExclusiveInCategory(VOLUMEBAR_OVERLAY_STYLE4, USER_CURRENT);     
                   } catch (RemoteException re) {
                      throw re.rethrowFromSystemServer();
                   }
                } else if (VolumeStyle == 5) {
                   try {
                      mOverlayService.setEnabledExclusiveInCategory(VOLUMEBAR_OVERLAY_STYLE5, USER_CURRENT);     
                   } catch (RemoteException re) {
                      throw re.rethrowFromSystemServer();
                   }
                }    
            return true;
          }
        return false;
    }
    
    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.SUPERIOR;
    }

}
