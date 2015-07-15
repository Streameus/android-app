package com.streameus.android.gui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.otto.Subscribe;
import com.streameus.android.R;
import com.streameus.android.bus.BusProvider;
import com.streameus.android.bus.GetLecturesEvent;
import com.streameus.android.bus.ReceiveAllLecturesEvent;
import com.streameus.android.model.IConference;
import com.streameus.android.utils.IConferenceAdaptater;

import java.util.List;

/**
 * Affiche une liste de conférence
 * - Affiche toute les conférénces
 * - Affiche les conférences d'un utilisateur
 */
public class ConferencesListFragment extends ListFragment {
    private static final String TAG = "ListConferencesFragment";
    private static final String PARAM_LIST_TYPE = "PARAM_LIST_TYPE";
    private static final String PARAM_VALUE = "PARAM_VALUE";
    private MainActivity ma;
    private IConferenceAdaptater iConferenceAdaptater;
    private ViewGroup rootView;
    private View loadingView;
    private int paramValue;
    private ConferenceListType listType;
    private boolean requestNeedParam;
    private RelativeLayout mErrorView;

    public enum ConferenceListType {
        USER, ALL, LIVE, SOON, CATEGORIE, RECOMMANDED, ATTENDED, REGISTERED, SEARCH
    }

    public static Fragment createFragment(ConferenceListType type, int param) {
        Fragment fragment = new ConferencesListFragment();
        Bundle b = new Bundle();
        b.putSerializable(PARAM_LIST_TYPE, type);
        b.putInt(PARAM_VALUE, param);
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        listType = (ConferenceListType) getArguments().getSerializable(PARAM_LIST_TYPE);
        paramValue = getArguments().getInt(PARAM_VALUE);
        ma = (MainActivity) getActivity();
        rootView = (ViewGroup) inflater.inflate(R.layout.simple_listview, container, false);
        loadingView = rootView.findViewById(R.id.loading);
        BusProvider.get().register(this);
        if (listType != ConferenceListType.SEARCH) {
            BusProvider.get().post(new GetLecturesEvent(listType, paramValue));
        } else {
            loadingView.setVisibility(View.GONE);
        }
        mErrorView = (RelativeLayout) rootView.findViewById(R.id.error_view);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        BusProvider.get().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        BusProvider.get().unregister(this);
    }

    @Subscribe
    public void onReceiveAllLecturesEvent(ReceiveAllLecturesEvent e) {
      if (e.getType() == listType) {
          if (e.getErrorMSG() != null) {
              mErrorView.setVisibility(View.VISIBLE);
              loadingView.setVisibility(View.GONE);
              ((TextView) mErrorView.getChildAt(1)).setText(e.getErrorMSG());
          } else {
              mErrorView.setVisibility(View.GONE);
              loadingView.setVisibility(View.GONE);
              List<? extends IConference> eventList = e.getConferenceList();
              if (eventList == null) {
                  return;
              }

              iConferenceAdaptater = new IConferenceAdaptater(ma, ma.getLayoutInflater(), eventList);
              ((ListView) rootView.findViewById(android.R.id.list)).setAdapter(iConferenceAdaptater);
          }
      }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        ma.loadFragment(ConferenceFragment.createFragment((int)id));
    }
}