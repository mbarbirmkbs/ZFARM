package com.example.odataapp01.asyncTasks;

import com.example.odataapp01.data.CowEntry;

import java.util.ArrayList;

public interface AsyncResponseEntries {
    void processFinish(ArrayList<CowEntry> output);
}
