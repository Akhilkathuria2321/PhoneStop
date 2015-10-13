package com.alpgeeks.phonestop;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


// FIXME : I've added a sample layout for this
// actual fragment to be taken from prashant and put here for home screen

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment{
//    implements CompoundButton.OnCheckedChangeListener{

    ListView lv;
    ArrayList<String> appList;
    // ApplicationListAdapter appListAdapter;
    ArrayAdapter arrayAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        lv = (ListView)rootView.findViewById(R.id.listView);

        String[] appNameList = {
                "App1","App2","App3"
        };

        final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        PackageManager pm = getActivity().getPackageManager();
        //   final List pkgAppsList = pm.queryIntentActivities(mainIntent, 0);
        ArrayList<String> results = new ArrayList<>();


        List<ResolveInfo> pkgAppsList = pm.queryIntentActivities(mainIntent,
                PackageManager.PERMISSION_GRANTED);
        for (ResolveInfo rInfo : pkgAppsList) {
            results.add(rInfo.activityInfo.applicationInfo.loadLabel(pm)
                    .toString());
            //  Log.w("Installed Applications", rInfo.activityInfo.applicationInfo
            //        .loadLabel(pm).toString());
        }


        appList = new ArrayList<String>(Arrays.asList(appNameList));
        arrayAdapter = new ArrayAdapter(getActivity(),
                R.layout.list_item_forecast,R.id.list_item_forecast_textview,results);

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