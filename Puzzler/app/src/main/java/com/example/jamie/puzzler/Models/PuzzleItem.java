package com.example.jamie.puzzler.Models;

import android.graphics.Bitmap;

import com.example.jamie.puzzler.Logic.Helpers;

/**
 * Created by Jamie on 27/02/2017.
 */

public class PuzzleItem {
    public int Id;
    public String Name;
    public Bitmap Image;
    public Helpers.SpecialTypes Type;
    public PuzzleItem(int id, String name, Bitmap image){
        Id = id;
        Name = name;
        Image = image;
        Type = Helpers.SpecialTypes.STANDARD;
    }

}
