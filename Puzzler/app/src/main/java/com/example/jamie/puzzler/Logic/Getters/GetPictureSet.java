package com.example.jamie.puzzler.Logic.Getters;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.example.jamie.puzzler.Activities.PuzzlesActivity;
import com.example.jamie.puzzler.Logic.PuzzleLogic;
import com.example.jamie.puzzler.Logic.Setters.InternalFileSetter;
import com.example.jamie.puzzler.Logic.UserInterfaceLogic;
import com.example.jamie.puzzler.Models.Puzzle;
import com.example.jamie.puzzler.R;

import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import static com.example.jamie.puzzler.Activities.PuzzlesActivity.CurrentPuzzle;


/**
 * Created by Jamie on 23/02/2017.
 */

public class GetPictureSet extends AsyncTask<Void,Void,Void> {
        public Activity activity;
        public String puzzleName;
        PuzzleLogic _logic;

    public GetPictureSet(Activity activityIn){
        activity = activityIn;
        _logic = new PuzzleLogic(activityIn, new InternalFileSetter(activity), new InternalFileGetter(activity));
        }

    @Override
    protected Void doInBackground(Void... params) {
        _logic.GetPictures();
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid)
    {
        _logic.setImageAdapter();
        _logic.InitializePuzzle(CurrentPuzzle);

    }

}

