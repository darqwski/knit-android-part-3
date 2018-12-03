package com.example.knitandroid.localizer;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Darqwski on 2018-12-02.
 */

public class GetDataFromServer extends AsyncTask<String,String,String> {

    Context context;
    int responseCode;
    GetDataFromServer(Context newContext){
        context=newContext;
    }
    protected void onPreExecute(){

    }

    @Override
    protected String doInBackground(String... strings) {
        String urlString = strings[0];
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
        if(responseCode==200){
            Toast.makeText(context, "Apka działa poprawnie", Toast.LENGTH_SHORT).show();
        }
        ArrayList <UserLog> logs = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray("MainArray");
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject tempObject = jsonArray.getJSONObject(i);
                    String time= tempObject.getString("Time");
                    /**
                     *
                     * TODO Odebranie całego JSONa Lon Lat Time Nickname
                     */
                    UserLog log = new UserLog(time);
                    logs.add(log);

                }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        /**
         * TODO Dodać adapter do listView
         */

        if(responseCode!=200){

            return;
        }


    }

}
