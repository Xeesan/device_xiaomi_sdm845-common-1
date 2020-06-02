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
import android.view.MenuItem;
import android.os.Bundle;
import android.util.Log;

import android.view.Menu;
import android.view.MenuInflater;

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
    private static final String DEFAULT_XIAOMI_PROFILE = "ADAPT 3";

    public static final String KEY_KCAL_RED = "kcal_red";
    public static final String KEY_KCAL_GREEN = "kcal_green";
    public static final String KEY_KCAL_BLUE = "kcal_blue";
    public static final String KEY_KCAL_SATURATION = "kcal_saturation";
    public static final String KEY_KCAL_CONTRAST = "kcal_contrast";
    public static final String KEY_KCAL_HUE = "kcal_hue";
    public static final String KEY_KCAL_VALUE = "kcal_value";
    public static final String KEY_KCAL_EYECARE = "kcal_eyecare";

    private SeekBarPreference mKcalRed;
    private SeekBarPreference mKcalBlue;
    private SeekBarPreference mKcalGreen;
    private SeekBarPreference mKcalSaturation;
    private SeekBarPreference mKcalContrast;
    private SeekBarPreference mKcalHue;
    private SeekBarPreference mKcalValue;
    private SeekBarPreference mKcalEyecare;
    private SharedPreferences mPrefs;
    private boolean mEnabled;

    private static final String COLOR_FILE = "/sys/module/msm_drm/parameters/";
    private static final String COLOR_FILE_RED = COLOR_FILE + "/kcal_red";
    private static final String COLOR_FILE_GREEN = COLOR_FILE + "/kcal_green";
    private static final String COLOR_FILE_BLUE = COLOR_FILE + "/kcal_blue";

    private static final String COLOR_FILE_SAT = COLOR_FILE + "/kcal_sat";
    private static final String COLOR_FILE_HUE = COLOR_FILE + "/kcal_hue";
    private static final String COLOR_FILE_VAL = COLOR_FILE + "/kcal_val";
    private static final String COLOR_FILE_CONT = COLOR_FILE + "/kcal_cont";


    private boolean mUnsupported = false;


    private ListPreference mXiaomiProfilePreference;
    private static String mXiaomiProfile;

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

        mKcalRed = (SeekBarPreference) findPreference(KEY_KCAL_RED);
        mKcalRed.setInitValue(mPrefs.getInt(KEY_KCAL_RED, mKcalRed.def));
        mKcalRed.setOnPreferenceChangeListener(this);

        mKcalGreen = (SeekBarPreference) findPreference(KEY_KCAL_GREEN);
        mKcalGreen.setInitValue(mPrefs.getInt(KEY_KCAL_GREEN, mKcalGreen.def));
        mKcalGreen.setOnPreferenceChangeListener(this);

        mKcalBlue = (SeekBarPreference) findPreference(KEY_KCAL_BLUE);
        mKcalBlue.setInitValue(mPrefs.getInt(KEY_KCAL_BLUE, mKcalBlue.def));
        mKcalBlue.setOnPreferenceChangeListener(this);

        mKcalSaturation = (SeekBarPreference) findPreference(KEY_KCAL_SATURATION);
        mKcalSaturation.setInitValue(mPrefs.getInt(KEY_KCAL_SATURATION, mKcalSaturation.def));
        mKcalSaturation.setOnPreferenceChangeListener(this);

        mKcalContrast = (SeekBarPreference) findPreference(KEY_KCAL_CONTRAST);
        mKcalContrast.setInitValue(mPrefs.getInt(KEY_KCAL_CONTRAST, mKcalContrast.def));
        mKcalContrast.setOnPreferenceChangeListener(this);

        mKcalValue = (SeekBarPreference) findPreference(KEY_KCAL_VALUE);
        mKcalValue.setInitValue(mPrefs.getInt(KEY_KCAL_VALUE, mKcalValue.def));
        mKcalValue.setOnPreferenceChangeListener(this);

        mKcalHue = (SeekBarPreference) findPreference(KEY_KCAL_HUE);
        mKcalHue.setInitValue(mPrefs.getInt(KEY_KCAL_HUE, mKcalHue.def));
        mKcalHue.setOnPreferenceChangeListener(this);

        mKcalEyecare = (SeekBarPreference) findPreference(KEY_KCAL_EYECARE);
        mKcalEyecare.setInitValue(mPrefs.getInt(KEY_KCAL_EYECARE, mKcalEyecare.def));
        mKcalEyecare.setOnPreferenceChangeListener(this);

    }

    private boolean isSupported(String file) {
        return true;
    }

    public static void restore(Context context) {
            String profile = PreferenceManager
                .getDefaultSharedPreferences(context).getString(XiaomiDisplay.PREF_XIAOMI_PROFILE, DEFAULT_XIAOMI_PROFILE);
		       int col = Integer.parseInt(profile);
          if (col == 1) {
             runCommand("display ADAPT 1");
         } else if(col == 2) {
             runCommand("display ADAPT 2");
         } else if (col == 3) {
             runCommand("display ADAPT 3");
         } else if (col == 4) {
             runCommand("display ENHANCE 1");
         } else if (col == 5) {
            runCommand("display STANDARD 1");
         } else if (col == 6) {
           int storedRed = PreferenceManager
                   .getDefaultSharedPreferences(context).getInt(XiaomiDisplay.KEY_KCAL_RED, 256);
           int storedGreen = PreferenceManager
                   .getDefaultSharedPreferences(context).getInt(XiaomiDisplay.KEY_KCAL_GREEN, 256);
           int storedBlue = PreferenceManager
                   .getDefaultSharedPreferences(context).getInt(XiaomiDisplay.KEY_KCAL_BLUE, 256);
           int storedSaturation = PreferenceManager
                   .getDefaultSharedPreferences(context).getInt(XiaomiDisplay.KEY_KCAL_SATURATION, 255);
           int storedContrast = PreferenceManager
                   .getDefaultSharedPreferences(context).getInt(XiaomiDisplay.KEY_KCAL_CONTRAST, 255);
           int storedValue = PreferenceManager
                   .getDefaultSharedPreferences(context).getInt(XiaomiDisplay.KEY_KCAL_VALUE, 255);
           int storedHue = PreferenceManager
                   .getDefaultSharedPreferences(context).getInt(XiaomiDisplay.KEY_KCAL_HUE, 0);
           Utils.writeValue(COLOR_FILE_RED, String.valueOf(storedRed));
           Utils.writeValue(COLOR_FILE_GREEN, String.valueOf(storedGreen));
           Utils.writeValue(COLOR_FILE_BLUE, String.valueOf(storedBlue));
           Utils.writeValue(COLOR_FILE_SAT, String.valueOf(storedSaturation));
           Utils.writeValue(COLOR_FILE_CONT, String.valueOf(storedContrast));
           Utils.writeValue(COLOR_FILE_VAL, String.valueOf(storedValue));
           Utils.writeValue(COLOR_FILE_HUE, String.valueOf(storedHue));
           runCommand("display ADAPT 3");
         }  else if (col == 7) {
           int storedeyecare = PreferenceManager
                   .getDefaultSharedPreferences(context).getInt(XiaomiDisplay.KEY_KCAL_EYECARE, 10);
            runCommand("display" + " " + "EYECARE" + " " + String.valueOf(storedeyecare));
         }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.kcal_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.menu_reset:
                reset();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void reset() {
        int red = mKcalRed.reset();
        int green = mKcalGreen.reset();
        int blue = mKcalBlue.reset();
        int saturation = mKcalSaturation.reset();
        int contrast = mKcalContrast.reset();
        int value = mKcalValue.reset();
        int hue = mKcalHue.reset();
        int eyecare = mKcalEyecare.reset();

        mPrefs.edit().putInt(KEY_KCAL_RED, red).commit();
        mPrefs.edit().putInt(KEY_KCAL_GREEN, green).commit();
        mPrefs.edit().putInt(KEY_KCAL_BLUE, blue).commit();
        mPrefs.edit().putInt(KEY_KCAL_SATURATION, saturation).commit();
        mPrefs.edit().putInt(KEY_KCAL_CONTRAST, contrast).commit();
        mPrefs.edit().putInt(KEY_KCAL_VALUE, value).commit();
        mPrefs.edit().putInt(KEY_KCAL_HUE, hue).commit();
        mPrefs.edit().putInt(KEY_KCAL_EYECARE, eyecare).commit();
        Utils.writeValue(COLOR_FILE_RED, Integer.toString(red));
        Utils.writeValue(COLOR_FILE_GREEN, Integer.toString(green));
        Utils.writeValue(COLOR_FILE_BLUE, Integer.toString(blue));
        Utils.writeValue(COLOR_FILE_SAT, Integer.toString(saturation));
        Utils.writeValue(COLOR_FILE_CONT, Integer.toString(contrast));
        Utils.writeValue(COLOR_FILE_VAL, Integer.toString(value));
        Utils.writeValue(COLOR_FILE_HUE, Integer.toString(hue));
        runCommand("display ADAPT 3");
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == mXiaomiProfilePreference) {
                mXiaomiProfile = newValue.toString();
                mPrefs.edit().putString(PREF_XIAOMI_PROFILE, mXiaomiProfile).commit();
                Utils.writeValue(COLOR_FILE_RED, Integer.toString(256));
                Utils.writeValue(COLOR_FILE_GREEN, Integer.toString(256));
                Utils.writeValue(COLOR_FILE_BLUE, Integer.toString(256));
                Utils.writeValue(COLOR_FILE_SAT, Integer.toString(255));
                Utils.writeValue(COLOR_FILE_CONT, Integer.toString(255));
                Utils.writeValue(COLOR_FILE_VAL, Integer.toString(255));
                Utils.writeValue(COLOR_FILE_HUE, Integer.toString(0));
                mKcalRed.setEnabled(false);
                mKcalGreen.setEnabled(false);
          	mKcalBlue.setEnabled(false);
      	    	mKcalSaturation.setEnabled(false);
	       	mKcalContrast.setEnabled(false);
	      	mKcalValue.setEnabled(false);
	      	mKcalHue.setEnabled(false);
	      	mKcalEyecare.setEnabled(false);
                updateColorOptions(mXiaomiProfile);
                return true;
        }  else if (preference == mKcalRed) {
            float val = Float.parseFloat((String) newValue);
            mPrefs.edit().putInt(KEY_KCAL_RED, (int) val).commit();
            String strVal = (String) newValue;
            Utils.writeValue(COLOR_FILE_RED, strVal);
            runCommand("display ADAPT 3");
            return true;
        } else if (preference == mKcalBlue) {
            float val = Float.parseFloat((String) newValue);
            mPrefs.edit().putInt(KEY_KCAL_BLUE, (int) val).commit();
            String strVal = (String) newValue;
            Utils.writeValue(COLOR_FILE_BLUE, strVal);
            runCommand("display ADAPT 3");
            return true;
        } else if (preference == mKcalGreen) {
            float val = Float.parseFloat((String) newValue);
            mPrefs.edit().putInt(KEY_KCAL_GREEN, (int) val).commit();
            String strVal = (String) newValue;
            Utils.writeValue(COLOR_FILE_GREEN, strVal);
            runCommand("display ADAPT 3");
            return true;
        } else if (preference == mKcalSaturation) {
            float val = Float.parseFloat((String) newValue);
            mPrefs.edit().putInt(KEY_KCAL_SATURATION, (int) val).commit();
            String strVal = (String) newValue;
            Utils.writeValue(COLOR_FILE_SAT, strVal);
            runCommand("display ADAPT 3");
            return true;
        } else if (preference == mKcalContrast) {
            float val = Float.parseFloat((String) newValue);
            mPrefs.edit().putInt(KEY_KCAL_CONTRAST, (int) val).commit();
            String strVal = (String) newValue;
            Utils.writeValue(COLOR_FILE_CONT, strVal);
            runCommand("display ADAPT 3");
            return true;
        } else if (preference == mKcalHue) {
            float val = Float.parseFloat((String) newValue);
            mPrefs.edit().putInt(KEY_KCAL_HUE, (int) val).commit();
            String strVal = (String) newValue;
            Utils.writeValue(COLOR_FILE_HUE, strVal);
            runCommand("display ADAPT 3");
            return true;
        } else if (preference == mKcalValue) {
            float val = Float.parseFloat((String) newValue);
            mPrefs.edit().putInt(KEY_KCAL_VALUE, (int) val).commit();
            String strVal = (String) newValue;
            Utils.writeValue(COLOR_FILE_VAL, strVal);
            runCommand("display ADAPT 3");
            return true;
        } else if (preference == mKcalEyecare) {
            float val = Float.parseFloat((String) newValue);
            mPrefs.edit().putInt(KEY_KCAL_EYECARE, (int) val).commit();
            String strVal = (String) newValue;
            runCommand("display" + " " + "EYECARE" + " " + strVal);
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

    private void updateColorOptions(String value) {
          int temp = Integer.parseInt(value);
          if (temp == 1) {
             runCommand("display ADAPT 1");
         } else if(temp == 2) {
             runCommand("display ADAPT 2");
         } else if (temp == 3) {
             runCommand("display ADAPT 3");
         } else if (temp == 4) {
             runCommand("display ENHANCE 1");
         } else if (temp == 5) {
            runCommand("display STANDARD 1");
         } else if (temp == 6) {
            mKcalRed.setEnabled(true);
            mKcalGreen.setEnabled(true);
            mKcalBlue.setEnabled(true);
      	    mKcalSaturation.setEnabled(true);
	    mKcalContrast.setEnabled(true);
	    mKcalValue.setEnabled(true);
	    mKcalHue.setEnabled(true);
           String mRed = String.valueOf(mPrefs.getInt(KEY_KCAL_RED, 256));
           String mGreen = String.valueOf(mPrefs.getInt(KEY_KCAL_GREEN, 256));
           String mBlue = String.valueOf(mPrefs.getInt(KEY_KCAL_BLUE, 256));
           String mSaturation = String.valueOf(mPrefs.getInt(KEY_KCAL_SATURATION, 255));
           String mContrast = String.valueOf(mPrefs.getInt(KEY_KCAL_CONTRAST, 255));
           String mHue = String.valueOf(mPrefs.getInt(KEY_KCAL_HUE, 255));
           String mValue = String.valueOf(mPrefs.getInt(KEY_KCAL_VALUE, 0));
    	   Utils.writeValue(COLOR_FILE_RED, String.valueOf(mRed));
           Utils.writeValue(COLOR_FILE_GREEN, String.valueOf(mGreen));
           Utils.writeValue(COLOR_FILE_BLUE, String.valueOf(mBlue));
           Utils.writeValue(COLOR_FILE_SAT, String.valueOf(mSaturation));
           Utils.writeValue(COLOR_FILE_CONT, String.valueOf(mContrast));
           Utils.writeValue(COLOR_FILE_VAL, String.valueOf(mValue));
           Utils.writeValue(COLOR_FILE_HUE, String.valueOf(mHue));
            runCommand("display ADAPT 3");
         } else if (temp == 7) {
  		     mKcalEyecare.setEnabled(true);
           String mEyecare = String.valueOf(mPrefs.getInt(KEY_KCAL_EYECARE, 10));
            runCommand("display" + " " + "EYECARE" + " " + String.valueOf(mEyecare));
         }

     }
}
