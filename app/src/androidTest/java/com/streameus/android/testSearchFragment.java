

package com.streameus.android;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.widget.RelativeLayout;

import com.streameus.android.bus.BusProvider;
import com.streameus.android.bus.ReceiveUserList;
import com.streameus.android.dataProvider.DataService;
import com.streameus.android.dataProvider.RESTClient;
import com.streameus.android.gui.MainActivity;
import com.streameus.android.gui.SearchFragment;
import com.streameus.android.gui.UsersListFragment;

import java.util.List;

/**
 * Created by Pol on 15/08/14.
 */
public class testSearchFragment extends ActivityInstrumentationTestCase2<MainActivity> {
    MainActivity a = null;
    SearchFragment searchFragment = null;
    private String TAG = "testSearchFragment";
    RESTClient restClient;

    public testSearchFragment() {
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        Intent i = new Intent();
        i.putExtra(MainActivity.INTENT_PARAM_USERID, 48);
        setActivityIntent(i);
        searchFragment = SearchFragment.createFragment();
        a = getActivity();
        a.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                DataService.setDebug();
                a.loadFragment(searchFragment);
            }
        });
        getInstrumentation().waitForIdleSync();
    }

    public void testPrecondition() {
        List<Fragment> fragmentList = a.getSupportFragmentManager().getFragments();
        a.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                DataService.removeCurrentService();
            }
        });
        getInstrumentation().waitForIdleSync();
        Log.d(TAG, "PEEEENIS");

        for (Fragment f : fragmentList) {
            Log.d(TAG, "Name: " + f.getClass().getSimpleName());
            if (f instanceof SearchFragment) {
                assertTrue(true);
                return;
            }
        }
        assertTrue("SearchFragment not started", false);
    }

    public void testErrorSearch() {
        a.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                BusProvider.get().post(new ReceiveUserList(UsersListFragment.UserListType.SEARCH, "Une erreur est survenue"));
            }
        });
        getInstrumentation().waitForIdleSync();
        List<Fragment> fragmentList = searchFragment.getChildFragmentManager().getFragments();
        if (fragmentList.size() != 2) {
            assertTrue(false);
        }
        for (Fragment f : fragmentList) {
            if (f instanceof UsersListFragment) {
                RelativeLayout relativeLayout = (RelativeLayout) f.getView().findViewById(R.id.error_view);
                assertNotNull(relativeLayout);
                assertEquals(relativeLayout.getVisibility(), RelativeLayout.VISIBLE);

                relativeLayout = (RelativeLayout) f.getView().findViewById(R.id.loading);
                assertNotNull(relativeLayout);
                assertEquals(relativeLayout.getVisibility(), RelativeLayout.GONE);
                return;
            }
        }
        assertTrue("Fragment introuvable", false);
    }

    public void testDisplayLoading() {
        List<Fragment> fragmentList = searchFragment.getChildFragmentManager().getFragments();
        if (fragmentList.size() != 2) {
            assertTrue(false);
        }
        for (Fragment f : fragmentList) {
            if (f instanceof UsersListFragment) {
                RelativeLayout relativeLayout = (RelativeLayout) f.getView().findViewById(R.id.error_view);
                assertNotNull(relativeLayout);
                assertEquals(relativeLayout.getVisibility(), RelativeLayout.GONE);

                relativeLayout = (RelativeLayout) f.getView().findViewById(android.R.id.empty);
                assertNotNull(relativeLayout);
                assertEquals(relativeLayout.getVisibility(), RelativeLayout.VISIBLE);
                return;
            }
        }
        assertTrue("Fragment introuvable", false);
    }

    public void testFoundResult() {
        a.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                BusProvider.get().post(new ReceiveUserList(UsersListFragment.UserListType.SEARCH, TestValues.getUserList()));
            }
        });
        getInstrumentation().waitForIdleSync();
        List<Fragment> fragmentList = searchFragment.getChildFragmentManager().getFragments();
        if (fragmentList.size() != 2) {
            assertTrue(false);
        }
        for (Fragment f : fragmentList) {
            if (f instanceof UsersListFragment) {
                RelativeLayout relativeLayout = (RelativeLayout) f.getView().findViewById(R.id.error_view);
                assertNotNull(relativeLayout);
                assertEquals(relativeLayout.getVisibility(), RelativeLayout.GONE);

                relativeLayout = (RelativeLayout) f.getView().findViewById(android.R.id.empty);
                assertNotNull(relativeLayout);
                assertEquals(relativeLayout.getVisibility(), RelativeLayout.GONE);

                assertEquals(((UsersListFragment) f).getListAdapter().getCount(), TestValues.getUserList().size());
                return;
            }
        }
        assertTrue("Fragment introuvable", false);
    }


}
