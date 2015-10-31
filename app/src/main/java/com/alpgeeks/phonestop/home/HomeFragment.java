package com.alpgeeks.phonestop.home;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.alpgeeks.phonestop.R;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;


// FIXME : I've added a sample layout for this
// actual fragment to be taken from prashant and put here for home screen
public class HomeFragment extends Fragment{
    private final static String LOG_TAG = HomeFragment.class.getSimpleName();
    ListView lv;
    ArrayList<String> appList;
    ArrayAdapter arrayAdapter;

    Button mEnableButton;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedhomInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        lv = (ListView)rootView.findViewById(R.id.listView);

        final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        PackageManager pm = getActivity().getPackageManager();
        //   final List pkgAppsList = pm.queryIntentActivities(mainIntent, 0);
        TreeSet<String> results = new TreeSet<>();


        List<ResolveInfo> pkgAppsList = pm.queryIntentActivities(mainIntent,
                PackageManager.PERMISSION_GRANTED);
        for (ResolveInfo rInfo : pkgAppsList) {
            Log.v(LOG_TAG,rInfo.toString());
            results.add(rInfo.activityInfo.applicationInfo.loadLabel(pm)
                    .toString());
        }

        mEnableButton = (Button)rootView.findViewById(R.id.enable_button);
        mEnableButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast t = Toast.makeText(getContext(),"Kill",Toast.LENGTH_SHORT);
                t.show();
                ActivityManager am = (ActivityManager) getActivity().getSystemService(Activity.ACTIVITY_SERVICE);
                am.killBackgroundProcesses("com.alpgeeks.samples");
                am.killBackgroundProcesses("com.whatsapp");
                am.killBackgroundProcesses("com.toi.reader.activities");


                WifiManager wifi = (WifiManager) getActivity().getSystemService(getContext().WIFI_SERVICE);
                wifi.setWifiEnabled(false);
            }
        });

        arrayAdapter = new ArrayAdapter(getActivity(),
                R.layout.list_item_forecast,R.id.list_item_forecast_textview,results.toArray());

        ListView listView = (ListView)rootView.findViewById(R.id.listView);
        listView.setAdapter(arrayAdapter);



        //displayAppList();
        // Inflate the layout for this fragment
        return rootView;
    }

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