package com.streameus.android.gui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.otto.Subscribe;
import com.streameus.android.R;
import com.streameus.android.bus.BusProvider;
import com.streameus.android.bus.GetAgendaEvent;
import com.streameus.android.bus.ReceiveAgendaEvent;
import com.streameus.android.model.ConferenceSet;
import com.streameus.android.utils.Dates;
import com.streameus.android.utils.IConferenceAdaptater;
import com.streameus.android.utils.Utils;

import java.util.List;

public class AgendaFragment extends Fragment {
    private static final String TAG = "AgendaFragment";
    private MainActivity ma;
    private View loadingView;
    private LinearLayout agendaContainerLinearLayout;
    private LayoutInflater layoutInflater;
    boolean loaded = false;


    public static Fragment createFragment() {
        Fragment fragment = new AgendaFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ma = (MainActivity) getActivity();
        ma.setTitle(getString(R.string.agenda));
        layoutInflater = inflater;
        View rootView = inflater.inflate(R.layout.fragment_agenda, container, false);
        loadingView = rootView.findViewById(R.id.loading);
        agendaContainerLinearLayout = (LinearLayout) rootView.findViewById(R.id.agenda_container);
        return rootView;
    }


    @Override
    public void onResume() {
        super.onResume();
        BusProvider.get().register(this);
        Log.v(TAG, "Send a GetAgendaEvent");
        BusProvider.get().post(new GetAgendaEvent());
    }


    @Override
    public void onPause() {
        super.onPause();
        BusProvider.get().unregister(this);
    }

    @Subscribe
    public void onReceiveAgendaEvent(ReceiveAgendaEvent e) {
        if (e.getErrorMSG() != null) {
            Toast.makeText(getActivity(), e.getErrorMSG(), Toast.LENGTH_SHORT).show();
        } else {
            loaded = true;

            List<ConferenceSet> conferenceSets = e.getConferenceSmallList();

            if (conferenceSets.size() == 0) {
                Toast.makeText(getActivity(), "You didn't subscribe to any lecture for now...", Toast.LENGTH_SHORT).show();
            } else {
                agendaContainerLinearLayout.removeAllViews();
                ConferenceSet lastConferenceSet = null;

                for (int i = 0; i < conferenceSets.size(); ++i) {
                    ConferenceSet c = conferenceSets.get(i);
                    if (lastConferenceSet == null || lastConferenceSet.getKey().substring(0, 10).equals(c.getKey().substring(0, 10))) {
                        TextView textView = new TextView(ma);
                        String text = c.getKey();

                        textView.setText(Dates.parse3339ToReadableText(text));
                        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        llp.setMargins(0, Utils.dpTopx(getActivity(), 16), 0, 0);
                        textView.setLayoutParams(llp);
                        textView.setTextAppearance(ma, R.style.StreameusTitre);
                        agendaContainerLinearLayout.addView(textView);
                    }

                    ListView modeList = new ListView(getActivity());
                    IConferenceAdaptater a = new IConferenceAdaptater(getActivity(), layoutInflater, conferenceSets.get(i).getValue());
                    modeList.setAdapter(a);
                    modeList.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    modeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Fragment f = ConferenceFragment.createFragment((int)l);
                            ma.loadFragment(f);
                        }
                    });
                    agendaContainerLinearLayout.addView(modeList);
                    lastConferenceSet = c;
                }
            }
            loadingView.setVisibility(View.GONE);
        }

    }





}