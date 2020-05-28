package com.example.odataapp01.asyncTasks;

import android.os.AsyncTask;

import com.example.odataapp01.R;
import com.example.odataapp01.activities.CowEntriesActivity;
import com.example.odataapp01.activities.MainActivity;
import com.example.odataapp01.data.CowEntry;
import com.example.odataapp01.odata.SAPCSRFBehavior;
import com.sun.jersey.api.client.ClientHandlerException;

import org.joda.time.LocalDateTime;
import org.odata4j.consumer.ODataConsumer;
import org.odata4j.consumer.ODataConsumers;
import org.odata4j.consumer.behaviors.BasicAuthenticationBehavior;
import org.odata4j.core.OEntity;
import org.odata4j.core.UnsignedByte;
import org.odata4j.exceptions.ServerErrorException;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class EntriesAsyncTask extends AsyncTask<String, Integer, String> {

    public AsyncResponseEntries delegate = null;

    private WeakReference<CowEntriesActivity> activityReference;
    private ArrayList<CowEntry> cowEntries = new ArrayList<CowEntry>();
    private String errorMsg = "";

    // only retain a weak reference to the activity
    public EntriesAsyncTask(CowEntriesActivity context) {
        activityReference = new WeakReference<>(context);
    }

    @Override
    protected String doInBackground(String[] args) {

        // do some long running task...

        String bukrs = args[0];
        String grlid = args[1];

        String serviceUrl = "http://mkitsap.mk-group.org:8055/sap/opu/odata/sap/ZFARM_TEST_01_SRV/";

        String user = activityReference.get().getResources().getString(R.string.gwu);
        String pass = activityReference.get().getResources().getString(R.string.gwp);

        String lvResult = null;
        UnsignedByte lvByte;

        try {

                ODataConsumer consumer;
                consumer = ODataConsumers.create(serviceUrl);
                ODataConsumer.Builder builder = ODataConsumers.newBuilder(serviceUrl);
                builder.setClientBehaviors( new BasicAuthenticationBehavior(user, pass), new SAPCSRFBehavior());
                consumer = builder.build();

                for (OEntity UnosSet : consumer.getEntities("UnosSet").
                    filter("Bukrs eq '"+bukrs+"' and Grlid  eq '"+grlid+"'").
                    execute()) {

                    CowEntry cowEntry = new CowEntry(
                            UnosSet.getProperty("Bukrs").getValue().toString(),
                            UnosSet.getProperty("Grlid").getValue().toString(),
                            UnosSet.getProperty("Tipunosa").getValue().toString(),
                            UnosSet.getProperty("Sazetak").getValue().toString(),
                            (LocalDateTime) UnosSet.getProperty("Datun").getValue()
                    );

                    this.cowEntries.add(cowEntry);

                }

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

        delegate.processFinish(cowEntries);

    }



}