package com.smartyads.sampleapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.smartyads.adcontainer.AdContainer;
import com.smartyads.adcontainer.BannerContainer;
import com.smartyads.adcontainer.InterstitialAdContainer;
import com.smartyads.adcontainer.VideoContainer;
import com.smartyads.adcontainer.context.PlacementType;
import com.smartyads.adcontainer.listener.BannerOnLoadListener;
import com.smartyads.adcontainer.listener.InterstitialListener;
import com.smartyads.adcontainer.listener.NativeOnLoadListener;
import com.smartyads.info.device.ScreenInfo;
import com.smartyads.info.provided.Gender;
import com.smartyads.info.provided.UserKeywords;
import com.smartyads.info.provided.YearOfBirth;
import java.util.Calendar;
import java.util.Collections;
import java.util.Random;

public class AdActivity extends AppCompatActivity implements View.OnClickListener {

  public static final String PLACEMENT_TYPE = "placement_type";

  private PlacementType mAdType;
  private TimeTracker mTimer;

  private TextView mStatusText;
  private View mContentView;
  private View mProgressView;
  private FrameLayout mAdContainer;

  public static void start(Context context, PlacementType placementType) {
    Intent intent = new Intent(context, AdActivity.class);
    intent.putExtra(PLACEMENT_TYPE, placementType);
    context.startActivity(intent);
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_ad);

    mAdType = (PlacementType) getIntent().getExtras().get(PLACEMENT_TYPE);
    mTimer = new TimeTracker();

    // arrow back
    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null) {
      actionBar.setHomeButtonEnabled(true);
      actionBar.setDisplayHomeAsUpEnabled(true);
      actionBar.setTitle(mAdType.getTitle());
    }

    // controls
    mStatusText = (TextView) findViewById(R.id.status_text);
    mContentView = findViewById(R.id.control_layout);
    mProgressView = findViewById(R.id.loading_layout);
    mAdContainer = (FrameLayout) findViewById(R.id.ad_container);

    findViewById(R.id.load_button).setOnClickListener(this);
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        onBackPressed();
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  private void switchState(boolean isContentLoading) {
    mContentView.setVisibility(isContentLoading ? View.GONE : View.VISIBLE);
    mProgressView.setVisibility(isContentLoading ? View.VISIBLE : View.GONE);
  }

  private void showResult(String result) {
    mStatusText.setText(
        String.format(getString(R.string.result_text), mTimer.stopTimeTracking() / 1000.00,
            result));
  }

  private void processVideo() {
    String placementId = // Choose random between horizontal and vertical ad
        System.currentTimeMillis() % 2 == 0 ? getString(R.string.video_placement_id)
            : getString(R.string.video_placement_id_horizontal);

    new VideoContainer(this, placementId).loadAd(provideFullScreenAdListener());
  }

  private void processInterstitial() {
    new InterstitialAdContainer(this, getString(R.string.interstitial_placement_id)).loadAd(
        provideFullScreenAdListener());
  }

  private void processBanner() {
    String containerId = "";
    switch (mAdType) { // choose container(placement) Id

      case MEDIUM_RECTANGLE:
        containerId = getString(R.string.medium_rectangle_placement_id);
        break;
      case STANDARD_BANNER:
        containerId = getString(R.string.standard_banner_placement_id);
        break;
      case LARGE_BANNER:
        containerId = getString(R.string.large_banner_placement_id);
        break;
      case FULL_SIZE_BANNER:
        containerId = getString(R.string.full_size_banner_placement_id);
        break;
      case LEADERBOARD:
        containerId = getString(R.string.leaderboard_placement_id);
        break;
    }

    // create custom banner container
    BannerContainer bannerContainer =
        new BannerContainer(this, mAdType.getWidth(), mAdType.getHeight(), 30, containerId);
    mAdContainer.addView(bannerContainer);

    fillSomeTargetingData(bannerContainer); // adding targeting params
    bannerContainer.loadAd(provideBannerAdListener());
  }

  private void processNative() {
    mAdContainer.removeAllViews();
    RecyclerView recyclerView = new RecyclerView(this);
    recyclerView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
        ScreenInfo.getScreenInfo(this).height / 2)); // List occupy half of the screen
    recyclerView.setAdapter(new NativeAdapter(provideNativeAdListener()));
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    mAdContainer.addView(recyclerView);
  }

  private void fillSomeTargetingData(AdContainer adContainer) {
    Random random = new Random();

    Gender randomGender = Gender.values()[random.nextInt(Gender.values().length)];

    int maxAge = 100;
    YearOfBirth yearOfBirth = new YearOfBirth(
        Calendar.getInstance().get(Calendar.YEAR) - maxAge + random.nextInt(maxAge));

    UserKeywords userKeywords = new UserKeywords().addKeywords("food", "music")
        .addKeywords(Collections.singletonList("games"));

    adContainer.addTargetingData(randomGender)
        .addTargetingData(yearOfBirth)
        .addTargetingData(userKeywords);
  }

  private InterstitialListener provideFullScreenAdListener() {
    return new InterstitialListener() {
      @Override public void closed() {
        // Fullscreen closed. Nothing to do here in example case
      }

      @Override public void onSuccess() {
        switchState(false);
        showResult("success");
      }

      @Override public void onFailure(Exception e) {
        switchState(false);
        showResult(e.getMessage());
      }
    };
  }

  private BannerOnLoadListener provideBannerAdListener() {
    return new BannerOnLoadListener() {

      @Override public void onSuccess() {
        switchState(false);
        showResult("success");
      }

      @Override public void onFailure(Exception e) {
        switchState(false);
        showResult(e.getMessage());
      }
    };
  }

  private NativeOnLoadListener provideNativeAdListener() {
    return new NativeOnLoadListener() {

      @Override public void onSuccess() {
        switchState(false);
        showResult("success");
      }

      @Override public void onFailure(Exception e) {
        switchState(false);
        showResult(e.getMessage());
      }
    };
  }

  @Override public void onClick(View view) {
    // preparation
    mTimer.startTimeTracking();
    switchState(true);

    // processing
    if (mAdType == PlacementType.INTERSTITIAL) { // fullscreen banner
      processInterstitial();
    } else if (mAdType == PlacementType.VIDEO) {
      processVideo();
    } else if (mAdType == PlacementType.NATIVE) {
      processNative();
    } else { // banners
      processBanner();
    }
  }

  private class TimeTracker {
    private long mTimestamp;

    void startTimeTracking() {
      mTimestamp = System.currentTimeMillis();
    }

    long stopTimeTracking() {
      return System.currentTimeMillis() - mTimestamp;
    }
  }
}
