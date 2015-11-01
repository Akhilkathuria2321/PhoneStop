package com.alpgeeks.phonestop.home;

import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.alpgeeks.phonestop.R;
import com.alpgeeks.phonestop.intro.IntroActivity;
import com.alpgeeks.phonestop.navigation.AboutFragment;
import com.alpgeeks.phonestop.navigation.BlankFragment;
import com.alpgeeks.phonestop.navigation.GetHelpFragment;
import com.alpgeeks.phonestop.navigation.PrefsFragment;
import com.alpgeeks.phonestop.navigation.ProfileListFragment;

import java.util.ArrayList;

// FIXME : Add toast messages if the user is trying to cheat
// FIXME : update the classes with Model view details etc.

/**
 * Main Activity for PhoneStop Application. It contains navigation drawer to
 * navigate across functionalities.
 */
public class HomeActivity extends ActionBarActivity {
    private final static String LOG_TAG = HomeActivity.class.getSimpleName();
    private Toolbar toolbar;
    private String[] mOptionTitles;
    private DrawerLayout
            mDrawerLayout;
    private ListView mDrawerList;
    private ArrayAdapter<String> mOptionAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private String mActivityTitle;
    private PrefsFragment prefsFragment;

    private DrawerArrowDrawable mDrawerArrow;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //Prashant
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        mTitle = mDrawerTitle = getString(R.string.title_statistics);;

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);
        if(fragment == null) {
            fragment = new HomeFragment();
            fm.beginTransaction().add(R.id.fragment_container,fragment).commit();
        }

        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mActivityTitle = getString(R.string.title_phonestop);

        mDrawerList = (ListView)findViewById(R.id.navList);
        addDrawerItems();

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction transaction = fm.beginTransaction();
                Fragment fragment1;
                switch (position) {
                    case 0:
                        mActivityTitle = getString(R.string.title_phonestop);
                        if(prefsFragment != null)
                            getFragmentManager().beginTransaction().remove(prefsFragment).commit();
                        fragment1 = new HomeFragment();
                        transaction.replace(R.id.fragment_container,fragment1).commit();
                        mDrawerLayout.closeDrawers();
                        break;

                    case 1 :
                        mActivityTitle = getString(R.string.title_statistics);
                        Toast.makeText(HomeActivity.this, "Statistics", Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        mActivityTitle = getString(R.string.title_get_help);
                        if(prefsFragment != null)
                            getFragmentManager().beginTransaction().remove(prefsFragment).commit();

                        fragment1 = new GetHelpFragment();
                        mDrawerLayout.closeDrawers();
                        transaction.replace(R.id.fragment_container, fragment1);
                        transaction.addToBackStack(null);
                        transaction.commit();
                        break;
                    case 3:
                        mActivityTitle = getString(R.string.title_profiles);
                        if(prefsFragment != null)
                            getFragmentManager().beginTransaction().remove(prefsFragment).commit();

                        fragment1 = new ProfileListFragment();
                        mDrawerLayout.closeDrawers();
                        transaction.replace(R.id.fragment_container,fragment1);
                        transaction.addToBackStack(null);
                        transaction.commit();
                        break;
                    case 4:
                        mActivityTitle = getString(R.string.title_settings);
                        mDrawerLayout.closeDrawers();
                        prefsFragment = new PrefsFragment();
                        transaction.replace(R.id.fragment_container,new BlankFragment());
                        getFragmentManager().beginTransaction().replace(R.id.fragment_container, prefsFragment).commit();
                        transaction.addToBackStack(null);
                        transaction.commit();
                        break;
                    case 5:
                        mActivityTitle = getString(R.string.title_intro);
                        if(prefsFragment != null)
                            getFragmentManager().beginTransaction().remove(prefsFragment).commit();

                        Intent intent = new Intent(HomeActivity.this,IntroActivity.class);
                        startActivity(intent);
                        break;
                    case 6:
                        mActivityTitle = getString(R.string.title_about);
                        if(prefsFragment != null)
                            getFragmentManager().beginTransaction().remove(prefsFragment).commit();
                        fragment1 = new AboutFragment();
                        mDrawerLayout.closeDrawers();
                        transaction.replace(R.id.fragment_container,fragment1);
                        transaction.addToBackStack(null);
                        transaction.commit();
                        break;
                    default:
                        Toast.makeText(HomeActivity.this, "Time for an upgrade!", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

        // Define drawer operations and change ActionBar title
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerOpened(View view) {
                super.onDrawerOpened(view);
                getSupportActionBar().setTitle(getString(R.string.title_phonestop));
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
            @Override
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu : This adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    private void addDrawerItems() {
        Resources resources = getResources();
        String[] mDrawerItems = resources.getStringArray(R.array.drawer_items);
        mOptionAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mDrawerItems);
        mDrawerList.setAdapter(mOptionAdapter);
    }
}