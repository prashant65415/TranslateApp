package com.example.translateapp;

import android.widget.TextView;

import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<MainActivity>(MainActivity.class);

    private MainActivity mActivity = null; //Activity instance

    @Before
    public void setUp() throws Exception {
        mActivity = mActivityTestRule.getActivity(); //Get activity
    }

    @Test
    public void testTranslateAppChinese() throws InterruptedException {

        //Click chinese button
        onView(withId(R.id.button_chinese)).perform(click());

        //Get textview
        TextView textview= ((TextView) mActivity.findViewById(R.id.tv_HelloWorld));

        //Get textview text
        String ch_string= textview.getText().toString();

        //Compare with the chinese text
        assertEquals("你好，世界!！", ch_string);
    }

    @After
    public void tearDown() throws Exception {
        mActivity = null; //make activity instance null
    }
}