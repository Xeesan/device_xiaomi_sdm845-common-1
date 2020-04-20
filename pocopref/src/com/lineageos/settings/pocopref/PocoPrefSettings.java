/*
 *  Poco Extras Settings Module
 *  Made by @shivatejapeddi 2019
 */

package com.lineageos.settings.pocopref;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceScreen;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Date;

import android.util.Log;
import android.os.SystemProperties;
import java.io.*;
import android.widget.Toast;
import android.preference.ListPreference;

import com.lineageos.settings.pocopref.R;

public class PocoPrefSettings extends PreferenceActivity implements OnPreferenceChangeListener {
	private static final boolean DEBUG = false;
	private static final String TAG = "PocoPref";
//	private static final String THERMAL_KEY = "thermal";
//	private static final String THERMAL_SYSTEM_PROPERTY = "thermal.profile";
  private static final String BOOST_SYSTEM_PROPERTY = "interaction.boost";
//    private ListPreference mTHERMAL;
    private Preference mAppprofile;
    private SwitchPreference mEnableBOOST;
    private Preference mPowerSave;
    private Context mContext;
    private SharedPreferences mPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.poco_settings);
        mAppprofile = findPreference("appprofile");
                mAppprofile.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                     @Override
                     public boolean onPreferenceClick(Preference preference) {
                         Intent intent = new Intent(getApplicationContext(), AppProfilesActivity.class);
                         startActivity(intent);
                         return true;
                     }
                });
/*
 *      mPowerSave = findPreference("powersave");
 *             mAppprofile.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
 *                    @Override
                     public boolean onPreferenceClick(Preference preference) {
                         Intent intent = new Intent(getApplicationContext(), PowerSave.class);
                         startActivity(intent);
                         return true;
                     }
                });
*/
        mContext = getApplicationContext();

        mEnableBOOST = (SwitchPreference) findPreference(BOOST_SYSTEM_PROPERTY);
        if( mEnableBOOST != null ) {
            mEnableBOOST.setChecked(SystemProperties.getBoolean(BOOST_SYSTEM_PROPERTY, false));
            mEnableBOOST.setOnPreferenceChangeListener(this);
        }

//        mTHERMAL = (ListPreference) findPreference(THERMAL_KEY);
//        if( mTHERMAL != null ) {
 //           mTHERMAL.setValue(SystemProperties.get(THERMAL_SYSTEM_PROPERTY, "0"));
 //           mTHERMAL.setOnPreferenceChangeListener(this);
 //       }

}

    // Set SPECTRUM
//    private void setTHERMAL(String value) {
//		SystemProperties.set(THERMAL_SYSTEM_PROPERTY, value);
//    }

    private void setEnable(String key, boolean value) {
	if(value) {
 	      SystemProperties.set(key, "1");
    	} else {
    		SystemProperties.set(key, "0");
    	}
    	if (DEBUG) Log.d(TAG, key + " setting changed");
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        final String key = preference.getKey();
        boolean value;
        String strvalue;
    	value = (Boolean) newValue;
    	((SwitchPreference)preference).setChecked(value);
    	setEnable(key,value);
	return true;
    }
}
