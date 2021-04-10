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
import androidx.preference.PreferenceFragment;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.SwitchPreference;
import com.lineageos.settings.pocopref.SecureSettingListPreference;

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

import com.lineageos.settings.pocopref.R;

public class SystemSettings extends PreferenceFragment implements
        Preference.OnPreferenceChangeListener {	
    public static final String DEFAULT_PERF_PROFILE = "default_perf_profile";
    public static final String PERFORMANCE_SYSTEM_PROPERTY = "persist.baikal.perf.default";
    public static final String DEFAULT_THERMAL_PROFILE = "default_therm_profile";
    public static final String THERMAL_SYSTEM_PROPERTY = "persist.baikal.therm.default";
    public static final String DEFAULT_SCREEN_PROFILE = "default_screen_profile";
    public static final String SCREEN_SYSTEM_PROPERTY = "persist.baikal.perf.scr_off";
    public static final String DEFAULT_IDLE_PROFILE = "default_idle_profile";
    public static final String IDLE_SYSTEM_PROPERTY = "persist.baikal.perf.idle";	

    private Context mContext;
    private Preference mAppprofile;
    private SecureSettingListPreference mDefaultPerfProfile;
    private SecureSettingListPreference mDefaultThermProfile;
    private SecureSettingListPreference mDefaultScreenProfile;
    private SecureSettingListPreference mDefaultIdleProfile;	

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.system_settings, rootKey);	
        mContext = this.getContext();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);

        mAppprofile = findPreference("appprofile");
                mAppprofile.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                     @Override
                     public boolean onPreferenceClick(Preference preference) {
                         Intent intent = new Intent(getActivity().getApplicationContext(), AppProfilesActivity.class);
                         startActivity(intent);
                         return true;
                     }
                });	

            mDefaultPerfProfile = (SecureSettingListPreference) findPreference(DEFAULT_PERF_PROFILE);
            mDefaultPerfProfile.setValue(FileUtils.getStringProp(PERFORMANCE_SYSTEM_PROPERTY, "balance2"));
            mDefaultPerfProfile.setOnPreferenceChangeListener(this);            

            mDefaultThermProfile = (SecureSettingListPreference) findPreference(DEFAULT_THERMAL_PROFILE);
            mDefaultThermProfile.setValue(FileUtils.getStringProp(THERMAL_SYSTEM_PROPERTY, "balance"));
            mDefaultThermProfile.setOnPreferenceChangeListener(this);

            mDefaultScreenProfile = (SecureSettingListPreference) findPreference(DEFAULT_SCREEN_PROFILE);
            mDefaultScreenProfile.setValue(FileUtils.getStringProp(SCREEN_SYSTEM_PROPERTY, "battery"));
            mDefaultScreenProfile.setOnPreferenceChangeListener(this);            

            mDefaultIdleProfile = (SecureSettingListPreference) findPreference(DEFAULT_IDLE_PROFILE);
            mDefaultIdleProfile.setValue(FileUtils.getStringProp(IDLE_SYSTEM_PROPERTY, "battery"));
            mDefaultIdleProfile.setOnPreferenceChangeListener(this);			

     }

    @Override
    public boolean onPreferenceChange(Preference preference, Object value) {
        final String key = preference.getKey();
        switch (key) {

            case DEFAULT_PERF_PROFILE:
                mDefaultPerfProfile.setValue((String) value);
                FileUtils.setStringProp(PERFORMANCE_SYSTEM_PROPERTY, (String) value);
                break;
                
            case DEFAULT_THERMAL_PROFILE:
                mDefaultThermProfile.setValue((String) value);
                FileUtils.setStringProp(THERMAL_SYSTEM_PROPERTY, (String) value);
                break;

            case DEFAULT_SCREEN_PROFILE:
                mDefaultPerfProfile.setValue((String) value);
                FileUtils.setStringProp(SCREEN_SYSTEM_PROPERTY, (String) value);
                break;
                
            case DEFAULT_IDLE_PROFILE:
                mDefaultIdleProfile.setValue((String) value);
                FileUtils.setStringProp(IDLE_SYSTEM_PROPERTY, (String) value);
                break;   				
				
            default:				
                break;
        }
        return true;				
    }
}
