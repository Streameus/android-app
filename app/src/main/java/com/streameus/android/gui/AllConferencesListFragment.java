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


public class AllConferencesListFragment extends Fragment {


    private LecturesPagerAdapter mLecturesPagerAdapter;
    private View rootView;
    private ViewPager mViewPager;

    public AllConferencesListFragment() {
        // Required empty public constructor
    }

    public static Fragment createFragment() {
        return new AllConferencesListFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_viewpager, container, false);
        mLecturesPagerAdapter = new LecturesPagerAdapter(getChildFragmentManager());
        mViewPager = (ViewPager) rootView.findViewById(R.id.pager);
        mViewPager.setAdapter(mLecturesPagerAdapter);
        ((MainActivity) getActivity()).setTitle(getString(R.string.conferences));
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private class LecturesPagerAdapter extends FragmentStatePagerAdapter {
        private static final String TAG = "DemoCollectionPagerAdapter";

        public LecturesPagerAdapter(FragmentManager fm) {
            super(fm);
            Log.d(TAG, "creation de l'adaptater");
        }

        @Override
        public android.support.v4.app.Fragment getItem(int i) {
            Log.d("TabManager", "on demande le tab " + i);
            android.support.v4.app.Fragment fragment;
            switch (i) {

                case 0:
                    fragment = CategoriesListFragment.createFragment();
                    break;
                case 1:
                    fragment = ConferencesListFragment.createFragment(ConferencesListFragment.ConferenceListType.RECOMMANDED, 0);
                    break;
                case 2:
                    fragment = ConferencesListFragment.createFragment(ConferencesListFragment.ConferenceListType.LIVE, 0);
                    break;
                case 3:
                    fragment = ConferencesListFragment.createFragment(ConferencesListFragment.ConferenceListType.SOON, 0);
                    break;
                case 4:
                    fragment = ConferencesListFragment.createFragment(ConferencesListFragment.ConferenceListType.ALL, 0);
                    break;
                default:
                    Log.e(TAG, "Ask for an inexisting tab");
                    fragment = null;
            }

            return fragment;
        }

        @Override
        public int getCount() {
            return 5;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.all_categories);
                case 1:
                    return getString(R.string.recommanded);
                case 2:
                    return getString(R.string.live);
                case 3:
                    return getString(R.string.soon);
                case 4:
                    return getString(R.string.all_the_conferences);
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
