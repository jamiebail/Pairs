package com.example.jamie.puzzler.Models;

import android.graphics.Bitmap;

/**
 * Created by Jamie on 20/03/2017.
 */

public class PictureSetModel {
    public int ID;
    public Bitmap Image;
    public String Name;

    public PictureSetModel(String name){
        Name = name;
    }
}
