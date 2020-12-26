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

public class Bframework extends PreferenceFragment implements
        Preference.OnPreferenceChangeListener {
    public static final String PREF_LAUNCH = "persist.vendor.perf.launch.boost";
    public static final String PREF_ANIM = "persist.vendor.perf.animation.boost";
    public static final String PREF_INSTALL = "persist.vendor.perf.install.boost";
    public static final String PREF_BGT = "persist.vendor.perf.bgt.enable";
    public static final String PREF_RENDER = "persist.vendor.perf.topAppRenderThreadBoost.enable";
    public static final String PREF_DEX = "persist.vendor.qti.cgroup_follow.dex2oat_only";
    public static final String PREF_CGROUP = "persist.vendor.qti.cgroup_follow.enable";
    public static final String PREF_BENABLE = "persist.vendor.qti.sys.fw.bservice_enable";
    public static final String PREF_BLIMIT = "pref_blimit";
    public static final String PREF_BLIMIT_PROPERTY = "persist.vendor.qti.sys.fw.bservice_limit";    
    public static final String PREF_BAGE = "pref_bage";
    public static final String PREF_BAGE_PROPERTY = "persist.vendor.qti.sys.fw.bservice_age";    
    private static final String PREF_TMEM = "pref_tmem";
    private static final String PREF_TMEM_PROPERTY = "persist.vendor.qti.sys.fw.trim_enable_memory";  
    private static final String PREF_TEMPTYCAC = "pref_temptycac";
    private static final String PREF_TEMPTYCAC_PROPERTY = "persist.vendor.qti.sys.fw.trim_cache_percent";    
    private static final String PREF_TEMPTY = "pref_tempty";
    private static final String PREF_TEMPTY_PROPERTY = "persist.vendor.qti.sys.fw.trim_empty_percent";    
    private static final String PREF_TEMPTYAPP = "pref_temptyapp";
    private static final String PREF_TEMPTYAPP_PROPERTY = "persist.vendor.qti.sys.fw.empty_app_percent";    
    private static final String PREF_TSETTINGS = "persist.vendor.qti.sys.fw.use_trim_settings";
    private static final String PREF_TBAPPS = "pref_tbapps";
    private static final String PREF_TBAPPS_PROPERTY = "persist.vendor.qti.sys.fw.bg_apps_limit";    
    private static final String PREF_TFLING = "persist.vendor.perf.gestureflingboost.enable";	

    private Context mContext;
    private SwitchPreference mBgt;
    private SwitchPreference mLaunch;
    private SwitchPreference mAnim;
    private SwitchPreference mInstall;
    private SwitchPreference mRender;
    private SwitchPreference mDex;
    private SwitchPreference mCgroup;
    private SwitchPreference mBenable;
    private SwitchPreference mFling;
    private SwitchPreference mTsettings;
    private CustomSeekBarPreference mBlimit;	
    private CustomSeekBarPreference mTemptycac;
    private CustomSeekBarPreference mTempty;	
    private CustomSeekBarPreference mTemptyapp;
    private CustomSeekBarPreference mTbapps;
    private SecureSettingListPreference mBage;
    private SecureSettingListPreference mTmem;	
    private SharedPreferences mPrefs;  

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.bframework_settings, rootKey);	
        mContext = this.getContext();
        mPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);

            mLaunch = (SwitchPreference) findPreference(PREF_LAUNCH);
            mLaunch.setChecked(SystemProperties.getBoolean(PREF_LAUNCH, true));
            mLaunch.setOnPreferenceChangeListener(this);
            
            mAnim = (SwitchPreference) findPreference(PREF_ANIM);
            mAnim.setChecked(SystemProperties.getBoolean(PREF_ANIM, true));
            mAnim.setOnPreferenceChangeListener(this);
            
            mInstall = (SwitchPreference) findPreference(PREF_INSTALL);
            mInstall.setChecked(SystemProperties.getBoolean(PREF_INSTALL, true));
            mInstall.setOnPreferenceChangeListener(this);
                                    
            mBgt = (SwitchPreference) findPreference(PREF_BGT);
            mBgt.setChecked(SystemProperties.getBoolean(PREF_BGT, true));
            mBgt.setOnPreferenceChangeListener(this);
			
            mRender = (SwitchPreference) findPreference(PREF_RENDER);
            mRender.setChecked(SystemProperties.getBoolean(PREF_RENDER, true));
            mRender.setOnPreferenceChangeListener(this);
            
            mFling = (SwitchPreference) findPreference(PREF_TFLING);
            mFling.setChecked(SystemProperties.getBoolean(PREF_TFLING, true));
            mFling.setOnPreferenceChangeListener(this);

            mTbapps = (CustomSeekBarPreference) findPreference(PREF_TBAPPS);
            mTbapps.setValue(mPrefs.getInt(PREF_TBAPPS, mTbapps.getValue()));                
            mTbapps.setOnPreferenceChangeListener(this);				

     }

    private void setSystemPropertyBoolean(String key, boolean value) {
    	if(value) {
 	      SystemProperties.set(key, "true");
    	} else {
    		SystemProperties.set(key, "false");
    	}
    }

    private void setSystemPropertyInt(String key, int value) {
        String text = Integer.toString(value);
        SystemProperties.set(key, text);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object value) {
        final String key = preference.getKey();
        switch (key) {

            case PREF_LAUNCH:
                ((SwitchPreference)preference).setChecked((Boolean) value);
                setSystemPropertyBoolean(PREF_LAUNCH, (Boolean) value);
                break;
                
            case PREF_ANIM:
                ((SwitchPreference)preference).setChecked((Boolean) value);
                setSystemPropertyBoolean(PREF_ANIM, (Boolean) value);
                break;
                
            case PREF_INSTALL:
                ((SwitchPreference)preference).setChecked((Boolean) value);
                setSystemPropertyBoolean(PREF_INSTALL, (Boolean) value);
                break;
                                                
            case PREF_BGT:
                ((SwitchPreference)preference).setChecked((Boolean) value);
                setSystemPropertyBoolean(PREF_BGT, (Boolean) value);
                break;

            case PREF_RENDER:
                ((SwitchPreference)preference).setChecked((Boolean) value);
                setSystemPropertyBoolean(PREF_RENDER, (Boolean) value);
                break;

            case PREF_BENABLE:
                ((SwitchPreference)preference).setChecked((Boolean) value);
                setSystemPropertyBoolean(PREF_BENABLE, (Boolean) value);
                break;

            case PREF_TFLING:
                ((SwitchPreference)preference).setChecked((Boolean) value);
                setSystemPropertyBoolean(PREF_TFLING, (Boolean) value);
                break;			
                             
            case PREF_TBAPPS:
                mPrefs.edit().putInt(PREF_TBAPPS, (int) value).apply();
                setSystemPropertyInt(PREF_TBAPPS_PROPERTY, (int) value);				
                break;					
				
            default:				
                break;
        }
        return true;				
    }
}
