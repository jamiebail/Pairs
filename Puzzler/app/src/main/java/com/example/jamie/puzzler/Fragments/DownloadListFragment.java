package com.example.jamie.puzzler.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;

import com.example.jamie.puzzler.Logic.PuzzleLogic;
import com.example.jamie.puzzler.R;

import java.util.List;

/**
 * Created by Jamie on 03/03/2017.
 */

public class DownloadListFragment  extends Fragment {

    ListView downloadList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the maintextview for this fragment
        View view = inflater.inflate(R.layout.download_list, container, false);
        downloadList = (ListView) view.findViewById(R.id.lv2);

        new PuzzleLogic(getActivity()).SetPuzzleListClickListener(downloadList);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public ListView getDownloadList(){
        return downloadList;
    }

    public void SetAdapter(List<String> puzzles){
        ArrayAdapter<String> adapterServer = new ArrayAdapter<String>(getActivity(), R.layout.maintextview, puzzles);
        downloadList.setAdapter(adapterServer);
    }
}
