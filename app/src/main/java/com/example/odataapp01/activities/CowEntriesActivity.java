package com.example.odataapp01.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.odataapp01.R;
import com.example.odataapp01.adapters.CowEntriesListAdapter;
import com.example.odataapp01.asyncTasks.AsyncResponseEntries;
import com.example.odataapp01.data.CowEntry;
import com.example.odataapp01.asyncTasks.EntriesAsyncTask;

import java.util.ArrayList;

public class CowEntriesActivity extends AppCompatActivity implements AsyncResponseEntries {

    private RecyclerView rvEntries;
    private ArrayList<CowEntry> cowEntries;
    private ProgressBar myProgressBar;
    private EntriesAsyncTask entriesAsyncTask;
    private CowEntriesListAdapter cowEntriesListAdapter;
    private int currentListSize;
    private ImageView ivIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_entries);

        //Action bar
        ActionBar actionBar = getSupportActionBar(); // or getActionBar();
        getSupportActionBar().setTitle("ZFARM - Unosi"); // set the top title

        //Show progress bar
        ProgressBar myProgressBar = findViewById(R.id.progressBar);
        myProgressBar.setVisibility(View.VISIBLE);

        // Lookup the recyclerview in activity layout
        rvEntries = findViewById(R.id.rvEntries);;

        // Set layout manager to position the items
        rvEntries.setLayoutManager(new LinearLayoutManager(this));

        // Create adapter passing in the empty data
        cowEntriesListAdapter = new CowEntriesListAdapter(cowEntries);
        currentListSize = 0;

        // Attach the adapter to the recyclerview to populate items
        rvEntries.setAdapter(cowEntriesListAdapter);

        //Get parameters passed from previous activity
        String bukrs = getIntent().getStringExtra("EXTRA_BUKRS");
        String grlid = getIntent().getStringExtra("EXTRA_GRLID");
        String grlib = getIntent().getStringExtra("EXTRA_GRLIB");
        String knjgk = getIntent().getStringExtra("EXTRA_KNJGK");

        //Create async task to call service in background
        EntriesAsyncTask myAsyncTask = new EntriesAsyncTask(this);

        //this to set delegate/listener back to this class
        myAsyncTask.delegate = this;

        //execute the async task
        myAsyncTask.execute(bukrs, grlid);

        //IB Grla
        TextView tvGrlib = (TextView) findViewById(R.id.tvGrlib);
        tvGrlib.setText(grlib);

        //Ikonica
        ivIcon = (ImageView) findViewById(R.id.ivIcon);

        //Ikona
        switch (knjgk) {
            case "K1":
            case "K2":
                ivIcon.setImageResource(R.drawable.ic_cow);
                break;
            case "B1":
                ivIcon.setImageResource(R.drawable.ic_bull);
                break;
            case "M1":
                ivIcon.setImageResource(R.drawable.ic_calf);
                break;
        }

    }

    //this override the implemented method from asyncTask
    @Override
    public void processFinish(ArrayList<CowEntry> output) {
        //Here you will receive the result fired from async class
        //of onPostExecute(result) method.
        cowEntries = output;

        // Create adapter passing in the sample user data
        cowEntriesListAdapter = new CowEntriesListAdapter(cowEntries);

        //Hide progress bar when finished
        ProgressBar myProgressBar = findViewById(R.id.progressBar);
        myProgressBar.setVisibility(View.GONE);

        if (cowEntries != null && cowEntries.size() > 0) {

            // Set layout manager to position the items
            rvEntries.setLayoutManager(new LinearLayoutManager(this));
            //Attach the adapter to the recyclerview to populate items
            rvEntries.setAdapter(cowEntriesListAdapter);
            cowEntriesListAdapter.notifyItemRangeInserted(currentListSize, cowEntries.size());
            cowEntriesListAdapter.notifyDataSetChanged();

        } else {

            Toast.makeText(this, "Nema krava na listi!",
                    Toast.LENGTH_LONG).show();

        }

    }

}
