package com.streameus.android.gui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.streameus.android.R;
import com.streameus.android.utils.StreameusPreferences;

public class LogoutFragment extends Fragment {

    public LogoutFragment() {
        // Empty constructor required for fragment subclasses
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle(getString(R.string.sedeconnecter));
        View rootView = inflater.inflate(R.layout.fragment_loading, container, false);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        StreameusPreferences.setToken(getActivity(), "");
        Intent i = new Intent(getActivity(), LoginActivity.class);
        startActivity(i);
        getActivity().finish();
    }
}