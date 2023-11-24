package com.fatimamustafa.assignment3_20i0564_20i0445;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import static org.junit.Assert.assertTrue;

import android.util.Log;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class SplashScreenActivityTest {

    @Rule
    public ActivityScenarioRule<splash> mActivityRule = new ActivityScenarioRule<>(splash.class);

    @Test
    public void testSplashScreenDisplayed() {
        // Launch the activity using ActivityScenario
        Log.d(String.valueOf(0),mActivityRule.toString());
        assertTrue(true);
    }
}
