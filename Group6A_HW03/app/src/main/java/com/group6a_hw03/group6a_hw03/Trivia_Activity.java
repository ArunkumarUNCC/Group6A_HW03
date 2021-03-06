package com.group6a_hw03.group6a_hw03;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.service.carrier.CarrierIdentifier;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class Trivia_Activity extends AppCompatActivity {

    private static TextView fQuestionNumber,fQuestionText;
    private static ImageView fQuestionImage;
    private static RadioGroup fOptions;
    private static ProgressBar fLoadImage;

    static ArrayList<Question> fQuestionData;
    private static int fCurrentQuestion;
    private static int fCorrectAnswersCount;

    private final String fGROUPID = "356f512ffd7616a7f33d3a9bbb41e5b2";
    private final String fGETQUESTIONS = "http://dev.theappsdr.com/apis/trivia_fall15/getAll.php";
    private final String fCHECKANSWER = "http://dev.theappsdr.com/apis/trivia_fall15/checkAnswer.php";
    private final String fDEFAULT_IMAGE = "http://www.bidhannagarmunicipality.org/uploaded_files/55966fba7a6d307032015164922.gif";
    private final String fGO_TO_RESULT = "com.group6a_hw03.group6a_hw03.intent.action.TEST_RESULT";
    private final String fGET = "GET";
    private final String fPOST = "POST";
    private final String fLOADQUESTIONS_PROGRESSBAR_LABEL = "Loading Questions...";
    private final String fGID = "gid";
    private final String fQID = "qid";
    private final String fA = "a";
    private final String fQ = "Q ";
    public static String fRESULT_FLAG = "Test Rsults";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trivia_activity);

        if(connectedOnline()) {
            fQuestionNumber = (TextView) findViewById(R.id.textViewQuestionNumber);
            fQuestionText = (TextView) findViewById(R.id.textViewQuestionText);
            fQuestionImage = (ImageView) findViewById(R.id.imageViewLoadingImage);
            fLoadImage = (ProgressBar) findViewById(R.id.progressBarLoadImage);
            fOptions = (RadioGroup) findViewById(R.id.radioGroupOptions);

            fCurrentQuestion = -1;
            fCorrectAnswersCount = 0;
            fQuestionData = new ArrayList<Question>();

            RequestParams lParams = new RequestParams(fGET,fGETQUESTIONS);
            new GetQuestion().execute(lParams);

        }
        else quitActivity();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_creaute__question, menu);
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



    //AsyncTask to return array of Question objects
    private class GetQuestion extends AsyncTask<RequestParams, Void, ArrayList<Question>> {
        protected BufferedReader lReader=null;
        ProgressDialog fActivityLoadProgress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            fActivityLoadProgress = new ProgressDialog(Trivia_Activity.this);
            fActivityLoadProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            fActivityLoadProgress.setCancelable(false);
            fActivityLoadProgress.setMessage(fLOADQUESTIONS_PROGRESSBAR_LABEL);
            fActivityLoadProgress.show();
        }

        @Override
        protected ArrayList<Question> doInBackground(RequestParams... params) {
            try {

                HttpURLConnection connection = params[0].setUpConnection();

                lReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                String lQuestionRef = "";
                LinkedList<String> lOption;

                ArrayList<Question> lQuestions = new ArrayList<Question>();

                while((lQuestionRef = lReader.readLine())!=null){
                    lOption = new LinkedList<String>();
                    String[] lParsedQuestion = lQuestionRef.split(";",-1);

                    try {
                        //Handling Exceptions for the received questions
                        //For Empty Question
                        if (lParsedQuestion[1].isEmpty()){
                            throw new InValidQuestionException("Missing Question",lParsedQuestion[0]);
                        }

                        int i;
                        for(i=2 ; i<lParsedQuestion.length-2 ; i++){
                            //For Empty Options
                            if(lParsedQuestion[i].isEmpty())
                                throw new InValidQuestionException("Empty Option",lParsedQuestion[0]);

                            else
                                lOption.add(lParsedQuestion[i]);
                        }

                        //For many options
                        if(i>9)
                            throw new InValidQuestionException("Too many options!",lParsedQuestion[0]);

                        if (!lParsedQuestion[i].isEmpty()) {
                            //Handling URL Exception
                            URL lCheckUrlException = new URL(lParsedQuestion[i]);
                        }

                        //Creating Question object if all values are valid
                        lQuestions.add(new Question(lParsedQuestion[0], lParsedQuestion[1],
                                lOption, lParsedQuestion[i]));

                    } catch (InValidQuestionException e) {
                        Log.d("Question " + e.getExceptionQuestion() + " not added to the test due to:", e.getExceptionReason());
//                        e.printStackTrace();
                    } catch (MalformedURLException e) {
                        Log.d("Question  not added to the test due to:", "Invalid URL");
//                        e.printStackTrace();
                    } catch (IOException e) {
                        Log.d("Question  not added to the test due to:","Invalid URL");
//                        e.printStackTrace();
                    }

                }
                return lQuestions;
            }  catch (MalformedURLException e) {
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
        protected void onPostExecute(ArrayList<Question> question) {
            //Returns arraylist of Question objects if the list is not empty
            super.onPostExecute(question);
            fActivityLoadProgress.dismiss();

            fQuestionData = question;

            if(question.isEmpty()) {
                showToast("No questions currently in the database");
                quitActivity();
            }
            else {

                displayDetails();
            }
        }

    }



    //AsyncTask to set the image bitmaps
    class GetImage extends AsyncTask<RequestParams,Void,Bitmap>{
        protected InputStream lImage = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            fLoadImage.setVisibility(View.VISIBLE);
        }

        @Override
        protected Bitmap doInBackground(RequestParams... params) {
            try {

                HttpURLConnection connection = params[0].setUpConnection();

                lImage = connection.getInputStream();

                return BitmapFactory.decodeStream(lImage);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(lImage != null){
                    try {
                        lImage.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Bitmap questionImage) {
            //Sets the image view
            super.onPostExecute(questionImage);
            fLoadImage.setVisibility(View.INVISIBLE);

            fQuestionImage.setImageBitmap(questionImage);

        }
    }


    //Function for clicking Next Button
    public void nextOnClick (View aView){
        if(connectedOnline()) {
            fQuestionImage.setImageResource(0);
            //Checks if the current question is last or not
            if (fCurrentQuestion == fQuestionData.size()-1){
                Intent lTestResult = new Intent(fGO_TO_RESULT);
                lTestResult.putExtra(fRESULT_FLAG,new int[]{fCorrectAnswersCount,fQuestionData.size()});

                startActivity(lTestResult);
                quitActivity();
            }
            else {
                RequestParams lParams = new RequestParams(fPOST,fCHECKANSWER);
                lParams.addParam(fGID,fGROUPID);
                lParams.addParam(fQID,fQuestionData.get(fCurrentQuestion).getfQuestionId());
                lParams.addParam(fA, String.valueOf(fOptions.getCheckedRadioButtonId()));
//                Log.d("Check Answer", String.valueOf(fOptions.getCheckedRadioButtonId()));
                new GetCorrectAnswer().execute(lParams);
                displayDetails();
            }
        }
    }


    //AsyncTask to check whether the answer is correct or not
    class GetCorrectAnswer extends AsyncTask<RequestParams,Void,Integer>{
        BufferedReader lReader = null;

        @Override
        protected Integer doInBackground(RequestParams... params) {
            try {

                HttpURLConnection connection = params[0].setUpConnection();

                lReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                return Integer.valueOf(lReader.readLine());


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
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
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);

            switch (result) {
                case 1:
                    showToast("Correct Answer");
                    fCorrectAnswersCount++;
                    break;
                case 0:
                    showToast("Wrong Answer");
                    break;
                default:
                    showToast("FATAL ERROR");
                    break;
            }
        }
    }


    //Function for clicking Quit button
    public void quitOnClick (View aView){
        quitActivity();
    }

    //Function to check whether the internet connection is available or not
    private boolean connectedOnline(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();

        if(info!=null && info.isConnected()){
            return true;
        }
        else{

            showToast("Internet Not Connected");
            return false;
        }
    }

    //Function to quit the activity
    public void quitActivity(){
        finish();
    }


    //Function to display all the views in the current activity
    private void displayDetails(){
        RadioButton lOption;
        ListIterator<String> lOptionsIterator;

        fCurrentQuestion++;
        fQuestionNumber.setText(fQ + (fCurrentQuestion + 1));



        //To download and set the image
        if(connectedOnline()) {
            RequestParams lParams;
            if (fQuestionData.get(fCurrentQuestion).getfImageLinks().isEmpty()) {
                lParams = new RequestParams(fGET,fDEFAULT_IMAGE);
                new GetImage().execute(lParams);
            } else {
                lParams = new RequestParams(fGET,fQuestionData.get(fCurrentQuestion).getfImageLinks());
                new GetImage().execute(lParams);
            }
        }

        //To set the question text and options
        fQuestionText.setText(fQuestionData.get(fCurrentQuestion).getfQuestions());

        lOptionsIterator = fQuestionData.get(fCurrentQuestion).getfRadioGroupOptions().listIterator();
        fOptions.removeAllViews();
        while(lOptionsIterator.hasNext()){
            lOption = new RadioButton(Trivia_Activity.this);
            lOption.setText(lOptionsIterator.next());
            lOption.setId(lOptionsIterator.nextIndex());
            fOptions.addView(lOption);
            fOptions.clearCheck();
        }

        //Toast.makeText(Trivia_Activity.this,fOptions.getCheckedRadioButtonId()+"",Toast.LENGTH_SHORT).show();
    }


    //Function to display all Toast messages
    public void showToast(String displayThis){
        Toast.makeText(Trivia_Activity.this,displayThis,Toast.LENGTH_SHORT).show();
    }
}


