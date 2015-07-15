package com.streameus.android;

import android.test.TouchUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.streameus.android.dataProvider.DataService;
import com.streameus.android.gui.LoginActivity;
import com.streameus.android.utils.StreameusPreferences;

/**
 * Created by Pol on 25/04/14.
 */
public class testLoginActivity extends android.test.ActivityInstrumentationTestCase2<LoginActivity> {
    LoginActivity mActivity;
    EditText mLogin;
    EditText mPassword;
    private LoginActivity a;

    public testLoginActivity() {
        super(LoginActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        setActivityInitialTouchMode(false);
        mActivity = getActivity();
        mLogin = (EditText) mActivity.findViewById(R.id.loginEditText);
        mPassword = (EditText) mActivity.findViewById(R.id.passwordEditText);
        a = getActivity();
        a.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d("DEBUG", "setDebug");
                DataService.setDebug();
                StreameusPreferences.setToken(getActivity(), "");
            }
        });
        getInstrumentation().waitForIdleSync();
    }

    public void testPrecondition() {
        assertNotNull(mActivity);
    }

    public void testLoginNotAMail() {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mLogin.setText("pasdarobase");
                mPassword.setText("pasdarobase");
            }
        });
        TouchUtils.clickView(this, mActivity.findViewById(R.id.loginButton));
        getInstrumentation().waitForIdleSync();
        assertNotNull(mLogin.getError());
    }

    public void testNoPasssword() {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mLogin.setText("eric@streameus.com");
                mPassword.setText("");
            }
        });
        TouchUtils.clickView(this, mActivity.findViewById(R.id.loginButton));
        getInstrumentation().waitForIdleSync();
        assertNotNull(mPassword.getError());
    }

    public void testhideFormOnLogin() {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mLogin.setText("eric@streameus.com");
                mPassword.setText("ccdcsdcs");
                getActivity().findViewById(R.id.loginButton).performClick();
            }
        });
        getInstrumentation().waitForIdleSync();
        assertNull(mPassword.getError());
        assertNull(mLogin.getError());
        assertEquals(mActivity.findViewById(R.id.loging_form).getVisibility(), LinearLayout.GONE);
    }


}
