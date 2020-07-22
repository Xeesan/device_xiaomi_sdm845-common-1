/**
 * Copyright (c) 2019 Qualcomm Technologies, Inc.
 * All Rights Reserved.
 * Confidential and Proprietary - Qualcomm Technologies, Inc.
 */

package com.lineageos.settings.pocopref;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.util.Log;
import android.widget.Toast;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.os.SystemProperties;
import android.util.BoostFramework;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import com.lineageos.settings.pocopref.R;

public class PerformanceModeActivity extends PreferenceActivity implements
        OnPreferenceChangeListener {

    private static final String TAG = PerformanceModeActivity.class.getSimpleName();
    private static final boolean DEBUG = false;    
    private boolean mIsPerformanceModeOn;
    public static final String KEY_PERF_ENABLED = "perf.mode.enabled";
    private Context mAppContext;
    private SharedPreferences mPrefs;
    private SwitchPreference mPerfEnabled;
    private BoostFramework mPerf = null;
    private boolean mIsPerfLockAcquired = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);        
        setContentView(R.layout.activity_performance_mode);

        addPreferencesFromResource(R.xml.perf_mode);

        mAppContext = getApplicationContext();
        final ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mPerfEnabled = (SwitchPreference) findPreference(KEY_PERF_ENABLED);
        if( mPerfEnabled != null ) {        
        mPerfEnabled.setChecked(SystemProperties.getBoolean(KEY_PERF_ENABLED, false));
        mPerfEnabled.setOnPreferenceChangeListener(this);
        }        

    }

    private void setEnable(String key, boolean value) {
	  if(value) {
 	      SystemProperties.set(key, "1");
        if (mPerf == null) {
            mPerf = new BoostFramework();
        }
        if (mPerf != null) {
            mPerf.perfHint(BoostFramework.VENDOR_HINT_PERFORMANCE_MODE, null, Integer.MAX_VALUE, -1);
            mIsPerfLockAcquired = true;
    	} else {
    		SystemProperties.set(key, "0");
            if (mPerf != null && mIsPerfLockAcquired) {
                mPerf.perfLockRelease();
                mIsPerfLockAcquired = false;
            }
    	}
    	if (DEBUG) Log.d(TAG, key + " setting changed");
    }
    
    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue)  {
        final String key = preference.getKey();
        boolean value;
        String strvalue;
        if (DEBUG) Log.d(TAG, "Preference changed.");
       	value = (Boolean) newValue;
      	((SwitchPreference)preference).setChecked(value);
       	setEnable(key,value);
      	return true;        
     	}       

}
