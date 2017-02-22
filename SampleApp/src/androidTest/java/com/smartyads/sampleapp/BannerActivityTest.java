package com.smartyads.sampleapp;

import android.app.Activity;
import android.support.annotation.IdRes;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.ViewAssertion;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import android.view.View;

import com.google.android.libraries.cloudtesting.screenshots.ScreenShotter;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.runner.lifecycle.Stage.RESUMED;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;


@RunWith(AndroidJUnit4.class)
@LargeTest
public class BannerActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void clickOnBannerListItemToShowBannerTest(){
        for (Banner banner : bannersToTest()) {
            onData(allOf(instanceOf(Banner.class), myCustomObjectShouldHaveString(banner.toString())))
                    .perform(click());
            verifyBannerAppeared(banner);
            pressBack();
        }
    }

    public void verifyBannerAppeared(Banner banner) {
        @IdRes int bannerId = getBannerId();
        Matcher<View> viewMatcher = withId(bannerId);
        waitFor(bannerId);
        onView(viewMatcher).check(matches(isDisplayed()));
        takeScreenshot(banner.toString());
    }

    public void takeScreenshot(String name){
        sleep(1000);
        ScreenShotter.takeScreenshot(name+"_"+System.currentTimeMillis(), getActivityInstance());
        sleep(1000);
    }

    public void sleep(long millis){
        try {
            TimeUnit.MILLISECONDS.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public @IdRes int getBannerId(){
         return R.id.banner_container;
    }

    public List<Banner> bannersToTest(){
        List<Banner> banners = new ArrayList<>();
        for (Banner banner : Banner.values()) {
            if(banner != Banner.FULLSCREEN_BANNER)
                banners.add(banner);
        }
        return banners;
    }

    private void waitFor(@IdRes int id){
        ViewIdlingResource viewIdlingResource = new ViewIdlingResource(getActivityInstance().findViewById(id));
        Espresso.registerIdlingResources(viewIdlingResource);
        dummyInterractionWithView(withId(id));
        Espresso.unregisterIdlingResources(viewIdlingResource);
    }

    public Activity getActivityInstance(){
        final Activity[] activity = new Activity[1];
        getInstrumentation().runOnMainSync(new Runnable() {
            public void run() {
                Collection<Activity> resumedActivities = ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(RESUMED);
                Iterator<Activity> iterator = resumedActivities.iterator();
                if (iterator.hasNext()){
                    activity[0] = iterator.next();
                }
            }
        });

        return activity[0];
    }

    private void dummyInterractionWithView(Matcher<View> matcher){
        try{
            onView(matcher).check(new ViewAssertion() {public void check(View view, NoMatchingViewException noView) {}});
        }catch (Exception e){
            //just a dummy interaction with view, do nothing in exception case
        }
    }

    public static Matcher<Object> myCustomObjectShouldHaveString(String expectedTest) {
        return myCustomObjectShouldHaveString(equalTo(expectedTest));
    }
    private static Matcher<Object> myCustomObjectShouldHaveString(final Matcher<String> expectedObject) {
        return new BoundedMatcher<Object, Banner>(Banner.class) {
            @Override
            public boolean matchesSafely(final Banner actualObject) {
                return expectedObject.matches(actualObject.toString());
            }
            @Override
            public void describeTo(final Description description) {
                description.appendText("getnumber should return ");
            }
        };
    }
}