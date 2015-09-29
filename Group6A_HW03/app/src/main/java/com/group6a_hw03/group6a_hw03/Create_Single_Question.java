package com.group6a_hw03.group6a_hw03;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Create_Single_Question extends AppCompatActivity implements GenericAsyncTask.IData {

    public ImageView fAddAnswer, fSelectedImg;
    public EditText fQuestion, fAnswer;
    public String fUriString,  fAnswerString;
    public static String fImgUrl = "z";

    public RadioGroup fAnswerGroup;

    final static int fSELECT_PICTURE = 1;
    final static int fTOAST_LENGTH = Toast.LENGTH_LONG;

    public int fNumberAnswers;
    final static String fUPLOADING = "Uploading question...";
    final static String fUPLOAD_PIC_URL = "http://dev.theappsdr.com/apis/trivia_fall15/uploadPhoto.php";
    final static String fSAVE_QUESTION_URL = "http://dev.theappsdr.com/apis/trivia_fall15/saveNew.php";
    final static String fGROUP_ID = "356f512ffd7616a7f33d3a9bbb41e5b2";
    final static String fGID = "gid";
    final static String fQID = "q";
    final static String fPHOTO_PARAM = "uploaded_file";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create__single__question);

        fAddAnswer = (ImageView) findViewById(R.id.imageViewAddAnswer);
        fQuestion = (EditText) findViewById(R.id.editTextEnterQuestion);
        fAnswer = (EditText) findViewById(R.id.editTextEnterAnswer);
        fSelectedImg = (ImageView) findViewById(R.id.imageViewImageSelected);
        fAnswerGroup = (RadioGroup) findViewById(R.id.radioGroupAnswers);

        fNumberAnswers = 0;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create__single__question, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void addAnswerOnClick(View aView) {
        RadioButton lRadioAnswer = new RadioButton(this);
        if (fAnswer.getText().toString().equals("")) {
                sendToast("Answer can't be blank, try again!");
        } else if (fNumberAnswers > 6){
            sendToast("You have reached maximum number of answers");
        } else{
            lRadioAnswer.setText(fAnswer.getText());
            if (fAnswerString == null)
                fAnswerString = fAnswer.getText().toString() + ";";
            else fAnswerString += fAnswer.getText().toString() + ";";
            fAnswerGroup.addView(lRadioAnswer);
            //resets answer text so the same answer can't be put more than once
            fAnswer.setText("");
            fNumberAnswers++;
        }
    }

    public void selectImageOnClick(View aView) {
        Intent lPictureIntent = new Intent(Intent.ACTION_PICK);
        lPictureIntent.setType("image/");
        startActivityForResult(lPictureIntent, fSELECT_PICTURE);
    }

    public void submitQuestionOnClick(View aView) {
        if (fQuestion.getText().toString().equals("")) {
            sendToast("Please add a question!");
        } else if (fNumberAnswers < 2) {
            sendToast("Please enter at least 2 possible answers.");
        } else if (fAnswerGroup.getCheckedRadioButtonId() == -1) {
            sendToast("Please select an answer.");
        } else {
            if(connectedOnline()) {
                RequestParams lParams;
                //1. uploadPhoto API (Returns URL)
                lParams = new RequestParams("POST", fUPLOAD_PIC_URL);
                new UploadeImageAsyncTask(fUriString).execute(lParams);

                //2. Submit question content (with new URL)

//                lParams = new RequestParams("POST", fSAVE_QUESTION_URL);
//                lParams.addParam(fQID, createQuestionString());
//                lParams.addParam(fGID, fGROUP_ID);
//                new GenericAsyncTask(this, fUPLOADING).execute(lParams);
//
//                finish();
            }
        }
    }

    public String createQuestionString() {
        String lTempString;

        lTempString = fQuestion.getText().toString() + ";";
        lTempString += fAnswerString;

        if (fImgUrl == "-1" || fImgUrl == "z")
            lTempString += ";";
        else
            lTempString += fImgUrl + ";";

        lTempString += fAnswerGroup.getCheckedRadioButtonId() + ";";
        return lTempString;
    }

    public void sendToast(String aString) {
        Toast.makeText(this, aString, fTOAST_LENGTH).show();
    }

    @Override
    protected void onActivityResult(int aRequestCode, int aResultCode, Intent aData) {
        super.onActivityResult(aRequestCode, aResultCode, aData);
        //sets imageView as the image selected
        if (aResultCode == RESULT_OK) {
            switch (aRequestCode) {
                case fSELECT_PICTURE:
                    Uri lSelectedImgUri = aData.getData();

                    String[] filePathColumn = { MediaStore.Images.Media.DATA };

                    // Get the cursor
                    Cursor cursor = getContentResolver().query(lSelectedImgUri,
                            filePathColumn, null, null, null);
                    // Move to first row
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    cursor.moveToFirst();
                    fUriString = cursor.getString(columnIndex);

                    cursor.close();

//                    fUriString = lSelectedImgUri.toString();
                    fSelectedImg.setImageURI(lSelectedImgUri);
            }
        }
    }

    @Override
    public Context getContext() {
        return this;
    }

    private boolean connectedOnline(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();

        if(info!=null && info.isConnected()){
            return true;
        }
        else{

            sendToast("Internet Not Connected");
            return false;
        }
    }

    private class UploadeImageAsyncTask extends AsyncTask<RequestParams,Void,String>{
        BufferedReader lReader = null;
        int lBytesRead,lBytesAvailable,lBufferSize;

        int lMaxBufferSize = 1*1024*1024;
        byte[] lBuffer;
        String lUriString;

        public UploadeImageAsyncTask(String fUriString) {
            this.lUriString = fUriString;
        }

        @Override
        protected String doInBackground(RequestParams... params) {
            String lAttachmentName = "uploaded_file";
            String lAttachmentFileName = "Image";
            String lCrlf = "\r\n";
            String lTwoHyphens = "--";
            String lBoundary =  "*****";


            try {
                //HttpURLConnection lCon = params[0].setUpConnection();
                if (lUriString!=null) {
                    URL url = new URL(fUPLOAD_PIC_URL);
                    HttpURLConnection lCon = (HttpURLConnection) url.openConnection();
                    FileInputStream lFileInputStream = new FileInputStream(new File(lUriString));

                    lCon.setUseCaches(false);
                    lCon.setDoOutput(true);
                    lCon.setRequestProperty("Connection", "Keep Alive");
                    lCon.setRequestProperty("Cache-Control", "no-cache");
                    lCon.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + lBoundary);


                    DataOutputStream lDataOutputStream = new DataOutputStream(lCon.getOutputStream());

                    lDataOutputStream.writeBytes(lTwoHyphens + lBoundary + lCrlf);
                    lDataOutputStream.writeBytes("Content-Disposition: form-data; name=\"" + lAttachmentName + "\";filename=\""
                            + lAttachmentFileName + "\"" + lCrlf);
                    lDataOutputStream.writeBytes(lCrlf);


                    lBytesAvailable = lFileInputStream.available();
                    lBufferSize = Math.min(lBytesAvailable, lMaxBufferSize);
                    lBuffer = new byte[lBufferSize];

                    lBytesRead = lFileInputStream.read(lBuffer, 0, lBufferSize);

                    while (lBytesRead > 0) {
                        lDataOutputStream.write(lBuffer, 0, lBufferSize);
                        lBytesAvailable = lFileInputStream.available();
                        lBufferSize = Math.min(lBytesAvailable, lMaxBufferSize);
                        lBytesRead = lFileInputStream.read(lBuffer, 0, lBufferSize);
                    }

                    lDataOutputStream.writeBytes(lCrlf);
                    lDataOutputStream.writeBytes(lTwoHyphens + lBoundary + lCrlf);

                    lDataOutputStream.flush();
                    lDataOutputStream.close();

                    lReader = new BufferedReader(new InputStreamReader(lCon.getInputStream()));
                    StringBuilder lStringBuilder = new StringBuilder();
                    String lLine = "";
                    while ((lLine = lReader.readLine()) != null) {
                        lStringBuilder.append(lLine);
                    }
                    return lStringBuilder.toString();
                }
                else return "z";
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
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
        protected void onPostExecute(String aImgUrl) {
            super.onPostExecute(aImgUrl);

            fImgUrl = aImgUrl;

            RequestParams lParams = new RequestParams("POST", fSAVE_QUESTION_URL);
            lParams.addParam(fQID, createQuestionString());
            lParams.addParam(fGID, fGROUP_ID);
            new GenericAsyncTask(Create_Single_Question.this, fUPLOADING).execute(lParams);

            finish();
        }
    }
}
