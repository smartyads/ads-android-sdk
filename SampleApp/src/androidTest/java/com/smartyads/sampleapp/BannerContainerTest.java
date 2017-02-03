package com.smartyads.sampleapp;

import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingPolicies;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.ViewAssertion;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import com.google.android.libraries.cloudtesting.screenshots.ScreenShotter;
import com.smartyads.adcontainer.BannerContainer;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.not;


@RunWith(AndroidJUnit4.class)
@LargeTest
public class BannerContainerTest {

    @Rule
    public ActivityTestRule<BannerActivity> mActivityRule =
            new ActivityTestRule<>(BannerActivity.class, false, false);

    @Before
    public void resetTimeout() {
        IdlingPolicies.setIdlingResourceTimeout(5, TimeUnit.SECONDS);
        Intent i = new Intent();
        i.putExtra(BannerActivity.BANNER_LAYOUT_INTENT_KEY, Banner.BANNER.layout);
        mActivityRule.launchActivity(i);
    }

    @Test
    public void bannerLoadedOnStartupTest() {
        BannerContainer bannerContainer = (BannerContainer) mActivityRule.getActivity().findViewById(R.id.banner_container);
        @IdRes int bannerId = R.id.banner_container;
        Matcher<View> viewMatcher = withId(bannerId);
        waitFor(bannerId);
        if(bannerContainer.isLoaded()){
            onView(viewMatcher).check(matches(isDisplayed()));
        }else{
            onView(viewMatcher).check(matches(not(isDisplayed())));
        }
        ScreenShotter.takeScreenshot("BannerContainerTest_"+System.currentTimeMillis(), mActivityRule.getActivity());
    }

    private void waitFor(@IdRes int id){
        ViewIdlingResource viewIdlingResource = new ViewIdlingResource(mActivityRule.getActivity().findViewById(id));
        Espresso.registerIdlingResources(viewIdlingResource);
        dummyInterractionWithView(withId(id));
        Espresso.unregisterIdlingResources(viewIdlingResource);
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void dummyInterractionWithView(Matcher<View> matcher){
        try{
            onView(matcher).check(new ViewAssertion() {public void check(View view, NoMatchingViewException noView) {}});
        }catch (Exception e){
            //just a dummy interaction with view, do nothing in exception case
        }
    }
}