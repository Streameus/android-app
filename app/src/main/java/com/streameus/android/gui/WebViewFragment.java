package com.streameus.android.gui;

import android.app.ProgressDialog;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.streameus.android.R;

/**
 * Created by Pol on 08/04/14.
 */
public class WebViewFragment extends Fragment {

    public static final String URI_TO_DISPLAY = "uri to display";
    public static final Integer WEBVIEW_TEAM = 1;
    public static final Integer WEBVIEW_ABOUT = 2;
    public static final Integer WEBVIEW_FAQ = 3;

    private MainActivity ma;
    private View rootView;
    private WebView webView;
    public ProgressDialog progress;

    public static Fragment createFragment(int urlToDisplay) {
        WebViewFragment fragment = new WebViewFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(WebViewFragment.URI_TO_DISPLAY, urlToDisplay);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ma = (MainActivity) getActivity();
        rootView =  inflater.inflate(R.layout.fragment_webview, container, false);
        webView = (WebView) rootView.findViewById(R.id.webView);
        progress = new ProgressDialog(ma);

        int pageID = getArguments().getInt(URI_TO_DISPLAY);
        String url = "";

        if (ma.getStreameusResources() != null) {
            if (pageID == WEBVIEW_ABOUT) {
                String versionName = getString(R.string.versionname_notFound);
                String versionCode = getString(R.string.versionname_notFound);
                try {
                    PackageInfo pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
                    versionName = pInfo.versionName;
                    versionCode = pInfo.versionCode + "";
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                Toast.makeText(getActivity(), versionName + " " + versionCode, Toast.LENGTH_LONG).show();
                ma.setTitle(R.string.apropos);
                url = ma.getStreameusResources().getAbout();
            } else if (pageID == WEBVIEW_FAQ) {
                ma.setTitle(R.string.aide);
                url = ma.getStreameusResources().getFaq();

            } else if (pageID == WEBVIEW_TEAM) {
                ma.setTitle(R.string.lequipe);
                url = ma.getStreameusResources().getTeam();
            }
            Log.i("Webview", "Open url: [" + url + "]");
            Log.i("Webview", "return of url equals: [" + url.equals("") + "]");

            if (url.equals("")) {
                Toast.makeText(ma, "Unable to getConferenceList this url to display", Toast.LENGTH_SHORT).show();
                Log.i("Webview", "cant open because empty url");

            } else {
                webView.setWebViewClient(new WebViewClient(){
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        progress.show();
                        view.loadUrl(url);
                        return true;
                    }
                    @Override
                    public void onPageFinished(WebView view, final String url) {
                        progress.dismiss();
                    }
                });
                webView.loadUrl(url);
            }

        } else {
            Toast.makeText(ma, "Webview urls not loaded yet", Toast.LENGTH_SHORT).show();
            Log.i("Webview", "Webview urls not loaded yet");

        }

        return rootView;
    }
}
