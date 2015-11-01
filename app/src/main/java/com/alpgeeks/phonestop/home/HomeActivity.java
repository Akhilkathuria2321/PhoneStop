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

// FIXME : Add animation to the hamburger icon
// FIXME : Add listener to the hanburger icon
// FIXME : Add toast messages if the user is trying to cheat
// FIXME : update the classes with Model view details etc.
// FIXME : Flash screen with mobile symbol and
// Phone Stop...Put a full stop to your phone addiction
public class HomeActivity extends ActionBarActivity {

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
//Prashant
       // mDrawerArrow = new DrawerArrowDrawable(this);
       // mDrawerArrow.setColor(0xffffffff);
       // mDrawerArrow.setSpinEnabled(true);

        mTitle = mDrawerTitle = "Title";

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);
        if(fragment == null) {
            fragment = new HomeFragment();
            fm.beginTransaction().add(R.id.fragment_container,fragment).commit();
        }

        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mActivityTitle = "PhoneStop";

        mDrawerList = (ListView)findViewById(R.id.navList);
        addDrawerItems();

        /*getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(mDrawerArrow);
        getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);*/


        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction transaction = fm.beginTransaction();
                Fragment fragment1;
                switch (position) {
                    case 0:
                        mActivityTitle = "PhoneStop";
                        if(prefsFragment != null)
                            getFragmentManager().beginTransaction().remove(prefsFragment).commit();
                        fragment1 = new HomeFragment();
                        transaction.replace(R.id.fragment_container,fragment1).commit();
                        mDrawerLayout.closeDrawers();
                        break;

                    case 1 :
                        mActivityTitle = "Statistics";
                     ArrayList sms = new ArrayList();
                    Uri uriSms = Uri.parse("content://sms/inbox");
                    Cursor cursor = getContentResolver().query(uriSms, new String[]{"_id", "address", "date", "body"}, null, null, null);

                    String msgData = "";
                    while  (cursor.moveToNext())
                    {
                        String address = cursor.getString(1);
                        String body = cursor.getString(3);

                        System.out.println("======&gt; Mobile number =&gt; "+address);
                        System.out.println("=====&gt; SMS Text =&gt; "+body);

                        sms.add("Address=&gt; "+address+"n SMS =&gt; "+body);
                    }
                    break;
                    case 2:
                        mActivityTitle = "Get Help";
                        if(prefsFragment != null)
                            getFragmentManager().beginTransaction().remove(prefsFragment).commit();

                        fragment1 = new GetHelpFragment();
                        mDrawerLayout.closeDrawers();
                        transaction.replace(R.id.fragment_container, fragment1);
                        transaction.addToBackStack(null);
                        transaction.commit();
                        break;
                    case 3:
                        mActivityTitle = "Profiles";
                        if(prefsFragment != null)
                            getFragmentManager().beginTransaction().remove(prefsFragment).commit();

                        fragment1 = new ProfileListFragment();
                        mDrawerLayout.closeDrawers();
                        transaction.replace(R.id.fragment_container,fragment1);
                        transaction.addToBackStack(null);
                        transaction.commit();
                        break;
                    case 4:
                        mActivityTitle = "Settings";
                        mDrawerLayout.closeDrawers();
                        prefsFragment = new PrefsFragment();
                        transaction.replace(R.id.fragment_container,new BlankFragment());
                        getFragmentManager().beginTransaction().replace(R.id.fragment_container, prefsFragment).commit();
                        transaction.addToBackStack(null);
                        transaction.commit();
                        break;
                    case 5:
                        mActivityTitle = "Intro";
                        if(prefsFragment != null)
                            getFragmentManager().beginTransaction().remove(prefsFragment).commit();

                        Intent intent = new Intent(HomeActivity.this,IntroActivity.class);
                        startActivity(intent);
                        break;
                    case 6:
                        mActivityTitle = "About";
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

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerOpened(View view) {
                super.onDrawerOpened(view);

               // getSupportActionBar().setHomeAsUpIndicator(R.drawable.icon);

                getSupportActionBar().setTitle("PhoneStop");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
            @Override
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
               //getSupportActionBar().setHomeAsUpIndicator(mDrawerArrow);

                getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

     /*      @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

                }*/

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
    /*    if (id == R.id.drawer_layout) {
            return true;
        }
        else if(id == 16908332) {
            if(mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
                mDrawerLayout.closeDrawer(Gravity.LEFT);
            }

            else
                mDrawerLayout.openDrawer(Gravity.LEFT);
        }*/

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

    private void setUpDrawer() {

    }
}

