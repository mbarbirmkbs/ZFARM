package com.example.odataapp01.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.odataapp01.R;
import com.example.odataapp01.asyncTasks.AsyncResponseMain;
import com.example.odataapp01.data.CowState;
import com.example.odataapp01.asyncTasks.CowsAsyncTask;
import com.example.odataapp01.adapters.CowsListAdapter;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AsyncResponseMain {

    private RecyclerView rvCows;
    private ArrayList<CowState> cows;
    private String[] grlibList;
    private String[] grlidList;
    private ProgressBar myProgressBar;
    private CowsAsyncTask cowsAsyncTask;
    private CowsListAdapter cowsListAdapter;
    private int currentListSize;
    private FloatingActionButton myFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        //Hide action bar
        ActionBar actionBar = getSupportActionBar(); // or getActionBar();
        getSupportActionBar().setTitle("ZFARM - PIK Bečej"); // set the top title
        actionBar.hide(); // or even hide the actionbar

        //Show progress bar
        ProgressBar myProgressBar = findViewById(R.id.progressBar);
        myProgressBar.setVisibility(View.VISIBLE);

        //FloatingActionButton
        myFab = (FloatingActionButton) findViewById(R.id.btnFLoat);
        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent searchIntent = new Intent(v.getContext(), SearchGrlib.class);

                Bundle myBundle = new Bundle();
                myBundle.putStringArray("EXTRA_GRLIB_LIST", grlibList);
                myBundle.putStringArray("EXTRA_GRLID_LIST", grlidList);
                searchIntent.putExtras(myBundle);

                v.getContext().startActivity(searchIntent);
            }
        });

        // Lookup the recyclerview in activity layout
        rvCows = findViewById(R.id.rvCows);;

        // Set layout manager to position the items
        rvCows.setLayoutManager(new LinearLayoutManager(this));

        // Create adapter passing in the empty data
        cowsListAdapter = new CowsListAdapter(cows);
        currentListSize = 0;

        // Attach the adapter to the recyclerview to populate items
        rvCows.setAdapter(cowsListAdapter);

        //Create async task to call service in background
        cowsAsyncTask = new CowsAsyncTask(this);

        //this to set delegate/listener back to this class
        cowsAsyncTask.delegate = this;

        //execute the async task
        cowsAsyncTask.execute();

    }

    //this override the implemented method from asyncTask
    @Override
    //public void processFinish(String output){
    public void processFinish(ArrayList<CowState> output){
        //Here you will receive the result fired from async class
        //of onPostExecute(result) method.
        cows = output;

        // Create adapter passing in the sample user data
        cowsListAdapter = new CowsListAdapter(cows);

        //Show action bar
        ActionBar actionBar = getSupportActionBar(); // or getActionBar();
        actionBar.show(); // or even hide the actionbar

        //Hide progress bar when finished
        ProgressBar myProgressBar = findViewById(R.id.progressBar);
        myProgressBar.setVisibility(View.GONE);

        //Show Floating Action button
        FloatingActionButton fabFind = findViewById(R.id.btnFLoat);
        fabFind.setVisibility(View.VISIBLE);

        //Replace splashcreen background with gradient background
        ConstraintLayout cLayout = findViewById(R.id.main_content);
        cLayout.setBackgroundResource(R.drawable.main_header_selector);
        //background="@drawable/main_header_selector"

        if (cows != null && cows.size() > 0) {

            // Set layout manager to position the items
            rvCows.setLayoutManager(new LinearLayoutManager(this));

            //Attach the adapter to the recyclerview to populate items
            rvCows.setAdapter(cowsListAdapter);
            cowsListAdapter.notifyItemRangeInserted(currentListSize, cows.size());
            cowsListAdapter.notifyDataSetChanged();

            grlibList = new String[cows.size()];
            grlidList = new String[cows.size()];
            for (int i = 0; i < cows.size(); i++) {

                grlibList[i]=  cows.get(i).getGrlib();
                grlidList[i]=  cows.get(i).getGrlid();

            }

        } else {

            Toast.makeText(this, "Nema krava na listi!",
                    Toast.LENGTH_LONG).show();

        }

    }

}
