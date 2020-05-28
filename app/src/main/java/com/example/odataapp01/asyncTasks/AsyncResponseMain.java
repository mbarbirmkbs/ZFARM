package com.example.odataapp01.asyncTasks;

import com.example.odataapp01.data.CowState;

import java.util.ArrayList;

public interface AsyncResponseMain {
    void processFinish(ArrayList<CowState> output);
    //void processFinish(String output);
}
