package com.group6a_hw03.group6a_hw03;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;


public class GenericAsyncTask extends AsyncTask<RequestParams, Integer, String> {
    ProgressDialog fProgress;
    Create_Single_Question fQuestionActivity;
    IData fActivity;
    String fProgressMessage;

//    public GenericAsyncTask(Create_Single_Question aQuestionActivity) {
//        this.fQuestionActivity = aQuestionActivity;
//    }
    public GenericAsyncTask(IData aActivity, String aString) {
        this.fActivity = aActivity;
        this.fProgressMessage = aString;
    }
    @Override
    protected void onPreExecute() {
        fProgress = new ProgressDialog((Context) fActivity);
        fProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        fProgress.setMax(100);
        fProgress.setCancelable(false);
        fProgress.setMessage(fProgressMessage);
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
        super.onPostExecute(s);;
        fProgress.dismiss();

    }

    public interface IData{
        Context getContext();
    }
}
