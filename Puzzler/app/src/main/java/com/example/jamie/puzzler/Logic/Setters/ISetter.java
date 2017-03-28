package com.example.jamie.puzzler.Logic.Setters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;

import com.example.jamie.puzzler.Models.PuzzleItem;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;

/**
 * Created by Jamie on 01/03/2017.
 */

public interface ISetter {

    Boolean Set(String fileName, String Json);
    Boolean SetPictureFile(String fileName, Bitmap image);
}
