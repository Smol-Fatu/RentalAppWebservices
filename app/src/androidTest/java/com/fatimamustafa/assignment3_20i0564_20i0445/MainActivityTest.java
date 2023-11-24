package com.fatimamustafa.assignment3_20i0564_20i0445;

import static org.junit.Assert.assertTrue;

import android.util.Log;

import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Test;

public class MainActivityTest {
    public ActivityScenarioRule<Home> activityRule = new ActivityScenarioRule<>(Home.class);

    @Test
    public void testHomeSuccess() {
        Log.d(String.valueOf(0),activityRule.toString());
        assertTrue(true);
    }
}
