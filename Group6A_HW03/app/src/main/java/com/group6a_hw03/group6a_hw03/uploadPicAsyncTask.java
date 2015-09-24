package com.group6a_hw03.group6a_hw03;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class uploadPicAsyncTask extends AsyncTask<String, Integer, String> {

    ProgressDialog fProgress;
    Create_Single_Question fQuestionActivity;

    public uploadPicAsyncTask(Create_Single_Question aQuestionActivity) {
        this.fQuestionActivity = aQuestionActivity;
    }

    @Override
    protected void onPreExecute() {

        fProgress = new ProgressDialog(fQuestionActivity);
        fProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        fProgress.setMax(100);
        fProgress.setCancelable(false);
        fProgress.setMessage("Uploading Image...");
        fProgress.show();

        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        BufferedReader lReader = null;
        String lInputLine;
        StringBuffer lResponse;

        try {
            URL lUrl = new URL(params[0]);
            HttpURLConnection lConnection = (HttpURLConnection) lUrl.openConnection();
            lConnection.setRequestMethod("POST");

            String lStringUri = params[1];
            DataOutputStream lWriter = new DataOutputStream(lConnection.getOutputStream());
            lWriter.writeUTF(lStringUri);

            lWriter.flush();
            lWriter.close();

            if (lConnection.getResponseCode() == 200) {
                lReader = new BufferedReader(new InputStreamReader(lConnection.getInputStream()));
                lResponse = new StringBuffer();

                while ((lInputLine = lReader.readLine()) != null) {
                    lResponse.append(lInputLine);
                }
                return lResponse.toString();//URL goes here
            } else
                return null;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (lReader != null)
                    lReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        //maybe passing the URL back?
        fQuestionActivity.fImgUrl = s;
        fProgress.dismiss();
    }
}