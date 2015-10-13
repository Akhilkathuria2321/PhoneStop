package com.alpgeeks.phonestop;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

// FIXME : Add animation to the hamburger icon
// FIXME : Add listener to the hanburger icon
// FIXME : Add toast messages if the user is trying to cheat
// FIXME : update the classes with Model view details etc.
// FIXME : Flash screen with mobile symbol and
// Phone Stop...Put a full stop to your phone addiction
public class HomeActivity extends ActionBarActivity {

    private String[] mOptionTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ArrayAdapter<String> mOptionAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private String mActivityTitle;


    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);
        if(fragment == null) {
            fragment = new HomeFragment();
            fm.beginTransaction().add(R.id.fragment_container,fragment).commit();
        }

        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mActivityTitle = getTitle().toString();

        mDrawerList = (ListView)findViewById(R.id.navList);
        addDrawerItems();
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FragmentManager fm = getSupportFragmentManager();
                Fragment fragment1;
                switch (position) {
                    case 0:
                        fragment1 = new HomeFragment();
                        //fm.beginTransaction().add(R.id.fragment_container,fragment1).commit();
                        fm.beginTransaction().replace(R.id.fragment_container,fragment1).commit();
                        mDrawerLayout.closeDrawers();
                        Toast.makeText(HomeActivity.this, "Home!", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        // Show GetHelpFragment
                      //  fragment1 = new GetHelpFragment();
                        // if(fragment1 == null) {
                        //   fragment1 = new HomeFragment();
                      //  fm.beginTransaction().add(R.id.fragment_container,fragment1).commit();
                       // mDrawerLayout.closeDrawers();
                        //}
                        Toast.makeText(HomeActivity.this, "Get Help!", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(HomeActivity.this, "Time for an upgrade!", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerOpened(View view) {
                super.onDrawerOpened(view);

                //getActionBar().setTitle("Navigation!");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
            @Override
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                //getActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        // getActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);
        // getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    private void addDrawerItems() {
        String[] osArray = { "Home", "Get Help", "Intro", "Settings", "About" };
        mOptionAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, osArray);
        mDrawerList.setAdapter(mOptionAdapter);
    }

    private void setUpDrawer() {

    }
}