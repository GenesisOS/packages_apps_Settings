/*
 * SPDX-FileCopyrightText: 2014 GenesisOS
 * SPDX-License-Identifier: Apache-2.0
 */

package com.android.settings.genesis.spoofing;

import android.os.Bundle;

import com.android.internal.logging.nano.MetricsProto;
import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

public class SpoofSettings extends SettingsPreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.spoofing_settings);
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.GENESIS;
    }
}