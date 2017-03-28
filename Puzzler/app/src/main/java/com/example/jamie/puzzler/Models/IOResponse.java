package com.example.jamie.puzzler.Models;

import java.util.Objects;

/**
 * Created by Jamie on 02/03/2017.
 */

public class IOResponse {

    public IOResponse(Boolean successIn, Object objIn){
        Success = successIn;
        object = objIn;
    }

    public Boolean Success;
    public Object object;
}
