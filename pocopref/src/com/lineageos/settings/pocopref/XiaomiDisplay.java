 /*
* Copyright (C) 2016 The OmniROM Project
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 2 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program. If not, see <http://www.gnu.org/licenses/>.
*
*/
package com.lineageos.settings.pocopref;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.ListPreference;
import android.preference.SwitchPreference;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.os.Bundle;
import android.util.Log;

import android.app.ActionBar;
import com.lineageos.settings.pocopref.utils.Shell;
import com.lineageos.settings.pocopref.utils.SeekBarPreference;
import com.lineageos.settings.pocopref.R;

import java.util.ArrayList;
import java.util.List;


public class XiaomiDisplay extends PreferenceActivity implements
        OnPreferenceChangeListener {

    private static final String TAG = Shell.class.getSimpleName();

    private static final String XIAOMI_DISPLAY_PROFILE = "xiaomi_display_profile";
    private static final String PREF_XIAOMI_PROFILE = "xiaomi_profile";
    private static final String PREF_GAME_PROFILE = "xiaomi_profile";
    private static final String DEFAULT_XIAOMI_PROFILE = "ADAPT 3";
    public static final String COLOR_TEMP = "color_temp";

    private boolean mUnsupported = false;

    private SharedPreferences mPrefs;

    private ListPreference mXiaomiProfilePreference;
    private static String mXiaomiProfile;
    private SeekBarPreference mColorTemp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate()");

        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        mXiaomiProfile = mPrefs.getString(PREF_XIAOMI_PROFILE,DEFAULT_XIAOMI_PROFILE);
        Log.d(TAG, "onCreate() profile=" + mXiaomiProfile);
        
        getActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.xiaomi_display);

        ImageView imageView = (ImageView) findViewById(R.id.calibration_pic);
        imageView.setImageResource(R.drawable.calibration_png);

        addPreferencesFromResource(R.xml.xiaomi_display);

        mXiaomiProfilePreference = (ListPreference) findPreference(XIAOMI_DISPLAY_PROFILE);
        if( mXiaomiProfilePreference == null ) {
            Log.d(TAG, "onCreate() mXiaomiProfilePreference=null");
            mUnsupported = true;
            return;
        }

        mXiaomiProfilePreference.setValue(mXiaomiProfile);
        mXiaomiProfilePreference.setOnPreferenceChangeListener(this);

        mColorTemp = (SeekBarPreference) findPreference(COLOR_TEMP);
        mColorTemp.setInitValue(mPrefs.getInt(COLOR_TEMP, mColorTemp.def));
        mColorTemp.setOnPreferenceChangeListener(this);


    }

    private boolean isSupported(String file) {
        return true;
    }

    public static void restore(Context context) {
        try {
            String profile = PreferenceManager
                .getDefaultSharedPreferences(context).getString(XiaomiDisplay.PREF_XIAOMI_PROFILE, DEFAULT_XIAOMI_PROFILE);
            runCommand("display" + " " + profile);

        } catch(Exception e) {
        }
    }


    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == mXiaomiProfilePreference) {
                mXiaomiProfile = newValue.toString();
                mPrefs.edit().putString(PREF_XIAOMI_PROFILE, mXiaomiProfile).commit();
                runCommand("display ENHANCE 1");
                setCurrentProfile();
            return true;
        } else if (preference == mColorTemp) {
          float val = Float.parseFloat((String) newValue);
           mPrefs.edit().putInt(COLOR_TEMP, (int) val).commit();
           String strVal = ((String) newValue);
           runCommand("display" + " " + "ADAPT" + " " + strVal);
      return true;
      }
      return false;
  }

    private static void setCurrentProfile() {
        runCommand("display" + " " + mXiaomiProfile);
    }
	
    private static void runCommand(String command) {
        Log.d(TAG, "setCurrentProfile() cmd=\'" + command + "\'");
        ArrayList<String> output = Shell.runWithShell(command);
        for (String line : output) {
            Log.d(TAG, "setCurrentProfile() output:" + line);
        }
    }
}