package com.example.odataapp01.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.odataapp01.R;
import com.example.odataapp01.asyncTasks.AsyncResponseDetails;
import com.example.odataapp01.data.CowState;
import com.example.odataapp01.asyncTasks.CowAsyncTask;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.view.ColumnChartView;

public class CowDetailsActivity extends AppCompatActivity implements AsyncResponseDetails {

    public ImageView ivIcon;

    private CowState cow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        String bukrs = getIntent().getStringExtra("EXTRA_BUKRS");
        String grlid = getIntent().getStringExtra("EXTRA_GRLID");
        String knjgk = getIntent().getStringExtra("EXTRA_KNJGK");

        CowAsyncTask myAsyncTask = new CowAsyncTask(this);

        //this to set delegate/listener back to this class
        myAsyncTask.delegate = this;

        //Show progress bar
        ProgressBar myProgressBar = findViewById(R.id.progressBar);
        myProgressBar.setVisibility(View.VISIBLE);

        //execute the async task
        myAsyncTask.execute(bukrs, grlid);

        //Ikonica
        ivIcon = (ImageView) findViewById(R.id.ivIcon);

        //Ikona
        setKategorija(knjgk);

    }

    //this override the implemented method from asyncTask
    @Override
    public void processFinish(CowState output) {
        //Here you will receive the result fired from async class
        //of onPostExecute(result) method.
        cow = output;

        Resources resources = getResources();

        //Action bar
        ActionBar actionBar = getSupportActionBar(); // or getActionBar();
        getSupportActionBar().setTitle("ZFARM - Karton grla"); // set the top title

        //Hide progress bar when finished
        ProgressBar myProgressBar = findViewById(R.id.progressBar);
        myProgressBar.setVisibility(View.GONE);

        //Aktiviraj dugme unosi
        Button btnUnosi  = (Button) findViewById(R.id.buttonUnosi);
        btnUnosi.setEnabled(true);

        //IB Grla
        TextView tvGrlib = (TextView) findViewById(R.id.tvGrlib);
        tvGrlib.setText(cow.getGrlib());

        //Ime grla
        if (cow.getGrlime() == "") {
            TableRow trGrlime = (TableRow) findViewById(R.id.trGrlime);
            trGrlime.setVisibility(View.GONE);
        } else {
            TextView tvGrlime = (TextView) findViewById(R.id.tvGrlime);
            tvGrlime.setText(cow.getGrlime());
        }

        //Kategorija grla
        TextView tvKnjgk = (TextView) findViewById(R.id.tvKnjgk);
        tvKnjgk.setText(String.format(resources.getString(R.string.kategorija), cow.getNazkg(), cow.getKnjgk()));
        setKategorija(cow.getKnjgk());

        //Repro status
        if (cow.getRepro() == "") {
            TableRow trRepro = (TableRow) findViewById(R.id.trRepro);
            trRepro.setVisibility(View.GONE);
        } else {
            TextView tvRepro = (TextView) findViewById(R.id.tvRepro);
            tvRepro.setText(String.format(resources.getString(R.string.GrloInfo1), cow.getRepro(), cow.getReproDate().toString("dd.MM.yyyy")));
        }

        //Faza laktacije
        if (cow.getFazal() == "") {
            TableRow trIbFazal = (TableRow) findViewById(R.id.trIbFazal);
            trIbFazal.setVisibility(View.GONE);
        } else {
            TextView tvFazal = (TextView) findViewById(R.id.tvFazal);
            tvFazal.setText(String.format(resources.getString(R.string.GrloInfo2), cow.getFazal(), cow.getBrlak(), cow.getDanul()));
        }

        //Datum rodjenja
        TextView tvDatRodj = (TextView) findViewById(R.id.tvDatRodj);
        //tvDatRodj.setText(String.format(resources.getString(R.string.kategorija), cow.getNazkg(), cow.getKnjgk()));
        tvDatRodj.setText(cow.getDatRodj().toString("dd.MM.yyyy"));

        //Prikaži grafik proizvodnje mleka
        if (cow.getKnjgk().equals("K1")) {
            generateDefaultData();
        }

    }

    private void generateDefaultData() {

        final int DEFAULT_DATA = 0;
        final int SUBCOLUMNS_DATA = 1;
        final int STACKED_DATA = 2;
        final int NEGATIVE_SUBCOLUMNS_DATA = 3;
        final int NEGATIVE_STACKED_DATA = 4;

        //private ColumnChartView chart;
        ColumnChartData data;
        boolean hasAxes = true;
        boolean hasAxesNames = true;
        boolean hasLabels = true;
        boolean hasLabelForSelected = false;
        int dataType = DEFAULT_DATA;
        int numSubcolumns = 1;
        int numColumns = 7;

        //Moje promenjive
        List myMilk = new ArrayList<Integer>();
        Integer minValue = 0;
        Integer maxValue = 0;

        // Column can have many subcolumns, here by default I use 1 subcolumn in each of 8 columns.
        List<Column> columns = new ArrayList<Column>();
        List<SubcolumnValue> values = null;
        List<AxisValue> axisValuesX = new ArrayList<AxisValue>();

        //Generate random values
        for (int i = 0; i < numColumns; ++i) {
            //values.add(new SubcolumnValue((float) Math.random() * 50f + 5));
            myMilk.add(((int) Math.round(Math.random() * 10f + 20)));
            Integer helpInt = (Integer) myMilk.get(i);
            if (helpInt > maxValue) {
                maxValue = helpInt;
            }
            if ((helpInt < minValue) || (minValue == 0)) {
                minValue = helpInt;
            }
        }

        for (int i = 0; i < numColumns; ++i) {

            values = new ArrayList<SubcolumnValue>();
            for (int j = 0; j < numSubcolumns; ++j) {

                //Dodeli vrednosti za stubiće
                SubcolumnValue helpSCV = new SubcolumnValue();
                //helpSCV.setValue((int) Math.round((float) Math.random() * 30f + 5));
                helpSCV.setValue((float) (Integer) myMilk.get(i));

                if (minValue.equals(myMilk.get(i))) {
                    helpSCV.setColor(Color.parseColor("#FF4444"));
                } else if (maxValue.equals(myMilk.get(i))) {
                    helpSCV.setColor(Color.parseColor("#99CC00"));
                } else {
                    helpSCV.setColor(Color.parseColor("#33B5E5"));
                }
                values.add(helpSCV);

                //Postavi nazive na X osi
                AxisValue axisValue = new AxisValue((float) 0.0);
                axisValue.setLabel("0" + Integer.toString(i + 1) + ".apr");
                axisValue.setValue(i);
                axisValuesX.add(axisValue);

            }

            Column column = new Column(values);
            column.setHasLabels(hasLabels);
            column.setHasLabelsOnlyForSelected(hasLabelForSelected);
            columns.add(column);

        }

        data = new ColumnChartData(columns);

        if (hasAxes) {
            Axis axisX = new Axis();

            axisX.setValues(axisValuesX);
            axisX.setTextColor(Color.parseColor("#1A237E"));
            axisX.setHasTiltedLabels(true);

            Axis axisY = new Axis().setHasLines(true);
            axisY.setTextColor(Color.parseColor("#1A237E"));

            if (hasAxesNames) {
                axisX.setName("Datum");
                axisY.setName("Litara mleka");
            }
            data.setAxisXBottom(axisX);
            data.setAxisYLeft(axisY);
        } else {
            data.setAxisXBottom(null);
            data.setAxisYLeft(null);
        }

        //Naslov grafika
        TextView tvChartTitle = (TextView) findViewById(R.id.tvChartTitle);
        tvChartTitle.setVisibility(View.VISIBLE);

        //Prikaži grafik
        ColumnChartView chart = (ColumnChartView) findViewById(R.id.ccvChart);
        chart.setColumnChartData(data);
        chart.setVisibility(View.VISIBLE);

    }

    public void setKategorija(String knjgk){

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

    public void onClickBtnUnosi(View view)
    {
        Intent entriesIntent = new Intent(view.getContext(), CowEntriesActivity.class);
        entriesIntent.putExtra("EXTRA_BUKRS", cow.getBukrs());
        entriesIntent.putExtra("EXTRA_GRLID", cow.getGrlid());
        entriesIntent.putExtra("EXTRA_GRLIB", cow.getGrlib());
        entriesIntent.putExtra("EXTRA_KNJGK", cow.getKnjgk());
        view.getContext().startActivity(entriesIntent);
    }

    public void onClickBtnNoviUnos(View view)
    {
        Intent entriesIntent = new Intent(view.getContext(), CowNewEntryActivity.class);
        entriesIntent.putExtra("EXTRA_BUKRS", cow.getBukrs());
        entriesIntent.putExtra("EXTRA_FRMID", cow.getFrmid());
        entriesIntent.putExtra("EXTRA_GRLID", cow.getGrlid());
        entriesIntent.putExtra("EXTRA_GRLIB", cow.getGrlib());
        entriesIntent.putExtra("EXTRA_KNJGK", cow.getKnjgk());
        view.getContext().startActivity(entriesIntent);
    }

}
