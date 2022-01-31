package com.example.pocketcontrol;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import java.util.Random;

public class AdHandler {
    private InterstitialAd advertisement;
    private Activity activity;

    public AdHandler(Activity currentActivity) {
        this.activity = currentActivity;
    }

    public void initialize() {
        // initialize ad module
        MobileAds.initialize(activity, initializationStatus -> {
        });
    }

    public void showAd() {
        // load ad
        AdRequest adRequest = new AdRequest.Builder().build();

        advertisement.load(activity,"ca-app-pub-3940256099942544/1033173712", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        advertisement = interstitialAd;
                        Log.i("", "onAdLoaded");

                        advertisement.setFullScreenContentCallback(new FullScreenContentCallback(){
                            @Override
                            public void onAdDismissedFullScreenContent() {
                                // Called when fullscreen content is dismissed.
                                Log.d("TAG", "The ad was dismissed.");
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(AdError adError) {
                                // Called when fullscreen content failed to show.
                                Log.d("TAG", "The ad failed to show.");
                            }

                            @Override
                            public void onAdShowedFullScreenContent() {
                                // Called when fullscreen content is shown.
                                // Make sure to set your reference to null so you don't
                                // show it a second time.
                                advertisement = null;
                                Log.d("TAG", "The ad was shown.");
                            }
                        });

                        if (advertisement != null) {
                            advertisement.show(activity);
                        }

                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        Log.i("", loadAdError.getMessage());
                        advertisement = null;
                    }
                });


    }

    public void showAdAtRandom(int oneInXChanceAd) {
        Random randomGenerator = new Random();

        // generate random number from 0 to (X-1)
        final int randomNumber = randomGenerator.nextInt(oneInXChanceAd);

        // check if the random number if 0, then show the ad
        if (randomNumber == 0) {
            this.showAd();
        }
    }

}
