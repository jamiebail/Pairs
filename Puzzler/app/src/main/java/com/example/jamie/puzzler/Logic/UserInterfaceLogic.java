package com.example.jamie.puzzler.Logic;

import android.app.Activity;
import android.app.Fragment;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jamie.puzzler.Activities.MainActivity;
import com.example.jamie.puzzler.Fragments.AllDownloadedFragment;
import com.example.jamie.puzzler.Fragments.DownloadListFragment;
import com.example.jamie.puzzler.Fragments.NeedPuzzlesFragment;
import com.example.jamie.puzzler.Fragments.NoConnectionFragment;
import com.example.jamie.puzzler.Fragments.PuzzleInterfaceFragment;
import com.example.jamie.puzzler.Fragments.PuzzleListFragment;
import com.example.jamie.puzzler.Models.ImageAdapter;
import com.example.jamie.puzzler.Models.PuzzleItem;
import com.example.jamie.puzzler.Models.PuzzleListViewModel;
import com.example.jamie.puzzler.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import static com.example.jamie.puzzler.Activities.PuzzlesActivity.CurrentGameManager;
import static com.example.jamie.puzzler.Activities.PuzzlesActivity.CurrentPuzzle;

/**
 * Created by Jamie on 06/03/2017.
 */


public class UserInterfaceLogic {

    public Activity activity;

    public UserInterfaceLogic(Activity activityIn) {
        activity = activityIn;
    }

    public Fragment GetFragment(String fragmentName) {
        Fragment f = activity.getFragmentManager().findFragmentByTag(fragmentName);
        return f;
    }

    public void GenerateSpecialCards() {

            Random rn = new Random();
            List<Integer> multiplierIndexes = new ArrayList<>();
            int numberOfSpecialCards = CurrentPuzzle.PuzzleItems.size() / 3;
            for (int i = 0; i < numberOfSpecialCards; i++) {
                int index = rn.nextInt(CurrentPuzzle.PuzzleItems.size());
                if (!multiplierIndexes.contains(index))
                    multiplierIndexes.add(index);
                else{
                    i--;
                }
            }

            for (Integer card : multiplierIndexes) {
                PuzzleItem item = CurrentPuzzle.PuzzleItems.get(card);

                    if(multiplierIndexes.indexOf(card) == multiplierIndexes.size() - 1)
                        item.Type = Helpers.SpecialTypes.FOURTIMES;
                    else
                        item.Type = Helpers.SpecialTypes.TWOTIMES;

            }
        }

    public void HideStatusBar(){
        activity.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

    }

    public int GetOrientation(){
        return activity.getResources().getConfiguration().orientation;
    }

    public void InitialiseListViews(PuzzleListViewModel puzzleLists){

        if (puzzleLists != null) {
            android.app.FragmentTransaction ft = activity.getFragmentManager().beginTransaction();
            Fragment f = activity.getFragmentManager().findFragmentByTag("localList");
            PuzzleListFragment list = (PuzzleListFragment) f;
            if (puzzleLists.localPuzzles.size() > 0) {
                if (list != null){
                    list.SetAdapter(puzzleLists.localPuzzles);
                }
            }
            else if(list != null){
                ft.replace(R.id.puzzleListFrame, new NeedPuzzlesFragment());
                ft.commit();
            }
        }
        if (MainActivity.isOnline){
        if (puzzleLists.serverPuzzles != null) {
            Fragment f = activity.getFragmentManager().findFragmentByTag("serverList");
            DownloadListFragment list = (DownloadListFragment) f;

            if (puzzleLists.serverPuzzles.size() > 0) {
                if (list != null) {
                    list.SetAdapter(puzzleLists.serverPuzzles);
                }
            }
            else if(list!= null){
                android.app.FragmentTransaction ft = activity.getFragmentManager().beginTransaction();
                ft.replace(R.id.downloadFrame, new AllDownloadedFragment());
                ft.commit();
            }

        }
        }
        else{
            Fragment f = activity.getFragmentManager().findFragmentByTag("serverList");
            DownloadListFragment list = (DownloadListFragment) f;
            if(list != null){
            android.app.FragmentTransaction ft = activity.getFragmentManager().beginTransaction();
            ft.replace(R.id.downloadFrame, new NoConnectionFragment());
            ft.commit();
        }
        }
    }

    public void ChangeMultiplierStar(){
            PuzzleInterfaceFragment fragment = (PuzzleInterfaceFragment) GetFragment("puzzleInterface");
            fragment.ChangeMultiplier();
    }

    public void InitialisePuzzleListFragment(){
        android.app.FragmentManager fm = activity.getFragmentManager();
        android.app.FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.puzzleListFrame, new PuzzleListFragment(), "localList");
        ft.commit();
    }

    public void InitialisePuzzleInterfaceFragment(){
        android.app.FragmentManager fm = activity.getFragmentManager();
        android.app.FragmentTransaction ft = fm.beginTransaction();

        PuzzleInterfaceFragment persistentFragment = (PuzzleInterfaceFragment) GetFragment("puzzleInterface");

        if(persistentFragment == null){
            ft.replace(R.id.puzzleFrame, new PuzzleInterfaceFragment(), "puzzleInterface");
            ft.commit();
        }
        else{
            ft.replace(R.id.puzzleFrame, persistentFragment, "puzzleInterface");
            PuzzleInterfaceFragment fragment = (PuzzleInterfaceFragment) GetFragment("puzzleInterface");
            fragment.SetScoreText(String.valueOf(CurrentGameManager.Score));

            ChangeMultiplierStar();
        }

    }





}




