package com.group6a_hw03.group6a_hw03;


import android.app.ProgressDialog;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class GenericAsyncTask extends AsyncTask<RequestParams, Integer, String> {
    ProgressDialog fProgress;
    Create_Single_Question fQuestionActivity;

    public GenericAsyncTask(Create_Single_Question aQuestionActivity) {
        this.fQuestionActivity = aQuestionActivity;
    }
    @Override
    protected void onPreExecute() {
        fProgress = new ProgressDialog(fQuestionActivity);
        fProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        fProgress.setMax(100);
        fProgress.setCancelable(false);
        fProgress.setMessage("Uploading Question...");
        fProgress.show();
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(RequestParams... params) {
        BufferedReader lReader = null;

        try {
            HttpURLConnection lCon = params[0].setUpConnection();
            lReader = new BufferedReader(new InputStreamReader(lCon.getInputStream()));
            StringBuilder lStringBuilder = new StringBuilder();
            String lLine = "";
            while ((lLine = lReader.readLine()) != null){
                lStringBuilder.append(lLine + "\n");
            }
            return lStringBuilder.toString();

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(lReader != null){
                try {
                    lReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        fProgress.dismiss();

    }
}
