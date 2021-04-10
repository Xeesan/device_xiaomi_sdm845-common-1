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
import android.os.SELinux;
import android.os.Handler;	
import androidx.preference.PreferenceFragment;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.SwitchPreference;
import com.lineageos.settings.pocopref.kcal.KCalSettingsActivity;
import com.lineageos.settings.pocopref.BframeworkActivity;
import com.lineageos.settings.pocopref.SecureSettingListPreference;
import com.lineageos.settings.pocopref.SuShell;
import com.lineageos.settings.pocopref.SuTask;

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

public class PocoPrefSettings extends PreferenceFragment implements
        Preference.OnPreferenceChangeListener {
	private static final boolean DEBUG = false;
	private static final String TAG = "PocoPref";
    public static final String PREF_DEVICE_KCAL = "device_kcal";
    public static final String PREF_BFRAMEWORK = "device_bframework";	
    public static final String CATEGORY_DISPLAY = "display";    
    private static final String SYSTEM_PROPERTY_NVT_FW = "persist.nvt_fw";
    private static final String SYSTEM_PROPERTY_NVT_ESD = "persist.nvt_esd";
    private static final String SYSTEM_PROPERTY_DOLBY = "persist.dolby.enable";
    private static final String SELINUX_CATEGORY = "selinux";
    private static final String PREF_SELINUX_MODE = "selinux_mode";
    private static final String PREF_SELINUX_PERSISTENCE = "selinux_persistence";
    
    private Context mContext;
    private Preference mSystemSettings;
    private Preference mKcal;
    private Preference mBframework;	
    private SwitchPreference mNvtFw;
    private SwitchPreference mNvtESD;
    private SwitchPreference mSelinuxMode;
    private SwitchPreference mSelinuxPersistence;    

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.poco_settings, rootKey);	
        mContext = this.getContext();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);

        PreferenceCategory displayCategory = (PreferenceCategory) findPreference(CATEGORY_DISPLAY);

        mKcal = findPreference(PREF_DEVICE_KCAL);

        mKcal.setOnPreferenceClickListener(preference -> {
            Intent intent = new Intent(getActivity().getApplicationContext(), KCalSettingsActivity.class);
            startActivity(intent);
            return true;
        });

        mSystemSettings = findPreference("systemsettings");
                mSystemSettings.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                     @Override
                     public boolean onPreferenceClick(Preference preference) {
                         Intent intent = new Intent(getActivity().getApplicationContext(), SystemSettingsActivity.class);
                         startActivity(intent);
                         return true;
                     }
                });


        mBframework = findPreference(PREF_BFRAMEWORK);

        mBframework.setOnPreferenceClickListener(preference -> {
            Intent intent = new Intent(getActivity().getApplicationContext(), BframeworkActivity.class);
            startActivity(intent);
            return true;
        });	        	
		
//            mNvtFw = (SwitchPreference) findPreference(SYSTEM_PROPERTY_NVT_FW);
//            mNvtFw.setChecked(SystemProperties.getBoolean(SYSTEM_PROPERTY_NVT_FW, false));
//            mNvtFw.setOnPreferenceChangeListener(this);

//            mNvtESD = (SwitchPreference) findPreference(SYSTEM_PROPERTY_NVT_ESD);
//            mNvtESD.setChecked(SystemProperties.getBoolean(SYSTEM_PROPERTY_NVT_ESD, false));
//            mNvtESD.setOnPreferenceChangeListener(this);

 
         // SELinux
        Preference selinuxCategory = findPreference(SELINUX_CATEGORY);
        mSelinuxMode = (SwitchPreference) findPreference(PREF_SELINUX_MODE);
        mSelinuxMode.setChecked(SELinux.isSELinuxEnforced());
        mSelinuxMode.setOnPreferenceChangeListener(this);

        mSelinuxPersistence =
        (SwitchPreference) findPreference(PREF_SELINUX_PERSISTENCE);
        mSelinuxPersistence.setOnPreferenceChangeListener(this);
        mSelinuxPersistence.setChecked(getContext()
        .getSharedPreferences("selinux_pref", Context.MODE_PRIVATE)
        .contains(PREF_SELINUX_MODE));
                   
     }

    private void setSystemPropertyBoolean(String key, boolean value) {
    	if(value) {
 	      SystemProperties.set(key, "true");
    	} else {
    		SystemProperties.set(key, "false");
    	}
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object value) {
        final String key = preference.getKey();      
        switch (key) {             

//            case SYSTEM_PROPERTY_NVT_FW:
//                ((SwitchPreference)preference).setChecked((Boolean) value);
//                setSystemPropertyBoolean(SYSTEM_PROPERTY_NVT_FW, (Boolean) value);
//                break;

//            case SYSTEM_PROPERTY_NVT_ESD:
//                ((SwitchPreference)preference).setChecked((Boolean) value);
//                setSystemPropertyBoolean(SYSTEM_PROPERTY_NVT_ESD, (Boolean) value);
//                break;

                
            case PREF_SELINUX_MODE:
                  if (preference == mSelinuxMode) {
                  boolean enabled = (Boolean) value;
                  new SwitchSelinuxTask(getActivity()).execute(enabled);
                  setSelinuxEnabled(enabled, mSelinuxPersistence.isChecked());
                  return true;
                } else if (preference == mSelinuxPersistence) {
                  setSelinuxEnabled(mSelinuxMode.isChecked(), (Boolean) value);
                  return true;
                }
                break;                
                                
            default:				
                break;
        }
        return true;				
    }

        private void setSelinuxEnabled(boolean status, boolean persistent) {
          SharedPreferences.Editor editor = getContext()
              .getSharedPreferences("selinux_pref", Context.MODE_PRIVATE).edit();
          if (persistent) {
            editor.putBoolean(PREF_SELINUX_MODE, status);
          } else {
            editor.remove(PREF_SELINUX_MODE);
          }
          editor.apply();
          mSelinuxMode.setChecked(status);
        }

        private class SwitchSelinuxTask extends SuTask<Boolean> {
          public SwitchSelinuxTask(Context context) {
            super(context);
          }
          @Override
          protected void sudoInBackground(Boolean... params) throws SuShell.SuDeniedException {
            if (params.length != 1) {
              Log.e(TAG, "SwitchSelinuxTask: invalid params count");
              return;
            }
            if (params[0]) {
              SuShell.runWithSuCheck("setenforce 1");
            } else {
              SuShell.runWithSuCheck("setenforce 0");
            }
          }

          @Override
          protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (!result) {
              // Did not work, so restore actual value
              setSelinuxEnabled(SELinux.isSELinuxEnforced(), mSelinuxPersistence.isChecked());
            }
          }
        }      
}
