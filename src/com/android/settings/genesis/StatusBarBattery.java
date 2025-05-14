/*
 * Copyright (C) 2023 GenesisOS
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

package com.android.settings.genesis;

import android.content.Context;
import android.os.Bundle;
import android.provider.SearchIndexableResource;
import android.provider.Settings;
import android.text.TextUtils;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceScreen;
import com.android.internal.logging.nano.MetricsProto;
import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settingslib.search.SearchIndexable;
import com.genesis.support.preferences.SystemSettingListPreference;
import java.util.Arrays;
import java.util.List;

@SearchIndexable
public class StatusBarBattery extends SettingsPreferenceFragment
    implements Preference.OnPreferenceChangeListener {

  private static final String CATEGORY_BATTERY = "status_bar_battery_key";
  private static final String STATUS_BAR_BATTERY_STYLE = "status_bar_battery_style";
  private static final String STATUS_BAR_SHOW_BATTERY_PERCENT = "status_bar_battery_percent";
  private static final int STATUS_BAR_BATTERY_STYLE_TEXT = 2;

  private SystemSettingListPreference mStatusBarBatteryShowPercent;
  private PreferenceCategory mStatusBarBatteryCategory;

  @Override
  public void onCreate(Bundle icicle) {
    super.onCreate(icicle);
    addPreferencesFromResource(R.xml.power_usage_summary);

    PreferenceScreen screen = getPreferenceScreen();
    mStatusBarBatteryShowPercent = screen.findPreference(STATUS_BAR_SHOW_BATTERY_PERCENT);
    SystemSettingListPreference statusBarBattery = screen.findPreference(STATUS_BAR_BATTERY_STYLE);

    if (statusBarBattery != null) {
      statusBarBattery.setOnPreferenceChangeListener(this);
      int value = Integer.parseInt(statusBarBattery.getValue());
      enableStatusBarBatteryDependents(value);
    }

    mStatusBarBatteryCategory = screen.findPreference(CATEGORY_BATTERY);
    String iconBlacklist =
        Settings.Secure.getString(getContext().getContentResolver(), "icon_blacklist");

    if (TextUtils.delimitedStringContains(iconBlacklist, ',', "battery")) {
      screen.removePreference(mStatusBarBatteryCategory);
    } else {
      screen.addPreference(mStatusBarBatteryCategory);
    }
  }

  @Override
  public boolean onPreferenceChange(Preference preference, Object newValue) {
    if (STATUS_BAR_BATTERY_STYLE.equals(preference.getKey())) {
      int value = Integer.parseInt((String) newValue);
      enableStatusBarBatteryDependents(value);
    }
    return true;
  }

  private void enableStatusBarBatteryDependents(int style) {
    if (mStatusBarBatteryShowPercent != null) {
      mStatusBarBatteryShowPercent.setEnabled(style != STATUS_BAR_BATTERY_STYLE_TEXT);
    }
  }

  @Override
  public int getMetricsCategory() {
    return MetricsProto.MetricsEvent.GENESIS;
  }

  public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER =
      new BaseSearchIndexProvider() {
        @Override
        public List<SearchIndexableResource> getXmlResourcesToIndex(
            Context context, boolean enabled) {
          SearchIndexableResource sir = new SearchIndexableResource(context);
          sir.xmlResId = R.xml.power_usage_summary;
          return Arrays.asList(sir);
        }
      };
}
