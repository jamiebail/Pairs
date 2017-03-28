package com.example.jamie.puzzler.Logic.Getters;

import android.os.AsyncTask;

import org.json.JSONObject;

/**
 * Created by Jamie on 23/02/2017.
 */

public interface IGetRequests {

    String urlAppend = "";

    JSONObject doInBackground(Void... params);
    

    void onPostExecute(JSONObject s);
}
