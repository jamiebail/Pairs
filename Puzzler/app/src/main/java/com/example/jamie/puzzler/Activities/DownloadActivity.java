package com.example.jamie.puzzler.Activities;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.jamie.puzzler.Fragments.DownloadListFragment;
import com.example.jamie.puzzler.Fragments.PuzzleListFragment;
import com.example.jamie.puzzler.Logic.Getters.GetPuzzleList;
import com.example.jamie.puzzler.R;

public class DownloadActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        android.app.FragmentManager fm = getFragmentManager();
        android.app.FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.downloadFrame, new DownloadListFragment(), "serverList");
        ft.commit();
            new GetPuzzleList(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

}
