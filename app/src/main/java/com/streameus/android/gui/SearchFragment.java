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
import android.widget.SearchView;

import com.streameus.android.R;
import com.streameus.android.bus.BusProvider;
import com.streameus.android.bus.SearchEvent;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class SearchFragment extends Fragment {

    private View rootView;
    private ViewPager mViewPager;
    private SearchView searchView;
    private String TAG = "SearchFragment";


    public SearchFragment() {
        // Required empty public constructor
    }

    public static SearchFragment createFragment() {
        return new SearchFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((MainActivity) getActivity()).setTitle(getString(R.string.search));

        rootView = inflater.inflate(R.layout.fragment_search, container, false);
        searchView = (SearchView) rootView.findViewById(R.id.searchView);
        searchView.setIconified(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                if (s.length() >= 2) {
                    Log.v(TAG, "We submitted " + s);
                    BusProvider.get().post(new SearchEvent(s));
                }
                return false;
            }
        });

        ConferencesListPagerAdapter mLecturesPagerAdapter = new ConferencesListPagerAdapter(getChildFragmentManager());
        mViewPager = (ViewPager) rootView.findViewById(R.id.pager);
        mViewPager.setAdapter(mLecturesPagerAdapter);
        return rootView;
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        mViewPager.setAdapter(mLecturesPagerAdapter);
//
//    }

    private class ConferencesListPagerAdapter extends FragmentStatePagerAdapter {
        private static final String TAG = "DemoCollectionPagerAdapter";

        public ConferencesListPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int i) {
            Log.d("TabManager", "on demande le tab " + i);
            android.support.v4.app.Fragment fragment;
            switch (i) {
                case 0:
                    fragment = UsersListFragment.createFragment(UsersListFragment.UserListType.SEARCH, 0, null);
                    break;
                case 1:
                    fragment = ConferencesListFragment.createFragment(ConferencesListFragment.ConferenceListType.SEARCH, 0);
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
                    return getString(R.string.users);
                case 1:
                    return getString(R.string.conferences);

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
