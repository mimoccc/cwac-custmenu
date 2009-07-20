package com.commonsware.cwac.custmenu;

import android.test.ActivityInstrumentationTestCase;

/**
 * This is a simple framework for a test of an Application.  See
 * {@link android.test.ApplicationTestCase ApplicationTestCase} for more information on
 * how to write and extend Application tests.
 * <p/>
 * To run this test, you can type:
 * adb shell am instrument -w \
 * -e class com.commonsware.cwac.custmenu.CustomMenuDemoTest \
 * com.commonsware.cwac.custmenu.tests/android.test.InstrumentationTestRunner
 */
public class CustomMenuDemoTest extends ActivityInstrumentationTestCase<CustomMenuDemo> {

    public CustomMenuDemoTest() {
        super("com.commonsware.cwac.custmenu", CustomMenuDemo.class);
    }

}
