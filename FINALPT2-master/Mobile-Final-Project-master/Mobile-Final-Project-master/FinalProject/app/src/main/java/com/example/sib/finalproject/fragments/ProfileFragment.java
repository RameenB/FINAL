package com.example.sib.finalproject.fragments;

/**
 * Created by Rameen Barish on 4/29/2017.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sib.finalproject.R;

public class ProfileFragment extends Fragment {

    private TextView profileName, longData, latData, distanceData;
    private Button mapsButton;
    private ImageView compassArrow;

    public static final String TAG_PROFILE_FRAGMENT = "profile_fragment";

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d("ProfileFragment:", "Messages Fragment ON_CREATE_VIEW()");
        View view = inflater.inflate(R.layout.profile_activity, container, false);

        return view;
    }


}