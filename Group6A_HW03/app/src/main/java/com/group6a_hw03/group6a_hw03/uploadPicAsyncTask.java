package com.group6a_hw03.group6a_hw03;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import java.io.IOException;
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
        try {
            URL lUrl = new URL(params[0]);
            HttpURLConnection lConnection = (HttpURLConnection) lUrl.openConnection();
            lConnection.setRequestMethod("POST");

            Bitmap lBitmap = getBitmap(Uri.parse(params[1]));



        } catch (MalformedURLException e) {
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

    public Bitmap getBitmap (Uri aUri) throws IOException {
       return MediaStore.Images.Media.getBitmap(fQuestionActivity.getContentResolver(), aUri);
    }
}