package com.example.pocketcontrol;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {
    // initialize variable for char sequence
    CharSequence charSequence;
    int index = 0;
    long delay = 80;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Launch the layout -> splash.xml
        setContentView(R.layout.splash);


        Thread splashThread = new Thread() {

            public void run() {
                try {
                    // sleep time in milliseconds
                    sleep(1500);

                }  catch(InterruptedException e) {
                    // Trace the error
                    e.printStackTrace();
                } finally
                {
                    // Launch the MainActivity class
                    Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(intent);
                }

            }
        };
        // To Start the thread
        splashThread.start();

        animateText("Pocket Control");
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            TextView appName = (TextView) findViewById(R.id.appNameText);
            appName.setText(charSequence.subSequence(0, index++));

            if (index <= charSequence.length()) {
                // when the index is same as text length run the handler
                handler.postDelayed(runnable, delay);
            }
        }
    };

    public void animateText(CharSequence cs) {
        // set the text
        charSequence = cs;

        // clear index
        index = 0;

        // remove callback
        handler.removeCallbacks(runnable);

        // run handler
        handler.postDelayed(runnable, delay);
    }


}
