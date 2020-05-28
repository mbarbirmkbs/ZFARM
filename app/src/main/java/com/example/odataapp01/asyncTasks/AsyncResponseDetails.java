package com.example.odataapp01.asyncTasks;

import com.example.odataapp01.data.CowState;

public interface AsyncResponseDetails {
    void processFinish(CowState output);
    //void processFinish(String output);
}
