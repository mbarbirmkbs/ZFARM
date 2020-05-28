package com.example.odataapp01.asyncTasks;

import android.os.AsyncTask;

import com.example.odataapp01.R;
import com.example.odataapp01.activities.CowNewEntryActivity;
import com.example.odataapp01.activities.MainActivity;
import com.example.odataapp01.odata.SAPCSRFBehavior;
import com.sun.jersey.api.client.ClientHandlerException;

import org.joda.time.LocalTime;
import org.odata4j.consumer.ODataConsumer;
import org.odata4j.consumer.ODataConsumers;
import org.odata4j.consumer.behaviors.BasicAuthenticationBehavior;
import org.odata4j.core.OEntity;
import org.odata4j.core.OEntityKey;
import org.odata4j.core.OProperties;
import org.odata4j.exceptions.BadRequestException;
import org.odata4j.exceptions.ServerErrorException;
import org.odata4j.format.FormatType;
import org.odata4j.jersey.consumer.behaviors.AllowSelfSignedCertsBehavior;
import java.lang.ref.WeakReference;
import org.joda.time.LocalDateTime;

public class CowNewEntryAsyncTask extends AsyncTask<String, Integer, String> {

    public AsyncResponseCowNewEntry delegate = null;
    private FormatType formatType;
    private WeakReference<CowNewEntryActivity> activityReference;
    private String errorMsg = "";

    // only retain a weak reference to the activity
    public CowNewEntryAsyncTask(CowNewEntryActivity context) {
        activityReference = new WeakReference<>(context);
    }

    @Override
    protected String doInBackground(String[] args) {

        String bukrs            = args[0];
        String frmid            = args[1];
        String grlid            = args[2];
        String grlib            = args[3];
        String tipUnosa         = args[4];
        String razlogPremestaja = args[5];
        String uStaju           = args[6];

        //Obriši tekstualni deo i ostavi samo key
        razlogPremestaja = razlogPremestaja.substring(0, razlogPremestaja.indexOf("-"));
        uStaju = uStaju.substring(0, uStaju.indexOf("-"));

        //String serviceUrl = "http://mkitsap.mk-group.org:8055/sap/opu/odata/sap/ZFARM_TEST_01_SRV/";
        String serviceUrl = "https://mkitsap.mk-group.org:44355/sap/opu/odata/sap/ZFARM_TEST_01_SRV/";

        String user = activityReference.get().getResources().getString(R.string.gwu);
        String pass = activityReference.get().getResources().getString(R.string.gwp);

        try {

            ODataConsumer consumer;
            consumer = ODataConsumers.create(serviceUrl);
            ODataConsumer.Builder builder = ODataConsumers.newBuilder(serviceUrl);
            builder.setClientBehaviors( new BasicAuthenticationBehavior(user, pass), new SAPCSRFBehavior(), AllowSelfSignedCertsBehavior.allowSelfSignedCerts());
            builder.setFormatType(FormatType.ATOM);
            consumer = builder.build();

            //Prvo uradi jedan GET method da bi dobio CRSF token koji onda koristi u POST metodi
            OEntityKey testKey = OEntityKey.create("Bukrs", "1029", "Frmid", "MG11", "Unosid", "1029000001");
            OEntity UnosSet = consumer.getEntity("UnosSet", testKey ).execute();

            OEntity newCowEntry = consumer.createEntity("UnosSet")
                    .properties(OProperties.string("Bukrs", bukrs)) //1029
                    .properties(OProperties.string("Frmid", frmid)) //MG11
                    .properties(OProperties.string("Grlid", grlid)) //1000000725
                    .properties(OProperties.string("Psind", "S"))
                    .properties(OProperties.string("Tipunosa", tipUnosa )) //"PREM"
                    .properties(OProperties.string("RazlPrem", razlogPremestaja)) //"285"
                    .properties(OProperties.string("Ustaju", uStaju)) //"04"
                    .properties(OProperties.datetime("Datun", new LocalDateTime()))
                    .properties(OProperties.time("Vrmun", new LocalTime()))
                    //.properties(OProperties.int32("Rating", 1))
                    //.properties(OProperties.decimal("Price", 1.23))
                    .execute();

        } catch (ClientHandlerException | BadRequestException |ServerErrorException e) {

            //errorMsg = "Calling service for cow details went wrong!";
            errorMsg = e.getMessage();
            return errorMsg;

        }

        return "Unos uspešno kreiran";
    }

    @Override
    protected void onPostExecute(String result) {

        delegate.processFinish(result);

    }



}