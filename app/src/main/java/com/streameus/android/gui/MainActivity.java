package com.streameus.android.gui;


import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.squareup.otto.Subscribe;
import com.streameus.android.R;
import com.streameus.android.bus.BusProvider;
import com.streameus.android.bus.GetResourceEvent;
import com.streameus.android.bus.ReceiveResourceEvent;
import com.streameus.android.dataProvider.DataService;
import com.streameus.android.model.StreameusResources;
import com.streameus.android.utils.MenuAdapter;

public class MainActivity extends ActionBarActivity {
    public static CharSequence mTitle = "";
    static public String INTENT_PARAM_USERID = "userID";
    public static String TAG = "MAINACTIVITY";

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ListView mDrawerView;
    private MainActivity ma;
    private StreameusResources streameusResources;
    private int userID;
    boolean firstFragment = true;

    private final int MENU_ITEM_NEWSFEED = 0;
    private final int MENU_ITEM_PROFIL = 1;
    private final int MENU_ITEM_AGENDA = 2;
    private final int MENU_ITEM_CONFERENCES = 3;
    private final int MENU_ITEM_MYCONTACTS = 4;

    int[][] menuItems = {
            {R.drawable.ic_action_view_as_list, R.string.fluxdactu, MENU_ITEM_NEWSFEED},
            {R.drawable.ic_action_person, R.string.profil, MENU_ITEM_PROFIL},
            {R.drawable.ic_action_go_to_today, R.string.agenda, MENU_ITEM_AGENDA},
            {R.drawable.ic_action_event, R.string.conferences, MENU_ITEM_CONFERENCES},
            {R.drawable.ic_action_group, R.string.mycontacts, MENU_ITEM_MYCONTACTS}};


    public static Intent createIntent(Context c, int userID) {
        Log.v(TAG, "create a new instance of streameus logged with userID == " + userID);
        Intent i = new Intent(c, MainActivity.class);
        i.putExtra(INTENT_PARAM_USERID, userID);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_layout);

        ma = this;
        userID = getIntent().getExtras().getInt(INTENT_PARAM_USERID);
        try {
            DataService.get(this);
        } catch (DataService.NotLoggedInException e) {
            Toast.makeText(this, getString(R.string.not_connected_toast), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerView = (ListView) findViewById(R.id.left_drawer);
        mDrawerView.setAdapter(new MenuAdapter(this, getLayoutInflater(), menuItems));
        mDrawerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mDrawerView.setItemChecked(position, true);
                openFragment(Math.round(id));
            }
        });
        mDrawerToggle = new ActionBarDrawerToggle(this, /* host Activity */
                mDrawerLayout, /* DrawerLayout object */
                R.drawable.ic_drawer, /* nav drawer icon to replace 'Up' caret */
                R.string.drawer_open, /* "open drawer" description */
                R.string.drawer_close /* "close drawer" description */
        ) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mTitle);
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle(R.string.menu);
            }
        };

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            openFragment(MENU_ITEM_NEWSFEED);
        }

        updateResources();

        BusProvider.get().register(this);
    }

    public void updateResources() {
        BusProvider.get().post(new GetResourceEvent());
    }


    public void openFragment(int i) {
        Fragment fragment = null;

        switch (i) {
            case 0:
                fragment = NewsFeedFragment.createFragment(0);
                break;
            case 1:
                fragment = UserFragment.createFragment(userID);
                break;
            case 2:
                fragment = AgendaFragment.createFragment();
                break;
            case 3:
                fragment = AllConferencesListFragment.createFragment();
                break;
            case 4:
                fragment = AllUsersListsFragment.createFragment(userID);
                break;
        }
        loadFragment(fragment);

    }

    public void loadFragment(Fragment fragment) {
        // Insert the fragment by replacing any existing fragment
        if (firstFragment) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, fragment).commit();
            firstFragment = false;
        } else {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, fragment).addToBackStack(null).commit();
        }

        mDrawerLayout.closeDrawer(mDrawerView);
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...
        if (!ActivityManager.isUserAMonkey()) {

            Fragment fragment = null;
            switch (item.getItemId()) {
                case R.id.drawer_menuItem_Search:
                    fragment = SearchFragment.createFragment();
                    break;
                case R.id.drawer_menuItem_Aide:
                    fragment = WebViewFragment.createFragment(WebViewFragment.WEBVIEW_FAQ);
                    break;
                case R.id.drawer_menuItem_A_Propos:
                    fragment = WebViewFragment.createFragment(WebViewFragment.WEBVIEW_ABOUT);
                    break;
//            case R.id.drawer_menuItem_l_equipe:
//                fragment = WebViewFragment.createFragment(WebViewFragment.WEBVIEW_TEAM);
//                break;
                case R.id.drawer_menuItem_Deconnexion:
                    fragment = new LogoutFragment();
                    break;

                default:
                    return super.onOptionsItemSelected(item);
            }
            loadFragment(fragment);
            return true;
        } else {
            return super.onOptionsItemSelected(item);

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    public int getUserID() {
        return userID;
    }

    @Subscribe
    public void onReceiveResourceEvent(ReceiveResourceEvent e) {
        if (e.getErrorMSG() != null) {
            Toast.makeText(ma, "Unable to retrieve webViewUrls information", Toast.LENGTH_SHORT).show();
            Log.i("Resources", "fail retriving webview urls");
        } else {
            streameusResources = e.get();
        }
    }


    public StreameusResources getStreameusResources() {
        return streameusResources;
    }
}
