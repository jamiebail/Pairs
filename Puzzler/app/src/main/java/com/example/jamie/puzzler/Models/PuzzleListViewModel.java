package com.example.jamie.puzzler.Models;

import java.util.List;

/**
 * Created by Jamie on 02/03/2017.
 */

public class PuzzleListViewModel {
    public List<String> localPuzzles;
    public List<String> serverPuzzles;

    public PuzzleListViewModel(List<String> localIn, List<String>serverIn){
        localPuzzles = localIn;
        serverPuzzles = serverIn;
    }
}
