package com.streameus.android.gui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.streameus.android.R;
import com.streameus.android.utils.StreameusPreferences;

public class DemoActivity extends FragmentActivity {

    ViewPager viewPager;
    Button letsStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (StreameusPreferences.hasAlreadySeenDemo(this)) {
            startActivity(LoginActivity.createIntent(this));
            finish();
        }

        setContentView(R.layout.activity_service_preview);

        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(new DemoPageAdaptater(getSupportFragmentManager()));

        letsStart = (Button) findViewById(R.id.discoverButton);
        letsStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StreameusPreferences.setHaveAlreadySeenDemo(DemoActivity.this, true);
                startActivity(LoginActivity.createIntent(DemoActivity.this));
                finish();
            }
        });
    }


    private class DemoPageAdaptater extends FragmentStatePagerAdapter {
        private static final String TAG = "DemoCollectionPagerAdapter";

        public DemoPageAdaptater(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            Log.d("TabManager", "on demande le tab " + i);
            Fragment fragment;
            switch (i) {
                case 0:
                    fragment = DemoPageFragment.createFragment(R.drawable.conference_screenshot, "Consultez le details des conférences qui vous intéressent");
                    break;
                case 1:
                    fragment = DemoPageFragment.createFragment(R.drawable.profil_screenshot, "Suivez les personnes qui vous inspirent");
                    break;
                case 2:
                    fragment = DemoPageFragment.createFragment(R.drawable.news_feed_screenshot, "Restez au courant de l'actualitée de Streameus");
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
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.service_preview, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
