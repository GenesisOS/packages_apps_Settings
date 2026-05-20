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
import android.content.Intent
import android.net.Uri
import android.os.SystemProperties
import androidx.preference.Preference
import com.android.settings.R
import com.android.settingslib.metadata.PreferenceAvailabilityProvider
import com.android.settingslib.metadata.PreferenceMetadata
import com.android.settingslib.metadata.PreferenceSummaryProvider
import com.android.settingslib.preference.PreferenceBinding

class GenesisVersionPreference :
    PreferenceMetadata,
    PreferenceAvailabilityProvider,
    PreferenceSummaryProvider,
    PreferenceBinding {

    companion object {
        private const val VERSION_PROPERTY = "ro.genesis.display.version"
        private const val GENESIS_BUILDTYPE = "ro.genesis.releasetype"
        private const val DEVICE_PROPERTY = "ro.product.device"
    }

    override val key: String
        get() = "genesis_version"

    override val title: Int
        get() = R.string.genesis_firmware_version

    override fun intent(context: Context): Intent? =
        Intent(Intent.ACTION_VIEW)
            .setData(Uri.parse(context.getString(R.string.genesis_website_uri)))

    override fun isAvailable(context: Context) = context.hasVersion()

    override fun getSummary(context: Context) = context.getVersion()

    private fun Context.hasVersion(): Boolean {
        val version = SystemProperties.get(VERSION_PROPERTY, "")
        val buildType = SystemProperties.get(GENESIS_BUILDTYPE, "")
        return version.isNotEmpty() && buildType.isNotEmpty()
    }

    private fun Context.getVersion(): String {
        val version = SystemProperties.get(VERSION_PROPERTY, "")
        val buildType = SystemProperties.get(GENESIS_BUILDTYPE, "")
        val device = SystemProperties.get(DEVICE_PROPERTY, "Unknown")

        return if (version.isNotEmpty() && buildType.isNotEmpty()) {
            "$version | $device | $buildType"
        } else {
            getString(R.string.device_info_default)
        }
    }

    override fun bind(preference: Preference, metadata: PreferenceMetadata) {
        super.bind(preference, metadata)
        preference.isCopyingEnabled = true
    }
}
