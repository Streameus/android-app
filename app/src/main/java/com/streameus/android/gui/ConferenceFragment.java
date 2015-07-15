package com.streameus.android.gui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;
import com.streameus.android.R;
import com.streameus.android.bus.BusProvider;
import com.streameus.android.bus.ChangeConferenceRegisterStatusEvent;
import com.streameus.android.bus.GetConferenceEvent;
import com.streameus.android.bus.ReceiveLectureEvent;
import com.streameus.android.dataProvider.RESTClient;
import com.streameus.android.model.Conference;
import com.streameus.android.model.User;
import com.streameus.android.utils.Dates;

public class ConferenceFragment extends Fragment {
    private static final String TAG = "ConferenceFragment";
    private MainActivity ma;
    public static String INTENT_PARAMETER_CONFERENCEID = "INTENT_PARAMETER_CONFERENCEID";

    private int conferenceID;

    private View rootView;
    private View loadingView;
    private ImageView profil_pictureImageView;
    private TextView titleTextView;

    private LectureDataStatePageAdaptater mLectureDataStatePageAdapter;
    private ViewPager mViewPager;
    private FloatingActionButton FAB;

    public static Fragment createFragment(int conferenceIDp) {
        Fragment fragment = new ConferenceFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ConferenceFragment.INTENT_PARAMETER_CONFERENCEID, conferenceIDp);
        Log.v(TAG, "On a cree une conf avec pour id " + conferenceIDp);

        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ma = (MainActivity) getActivity();

        rootView = inflater.inflate(R.layout.fragment_conference, container, false);
        loadingView = rootView.findViewById(R.id.loading);
        profil_pictureImageView = (ImageView) rootView.findViewById(R.id.conference_picture);
        titleTextView = (TextView) rootView.findViewById((R.id.conference_name));

        conferenceID = getArguments().getInt(INTENT_PARAMETER_CONFERENCEID);
        Log.v(TAG, "On recupere dans la vue conf la valeur"  + conferenceID);
        FAB = (FloatingActionButton) rootView.findViewById(R.id.floatingactionbutton);

        getActivity().setTheme(R.style.TransparentBar);
        loadingView.setVisibility(View.VISIBLE);
        ma.setTitle(getString(R.string.conference));
        BusProvider.get().register(this);
        BusProvider.get().post(new GetConferenceEvent(conferenceID));
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
    public void onReceiveLectureEvent(ReceiveLectureEvent e) {
        if (e.getErrorMSG() != null) {
            Toast.makeText(ma, "Unable to getConferenceList this page, please try again later", Toast.LENGTH_SHORT).show();
        } else {
            Conference conference = e.getConference();
            final User conferencier = e.getConferencier();

            loadingView.setVisibility(View.GONE);
            getActivity().setTitle(conference.getName());
            titleTextView.setText(conference.getName());
//            dateTextView.setText(Dates.parse3339ToReadable(conference.getDate()));

            Picasso.with(ma).load(RESTClient.API_URL + "/Picture/Conference/" + conference.getId())
                    .placeholder(R.drawable.default_avatar)
                    .into(profil_pictureImageView);



            if (Dates.compareTime(conference.getTime()) < 0) {
//                registerCheckBox.setText(ma.getString(R.string.event_passed));
//                registerCheckBox.setActivated(false);
//                registerCheckBox.setClickable(false);
            } else if (conference.getOwner() == ma.getUserID()) {
//                registerCheckBox.setText(ma.getString(R.string.you_are_the_owner));
//                registerCheckBox.setActivated(false);
//                registerCheckBox.setClickable(false);
            } else if (conference.isRegistered()) {
                FAB.setIcon(R.drawable.ic_remove_white_24dp);
                FAB.setTag(true);
                FAB.setVisibility(View.VISIBLE);
            } else {
                FAB.setIcon(R.drawable.ic_add_white_24dp);
                FAB.setTag(false);
                FAB.setVisibility(View.VISIBLE);

            }
            FAB.setOnClickListener(onCheckboxFollowClickListener);

            mLectureDataStatePageAdapter = new LectureDataStatePageAdaptater(getChildFragmentManager(), conference, conferencier);
            mViewPager = (ViewPager) rootView.findViewById(R.id.pager);
            mViewPager.setAdapter(mLectureDataStatePageAdapter);
            rootView.invalidate();
            Log.i(TAG, "le view pager fait " + mViewPager.getHeight() + "px");
            Log.i(TAG, "l'adaptateur est bien callé");

        }
    }


    private View.OnClickListener onCheckboxFollowClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
//            Log.v(TAG, "Actual state: " + registerCheckBox.isChecked());
            if ((boolean) FAB.getTag()) {
                BusProvider.get().post(new ChangeConferenceRegisterStatusEvent(conferenceID, false));
                FAB.setIcon(R.drawable.ic_add_white_24dp);
                Log.v(TAG, "uncheck");
            } else {
                BusProvider.get().post(new ChangeConferenceRegisterStatusEvent(conferenceID, true));
                FAB.setIcon(R.drawable.ic_remove_white_24dp);
                Log.v(TAG, "check");
            }
        }
    };


    private class LectureDataStatePageAdaptater extends FragmentStatePagerAdapter {
        private static final String TAG = "DemoCollectionPagerAdapter";
        private Conference conference;

        private User conferencier;

        public LectureDataStatePageAdaptater(FragmentManager fm, Conference conference, User conferencier) {
            super(fm);
            Log.d(TAG, "creation de l'adaptater");
            this.conference = conference;
            this.conferencier = conferencier;
        }

        @Override
        public android.support.v4.app.Fragment getItem(int i) {
            Log.d("TabManager", "on demande le tab " + i);
            android.support.v4.app.Fragment fragment;
            Gson g = new Gson();
            switch (i) {
                case 0:
                    fragment = ConferenceAproposFragment.createFragment(g.toJson(conference), g.toJson(conferencier));
                    break;
                case 1:
                    fragment = UsersListFragment.createFragment(UsersListFragment.UserListType.CONFERENCE_ATTENDENT, conferenceID, null);
                    break;

                default:
                    Log.e(TAG, "Ask for an inexisting tab");
                    fragment = null;
            }

            return fragment;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.infos);
                case 1:
                    return getString(R.string.participants);
                default:
                    return "Ask title for an inexisting tab";
            }
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }

}
