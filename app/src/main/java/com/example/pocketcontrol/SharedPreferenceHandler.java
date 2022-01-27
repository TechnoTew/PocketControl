package com.example.pocketcontrol;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceHandler {
    private static final String SharedPreferenceFileKey = "PocketControl";
    private static final String KEY_SETUP_STATUS = "setup_status";
    private static final String KEY_PREFS_NAME = "user_name";
    private static final String KEY_PREFS_THEME = "user_theme";

    private SharedPreferences _sharedPreference;
    private SharedPreferences.Editor _preferenceEditor;

    public SharedPreferenceHandler(Context context) {
        this._sharedPreference = context.getSharedPreferences(SharedPreferenceFileKey, Context.MODE_PRIVATE);
        this._preferenceEditor = _sharedPreference.edit();
    }

    public Boolean getSetupStatus() {
        return _sharedPreference.getBoolean(KEY_SETUP_STATUS, false);
    }

    public void setSetupStatus(Boolean newSetupStatus) {
        _preferenceEditor.putBoolean(KEY_SETUP_STATUS, newSetupStatus);
        _preferenceEditor.commit();
    }

    public String getUserName() {
        return _sharedPreference.getString(KEY_PREFS_NAME, "Test Name");
    }

    public void setUserName(String newName) {
        _preferenceEditor.putString(KEY_PREFS_NAME, newName);
        _preferenceEditor.commit();
    }

    public String getTheme() {
        return _sharedPreference.getString(KEY_PREFS_THEME, "light");
    }

    public void setTheme(String newTheme) {
        _preferenceEditor.putString(KEY_PREFS_THEME, newTheme);
        _preferenceEditor.commit();
    }

    public void clearSharedPreferences() {
        _preferenceEditor.clear();
        _preferenceEditor.commit();
    }
}
