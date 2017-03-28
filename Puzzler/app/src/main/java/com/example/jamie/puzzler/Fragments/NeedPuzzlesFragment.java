package com.example.jamie.puzzler.Fragments;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jamie.puzzler.R;

/**
 * Created by Jamie on 03/03/2017.
 */

public class NeedPuzzlesFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.puzzles_needed, container, false);
        return view;
    }
}
