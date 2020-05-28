package com.example.odataapp01.asyncTasks;

import android.os.AsyncTask;

import com.example.odataapp01.R;
import com.example.odataapp01.activities.MainActivity;
import com.example.odataapp01.data.CowState;
import com.example.odataapp01.odata.SAPCSRFBehavior;
import com.sun.jersey.api.client.ClientHandlerException;

import org.joda.time.LocalDateTime;
import org.odata4j.consumer.ODataConsumer;
import org.odata4j.consumer.ODataConsumers;
import org.odata4j.consumer.behaviors.BasicAuthenticationBehavior;
import org.odata4j.core.OEntity;
import org.odata4j.core.UnsignedByte;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class CowsAsyncTask  extends AsyncTask<Void, Void, String> {

    public AsyncResponseMain delegate = null;

    private WeakReference<MainActivity> activityReference;
    private ArrayList<CowState> cows = new ArrayList<CowState>();

    //only retain a weak reference to the activity
    public CowsAsyncTask(MainActivity context) {
        activityReference = new WeakReference<>(context);
    }

    @Override
    protected String doInBackground(Void... params) {

        // do some long running task...

        String serviceUrl = "http://mkitsap.mk-group.org:8055/sap/opu/odata/sap/ZFARM_TEST_01_SRV/";

        String user = activityReference.get().getResources().getString(R.string.gwu);
        String pass = activityReference.get().getResources().getString(R.string.gwp);

        String lvResult = null;
        org.odata4j.core.UnsignedByte lvByte;

        try {

            ODataConsumer consumer;
            consumer = ODataConsumers.create(serviceUrl);
            ODataConsumer.Builder builder = ODataConsumers.newBuilder(serviceUrl);
            builder.setClientBehaviors( new BasicAuthenticationBehavior(user, pass), new SAPCSRFBehavior());
            consumer = builder.build();

            for (OEntity GrloSet : consumer.getEntities("GrloCardSet").
                    //filter("Bukrs eq '1029' and Statg  eq 'A' and Knjgk  eq 'K1'").
                    filter("Bukrs eq '1029' and Statg  eq 'A'").
                    execute()) {

                lvByte = (UnsignedByte) GrloSet.getProperty("Brlak").getValue();

                CowState cow = new CowState(
                        GrloSet.getProperty("Bukrs").getValue().toString(),
                        GrloSet.getProperty("Frmid").getValue().toString(),
                        GrloSet.getProperty("Grlid").getValue().toString(),
                        GrloSet.getProperty("Grlib").getValue().toString(),
                        GrloSet.getProperty("Knjgk").getValue().toString(),
                        GrloSet.getProperty("Nazkg").getValue().toString(),
                        GrloSet.getProperty("Grlime").getValue().toString(),
                        GrloSet.getProperty("Repro").getValue().toString(),
                        (LocalDateTime) GrloSet.getProperty("ReproDate").getValue(),
                        (LocalDateTime) GrloSet.getProperty("Datrodj").getValue(),
                        GrloSet.getProperty("Fazal").getValue().toString(),
                        lvByte.toString(),
                        (Integer) GrloSet.getProperty("Danul").getValue()
                    );

                this.cows.add(cow);

            }

        } catch (ClientHandlerException e) {

            lvResult = e.getMessage();

        }

        return lvResult;
    }

    @Override
    protected void onPostExecute(String result) {

        delegate.processFinish(cows);

    }



}