package com.smartyads.sampleapp;

import android.support.annotation.IdRes;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;


@RunWith(AndroidJUnit4.class)
@LargeTest
public class InterstitialContainerTest extends BannerActivityTest{

    @Test
    public void clickOnBannerListItemToShowBannerTest() {
        super.clickOnBannerListItemToShowBannerTest();
    }

    public void verifyBannerAppeared(Banner banner) {
        super.verifyBannerAppeared(banner);

        //click close interstitial
        onView(withId(R.id.close_button)).perform(click());
        onView(withId(R.id.refresh)).check(matches(isDisplayed()));
        takeScreenshot("Interstitial_dismissed");
    }

    @Override
    public @IdRes int getBannerId() {
        return R.id.interstitial_banner_container;
    }

    @Override
    public List<Banner> bannersToTest(){
        return Arrays.asList(Banner.FULLSCREEN_BANNER);
    }


}