package com.alpgeeks.phonestop.navigation;

import android.app.AppOpsManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import com.alpgeeks.phonestop.R;
import com.alpgeeks.phonestop.model.PackageInfo;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

// FIXME - remove PhoneStop from the list

/**
 * Home Fragment which contains list of applications and an enable disable
 * button. This button toggles the state of phonestop.
 */
public class AppListFragment extends android.support.v4.app.Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_profileName = null;
    private static final String ARG_PARAM2 = "param2";
    private CheckBox mSelectAppCheckbox;
    private Button mEnableButton;
    private ListView mAppList;
    private Context context;
    private boolean mPhoneStopEnabled = false;

    // TODO: Rename and change types of parameters
    private String mprofileName;
    private String mParam2;

    // TODO: Rename and change types and number of parameters
    public static AppListFragment newInstance(String profileName) {
        AppListFragment fragment = new AppListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_profileName, profileName);

        fragment.setArguments(args);
        return fragment;
    }

    public AppListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mprofileName = getArguments().getString(ARG_profileName);
            // mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_app_list, container, false);
        mSelectAppCheckbox=(CheckBox)rootView.findViewById(R.id.app_selected);
        context=getContext();
        final Intent intent = new Intent(getActivity(),AppBlockService.class);
        final ArrayList<PackageInfo> apps=getPackages();
        mAppList = (ListView)rootView.findViewById(R.id.listView_apps);
        mAppList.setAdapter(new CustomAdapter(getActivity(), apps));
        mEnableButton =(Button)rootView.findViewById(R.id.btnSaveSelection);
        mEnableButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mPhoneStopEnabled) {
                    mPhoneStopEnabled = true;
                    StringBuilder stringBuilder = new StringBuilder();
                    String blockApp = "";
                    for (int i = 0; i < apps.size(); i++) {
                        if (apps.get(i).selected) {
                            stringBuilder.append(apps.get(i).appname + "\n");
                            blockApp = blockApp + "\n" + apps.get(i).pname;
                        }
                    }
                    // Delete additional endline character
                    String s = stringBuilder.substring(0, stringBuilder.length()-1);
                    Toast.makeText(context, s, Toast.LENGTH_SHORT).show();

                    try {
                        context.deleteFile("Profile.ini");
                        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("Profile.ini", context.MODE_PRIVATE));
                        outputStreamWriter.write(blockApp);
                        outputStreamWriter.close();
                    } catch (IOException e) {
                        Log.e("Exception", "File write failed: " + e.toString());
                    }

                    // Enable PhoneStop by calling AppBlockService
                    try {
                        try {
                            PackageManager packageManager = getActivity().getPackageManager();
                            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(getActivity().getPackageName(), 0);
                            AppOpsManager appOpsManager = (AppOpsManager) getActivity().getSystemService(Context.APP_OPS_SERVICE);
                            int mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, applicationInfo.uid, applicationInfo.packageName);

                            if (mode != AppOpsManager.MODE_ALLOWED) {
                                Toast.makeText(getContext(), "Enable the permission to use PhoneStop", Toast.LENGTH_SHORT).show();
                                Intent intent1 = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                                startActivity(intent1);
                            }
                        } catch (PackageManager.NameNotFoundException e) {
                            Intent intent1 = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                            startActivity(intent1);

                        }
                        getActivity().startService(intent);
                        mEnableButton.setText(R.string.button_disable_phonestop);
                        getActivity().finish();
                    } catch (Exception e) {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else if (mPhoneStopEnabled) {
                    mPhoneStopEnabled = false;
                    getActivity().stopService(intent);
                    mEnableButton.setText(R.string.button_enable_phonestop);
                    Toast.makeText(context, R.string.disable_msg, Toast.LENGTH_SHORT).show();
                }
            }
        });

        return rootView;
    }

    private ArrayList<PackageInfo> getPackages()
    {
        ArrayList<PackageInfo> apps = getInstalledApps(false);
        final int max = apps.size();
        for (int i=0; i<max; i++) {
            //  apps.get(i).prettyPrint();
        }
        Collections.sort(apps, new Comparator<PackageInfo>() {
            @Override
            public int compare(PackageInfo lhs, PackageInfo rhs) {
                return lhs.appname.compareToIgnoreCase(rhs.appname);
            }
        });
        return apps;
    }

    private ArrayList<PackageInfo> getInstalledApps(boolean getSysPackages) {
        PackageManager pm = getContext().getPackageManager();
        ArrayList<PackageInfo> res = new ArrayList<PackageInfo>();
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
        for(ApplicationInfo packageInfo:packages){
            PackageInfo newInfo = new PackageInfo();
            Application app;

            if( pm.getLaunchIntentForPackage(packageInfo.packageName) != null ){
                newInfo.appname = pm.getApplicationLabel(packageInfo).toString();
                newInfo.icon = pm.getApplicationIcon(packageInfo);
                newInfo.pname=packageInfo.packageName;
                //This app is a non-system app
                res.add(newInfo);
            }
        }

        // FIXME : Delete this commented code in final application
        /*
        for(int i=0;i<packs.size();i++) {
            PackageInfo p = packs.get(i);
            if ((!getSysPackages) && (p.versionName == null)) {
                continue ;
            }
            PInfo newInfo = new PInfo();
            newInfo.appname = p.applicationInfo.loadLabel(getPackageManager()).toString();
           // newInfo.pname = p.packageName;
            //newInfo.versionName = p.versionName;
            //newInfo.versionCode = p.versionCode;
            newInfo.icon = p.applicationInfo.loadIcon(getPackageManager());
            res.add(newInfo);
        }*/
        return res;
    }

    @Override
    public void onPause() {
        super.onPause();
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(getString(R.string.saved_phonestop_state), mPhoneStopEnabled);
        editor.commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        mPhoneStopEnabled = sharedPref.getBoolean(getString(R.string.saved_phonestop_state), false);

        // Update button text
        if(mPhoneStopEnabled)
            mEnableButton.setText(R.string.button_disable_phonestop);
        else
            mEnableButton.setText(R.string.button_enable_phonestop);
    }
}
