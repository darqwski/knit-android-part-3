package com.example.knitandroid.localizer;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Darqwski on 2018-12-02.
 */

public class SendDataToServer  extends AsyncTask<String,String,String> {

    Context context;
    int responseCode;
    SendDataToServer(Context newContext){
        context=newContext;
    }
    protected void onPreExecute(){

    }

    @Override
    protected String doInBackground(String... strings) {
        String urlString = strings[0];
        String urlData = strings[1];
        Log.wtf("URLDATA",urlData);
        OutputStream out = null;
        try {

            URL url = new URL(urlString);
            HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
            httpCon.setDoOutput(true);
            httpCon.setRequestMethod("POST");
                httpCon.setConnectTimeout(10000);
                httpCon.setReadTimeout(10000);


            OutputStream os = httpCon.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
            osw.write(urlData);
            osw.flush();
            osw.close();
            os.close();
            httpCon.connect();
            responseCode=httpCon.getResponseCode();
            BufferedInputStream bis = null;
            if(httpCon.getResponseCode() == HttpURLConnection.HTTP_OK)bis = new BufferedInputStream(httpCon.getInputStream());
            else bis = new BufferedInputStream(httpCon.getErrorStream());
            ByteArrayOutputStream buf = new ByteArrayOutputStream();
            int result = bis.read();
            while(result != -1) {
                buf.write((byte) result);
                result = bis.read();
            }

            return  buf.toString();

        } catch (Exception e) {
            return e.getMessage();

        }
    }
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        Log.wtf("Responsecode",String.valueOf(responseCode));
        if(result.equals("{\"message\":\"OK\"}"))
            Toast.makeText(context, "ARRR", Toast.LENGTH_SHORT).show();
        else
            Log.wtf("RESULT",result);
        /**
         * TODO if ok {"message":"OK"}
         * TODO if notok {"message":"ERROR"}
         */
        if(responseCode!=200){

            return;
        }


    }

}
