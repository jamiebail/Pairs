package com.example.jamie.puzzler.Logic.Getters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import com.example.jamie.puzzler.Activities.PuzzlesActivity;
import com.example.jamie.puzzler.Logic.Helpers;
import com.example.jamie.puzzler.Logic.Setters.ISetter;
import com.example.jamie.puzzler.Models.Puzzle;
import com.example.jamie.puzzler.Logic.PuzzleLogic;
import com.example.jamie.puzzler.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static com.example.jamie.puzzler.Activities.PuzzlesActivity.CurrentPuzzle;


/**
 * Created by Jamie on 23/02/2017.
 */

public class GetPuzzle extends AsyncTask<JSONObject,Void,JSONObject> {
    public PuzzlesActivity activity;
    public String puzzleName;
    private ISetter setter;

    public GetPuzzle(PuzzlesActivity activityIn){
        activity = activityIn;
    }
    public GetPuzzle(PuzzlesActivity activityIn, ISetter setterIn){
        activity = activityIn;
        setter = setterIn;
    }

        // MOVE THIS TO STRINGS
    private String urlAppend = "puzzles/";

    @Override
    public JSONObject doInBackground(JSONObject... params) {
        if(!CurrentPuzzle.IsLocal){
        JSONObject response = null;
        try {
            puzzleName = CurrentPuzzle.Name;
            urlAppend += puzzleName + ".json";
            response = new Getter(activity).GetJSON(urlAppend);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
        }
        else return params[0];
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        try {
            PuzzleLogic _logic = new PuzzleLogic(activity);

            if(Helpers.ToBeSaved){
                setter.Set(puzzleName, jsonObject.toString());
                Helpers.ToBeSaved = false;
                Log.d("GetPuzzle() ", puzzleName + " loaded from server and saved in Internal Storage");
            }
            _logic.ParsePuzzleJson(jsonObject.getJSONObject("Puzzle"));
            new GetPictureSet(activity).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR).get();
            _logic.DisplayCompleteImages();

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }
}
