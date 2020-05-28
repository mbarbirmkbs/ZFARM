package com.example.odataapp01.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.example.odataapp01.R;

import java.util.Arrays;

public class SearchGrlib extends AppCompatActivity {

    private String[] grlibList;
    private String[] grlidList;
    private String grlid;
    private ArrayAdapter<String> adapterGrlibList;
    private AutoCompleteTextView editTextFilledExposedDropdownGrlibList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search_grlib);

        //Get parameters passed from previous activity
        grlibList = getIntent().getStringArrayExtra("EXTRA_GRLIB_LIST");
        grlidList = getIntent().getStringArrayExtra("EXTRA_GRLID_LIST");

        assert grlibList != null;
        adapterGrlibList =
                new ArrayAdapter<>(
                        this,
                        R.layout.dropdown_menu_popup_item,
                        grlibList);

        editTextFilledExposedDropdownGrlibList =
                findViewById(R.id.filled_exposed_dropdownGrlib);
        editTextFilledExposedDropdownGrlibList.setAdapter(adapterGrlibList);
        //Tip unosa ---------------------------------------------------->>>

    }

    public void onClickBtnOpen(View view) {

        //Traži grlid za odabrani grlib
        int i = Arrays.asList(grlibList)
                      .lastIndexOf(editTextFilledExposedDropdownGrlibList.getText().toString());
        grlid = grlidList[i];

        Intent detailsIntent = new Intent(view.getContext(), CowDetailsActivity.class);
        detailsIntent.putExtra("EXTRA_BUKRS", "1029");
        detailsIntent.putExtra("EXTRA_GRLID", grlid);
        detailsIntent.putExtra("EXTRA_KNJGK", "");
        view.getContext().startActivity(detailsIntent);

    }

}
