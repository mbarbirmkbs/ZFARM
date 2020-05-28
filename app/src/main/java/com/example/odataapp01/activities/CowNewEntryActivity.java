package com.example.odataapp01.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.odataapp01.R;
import com.example.odataapp01.asyncTasks.AsyncResponseCowNewEntry;
import com.example.odataapp01.asyncTasks.CowNewEntryAsyncTask;
import com.example.odataapp01.data.CowEntry;

public class CowNewEntryActivity extends AppCompatActivity implements AsyncResponseCowNewEntry {

    private ImageView ivIcon;
    private CowEntry cowEntry = new CowEntry();
    private String errorMsg = "";
    private CowNewEntryAsyncTask cowNewEntryAsyncTask;
    private String bukrs;
    private String frmid;
    private String grlid;
    private String grlib;
    private String knjgk;
    private ArrayAdapter<String> adapterTipoviUnosa;
    private AutoCompleteTextView editTextFilledExposedDropdownTipUnosa;
    private AutoCompleteTextView editTextFilledExposedDropdownStaja;
    private AutoCompleteTextView editTextFilledExposedDropdownRazlogPremestaja;
    private com.google.android.material.textfield.TextInputLayout TextInputLayoutRazlogPremestaja;
    private com.google.android.material.textfield.TextInputLayout TextInputLayoutStaja;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_cow_new_entry);

        Resources resources = getResources();

        //Get parameters passed from previous activity
        bukrs = getIntent().getStringExtra("EXTRA_BUKRS");
        frmid = getIntent().getStringExtra("EXTRA_FRMID");
        grlid = getIntent().getStringExtra("EXTRA_GRLID");
        grlib = getIntent().getStringExtra("EXTRA_GRLIB");
        knjgk = getIntent().getStringExtra("EXTRA_KNJGK");

        //Action bar
        ActionBar actionBar = getSupportActionBar(); // or getActionBar();
        getSupportActionBar().setTitle("ZFARM - Novi unos"); // set the top title

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

        //Tip unosa ----------------------------------------------------<<<
        String[] tipoviUnosa = new String[] {
                "Premeštaj",
                "Tretman",
                "Teljenje",
                "Uginuće"
        };

        adapterTipoviUnosa =
                new ArrayAdapter<>(
                        this,
                        R.layout.dropdown_menu_popup_item,
                        tipoviUnosa);

        editTextFilledExposedDropdownTipUnosa =
                findViewById(R.id.filled_exposed_dropdownTipUnosa);
        editTextFilledExposedDropdownTipUnosa.setAdapter(adapterTipoviUnosa);
        //Tip unosa ---------------------------------------------------->>>



        //U Staju ----------------------------------------------------------------------<<<
        TextInputLayoutStaja = findViewById(R.id.TextInputLayoutStaja);

        String[] spisakStaja = new String[] {
                "1-Junice",
                "2-Visoko steone junice",
                "3-Krave muzne",
                "4-Krave zasušene",
                "5-Muška telad",
                "6-Ženska telad"
        };

        ArrayAdapter<String> adapterStaja =
                new ArrayAdapter<>(
                        this,
                        R.layout.dropdown_menu_popup_item,
                        spisakStaja);

        editTextFilledExposedDropdownStaja = findViewById(R.id.filled_exposed_dropdownStaja);
        editTextFilledExposedDropdownStaja.setAdapter(adapterStaja);
        TextInputLayoutStaja.setVisibility(View.GONE); //Sakrij inicijalno
        //U Staju ---------------------------------------------------------------------->>>



        //Razlog premeštaja --------------------------------------------------------------<<<
        TextInputLayoutRazlogPremestaja = findViewById(R.id.TextInputLayoutRazlogPremestaja);

        String[] razlogPremestaja = new String[] {
                "282-Mastitis",
                "284-Pobačaj",
                "285-Standardni",
                "286-Teljenje",
                "287-Zasušenje"
        };

        ArrayAdapter<String> adapterRazlogPremestaja =
                new ArrayAdapter<>(
                        this,
                        R.layout.dropdown_menu_popup_item,
                        razlogPremestaja);

        editTextFilledExposedDropdownRazlogPremestaja = findViewById(R.id.filled_exposed_dropdownRazlogPremestaja);
        editTextFilledExposedDropdownRazlogPremestaja.setAdapter(adapterRazlogPremestaja);
        editTextFilledExposedDropdownRazlogPremestaja.setText(razlogPremestaja[2], false);
        TextInputLayoutRazlogPremestaja.setVisibility(View.GONE);//Sakrij inicijalno
        //Razlog premeštaja ----------------------------------------------------------------->>>


        editTextFilledExposedDropdownTipUnosa.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("Premeštaj")){
                    //Prikaži Razlog premeštaja
                    TextInputLayoutRazlogPremestaja.setVisibility(View.VISIBLE);
                    //Prikaži Staju/boks
                    TextInputLayoutStaja.setVisibility(View.VISIBLE);
                }
                else {
                    //Sakrij Razlog premeštaja
                    TextInputLayoutRazlogPremestaja.setVisibility(View.GONE);
                    //Sakrij Staju/boks
                    TextInputLayoutStaja.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }

        });

    }

    public void onClickBtnSave(View view) {

        //Show progress bar
        ProgressBar myProgressBar = findViewById(R.id.progressBar);
        myProgressBar.setVisibility(View.VISIBLE);

        //Create async task to call service in background
        cowNewEntryAsyncTask = new CowNewEntryAsyncTask(this);

        //this to set delegate/listener back to this class
        cowNewEntryAsyncTask.delegate = this;

        //execute the async task
        cowNewEntryAsyncTask.execute(
                bukrs,
                frmid,
                grlid,
                grlib,
                //editTextFilledExposedDropdownTipUnosa.getText().toString(),
                "PREM", //Premeštaj je hardkodiran jer jedini trenutno radi
                editTextFilledExposedDropdownRazlogPremestaja.getText().toString(),
                editTextFilledExposedDropdownStaja.getText().toString()
        );

        //Deaktiviraj dugme unosi
        Button btnSnimiUnos  = (Button) findViewById(R.id.buttonSnimiUnos);
        btnSnimiUnos.setEnabled(false);

    }

    @Override
    public void processFinish(String output) {

//        Toast.makeText(this, output,
//                Toast.LENGTH_LONG).show();

        //Hide progress bar when finished
        ProgressBar myProgressBar = findViewById(R.id.progressBar);
        myProgressBar.setVisibility(View.GONE);

        Drawable icon = ContextCompat.getDrawable(this, android.R.drawable.ic_dialog_info).mutate();
        int color = Color.parseColor("#1A237E");
        icon.setColorFilter(color, PorterDuff.Mode.MULTIPLY);

        new AlertDialog.Builder(this)
                .setTitle("Kreiranje unosa:")
                .setMessage(output)
                .setIcon(icon)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        //Toast.makeText(MainActivity.this, "Yaay", Toast.LENGTH_SHORT).show();
                        finish(); //Završi ovaj activity i vrati se na prethodni
                    }})
                .show();
                //.setNegativeButton(android.R.string.no, null);

    }

}
