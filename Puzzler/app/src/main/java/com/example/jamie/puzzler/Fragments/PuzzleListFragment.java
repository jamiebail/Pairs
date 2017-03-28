package com.example.jamie.puzzler.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.jamie.puzzler.Logic.PuzzleLogic;
import com.example.jamie.puzzler.R;

import java.util.Collections;
import java.util.List;

/**
 * Created by Jamie on 03/03/2017.
 */

public class PuzzleListFragment extends Fragment {

    ListView localList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the maintextview for this fragment
        View view = inflater.inflate(R.layout.puzzle_list, container, false);
        ListView localPuzzles = (ListView) view.findViewById(R.id.lv1);
        PuzzleLogic logic = new PuzzleLogic(getActivity());
        logic.SetPuzzleListClickListener(localPuzzles);
        localList = (ListView)view.findViewById(R.id.lv1);


        new PuzzleLogic(getActivity()).SetPuzzleListClickListener(localList);

        return view;
    }


    public ListView GetDownloadList(){
        return localList;
    }

    public void SetAdapter(List<String> puzzles){
        Collections.sort(puzzles);
        ArrayAdapter<String> adapterLocal = new ArrayAdapter<String>(getActivity(), R.layout.maintextview, puzzles);
        localList.setAdapter(adapterLocal);
        new PuzzleLogic(getActivity()).SetPuzzleListClickListener(localList);
    }

    public ListAdapter GetListAdapter(){
        return localList.getAdapter();
    }
}
