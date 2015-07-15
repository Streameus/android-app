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

import com.streameus.android.R;


public class AllUsersListsFragment extends Fragment {


    private static String INTENT_PARAM_USER_ID = "INTENT_PARAM_USER_ID";
    private UserListPagerAdapter mLecturesPagerAdapter;
    private View rootView;
    private ViewPager mViewPager;
    private int userID;
    public AllUsersListsFragment() {
        // Required empty public constructor
    }

    public static Fragment createFragment(int userID) {
        Fragment f = new AllUsersListsFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(INTENT_PARAM_USER_ID, userID);
        f.setArguments(bundle);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        userID = getArguments().getInt(INTENT_PARAM_USER_ID);
        rootView = inflater.inflate(R.layout.fragment_viewpager, container, false);
        mLecturesPagerAdapter = new UserListPagerAdapter(getChildFragmentManager());
        mViewPager = (ViewPager) rootView.findViewById(R.id.pager);
        mViewPager.setAdapter(mLecturesPagerAdapter);
        ((MainActivity) getActivity()).setTitle(getString(R.string.users));
        return rootView;
    }


    private class UserListPagerAdapter extends FragmentStatePagerAdapter {
        private static final String TAG = "UserListPagerAdapter";

        public UserListPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            Log.d("TabManager", "on demande le tab " + i);
            Fragment fragment;
            switch (i) {
                case 0:
                    fragment = UsersListFragment.createFragment(UsersListFragment.UserListType.USER_FOLLOWING, userID, null);
                    break;
                case 1:
                    fragment = UsersListFragment.createFragment(UsersListFragment.UserListType.RECOMMANDED_FRIEND, 0, null);
                    break;
                case 2:
                    fragment = UsersListFragment.createFragment(UsersListFragment.UserListType.ALL_USERS, 0, null);
                    break;
                default:
                    Log.e(TAG, "Ask for an inexisting tab");
                    fragment = null;
            }

            return fragment;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.your_friends);
                case 1:
                    return getString(R.string.user_recommanded_for_you);
                case 2:
                    return getString(R.string.all_users);
                default:
                    return "What ? An other tab ?!";
            }
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }

}
