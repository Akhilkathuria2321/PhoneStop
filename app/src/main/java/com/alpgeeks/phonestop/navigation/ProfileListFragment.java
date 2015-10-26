package com.alpgeeks.phonestop.navigation;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.alpgeeks.phonestop.R;
import com.alpgeeks.phonestop.model.Profile;
import com.alpgeeks.phonestop.model.ProfileList;
import com.alpgeeks.phonestop.navigation.dummy.DummyContent;

import java.util.List;

public class ProfileListFragment extends android.support.v4.app.Fragment{
    private Button mNewProfileButton;
    private ListAdapter profileListAdapter;
    private ListView mProfileListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_list, container, false);

        mProfileListView = (ListView)view.findViewById(R.id.profiles_list_view);
        // custom adapter to be implemented
        List<String> list = ProfileList.getInstance().getUserProfiles();
        profileListAdapter = new ArrayAdapter<String>(getActivity(),
                R.layout.list_item_forecast,R.id.list_item_forecast_textview,list);
        mProfileListView.setAdapter(profileListAdapter);

        mNewProfileButton = (Button)view.findViewById(R.id.new_profile_button);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("","Called");
                android.support.v4.app.Fragment fragment1 = new ProfileTimeFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, fragment1);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        return view;
    }
}