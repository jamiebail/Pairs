package com.example.jamie.puzzler.Logic.Getters;
import android.app.Activity;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.jamie.puzzler.Activities.MainActivity;
import com.example.jamie.puzzler.Fragments.AllDownloadedFragment;
import com.example.jamie.puzzler.Fragments.DownloadListFragment;
import com.example.jamie.puzzler.Fragments.NeedPuzzlesFragment;
import com.example.jamie.puzzler.Fragments.PuzzleListFragment;
import com.example.jamie.puzzler.Logic.Helpers;
import com.example.jamie.puzzler.Logic.IOLogic;
import com.example.jamie.puzzler.Logic.Setters.InternalFileSetter;
import com.example.jamie.puzzler.Logic.UserInterfaceLogic;
import com.example.jamie.puzzler.Models.DBHandler;
import com.example.jamie.puzzler.Models.IOResponse;
import com.example.jamie.puzzler.Models.PuzzleListViewModel;
import com.example.jamie.puzzler.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileDescriptor;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import static android.R.id.list;


/**
 * Created by Jamie on 22/02/2017.
 */

public class GetPuzzleList extends AsyncTask<Void,Void,PuzzleListViewModel> {

    public Activity activity;

    public GetPuzzleList(Activity activityIn){
        activity = activityIn;
    }
    IOLogic IOlogic = new IOLogic(activity, new InternalFileSetter(activity), new InternalFileGetter(activity));
    private String urlAppend = "index.json";

    @Override
    public PuzzleListViewModel doInBackground(Void... params) {
        Log.d("GetPuzzleList", "method called");
        JSONObject response = null;
        List<String> serverPuzzles = new ArrayList<>();
        DBHandler db = new DBHandler(activity);
        List<String> localPuzzles = new ArrayList<>();
        try {
          localPuzzles = db.getAllPuzzles();
            for(int i = 0; i < localPuzzles.size(); i++){
                String puzzlename = localPuzzles.get(i);
                localPuzzles.set(i, puzzlename + ".json");
            }
            response = new Getter(activity).GetJSON(urlAppend);

            if(response != null){
            serverPuzzles = Helpers.ConvertJSONArrayToStringList(response.getJSONArray("PuzzleIndex"));
            serverPuzzles.removeAll(localPuzzles);
            return new PuzzleListViewModel(localPuzzles, serverPuzzles);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // No downloads available
        return new PuzzleListViewModel(localPuzzles, null);
    }


    @Override
    public void onPostExecute(PuzzleListViewModel puzzleLists) {
        for(int i = 0; i < puzzleLists.localPuzzles.size(); i++)
        puzzleLists.localPuzzles.set(i, puzzleLists.localPuzzles.get(i).replace(".json", ""));
        if (puzzleLists.serverPuzzles != null) {

        for(int j = 0; j < puzzleLists.serverPuzzles.size(); j++)
            puzzleLists.serverPuzzles.set(j, puzzleLists.serverPuzzles.get(j).replace(".json", ""));
        }
        new UserInterfaceLogic(activity).InitialiseListViews(puzzleLists);

    }
}
