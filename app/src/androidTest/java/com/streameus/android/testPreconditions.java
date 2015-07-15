package com.streameus.android;

import android.test.InstrumentationTestCase;

/**
 * Created by Pol on 19/04/14.
 */
public class testPreconditions extends InstrumentationTestCase {
    public void test() throws Exception {
        final int expected = 1;
        final int reality = 5;

        assertEquals(expected, expected);
        assertFalse(expected == reality);
    }
}

