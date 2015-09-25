package com.group6a_hw03.group6a_hw03;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
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
    private static RadioButton fAnswerOne, fAnswerTwo;
    private static ProgressBar fLoadImage;
    private static ProgressDialog fActivityLoadProgress;

    static ArrayList<Question> fQuestionData;

    private static int fCurrentQuestion;
    private static int fCorrectAnswersCount;
    private final String GROUPID = "356f512ffd7616a7f33d3a9bbb41e5b2";
    private final String CHECKANSWER = "http://dev.theappsdr.com/apis/trivia_fall15/checkAnswer.php";

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
//            fAnswerOne = (RadioButton) findViewById(R.id.radioButtonAnswer1);
//            fAnswerTwo = (RadioButton) findViewById(R.id.radioButtonAnswer2);

            fCurrentQuestion = -1;
            fCorrectAnswersCount = 0;
            fQuestionData = new ArrayList<Question>();

            new getQuestion().execute("http://dev.theappsdr.com/apis/trivia_fall15/getAll.php");

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
    private class getQuestion extends AsyncTask<String, Void, ArrayList<Question>> {
        protected BufferedReader reader=null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            fActivityLoadProgress = new ProgressDialog(Trivia_Activity.this);
            fActivityLoadProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            fActivityLoadProgress.setCancelable(false);
            fActivityLoadProgress.setMessage("Loading Questions...");
            fActivityLoadProgress.show();
        }

        @Override
        protected ArrayList<Question> doInBackground(String... params) {
            try {
                URL url = new URL(params[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                String lQuestionRef = "";
                LinkedList<String> lOption;

                ArrayList<Question> lQuestions = new ArrayList<Question>();

                while((lQuestionRef = reader.readLine())!=null){
                    lOption = new LinkedList<String>();
                    String[] lParsedQuestion = lQuestionRef.split(";",-1);

                    int i = 0;
                    for(i=2 ; i<lParsedQuestion.length-2 ; i++){
                        lOption.add(lParsedQuestion[i]);
                    }

                    lQuestions.add(new Question(lParsedQuestion[0], lParsedQuestion[1],
                            lOption, lParsedQuestion[i]));
                }
                return lQuestions;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<Question> question) {
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

    class getImage extends AsyncTask<String,Void,Bitmap>{
        protected InputStream in = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            fLoadImage.setVisibility(View.VISIBLE);
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            try {
                URL url = new URL(params[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                in = connection.getInputStream();
                Bitmap image = BitmapFactory.decodeStream(in);

                return image;
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
        protected void onPostExecute(Bitmap questionImage) {
            super.onPostExecute(questionImage);
            fLoadImage.setVisibility(View.INVISIBLE);

            fQuestionImage.setImageBitmap(questionImage);

        }
    }

    public void nextOnClick (View aView){
        if(connectedOnline()) {
            new GetCorrectAnswer().execute(new GetAnswer(fOptions.getCheckedRadioButtonId(),
                    fQuestionData.get(fCurrentQuestion).getfQuestionId()));
            displayDetails();
        }
    }

    class GetCorrectAnswer extends AsyncTask<GetAnswer,Void,String>{
        BufferedReader reader = null;

        @Override
        protected String doInBackground(GetAnswer... params) {
            try {
                URL url = new URL(CHECKANSWER);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);

                OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
                writer.write("gid="+ URLEncoder.encode(GROUPID,"UTF-8")+"&qid="+URLEncoder.encode(params[0].getQuestionId(),"UTF-8")
                        +"&a="+URLEncoder.encode(String.valueOf(params[0].getAnswer()),"UTF-8"));
                writer.flush();

                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                return reader.readLine();


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
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if(result.equals("1")) {
                showToast("Correct Answer");
                fCorrectAnswersCount++;
            }
            else if (result.equals("0"))
                showToast("Wrong Answer");
            else showToast("FATAL ERROR");
        }
    }

    public void quitOnClick (View aView){
        quitActivity();
    }

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

    public void quitActivity(){
        finish();
    }

    private void displayDetails(){
        fCurrentQuestion++;
        fQuestionNumber.setText("Q " + (fCurrentQuestion + 1));

        if(connectedOnline()) {
            if (fQuestionData.get(fCurrentQuestion).getfImageLinks().isEmpty()) {
                new getImage().execute("http://www.bidhannagarmunicipality.org/uploaded_files/55966fba7a6d307032015164922.gif");
            } else {
                new getImage().execute(fQuestionData.get(fCurrentQuestion).getfImageLinks());
            }
        }

        fQuestionText.setText(fQuestionData.get(fCurrentQuestion).getfQuestions());

        ListIterator<String> lOptionsIterator = fQuestionData.get(fCurrentQuestion).getfRadioGroupOptions().listIterator();
        fOptions.removeAllViews();
        while(lOptionsIterator.hasNext()){
            RadioButton lOption = new RadioButton(Trivia_Activity.this);
            lOption.setText(lOptionsIterator.next());
            lOption.setId(lOptionsIterator.nextIndex());
            fOptions.addView(lOption);
        }
    }

    public void showToast(String displayThis){
        Toast.makeText(Trivia_Activity.this,displayThis,Toast.LENGTH_SHORT).show();
    }
}


