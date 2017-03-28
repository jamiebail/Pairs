package com.example.jamie.puzzler.Logic;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.media.Image;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.jamie.puzzler.Activities.PuzzlesActivity;
import com.example.jamie.puzzler.Fragments.PuzzleInterfaceFragment;
import com.example.jamie.puzzler.Fragments.PuzzleListFragment;
import com.example.jamie.puzzler.Logic.Getters.Getter;
import com.example.jamie.puzzler.Logic.Getters.IGetter;
import com.example.jamie.puzzler.Logic.Getters.InternalFileGetter;
import com.example.jamie.puzzler.Logic.Setters.ISetter;
import com.example.jamie.puzzler.Logic.Setters.InternalFileSetter;
import com.example.jamie.puzzler.Models.IOResponse;
import com.example.jamie.puzzler.Models.ImageAdapter;
import com.example.jamie.puzzler.Models.Puzzle;
import com.example.jamie.puzzler.Models.PuzzleItem;

import com.example.jamie.puzzler.Models.Stopwatch;
import com.example.jamie.puzzler.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.example.jamie.puzzler.Activities.MainActivity.CurrentList;
import static com.example.jamie.puzzler.Activities.PuzzlesActivity.CurrentGameManager;
import static com.example.jamie.puzzler.Activities.PuzzlesActivity.CurrentPuzzle;


/**
 * Created by Jamie on 22/02/2017.
 */

public class PuzzleLogic {

    public final Activity activity;


    private int first = -1;
    private int second = -1;
    private ImageView firstTile;
    private ImageView secondTile;
    private int selectedCount = 0;
    private ISetter setter;
    private IGetter getter;
    private UserInterfaceLogic UILogic;
    String selectedPuzzle;


    public PuzzleLogic(Activity activityIn){
        activity = activityIn;
        UILogic = new UserInterfaceLogic(activityIn);
    }
    public PuzzleLogic(Activity activityIn, ISetter setterIn, IGetter getterIn){
        activity = activityIn;
        setter = setterIn;
        getter = getterIn;
        UILogic = new UserInterfaceLogic(activity);
    }

    public void setImageAdapter(){
    PuzzleInterfaceFragment puzzleFragment = (PuzzleInterfaceFragment)UILogic.GetFragment("puzzleInterface");
    puzzleFragment.GetPuzzleGrid().setAdapter(new ImageAdapter(activity, CurrentPuzzle.PuzzleItemPictures()));
//        UILogic.ReinitialiseCompleteTiles();
    }

    public void setNullImageAdapter(){
        PuzzleInterfaceFragment puzzleFragment = (PuzzleInterfaceFragment)UILogic.GetFragment("puzzleInterface");
        puzzleFragment.GetPuzzleGrid().setAdapter(null);
    }

    public void InitializePuzzle(Puzzle puzzle){
        PuzzleInterfaceFragment view = (PuzzleInterfaceFragment) UILogic.GetFragment("puzzleInterface");

        GridView gridView = view.GetPuzzleGrid();

        gridView.setNumColumns(CurrentPuzzle.Columns);

        new UserInterfaceLogic(activity).GenerateSpecialCards();

        SetTileClickListener();

        view.SetHighscoreText();
    }

    public void DisplayCompleteImages(){
        final PuzzleInterfaceFragment puzzleFragment = (PuzzleInterfaceFragment) new UserInterfaceLogic(activity).GetFragment("puzzleInterface");
        if(puzzleFragment != null){
        final GridView gridView = puzzleFragment.GetPuzzleGrid();
        for(int i=0; i< CurrentGameManager.CompleteMatches.size(); i++) {
            ImageView tile = (ImageView)gridView.getChildAt(CurrentGameManager.CompleteMatches.get(i));
            if(tile != null)
            tile.setImageBitmap(Bitmap.createScaledBitmap(CurrentPuzzle.PuzzleItemPictures().get(CurrentGameManager.CompleteMatches.get(i)), (tile.getWidth() - tile.getWidth()/17) , tile.getHeight() - tile.getHeight()/25, false));
        }
        }
    }

    public void DisplayAllImages(){
        final PuzzleInterfaceFragment puzzleFragment = (PuzzleInterfaceFragment) new UserInterfaceLogic(activity).GetFragment("puzzleInterface");
        final GridView gridView = puzzleFragment.GetPuzzleGrid();
        final TextView gameTextView = puzzleFragment.GetPuzzleText();
        for(int i=0; i< gridView.getChildCount(); i++) {
            ImageView tile = (ImageView)gridView.getChildAt(i);
            tile.setImageBitmap(Bitmap.createScaledBitmap(CurrentPuzzle.PuzzleItemPictures().get(i), (tile.getWidth() - tile.getWidth()/17) , tile.getHeight() - tile.getHeight()/25, false));
        }
        gridView.setOnItemClickListener(null);
        Log.d("DisplayAllImages", "Images shown ");
         new CountDownTimer(1000, 1000){

            long threadId = Thread.currentThread().getId();
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                for(int i=0; i< gridView.getChildCount(); i++) {
                    ImageView tile = (ImageView)gridView.getChildAt(i);
                    tile.setImageBitmap(null);
                }
                gameTextView.setText(activity.getString(R.string.postgamestarttext));
                // Start stopwatch
                CurrentGameManager.Stopwatch = new Stopwatch(activity);
                CurrentGameManager.Stopwatch.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                SetTileClickListener();
                Log.d("DisplayAllImages", "GAME STARTED" + threadId);
            }
        }.start();
    }

    public Void ParsePuzzleJson(JSONObject puzzleJSON){
        try {

            CurrentPuzzle.Id = puzzleJSON.getInt("Id");
            CurrentPuzzle.Rows = puzzleJSON.getInt("Rows");
            CurrentPuzzle.Layout = puzzleJSON.getJSONArray("Layout");
            CurrentPuzzle.Columns = CurrentPuzzle.Layout.length() / CurrentPuzzle.Rows;
            CurrentPuzzle.PictureSet = puzzleJSON.getString("PictureSet");
            new IOLogic(activity).GetCurrentPuzzleHighscore();

            int size = CurrentPuzzle.Layout.length();
            if(size <= 12)
                CurrentPuzzle.Size = Helpers.PuzzleSize.SMALL;
            else if(size > 12 && size < 20){
                CurrentPuzzle.Size = Helpers.PuzzleSize.MEDIUM;
            }
            else if(size >= 20)
                CurrentPuzzle.Size = Helpers.PuzzleSize.LARGE;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void SetTileClickListener(){
        PuzzleInterfaceFragment view = (PuzzleInterfaceFragment) UILogic.GetFragment("puzzleInterface");
        GridView gridView = view.GetPuzzleGrid();
        final TextView gameTextView = view.GetPuzzleText();

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // Start of a game
                if(! CurrentGameManager.isPlaying) {
                    CurrentPuzzle.startTime = new Date();
                    CurrentGameManager.isPlaying = true;
                    gameTextView.setText(activity.getString(R.string.pregamestarttext));
                    DisplayAllImages();
                }
                else {
                    // check if selected values match asynchronously.
                    final CheckMatchTask checker = new CheckMatchTask(Helpers.ConvertJSONArrayToIntList(CurrentPuzzle.Layout), activity);
//                    ((ImageView) view).setImageBitmap(Bitmap.createScaledBitmap(CurrentPuzzle.PuzzleItemPictures().get(position), 160, 250, false));

                    if (selectedCount == 0) {
                        first = position;
                        firstTile = (ImageView)view;
                        firstTile.setImageBitmap(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(activity.getResources(), R.drawable.selected1), 150, 150, false));
                        gameTextView.setText(activity.getString(R.string.firsttileselected));
                    } else {
                        second = position;
                        secondTile = (ImageView)view;
                        ImageView tile = (ImageView)view;
                        tile.setImageBitmap(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(activity.getResources(), R.drawable.selected1), (tile.getWidth() - tile.getWidth()/17) , tile.getHeight() - tile.getHeight()/25, false));
                    }
                    selectedCount++;

                    if (selectedCount == 2 && first != second) {
                        checker.firstItem = firstTile;
                        checker.secondItem = secondTile;
                        firstTile.setImageBitmap(Bitmap.createScaledBitmap(CurrentPuzzle.PuzzleItemPictures().get(first),  (firstTile.getWidth() - firstTile.getWidth()/17) , firstTile.getHeight() - firstTile.getHeight()/25, false));
                        secondTile.setImageBitmap(Bitmap.createScaledBitmap(CurrentPuzzle.PuzzleItemPictures().get(second),  (secondTile.getWidth() - secondTile.getWidth()/17) , secondTile.getHeight() - secondTile.getHeight()/25, false));
                        checker.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, first, second);
                        first = -1;
                        second = -1;
                        selectedCount = 0;


                    } else if (first == second) {
                        selectedCount = 0;
                        first = -1;
                        second = -1;
                        firstTile.setImageBitmap(null);
                        secondTile.setImageBitmap(null);
                        gameTextView.setText(activity.getString(R.string.sametileclicked));
                    }

                }
            }

        });
    }

    public void GetPictures(){
        try {
            IOLogic IOlogic = new IOLogic(activity, new InternalFileSetter(activity), new InternalFileGetter(activity));
            Getter serverGetter = new Getter(activity);
            JSONObject array = null;
            List<String> pictures = new ArrayList<String>();

            IOResponse response = IOlogic.CheckIfSaved(CurrentPuzzle.PictureSet);
            // If picture set is saved locally
            if(response.Success && response.object != null)
            {
                array = new JSONObject(response.object.toString());
                Log.d("GetPictures()",CurrentPuzzle.PictureSet + " loaded from Internal Storage");
            }
            else{
                array = serverGetter.GetJSON(activity.getString(R.string.picturesetURLAppend) + CurrentPuzzle.PictureSet);
                setter.Set(CurrentPuzzle.PictureSet, array.toString());
                Log.d("GetPictures()",CurrentPuzzle.PictureSet + " loaded from server and saved locally");
            }

             pictures = Helpers.ConvertJSONArrayToStringList(array.getJSONArray("PictureFiles"));

            int pictureID = 1;
            // add puzzle item name and picture to current list
            for(String pictureName: pictures){

                Bitmap image = IOlogic.CheckPicSaved(pictureName);

                if(image == null){
                    image = serverGetter.GetPicture(pictureName);
                    setter.SetPictureFile(pictureName, image);
                    Log.d("GetPictures()",pictureName + " loaded from server");
                }
                else{
                    Log.d("GetPictures()",pictureName + " loaded from Internal Storage");
                }
                CurrentPuzzle.PuzzleItems.add(new PuzzleItem(pictureID,pictureName, image));

                pictureID++;
            }
            pictureID = 1;
        SetupSequence();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    public void SetupSequence(){
        List<PuzzleItem> puzzleItems = new ArrayList<PuzzleItem>();
        for(int i : Helpers.ConvertJSONArrayToIntList(CurrentPuzzle.Layout)){
            for(PuzzleItem item : CurrentPuzzle.PuzzleItems){
                if( item.Id == i) {
                    PuzzleItem newItem = new PuzzleItem(item.Id, item.Name, item.Image);
                    puzzleItems.add(newItem);
                }

            }
        }
        CurrentPuzzle.PuzzleItems = puzzleItems;
    }

    public void SetPuzzleListClickListener(final AdapterView lv){
       final IOLogic IOlogic = new IOLogic(activity);
        switch(CurrentList){

            case "alphabetical":
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        selectedPuzzle = (String) (lv.getItemAtPosition(position));
                        Intent intent = new Intent(activity, PuzzlesActivity.class);
                        intent.putExtra("selectedPuzzle", selectedPuzzle);
                        IOlogic.SavePuzzleNameDB(selectedPuzzle);
                        IOlogic.LoadPuzzles();
                        activity.startActivity(intent);
                    }

                });
                    break;
            case "picturesets":
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        CurrentList = "alphabetical";
                        String selectedPictureSet = (String) (lv.getItemAtPosition(position) + ".json");
                        List<String> puzzles = IOlogic.GetPuzzlesNamesByPictureSet(selectedPictureSet);
                        PuzzleListFragment fragment = (PuzzleListFragment) UILogic.GetFragment("localList");
                        if(fragment != null)
                            fragment.SetAdapter(puzzles);

                    }

                });
                    break;
            case "size":
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        CurrentList = "alphabetical";
                        String selectedPuzzleSize = (String) (lv.getItemAtPosition(position));
                        List<String> puzzles = IOlogic.GetPuzzlesNamesBySize(Integer.valueOf(selectedPuzzleSize));
                        PuzzleListFragment fragment = (PuzzleListFragment) UILogic.GetFragment("localList");
                        if(fragment != null)
                            fragment.SetAdapter(puzzles);

                    }

                });
                break;


        }

    }

    public CountDownTimer InitialiseTimer(int timePeriod, int interval){
        return new CountDownTimer(timePeriod * 1000, interval * 1000){
            PuzzleInterfaceFragment puzzleFragment = (PuzzleInterfaceFragment) new UserInterfaceLogic(activity).GetFragment("puzzleInterface");
            long threadId = Thread.currentThread().getId();
            @Override
            public void onTick(long millisUntilFinished) {
                long seconds = millisUntilFinished/1000;
                String text = "";
                text += seconds;
                puzzleFragment.SetCountdownTimer(text);
                puzzleFragment.ChangeMultiplier();
                CurrentGameManager.PowerupTimeRemaining--;
            }

            @Override
            public void onFinish() {
                CurrentGameManager.PowerupType = Helpers.SpecialTypes.STANDARD;
                puzzleFragment.SetCountdownTimer("0");
                puzzleFragment.ChangeMultiplier();
                Log.d("InitialiseTimer", "Countdown timer finished, thread : " + threadId);
            }
        };
    }

    public boolean CheckIfGameOver(){
        int completeTiles = CurrentGameManager.CompleteMatches.size();
        int incompleteTiles = CurrentPuzzle.PuzzleItems.size();
        if(incompleteTiles == completeTiles)
            return true;

        return false;
    }

    public boolean CheckIfCardRevealed(int position){
        if(CurrentGameManager.CompleteMatches.contains(position)){
            return true;
        }
        return false;
    }


}
