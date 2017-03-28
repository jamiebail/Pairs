package com.example.jamie.puzzler.Logic;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.example.jamie.puzzler.Logic.Getters.GetPuzzleList;
import com.example.jamie.puzzler.Logic.Getters.IGetter;
import com.example.jamie.puzzler.Logic.Getters.InternalFileGetter;
import com.example.jamie.puzzler.Logic.Setters.ISetter;
import com.example.jamie.puzzler.Logic.Setters.InternalFileSetter;
import com.example.jamie.puzzler.Models.DBHandler;
import com.example.jamie.puzzler.Models.IOResponse;
import com.example.jamie.puzzler.Models.Puzzle;
import com.example.jamie.puzzler.Models.PuzzleListViewModel;
import com.example.jamie.puzzler.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.example.jamie.puzzler.Activities.PuzzlesActivity.CurrentGameManager;
import static com.example.jamie.puzzler.Activities.PuzzlesActivity.CurrentPuzzle;

/**
 * Created by Jamie on 01/03/2017.
 */

public class IOLogic {

    public Activity activity;
    private ISetter setter;
    private IGetter getter;
    DBHandler db;

    public IOLogic(Activity activityIn, ISetter setterIn, IGetter getterIn)
    {
        activity = activityIn;
        setter = setterIn;
        getter = getterIn;
        db = new DBHandler(activityIn);
    }

    public IOLogic(Activity activityIn){activity = activityIn; db = new DBHandler(activityIn);}

    public IOResponse CheckIfSaved(String puzzleName){
        puzzleName = puzzleName + activity.getString(R.string.FileOutputType);
        Object IOresponse = getter.Get(puzzleName);
        String contents = "";
        if(IOresponse != null)
            contents = IOresponse.toString();
        if(contents != "")
            return new IOResponse(true, contents);
        return new IOResponse(false, null);
    }

    public void InitialiseDatabase(){
        DBHandler db = new DBHandler(activity);
    }

    public Bitmap CheckPicSaved(String pictureName){
        try {
            Bitmap image = getter.GetImage(pictureName);
            if(image != null)
                return image;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void SetCurrentPuzzleHighscore(){
        new DBHandler(activity).updateHighscore(CurrentPuzzle.Name, CurrentGameManager.Score);
    }

    public void GetCurrentPuzzleHighscore(){
        CurrentPuzzle.Highscore = new DBHandler(activity).getHighscore(CurrentPuzzle.Name);
    }

    public void SavePuzzleNameDB(String puzzleName){
       if(!CheckPuzzlesDB(puzzleName))
            db.addPuzzle(puzzleName, 0);
    }

    public Boolean CheckPuzzlesDB(String puzzleName){
        List<String> puzzles = db.getPuzzle(puzzleName);
        if(puzzles != null)
            return true;
        else return false;
    }

    public void DropPuzzleTable(){
        db.DropTable();
    }

    public void LoadPuzzles(){
        AsyncTask<Void,Void,PuzzleListViewModel> task = new GetPuzzleList(activity);
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public List<String> GetPuzzlesNamesByPictureSet(String pictureSet){
        List<String> puzzleNames = new DBHandler(activity).getAllPuzzles();
        List<String> matchingPuzzles = new ArrayList<>();
        List<JSONObject> puzzlesJSON = GetPuzzlesJSON(puzzleNames);
        // reference to name found in puzzleNames as not present in JSON string.
        int nameCount = 0;
        for(JSONObject puzzleJSON : puzzlesJSON){
            try {
                String puzzlePictures = (puzzleJSON.getString("PictureSet"));
                if(puzzlePictures.equals(pictureSet)){
                    matchingPuzzles.add(puzzleNames.get(nameCount));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            nameCount++;
        }
        return matchingPuzzles;
    }

    public List<JSONObject> GetPuzzlesJSON(List<String> puzzlesNames){
        List<JSONObject> puzzlesJSON = new ArrayList<>();
        for(String puzzleName : puzzlesNames){
            IOResponse response = new IOLogic(activity, new InternalFileSetter(activity), new InternalFileGetter(activity)).CheckIfSaved(puzzleName);
            if(response.Success){
                try {
                    puzzlesJSON.add(new JSONObject(response.object.toString()).getJSONObject("Puzzle"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return puzzlesJSON;
    }

    public List<String> GetPuzzlesNamesBySize(int selectedSize){
        List<String> puzzleNames = new DBHandler(activity).getAllPuzzles();
        List<String> matchingPuzzles = new ArrayList<>();
        List<JSONObject> puzzlesJSON = GetPuzzlesJSON(puzzleNames);
        // reference to name found in puzzleNames as not present in JSON string.
        int nameCount = 0;
        for(JSONObject puzzleJSON : puzzlesJSON){
            try {
                int puzzleSize  = (puzzleJSON.getJSONArray("Layout").length());
                if(puzzleSize == selectedSize){
                    matchingPuzzles.add(puzzleNames.get(nameCount));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            nameCount++;
        }
        return matchingPuzzles;
    }

    public List<String> GetPuzzlesSizes() throws JSONException {
        List<String> puzzleNames = new DBHandler(activity).getAllPuzzles();
        List<String> sizes = new ArrayList<String>();
        List<JSONObject> puzzlesJSON = GetPuzzlesJSON(puzzleNames);
        // reference to name found in puzzleNames as not present in JSON string.
        for(JSONObject puzzleJSON : puzzlesJSON){
            int puzzleSize = puzzleJSON.getJSONArray("Layout").length();
            if(! sizes.contains(String.valueOf(puzzleSize)))
                sizes.add(String.valueOf(puzzleSize));
        }

        return sizes;

    }
}
