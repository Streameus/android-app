package com.streameus.android.gui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.otto.Subscribe;
import com.streameus.android.R;
import com.streameus.android.bus.BusProvider;
import com.streameus.android.bus.GetLoggedUserEvent;
import com.streameus.android.bus.GetProviderInfosEvent;
import com.streameus.android.bus.GetTokenEvent;
import com.streameus.android.bus.OnRegisterCallBack;
import com.streameus.android.bus.ReceiveLoggedUserEvent;
import com.streameus.android.bus.ReceiveProviderInfoEvent;
import com.streameus.android.bus.ReceiveTokenEvent;
import com.streameus.android.bus.RegisterEvent;
import com.streameus.android.dataProvider.AuthNative;
import com.streameus.android.dataProvider.DataService;
import com.streameus.android.dataProvider.RESTClient;
import com.streameus.android.model.OAuthProviderInfo;
import com.streameus.android.model.User;
import com.streameus.android.utils.StreameusPreferences;

import java.util.List;


public class LoginActivity extends Activity {
    private final String TAG = "LoginActivity";
    public static final String TOKEN = "token";

    public static Intent createIntent(Context c) {
        Intent i = new Intent(c, LoginActivity.class);
        return i;
    }

    private View mGoogleButton;
    private View mFacebookButton;
    private Button mLoginButton;
    private EditText mLoginEditText;
    private EditText mPasswordEditText;
    private LoginActivity ref;
    private AuthNative authNative = new AuthNative();
    private String OAuthAsked = "";
    private boolean isLoginView;
    //    private Button mSwitchLoginButton;
//    private Button mSwitchRegisterButton;
    private TextView switchForm;
    private LinearLayout loginSection;
    private LinearLayout registerSection;

    private enum FormDisplayed {
        LOGIN, REGISTER
    }
    private EditText MailRegisterEditTextView;
    private EditText PseudoRegisterEditTextView;
    private EditText passwordRegisterEditTextView;
    private EditText passwordConfirmRegisterEditTextView;
    private Button SendRegisterButtonView;
    private WebView externalLogingWebview;
    private boolean isResetedWebview = false;
    private LinearLayout loging_form;
    private ImageView bigTopLogoImage;

    LoadingView loadingView;

    private class LoadingView {
        private ImageView loadingImage;
        final private Animation loadingAnimation;
        final private Animation loadingAnimation2;
        boolean isShowing = false;

        public LoadingView(Context c) {
            loadingImage = (ImageView) findViewById(R.id.loadingLogin);
            loadingAnimation = AnimationUtils.loadAnimation(c, R.anim.logo_rotate);
            loadingAnimation2 = AnimationUtils.loadAnimation(c, R.anim.logo_rotate_part2);
            loadingAnimation2.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    if (isShowing) {
                        loadingImage.startAnimation(loadingAnimation);
                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            loadingAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    if (isShowing) {
                        loadingImage.startAnimation(loadingAnimation2);
                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            hide();
        }

        public void show() {
            if (!isShowing) {
                isShowing = true;
                loadingImage.setVisibility(View.VISIBLE);
                loadingImage.startAnimation(loadingAnimation);
            }
        }

        public void hide() {
            if (isShowing) {
                isShowing = false;
                loadingImage.clearAnimation();
                loadingImage.setVisibility(View.GONE);
            }
        }

    }

    private class RegistrationButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            passwordConfirmRegisterEditTextView.setError(null);
            PseudoRegisterEditTextView.setError(null);
            MailRegisterEditTextView.setError(null);
            passwordRegisterEditTextView.setError(null);
            RegisterEvent e = new RegisterEvent(PseudoRegisterEditTextView.getText().toString(),
                    MailRegisterEditTextView.getText().toString(),
                    passwordRegisterEditTextView.getText().toString(),
                    passwordConfirmRegisterEditTextView.getText().toString());
            //check the data
            if (!e.getPassword().equals(e.getComfirmPassword())) {
                passwordConfirmRegisterEditTextView.setError("Both Password doesn't match");
                return;
            }
            if (e.getPseudo().length() < 6) {
                PseudoRegisterEditTextView.setError("Nickname a little to short.. have to be > 6");
                return;
            }
            if (e.getEmail().equals("")) {
                MailRegisterEditTextView.setError("Empty :-)");
                return;
            }
            if (e.getPassword().equals("")) {
                passwordRegisterEditTextView.setError("Empty :-)");
                return;
            }
            if (e.getComfirmPassword().equals("")) {
                passwordConfirmRegisterEditTextView.setError("Empty :-)");
                return;
            }
            displayLoading();
            BusProvider.get().post(e);
        }
    }


    private View.OnClickListener externalLoginClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            displayLoading();
            switch (view.getId()) {
                case R.id.sign_in_Facebook:
                    Log.d(TAG, "ask facebook Oauth url");
                    OAuthAsked = "Facebook";
                    break;
                case R.id.sign_in_google:
                    Log.d(TAG, "ask google Oauth url");
                    OAuthAsked = "Google";
                    break;
            }

            BusProvider.get().post(new GetProviderInfosEvent());
        }
    };

    private View.OnClickListener loginClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mLoginEditText.setError(null);
            mPasswordEditText.setError(null);

            String login = mLoginEditText.getText().toString();
            String password = mPasswordEditText.getText().toString();
            if (login.isEmpty()) {
                mLoginEditText.setError("Please input your login");
            } else if (!login.contains("@") || !login.contains(".")) {
                mLoginEditText.setError("Email invalid");
            } else if (password.isEmpty()) {
                mPasswordEditText.setError("Please input your password");
            } else {
                displayLoading();
                BusProvider.get().post(new GetTokenEvent(login, password));
            }

        }
    };

    private void initWebview() {
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();

        externalLogingWebview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if (!isResetedWebview) {
                    Log.d(TAG, "Changement d'url " + url);
                    loadingView.show();
                    if (url.contains("#access_token=")) {
                        url = url.substring(url.indexOf("#access_token="));
                        String[] args = url.split("&");
                        String token = args[0].substring(args[0].indexOf("#access_token=") + "#access_token=".length());
                        Log.i(TAG, "Token found:" + token);
                        externalLogingWebview.stopLoading();
                        isResetedWebview = true;
                        Log.i(TAG, "Dumps des args " + args.length);

                        StreameusPreferences.setToken(LoginActivity.this, token);
                        checkConnexion();
                    }
                }

            }


            @Override
            public void onPageFinished(WebView view, String url) {
                loadingView.hide();
            }
        });


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ref = this;
        DataService.getNotLogged();
        setContentView(R.layout.activity_login);


        externalLogingWebview = (WebView) findViewById(R.id.externalLogingWebview);
        initWebview();

        MailRegisterEditTextView = (EditText) findViewById(R.id.MailRegister);
        PseudoRegisterEditTextView = (EditText) findViewById(R.id.PseudoRegister);
        passwordRegisterEditTextView = (EditText) findViewById(R.id.passwordRegister);
        passwordConfirmRegisterEditTextView = (EditText) findViewById(R.id.passwordConfirmRegister);
        SendRegisterButtonView = (Button) findViewById(R.id.SendRegister);
        SendRegisterButtonView.setOnClickListener(new RegistrationButtonListener());

        switchForm = (TextView) findViewById(R.id.switcher);
        switchForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((FormDisplayed) view.getTag() == FormDisplayed.LOGIN) {
                    displayRegister();
                } else {
                    displayLogin();
                }
            }
        });

        registerSection = (LinearLayout) findViewById(R.id.RegisterSection);
        loginSection = (LinearLayout) findViewById(R.id.LoginSection);

        loging_form = (LinearLayout) findViewById(R.id.loging_form);
        mLoginButton = (Button) findViewById(R.id.loginButton);
        mLoginEditText = (EditText) findViewById(R.id.loginEditText);
        mPasswordEditText = (EditText) findViewById(R.id.passwordEditText);
        mLoginButton.setOnClickListener(loginClickListener);

        bigTopLogoImage = (ImageView) findViewById(R.id.bigTopLogo);
        bigTopLogoImage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mLoginEditText.setText("eric@streameus.com");
                mPasswordEditText.setText("lolipopi");
                return true;
            }
        });

        mGoogleButton = findViewById(R.id.sign_in_google);
        mGoogleButton.setOnClickListener(externalLoginClickListener);

        mFacebookButton = findViewById(R.id.sign_in_Facebook);
        mFacebookButton.setOnClickListener(externalLoginClickListener);

        loadingView = new LoadingView(this);
        BusProvider.get().register(this);
        checkConnexion();
    }




    @Override
    protected void onResume() {
        super.onResume();
        BusProvider.get().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        BusProvider.get().unregister(this);
    }

    public void checkConnexion() {

        String token = StreameusPreferences.getToken(this);
        if (!token.equals("")) {
            Log.v(TAG, "Token Found in Preferences: " + token);
            displayLoading();
            BusProvider.get().post(new GetLoggedUserEvent(token));

        } else {
            onAskToLogin();
        }

    }


    public void onAskToLogin() {
        displayLogin();
    }


    public void launchMainActivity(int userID) {
        Log.v(TAG, "LaunchMainActivity()");
        Intent i = MainActivity.createIntent(this, userID);
        startActivity(i);
        BusProvider.get().unregister(authNative);
        finish();
    }

    private void loadOAauthWebview(String url) {



        Log.i(TAG, RESTClient.API_URL + url.substring("/api".length()));
        isResetedWebview = false;
        externalLogingWebview.loadUrl(RESTClient.API_URL + url.substring("/api".length()));
        externalLogingWebview.setVisibility(View.VISIBLE);
        loadingView.hide();
    }


    /*****************
     *
     *
     * EVENTS HANDLING
     *
     *
     *******************/



    @Subscribe
    public void onReceiveLoggedUserEvent(ReceiveLoggedUserEvent e) {
        if (e.getErrorMSG() == null) {
            User currentUser = e.getUser();

            Log.v(TAG, "On success de onReceiveLoggedUserEvent called");
            launchMainActivity(currentUser.getId());

        } else {
            Log.v(TAG, "Onfail de onReceiveLoggedUserEvent called");
            //remove the token
            StreameusPreferences.setToken(this, "");


            Toast.makeText(this, getString(R.string.login_error) + e.getErrorMSG(), Toast.LENGTH_SHORT).show();
            onAskToLogin();
        }
    }

    @Subscribe
    public void onReceiveProviderInfoEvent(ReceiveProviderInfoEvent e) {
        loadingView.hide();
        List<OAuthProviderInfo> providerInfoList;

        if (e.getErrorMSG() != null) {
            Toast.makeText(LoginActivity.this, "Unable to retrieve The Oauths links", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Unable to retrieve The Oauths link");
            displayLogin();
        } else {
            providerInfoList = e.get();
            String url = "";
            Log.v(TAG, "onSuccess du retrieve des urls OAuth");
            for (int i = 0; i < providerInfoList.size(); ++i) {
                if (providerInfoList.get(i).getName().equals(OAuthAsked)) {
                    url = providerInfoList.get(i).getUrl();
                    break;
                }
            }
            if (!url.equals("")) {
                loadOAauthWebview(url);
            }
        }
    }


    @Subscribe
    public void onReceiveTokenEvent(ReceiveTokenEvent e) {
        loadingView.hide();

        String token = e.getToken();
        if (token == null || token.length() == 0) {
            Toast.makeText(this ,getString(R.string.login_error), Toast.LENGTH_SHORT).show();
            displayLogin();
        } else {

            StreameusPreferences.setToken(this, token);

            checkConnexion();
        }
    }

    @Subscribe
    public void OnRegisterCallBack(OnRegisterCallBack e) {
        if (e.getErrorMSG() != null) {
            Toast.makeText(this, "Unable to register: " + e.getErrorMSG(), Toast.LENGTH_SHORT).show();
            displayRegister();
        } else if (e.getPassword() != null && e.getUserName() != null) {
            Toast.makeText(this, "Success while Registering, we are now logging you in", Toast.LENGTH_SHORT).show();
            BusProvider.get().post(new GetTokenEvent(e.getUserName(), e.getPassword()));
        }
    }


    private void displayLogin() {
        switchForm.setText("Register");
        switchForm.setTag(FormDisplayed.LOGIN);
        registerSection.setVisibility(View.GONE);
        loginSection.setVisibility(View.VISIBLE);
        loging_form.setVisibility(View.VISIBLE);
        loadingView.hide();
    }

    private void displayRegister() {
        switchForm.setText("Login");
        switchForm.setTag(FormDisplayed.REGISTER);
        loginSection.setVisibility(View.GONE);
        registerSection.setVisibility(View.VISIBLE);
        loging_form.setVisibility(View.VISIBLE);
        loadingView.hide();
    }

    private void displayLoading() {
        loging_form.setVisibility(View.GONE);
        loadingView.show();
    }
}