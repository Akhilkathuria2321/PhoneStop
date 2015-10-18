package com.alpgeeks.phonestop.navigation;

import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alpgeeks.phonestop.R;
import com.alpgeeks.phonestop.home.HomeActivity;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link GetHelpFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GetHelpFragment extends android.support.v4.app.Fragment {
    private static final String LOG_TAG = GetHelpFragment.class.getSimpleName();

    private Button mHelpButton;
    private long phoneNumber;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GetHelpFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GetHelpFragment newInstance(String param1, String param2) {
        GetHelpFragment fragment = new GetHelpFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public GetHelpFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_get_help, container, false);

        final EditText text = (EditText)v.findViewById(R.id.phone_number_text);

        mHelpButton =  (Button)v.findViewById(R.id.get_help_button);
        mHelpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateNumber(text.getText().toString())) {
                    Intent intent = new Intent(getActivity(), HomeActivity.class);
                    getActivity().startActivity(intent);
                    startActivity(intent);
                    Toast.makeText(getContext(), "PhoneStop Enabled", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getContext(), "Please Enter Valid Mobile Number", Toast.LENGTH_SHORT).show();
                }
            }
        });
        // Inflate the layout for this fragment
        return v;
    }

    private boolean validateNumber(String phoneNumber) {
        boolean isValid = true;
        if(phoneNumber.length() < 9) return false;
        try {
            this.phoneNumber = Long.parseLong(phoneNumber);
            Log.v(LOG_TAG,"Number is : "+this.phoneNumber);
        }catch(NumberFormatException n) {
            return false;
        }
        return isValid;
    }
}
