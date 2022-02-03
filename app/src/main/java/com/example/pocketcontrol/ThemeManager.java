package com.example.pocketcontrol;

import android.content.res.Configuration;
import android.view.View;

public class ThemeManager {
    public static Boolean isDarkTheme(View v) {
        // return false for light theme/ default
        // return true for dark theme

        int nightModeFlags =
                v.getContext().getResources().getConfiguration().uiMode &
                        Configuration.UI_MODE_NIGHT_MASK;
        switch (nightModeFlags) {
            case Configuration.UI_MODE_NIGHT_YES:
                // these colours are for dark theme
                return true;

            default: // can cover undefined type or light type
                // default to light theme
                return false;
        }
    }
}
