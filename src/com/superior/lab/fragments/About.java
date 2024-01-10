/*
 * Copyright (C) 2023-2024 FebriCahyaa <febricahya12345@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.superior.lab.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;

import com.android.internal.logging.nano.MetricsProto;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settingslib.search.SearchIndexable;

import com.android.settings.R;

import java.util.List;
import java.util.ArrayList;

@SearchIndexable
public class About extends SettingsPreferenceFragment {

    public static final String TAG = "About";

    private String KEY_LC_DONATE = "lc_donate";
    private String KEY_LC_TELEGRAM = "lc_telegram";
    private String KEY_LC_TELEGRAM_CHANNEL = "lc_telegram_channel";

    private Preference mDonate;
    private Preference mTelegramUrl;
    private Preference mTelegramChannelUrl;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.lc_settings_about);

        mDonate = findPreference(KEY_LC_DONATE);
        mTelegramUrl = findPreference(KEY_LC_TELEGRAM);
        mTelegramChannelUrl = findPreference(KEY_LC_TELEGRAM_CHANNEL);
    }

    @Override
    public boolean onPreferenceTreeClick(Preference preference) {
        if (preference == mDonate) {
            launchUrl("https://t.me/usernameaboutme/24");
        } else if (preference == mTelegramUrl) {
            launchUrl("https://t.me/localcomchat");
        } else if (preference == mTelegramChannelUrl) {
            launchUrl("https://t.me/dumpsbuild");
        }

        return super.onPreferenceTreeClick(preference);
    }

    private void launchUrl(String url) {
        Uri uriUrl = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uriUrl);
        getActivity().startActivity(intent);
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.SUPERIOR;
    }

    /**
     * For search
     */
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER =
            new BaseSearchIndexProvider(R.xml.lc_settings_about);
}