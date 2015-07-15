package com.streameus.android.gui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;
import com.streameus.android.R;
import com.streameus.android.bus.BusProvider;
import com.streameus.android.bus.FollowUserEvent;
import com.streameus.android.bus.GetProfilDataEvent;
import com.streameus.android.bus.ReceiveProfilDataEvent;
import com.streameus.android.bus.UnfollowUserEvent;
import com.streameus.android.dataProvider.RESTClient;
import com.streameus.android.model.User;

public class UserFragment extends Fragment {
    private static final String TAG = "PROFILE-FRAGMENT";
    private MainActivity ma;
    public static String INTENT_PARAMETER_USERID = "userid";

    private ImageView profilPictureImage;
    private TextView profilNameTextView;
    //    private CheckBox followCheckBox;
    private View rootView;
    private View loadingView;
    private User user = null;
    private int userID;
    private ProfileCollectionPagerAdapter mDemoCollectionPagerAdapter;
    private ViewPager mViewPager;
    //    private TextView mUserGradeTextView;
    private FloatingActionButton FAB;

    public static Fragment createFragment(int userID) {
        Fragment fragment = new UserFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(UserFragment.INTENT_PARAMETER_USERID, userID);
        fragment.setArguments(bundle);
        return fragment;
    }

    private class ProfileCollectionPagerAdapter extends FragmentStatePagerAdapter {
        private static final String TAG = "DemoCollectionPagerAdapter";

        public ProfileCollectionPagerAdapter(FragmentManager fm) {
            super(fm);
            Log.d(TAG, "creation de l'adaptater");
        }

        @Override
        public Fragment getItem(int i) {
            Log.d("TabManager", "on demande le tab " + i);
            Fragment fragment;
            switch (i) {
                case 0:
                    fragment = NewsFeedFragment.createFragment(userID);
                    break;
                case 1:
                    fragment = UsersListFragment.createFragment(UsersListFragment.UserListType.USER_FOLLOWING, userID, null);
                    break;
                case 2:
                    fragment = UsersListFragment.createFragment(UsersListFragment.UserListType.USER_FOLLOWER, userID, null);
                    break;
                case 3:
                    fragment = ConferencesListFragment.createFragment(ConferencesListFragment.ConferenceListType.USER, userID);
                    break;

                case 4:
                    fragment = ConferencesListFragment.createFragment(ConferencesListFragment.ConferenceListType.ATTENDED, userID);
                    break;
                case 5:
                    fragment = ConferencesListFragment.createFragment(ConferencesListFragment.ConferenceListType.REGISTERED, userID);
                    break;
                case 6:
                    fragment = UserAproposFragment.creatFragment(user.getPropertyDisplayable());
                    break;
                default:
                    Log.e(TAG, "Ask for an inexisting tab");
                    fragment = null;
            }

            return fragment;
        }

        @Override
        public int getCount() {
            return 7;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.fluxdactu);
                case 1:
                    return getString(R.string.following);
                case 2:
                    return getString(R.string.followers);
                case 3:
                    return getString(R.string.his_conferences);
                case 4:
                    return getString(R.string.took_part);
                case 5:
                    return getString(R.string.subscribe_to);
                case 6:
                    return getString(R.string.apropos);
                default:
                    return "What ? An other tab ?!";
            }
        }


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ma = (MainActivity) getActivity();
        ma.setTitle(getString(R.string.profil));
        userID = getArguments().getInt(INTENT_PARAMETER_USERID);

        rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        mDemoCollectionPagerAdapter = new ProfileCollectionPagerAdapter(getChildFragmentManager());
        mViewPager = (ViewPager) rootView.findViewById(R.id.pager);
        mViewPager.setAdapter(mDemoCollectionPagerAdapter);
        profilPictureImage = (ImageView) rootView.findViewById(R.id.profil_picture);
        profilNameTextView = (TextView) rootView.findViewById(R.id.profil_user_name);
        loadingView = rootView.findViewById(R.id.loading);
        FAB = (FloatingActionButton) rootView.findViewById(R.id.floatingactionbutton);

        mViewPager.setAdapter(mDemoCollectionPagerAdapter);
        BusProvider.get().register(this);
        BusProvider.get().post(new GetProfilDataEvent(userID, !(userID == ma.getUserID())));
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
        Log.d(TAG, "onPause");
        BusProvider.get().unregister(this);
    }

    @Subscribe
    public void OnReceiveProfilDataEvent(ReceiveProfilDataEvent e) {

        loadingView.setVisibility(View.GONE);
        user = e.getUser();

        if (user == null) {
            Toast.makeText(ma, "unable to retrieve profil", Toast.LENGTH_SHORT).show();
        } else {
            Log.v(TAG, "displayProfil called with param = " + user.getId());
            ((MainActivity) getActivity()).setTitle(user.getPseudo());
//            mUserGradeTextView.setText(user.getReputation().intValue() + "");
//            ((TextView) rootView.findViewById(R.id.userGradeMax)).setText("/5");
            if (user.getId() != ma.getUserID()) {
                FAB.setVisibility(View.VISIBLE);
                if (e.isFollowing()) {
                    FAB.setIcon(R.drawable.ic_remove_white_24dp);
                    FAB.setTag(true);
                } else {
                    FAB.setIcon(R.drawable.ic_add_white_24dp);
                    FAB.setTag(false);
                }
                FAB.setOnClickListener(onCheckboxFollowClickListener);

            }

            profilNameTextView.setText(user.getPseudo());
            Picasso.with(ma).load(RESTClient.API_URL + "/Picture/User/" + user.getId())
                    .placeholder(R.drawable.default_avatar)
                    .into(profilPictureImage);
        }
    }

    private OnClickListener onCheckboxFollowClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {

            if ((boolean) FAB.getTag()) {
                BusProvider.get().post(new UnfollowUserEvent(userID));
                FAB.setIcon(R.drawable.ic_add_white_24dp);
                Log.v(TAG, "uncheck");
            } else {
                BusProvider.get().post(new FollowUserEvent(userID));
                FAB.setIcon(R.drawable.ic_remove_white_24dp);
                Log.v(TAG, "check");
            }

        }
    };

}
