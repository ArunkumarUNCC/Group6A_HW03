package com.group6a_hw03.group6a_hw03;


import android.app.ProgressDialog;
import android.os.AsyncTask;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class saveQuestionAsyncTask extends AsyncTask<String, Integer, String> {
    ProgressDialog fProgress;
    Create_Single_Question fQuestionActivity;

    public saveQuestionAsyncTask(Create_Single_Question aQuestionActivity) {
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
    protected String doInBackground(String... params) {
        try {
            URL lUrl = new URL(params[0]);
            HttpURLConnection lConnection = (HttpURLConnection) lUrl.openConnection();
            lConnection.setRequestMethod("POST");

            String lAnswerString = params[1];
            DataOutputStream lWriter = new DataOutputStream(lConnection.getOutputStream());
            lWriter.writeUTF(lAnswerString);

            lWriter.flush();
            lWriter.close();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        fProgress.dismiss();

    }
}
