/*
 * Copyright (C) 2024 GenesisOS
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
package com.android.settings.deviceinfo.firmwareversion

import android.content.Context
import android.os.SystemProperties
import androidx.preference.Preference
import com.android.settings.R
import com.android.settingslib.metadata.PreferenceAvailabilityProvider
import com.android.settingslib.metadata.PreferenceMetadata
import com.android.settingslib.metadata.PreferenceSummaryProvider
import com.android.settingslib.preference.PreferenceBinding

class GenesisMaintainerPreference :
    PreferenceMetadata,
    PreferenceAvailabilityProvider,
    PreferenceSummaryProvider,
    PreferenceBinding {

    companion object {
        private const val ROM_PROPERTY = "ro.genesis.maintainer"
    }

    override val key: String
        get() = "genesis_maintainer"

    override val title: Int
        get() = R.string.genesis_maintainer

    override fun isAvailable(context: Context) = context.hasMaintainer()

    override fun getSummary(context: Context) = context.getMaintainer()

    private fun Context.hasMaintainer(): Boolean {
        return SystemProperties.get(ROM_PROPERTY, "").isNotEmpty()
    }

    private fun Context.getMaintainer(): String {
        val maintainer = SystemProperties.get(ROM_PROPERTY, "")
        return maintainer.ifEmpty { getString(R.string.device_info_default) }
    }

    override fun bind(preference: Preference, metadata: PreferenceMetadata) {
        super.bind(preference, metadata)
        preference.isCopyingEnabled = true
    }
}
