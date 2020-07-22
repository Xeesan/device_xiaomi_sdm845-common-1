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
import com.lineageos.settings.pocopref.utils.FileUtils;

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
    private static final String BOOST_SYSTEM_PROPERTY = "interaction.boost";
    private static final String SYSTEM_PROPERTY_NVT_FW = "persist.baikalos.nvt_fw";    
	
    private Preference mAppprofile;
    private SwitchPreference mEnableBOOST;
    private Preference mPowerSave;
    private Context mContext;
    private SharedPreferences mPreferences;
    private Preference mKcalPref;
    private ListPreference mThermalEngine;	
    private ListPreference mDefaultPerfProfile;
    private ListPreference mDefaultThermProfile;
    private ListPreference mScrOffPerfProfile;
    private ListPreference mIdlePerfProfile;
    private SwitchPreference mNvtFw;	

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
        mKcalPref = findPreference("xiaomi_display");
                mKcalPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                     @Override
                     public boolean onPreferenceClick(Preference preference) {
                         Intent intent = new Intent(getApplicationContext(), XiaomiDisplay.class);
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

            mDefaultPerfProfile = (ListPreference) findPreference("default_perf_profile");
            if( mDefaultPerfProfile != null ) {
                    String profile = getSystemPropertyString("persist.baikal.perf.default","balance");
                    Log.e(TAG, "mDefaultPerfProfile: getProfile=" + profile);
                    mDefaultPerfProfile.setValue(profile);
                    mDefaultPerfProfile.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                      public boolean onPreferenceChange(Preference preference, Object newValue) {
                        try {
                            Log.e(TAG, "mDefaultPerfProfile: setProfile=" + newValue.toString());
			                setSystemPropertyString("persist.baikal.perf.default",newValue.toString());
                        } catch(Exception re) {
                            Log.e(TAG, "onCreate: mDefaultPerfProfile Fatal! exception", re );
                        }
                        return true;
                      }
                    });
                }

            mThermalEngine = (ListPreference) findPreference("default_therm_engine");
            if( mThermalEngine != null ) {
                    String profile = getSystemPropertyString("persist.thermal.engine","stock");
                    Log.e(TAG, "mThermalEngine: getProfile=" + profile);
                    mThermalEngine.setValue(profile);
                    mThermalEngine.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                      public boolean onPreferenceChange(Preference preference, Object newValue) {
                        try {
                            Log.e(TAG, "mThermalEngine: setProfile=" + newValue.toString());
			                setSystemPropertyString("persist.thermal.engine",newValue.toString());
                        } catch(Exception re) {
                            Log.e(TAG, "onCreate: mThermalEngine Fatal! exception", re );
                        }
                        return true;
                      }
                    });
                }

            mDefaultThermProfile = (ListPreference) findPreference("default_therm_profile");
            if( mDefaultThermProfile != null ) {
                    String profile = getSystemPropertyString("persist.baikal.therm.default","balance");
                    Log.e(TAG, "mDefaultThermProfile: getProfile=" + profile);
                    mDefaultThermProfile.setValue(profile);
                    mDefaultThermProfile.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                      public boolean onPreferenceChange(Preference preference, Object newValue) {
                        try {
                            Log.e(TAG, "mDefaultThermProfile: setProfile=" + newValue.toString());
			                setSystemPropertyString("persist.baikal.therm.default",newValue.toString());
                        } catch(Exception re) {
                            Log.e(TAG, "onCreate: mDefaultPerfProfile Fatal! exception", re );
                        }
                        return true;
                      }
                    });
                }

            mScrOffPerfProfile = (ListPreference) findPreference("scr_off_perf_profile");
            if( mScrOffPerfProfile != null ) { 
                    String profile = getSystemPropertyString("persist.baikal.perf.scr_off","battery");
                    Log.e(TAG, "mScrOffPerfProfile: getProfile=" + profile);
                    mScrOffPerfProfile.setValue(profile);
                    mScrOffPerfProfile.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                      public boolean onPreferenceChange(Preference preference, Object newValue) {
                        try {
                            Log.e(TAG, "mScrOffPerfProfile: setProfile=" + newValue.toString());
			                setSystemPropertyString("persist.baikal.perf.scr_off",newValue.toString());
                        } catch(Exception re) {
                            Log.e(TAG, "onCreate: mScrOffPerfProfile Fatal! exception", re );
                        }
                        return true;
                      }
                    });
                }


            mIdlePerfProfile = (ListPreference) findPreference("idle_perf_profile");
            if( mIdlePerfProfile != null ) {
                    String profile = getSystemPropertyString("persist.baikal.perf.idle","battery");
                    Log.e(TAG, "mScrOffPerfProfile: getProfile=" + profile);
                    mIdlePerfProfile.setValue(profile);
                    mIdlePerfProfile.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                      public boolean onPreferenceChange(Preference preference, Object newValue) {
                        try {
                            Log.e(TAG, "mIdlePerfProfile: setProfile=" + newValue.toString());
			                setSystemPropertyString("persist.baikal.perf.idle",newValue.toString());
                        } catch(Exception re) {
                            Log.e(TAG, "onCreate: mIdlePerfProfile Fatal! exception", re );
                        }
                        return true;
                      }
                    });
                }
                
        mNvtFw = (SwitchPreference) findPreference(SYSTEM_PROPERTY_NVT_FW);
        if( mNvtFw != null ) {

                mNvtFw.setChecked(SystemProperties.getBoolean(SYSTEM_PROPERTY_NVT_FW, false));
                mNvtFw.setOnPreferenceChangeListener(this);
            }

}

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
    
     private void setSystemPropertyString(String key, String value) {
        Log.e(TAG, "setSystemPropertyBoolean: key=" + key + ", value=" + value);
        SystemProperties.set(key, value);
    }

    private String getSystemPropertyString(String key, String def) {
        return SystemProperties.get(key,def);
    }

    private boolean getSystemPropertyBoolean(String key) {
        if( SystemProperties.get(key,"0").equals("1") || SystemProperties.get(key,"0").equals("true") ) return true;
	    return false;
    }

    private void setSystemPropertyBoolean(String key, boolean value) {
        String text = value?"1":"0";
        Log.e(TAG, "setSystemPropertyBoolean: key=" + key + ", value=" + value);
        SystemProperties.set(key, text);
    }   
}
