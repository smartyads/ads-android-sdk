package com.smartyads.sampleapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.smartyads.adcontainer.InterstitialAdContainer;
import com.smartyads.adcontainer.listener.BannerOnLoadListener;
import com.smartyads.adcontainer.listener.InterstitialListener;
import com.smartyads.utils.StringUtils;
import com.smartyads.vast.VideoContainer;

import static com.smartyads.sampleapp.Banner.FULLSCREEN_BANNER;
import static com.smartyads.sampleapp.Banner.VAST;

public class FullscreenAdActivity extends AbstractAdActivity {

    private InterstitialAdContainer interstitialContainer;
    private Button loadAdButton;
    private Button showAdButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_fullscreen_ad);
        super.onCreate(savedInstanceState);
        interstitialContainer = createContainer(bannerFromIntent());
        loadAdButton = (Button) findViewById(R.id.load_ad);
        showAdButton = (Button) findViewById(R.id.show_ad);
    }

    public void onLoadAdButtonClicked(View view){
        onAdLoadStarted();
        loadAdButton.setEnabled(false);
        interstitialContainer.loadAd(createBannerOnLoadListener());
    }

    public void onShowAdButtonClicked(View view){
        showAdButton.setEnabled(false);
        interstitialContainer.showAd();
        loadAdButton.setEnabled(true);
    }

    InterstitialListener createBannerOnLoadListener(){
        final BannerOnLoadListener bannerOnLoadListener = super.createBannerOnLoadListener();
        return new InterstitialListener() {
            @Override
            public void closed() {
                Log.d(StringUtils.SMARTY_TAG, "Ad closed!");
                Toast.makeText(
                        FullscreenAdActivity.this,
                        getString(R.string.interstitial_closed),
                        Toast.LENGTH_SHORT
                ).show();
            }

            @Override
            public void onSuccess() {
                showAdButton.setEnabled(interstitialContainer.isLoaded());
                bannerOnLoadListener.onSuccess();
            }

            @Override
            public void onFailure(Exception e) {
                loadAdButton.setEnabled(true);
                bannerOnLoadListener.onFailure(e);
            }
        };
    }

    private InterstitialAdContainer createContainer(Banner banner){
        switch (banner){
            case FULLSCREEN_BANNER:
                return new InterstitialAdContainer(this, banner.containerId);
            case VAST:
                return new VideoContainer(this, banner.containerId);
        }
        throw new RuntimeException("Received invalid banner="+banner+", expected "+FULLSCREEN_BANNER+" or "+VAST);
    }
}
