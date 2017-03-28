package com.example.jamie.puzzler.Fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.jamie.puzzler.Activities.MainActivity;
import com.example.jamie.puzzler.R;

/**
 * Created by Jamie on 03/03/2017.
 */

public class AllDownloadedFragment extends Fragment {

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the maintextview for this fragment
        View view = inflater.inflate(R.layout.none_available, container, false);

        Button libraryButton = (Button) view.findViewById(R.id.mygamesbutton);
        libraryButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }
}
