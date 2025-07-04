package com.tcg.empires.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tcg.empires.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LifetapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LifetapFragment extends Fragment {

    public static LifetapFragment newInstance (){
        return new LifetapFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lifetap, container, false);
    }
}