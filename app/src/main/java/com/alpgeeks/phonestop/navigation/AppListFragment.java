package com.alpgeeks.phonestop.navigation;

import android.app.AppOpsManager;
import android.app.Application;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
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

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AppListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AppListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AppListFragment extends android.support.v4.app.Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_profileName = null;
    private static final String ARG_PARAM2 = "param2";
    CheckBox mSelectAppCheckbox;
    Button mSaveButton;
    ListView listView;
    Context context;


    // TODO: Rename and change types of parameters
    private String mprofileName;
    private String mParam2;



//    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AppListFragment.
     */
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
        listView = (ListView)rootView.findViewById(R.id.listView_apps);
        listView.setAdapter(new CustomAdapter(getActivity(), apps));
        mSaveButton =(Button)rootView.findViewById(R.id.btnSaveSelection);
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mSaveButton.getText().equals("Enable PhoneStop")) {
                    String s="";
                    String blockApp="";
                    for(int i=0; i<  apps.size(); i++){

                        if(apps.get(i).selected) {
                            s = s + apps.get(i).appname+"\n";
                            blockApp = blockApp + "\n" + apps.get(i).pname;
                        }

                    }
                    Toast.makeText(context, s, Toast.LENGTH_LONG).show();

                    try {


                        context.deleteFile("Profile.ini");
                        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("Profile.ini", context.MODE_PRIVATE));
                        outputStreamWriter.write(blockApp);
                        outputStreamWriter.close();




                    }
                    catch (IOException e) {
                        Log.e("Exception", "File write failed: " + e.toString());
                    }
/* Enable phonestop by calling AppBlockService
*/
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
                        mSaveButton.setText("Disable");
                        // getActivity().finish();
                    } catch (Exception e) {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
                else if(mSaveButton.getText().equals("Disable")) {
                    getActivity().stopService(intent);
                    mSaveButton.setText("Enable PhoneStop");
                    Toast.makeText(context, "Disable", Toast.LENGTH_LONG).show();
                }


            }
        });

/* Enable phonestop by calling AppBlockService
*/


        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    /*public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }*/

/*    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }
*//*
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
*/
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
   /* public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }*/

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
        }/*
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
    public  void set(String name)
    {


        Toast.makeText(getContext(),name,Toast.LENGTH_SHORT).show();


    }
}
