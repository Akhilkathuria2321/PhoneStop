package com.alpgeeks.phonestop.home;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.alpgeeks.phonestop.R;
import com.alpgeeks.phonestop.navigation.AppListFragment;
import com.alpgeeks.phonestop.navigation.PrefsFragment;

import java.util.ArrayList;


// FIXME : I've added a sample layout for this
// actual fragment to be taken from prashant and put here for home screen
public class HomeFragment extends Fragment{
    private final static String LOG_TAG = HomeFragment.class.getSimpleName();
    ListView lv;
    ArrayList<String> appList;
    ArrayAdapter arrayAdapter;
    private ListView mDrawerList;
    private PrefsFragment prefsFragment;
    Button mDisableButton;
    Button mStartButton;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedhomInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        android.support.v4.app.Fragment fragment1 = new AppListFragment();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment1);
        transaction.addToBackStack(null);
        transaction.commit();
        return rootView;
    }


/*Old with two buttons
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedhomInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

  final Intent intent = new Intent(getActivity(),AppBlockService.class);
        mDisableButton = (Button)rootView.findViewById(R.id.disable_button);
        mDisableButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast t = Toast.makeText(getContext(), "PhoneStop Disabled", Toast.LENGTH_LONG);
                t.show();
                getActivity().stopService(intent);

            }
        });


        mStartButton = (Button)rootView.findViewById(R.id.start_button);
        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {


                    try {
                        PackageManager packageManager = getActivity().getPackageManager();
                        ApplicationInfo applicationInfo = packageManager.getApplicationInfo(getActivity().getPackageName(), 0);
                        AppOpsManager appOpsManager = (AppOpsManager) getActivity().getSystemService(Context.APP_OPS_SERVICE);
                        int mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, applicationInfo.uid, applicationInfo.packageName);

                        if (mode != AppOpsManager.MODE_ALLOWED) {
                            Toast.makeText(getContext(),"Enable the permission to use PhoneStop",Toast.LENGTH_SHORT).show();
                            Intent intent1 = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                            startActivity(intent1);
                        }
                    } catch (PackageManager.NameNotFoundException e) {

                        Intent intent1 = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                        startActivity(intent1);

                    }
                    getActivity().startService(intent);
                    getActivity().finish();




                } catch (Exception e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });




        return rootView;
    }
    */



    /*private void displayAppList() {
        appList = new ArrayList<>();
        appList.add(new Application("App1"));
        appList.add(new Application("App2"));
        appList.add(new Application("App3"));
        appList.add(new Application("App4"));
        appList.add(new Application("App5"));
        appList.add(new Application("App5"));
        appList.add(new Application("App5"));
        appList.add(new Application("App5"));
        appList.add(new Application("App5"));
        appList.add(new Application("App5"));


        appListAdapter = new ApplicationListAdapter(appList,getActivity());
        lv.setAdapter(appListAdapter);
    }*/

    /*@Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            int pos = lv.getPositionForView(buttonView);
            if(pos != ListView.INVALID_POSITION) {
                Application app = appList.get(pos);
                app.setSelected(isChecked);
                String msg = "Clicked on : "+app.getAppName()+"State : "+isChecked;

                Toast t = Toast.makeText(getActivity(),msg,Toast.LENGTH_SHORT);
                t.show();
            }
    }*/
}