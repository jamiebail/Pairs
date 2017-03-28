package com.example.jamie.puzzler.Activities;

import android.content.Intent;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.jamie.puzzler.Fragments.NeedPuzzlesFragment;
import com.example.jamie.puzzler.Fragments.PuzzleListFragment;
import com.example.jamie.puzzler.Logic.Getters.GetPictureSet;
import com.example.jamie.puzzler.Logic.Getters.GetPuzzleList;
import com.example.jamie.puzzler.Logic.Getters.InternalFileGetter;
import com.example.jamie.puzzler.Logic.IOLogic;
import com.example.jamie.puzzler.Logic.PuzzleLogic;
import com.example.jamie.puzzler.Logic.Setters.InternalFileSetter;
import com.example.jamie.puzzler.Logic.UserInterfaceLogic;
import com.example.jamie.puzzler.Models.DBHandler;
import com.example.jamie.puzzler.Models.IOResponse;
import com.example.jamie.puzzler.Models.PictureSetModel;
import com.example.jamie.puzzler.Models.Puzzle;
import com.example.jamie.puzzler.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.example.jamie.puzzler.Activities.PuzzlesActivity.CurrentPuzzle;

public class MainActivity extends AppCompatActivity {
    IOLogic logic = new IOLogic(this);
    PuzzleLogic puzzleLogic = new PuzzleLogic(this);
    public final MainActivity context = this;
    public static boolean isOnline;
    public static String CurrentList = "";
    public ArrayList<String> MenuItems;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final UserInterfaceLogic UILogic = new UserInterfaceLogic(this);
        UILogic.HideStatusBar();
        logic.InitialiseDatabase();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        final Button resetButton = (Button)findViewById(R.id.button);
//
//        resetButton.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                DeleteLocalPuzzles();
//            }
//        });
        if(CurrentList == ""){
            CurrentList = "alphabetical";
        }

        Button ToDownloads = (Button)findViewById(R.id.downloadbutton);
        ToDownloads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CurrentList = "alphabetical";
                Intent intent = new Intent(context, DownloadActivity.class);
                startActivity(intent);
            }
        });

        ImageButton alphabeticalSort =  (ImageButton)findViewById(R.id.alphabutton);
        final ImageButton pictureButton =  (ImageButton)findViewById(R.id.picturebutton);
        ImageButton sizeButton =  (ImageButton)findViewById(R.id.sizebutton);

        alphabeticalSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CurrentList = "alphabetical";
                logic.LoadPuzzles();
            }
        });

        pictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CurrentList = "picturesets";
                List<String> puzzleNames = new DBHandler(context).getAllPuzzles();
                List<String> pictureSets = new ArrayList<String>();
                for(String puzzle : puzzleNames)
                {
                    IOResponse response = new IOLogic(context, new InternalFileSetter(context), new InternalFileGetter(context)).CheckIfSaved(puzzle);
                    try {
                        CurrentPuzzle = new Puzzle();
                        JSONObject puzzleJSON = new JSONObject(response.object.toString()).getJSONObject("Puzzle");
                        String pictureSet = puzzleJSON.getString("PictureSet");
                        pictureSet = pictureSet.replace(".json", "");
                        if(!pictureSets.contains(pictureSet))
                            pictureSets.add(pictureSet);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            PuzzleListFragment fragment = (PuzzleListFragment) UILogic.GetFragment("localList");
                if(pictureSets.size() > 0)
                    fragment.SetAdapter(pictureSets);
            }
        });

        sizeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CurrentList = "size";
                PuzzleListFragment fragment = (PuzzleListFragment) UILogic.GetFragment("localList");
                try {
                    if(logic.GetPuzzlesSizes().size() > 0)
                        fragment.SetAdapter(logic.GetPuzzlesSizes());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        android.app.FragmentManager fm = getFragmentManager();
        android.app.FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.puzzleListFrame, new PuzzleListFragment(), "localList");
        ft.commit();

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if(MenuItems == null)
            new IOLogic(this).LoadPuzzles();
        else{
            PuzzleListFragment fragment = (PuzzleListFragment) new UserInterfaceLogic(this).GetFragment("localList");
            fragment.SetAdapter(MenuItems);
        }


    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("CurrentList",CurrentList);
        ArrayList<String> menuItems = new ArrayList<String>();
        PuzzleListFragment fragment = (PuzzleListFragment) new UserInterfaceLogic(this).GetFragment("localList");
        ListAdapter adapter = null;
        if(fragment != null){
            adapter = fragment.GetListAdapter();
        }
        if(adapter != null){
        for(int i=0; i < adapter.getCount();i++){
            menuItems.add((String) adapter.getItem(i));
        }
        }
        outState.putStringArrayList("MenuItems",menuItems);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        CurrentList = savedInstanceState.getString("CurrentList");
        MenuItems = savedInstanceState.getStringArrayList("MenuItems");
    }

    public void DeleteLocalPuzzles(){
        new IOLogic(this).DropPuzzleTable();
        List<String> s = new DBHandler(this).getAllPuzzles();
        // reset adapters
        // get server puzzles
        logic.LoadPuzzles();

    }


}
