package com.example.odataapp01.asyncTasks;

import android.content.res.Resources;
import android.os.AsyncTask;

import com.example.odataapp01.R;
import com.example.odataapp01.activities.CowDetailsActivity;
import com.example.odataapp01.activities.MainActivity;
import com.example.odataapp01.data.CowState;
import com.example.odataapp01.odata.SAPCSRFBehavior;
import com.sun.jersey.api.client.ClientHandlerException;

import org.joda.time.LocalDateTime;
import org.odata4j.consumer.ODataConsumer;
import org.odata4j.consumer.ODataConsumers;
import org.odata4j.consumer.behaviors.BasicAuthenticationBehavior;
import org.odata4j.core.OEntity;
import org.odata4j.core.OEntityKey;
import org.odata4j.core.UnsignedByte;
import org.odata4j.exceptions.ServerErrorException;

import java.lang.ref.WeakReference;

public class CowAsyncTask extends AsyncTask<String, Integer, String> {

    public AsyncResponseDetails delegate = null;

    private WeakReference<CowDetailsActivity> activityReference;
    private CowState cow = new CowState();
    private String errorMsg = "";

    public CowAsyncTask(CowDetailsActivity context) {
        activityReference = new WeakReference<>(context);
    }

    @Override
    protected String doInBackground(String[] args) {

        // do some long running task...


        String bukrs = args[0];
        String grlid = args[1];

        String serviceUrl = "http://mkitsap.mk-group.org:8055/sap/opu/odata/sap/ZFARM_TEST_01_SRV/";

        //getApplicationContext().getResources().getString(R.string.toast_sync_completed)
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

            OEntityKey cowKey = OEntityKey.create("Bukrs", bukrs, "Grlid", grlid);
            //OEntityKey cowKey = OEntityKey.create("Bukrs", "1000", "Grlid", "1001000006");

            OEntity GrloCardSet = consumer.getEntity("GrloCardSet", cowKey ).execute();

            lvByte = (UnsignedByte) GrloCardSet.getProperty("Brlak").getValue();

            cow = new CowState(
                GrloCardSet.getProperty("Bukrs").getValue().toString(),
                GrloCardSet.getProperty("Frmid").getValue().toString(),
                GrloCardSet.getProperty("Grlid").getValue().toString(),
                GrloCardSet.getProperty("Grlib").getValue().toString(),
                GrloCardSet.getProperty("Knjgk").getValue().toString(),
                GrloCardSet.getProperty("Nazkg").getValue().toString(),
                GrloCardSet.getProperty("Grlime").getValue().toString(),
                GrloCardSet.getProperty("Repro").getValue().toString(),
                ((LocalDateTime) GrloCardSet.getProperty("ReproDate").getValue()),
                ((LocalDateTime) GrloCardSet.getProperty("Datrodj").getValue()),
                GrloCardSet.getProperty("Fazal").getValue().toString(),
                lvByte.toString(),
                (Integer) GrloCardSet.getProperty("Danul").getValue()
            );

        } catch (ClientHandlerException e) {

            //errorMsg = "Calling service for cow details went wrong!";
            errorMsg = e.getMessage();

        } catch (ServerErrorException e) {

            //errorMsg = "Calling service for cow details went wrong!";
            errorMsg = e.getMessage();

        }

        return "OK";
    }

    @Override
    protected void onPostExecute(String result) {

        delegate.processFinish(cow);

    }



}