package com.e_schedule.e_schedule.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.e_schedule.e_schedule.MainActivity;
import com.e_schedule.e_schedule.R;

public class fragment_home extends Fragment {

    public static fragment_home newInstance(){
        return new fragment_home();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        ((MainActivity)getActivity()).getSupportActionBar().setTitle("E-SCHEDULE");
        return rootView;
    }

}