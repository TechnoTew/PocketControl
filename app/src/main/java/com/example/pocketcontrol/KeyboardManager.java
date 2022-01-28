package com.example.pocketcontrol;

import static android.content.Context.INPUT_METHOD_SERVICE;

import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class KeyboardManager {
    public static void hideKeyboard(View v) {
        InputMethodManager inputMethodManager = (InputMethodManager)v.getContext().getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(),0);
    }
}
