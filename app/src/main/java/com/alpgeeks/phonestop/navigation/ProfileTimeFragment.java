    package com.alpgeeks.phonestop.navigation;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.alpgeeks.phonestop.R;
import com.alpgeeks.phonestop.home.HomeActivity;
import com.alpgeeks.phonestop.model.Day;
import com.alpgeeks.phonestop.model.Profile;
import com.alpgeeks.phonestop.model.ProfileList;
import com.alpgeeks.phonestop.view.TimeSelector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProfileTimeFragment extends android.support.v4.app.Fragment {
    private static final String LOG_TAG = ProfileTimeFragment.class.getSimpleName();
    private static int id = 1;
    private TimeSelector timeSelector;
    private TextView mMinTime;
    private TextView mMaxTime;
    private Button mAddTimeSlotButton;
    private Button mSelectAppsButton;
    private ListView mTimeListView;
    private String profileName;
    ArrayAdapter<String> timeSlotsAdapter;
    List<String> timeSlotsList;
    private ToggleButton dayToggleButton;
    String startTime="00:00";
    String endTime="24:00";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_profiles_time, container, false);

        mMinTime = (TextView)view.findViewById(R.id.min_time);
        mMaxTime = (TextView)view.findViewById(R.id.max_time);
        mAddTimeSlotButton = (Button)view.findViewById(R.id.add_time_button);
        mSelectAppsButton = (Button)view.findViewById(R.id.select_apps_button);
        mTimeListView = (ListView)view.findViewById(R.id.time_list_view);;

        timeSelector = (TimeSelector)view.findViewById(R.id.time_selector_bar);
        timeSelector.setOnRangeSeekBarChangeListener(new TimeSelector.OnRangeSeekBarChangeListener() {
            @Override
            public void onRangeSeekBarValuesChanged(TimeSelector bar, float minValue, float maxValue) {

                startTime = timeSelector.toTimeValue(minValue);
                endTime = timeSelector.toTimeValue(maxValue);
                mMinTime.setText(startTime);
                mMaxTime.setText(endTime);
            }
        });


        timeSlotsList = new ArrayList<>();

        timeSlotsAdapter = new ArrayAdapter<String>(getActivity(),
                R.layout.list_item_forecast,R.id.list_item_forecast_textview,timeSlotsList);
        mTimeListView.setAdapter(timeSlotsAdapter);

        mAddTimeSlotButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                timeSlotsList.add("   " + startTime + " To " + endTime);
                timeSlotsAdapter.add("   " + startTime + " To " + endTime);
            }
        });

        mSelectAppsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Profile Name");

                // Set up the input
                final EditText input = new EditText(getActivity());
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT );
                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String m_Text = input.getText().toString();
                        profileName = m_Text;
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();

                if(profileName==null)
                    profileName="Profile "+id++;

                //profileName = mProfileText.getText().toString();
                Profile userProfile = makeProfile(view,profileName);
                ProfileList.getInstance().addProfile(userProfile);
                Log.e(LOG_TAG, userProfile.toString());
                Toast.makeText(getContext(), userProfile.toString(), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getActivity(), HomeActivity.class);
                getActivity().startActivity(intent);
                startActivity(intent);
            }
        });
        return view;
    }

    private Profile makeProfile(View view,String profileName) {
        Profile userProfile = new Profile(profileName);
        addDays(userProfile,view);
        for(String timeSlot: timeSlotsList) {
            userProfile.addSlot(timeSlot);
        }
        return userProfile;
    }

    private void addDays(Profile userProfile,View view) {
        ToggleButton dayToggleButton;

        dayToggleButton = (ToggleButton)view.findViewById(R.id.mon_button);
        if(dayToggleButton.isChecked())
            userProfile.addDay(Day.Mon);

        dayToggleButton = (ToggleButton)view.findViewById(R.id.tue_button);
        if(dayToggleButton.isChecked())
            userProfile.addDay(Day.Tue);

        dayToggleButton = (ToggleButton)view.findViewById(R.id.wed_button);
        if(dayToggleButton.isChecked())
            userProfile.addDay(Day.Wed);

        dayToggleButton = (ToggleButton)view.findViewById(R.id.thu_button);
        if(dayToggleButton.isChecked())
            userProfile.addDay(Day.Thu);

        dayToggleButton = (ToggleButton)view.findViewById(R.id.fri_button);
        if(dayToggleButton.isChecked())
            userProfile.addDay(Day.Fri);

        dayToggleButton = (ToggleButton)view.findViewById(R.id.sat_button);
        if(dayToggleButton.isChecked())
            userProfile.addDay(Day.Sat);

        dayToggleButton = (ToggleButton)view.findViewById(R.id.sun_button);
        if(dayToggleButton.isChecked())
            userProfile.addDay(Day.Sun);
    }
}