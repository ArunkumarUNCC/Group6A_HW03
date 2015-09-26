package com.group6a_hw03.group6a_hw03;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class Results_Activity extends AppCompatActivity {

    public ProgressBar fCorrectProgress;
    public TextView fProgressPercent;
    public TextView fMoreText;

    private final String fGO_TO_TEST = "com.group6a_hw03.group6a_hw03.intent.action.VIEW";
    private final String fPercentage = " %";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results_);

        int[] lTestResults = getIntent().getExtras().getIntArray(Trivia_Activity.fRESULT_FLAG);
//        Integer[] lTestResults = lIntentResult.getIntArray(Trivia_Activity.RESULT_FLAG);
        //Toast.makeText(Results_Activity.this,lTestResults.length,Toast.LENGTH_LONG).show();

        fCorrectProgress = (ProgressBar) findViewById(R.id.progressBarCorrectAnswers);
        fProgressPercent = (TextView) findViewById(R.id.textViewPercentCorrect);
        fMoreText = (TextView) findViewById(R.id.textViewMoreText);

        assert lTestResults != null;
        int lpercentCorret = (lTestResults[0]/lTestResults[1])/100;

        fProgressPercent.setText(lpercentCorret + fPercentage);
        fCorrectProgress.setProgress(lpercentCorret);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_results_, menu);
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

    public void tryAgainOnClick (View aView){
        Intent lStartTrivia = new Intent(fGO_TO_TEST);
        startActivity(lStartTrivia);
        finish();
    }

    public void quitOnClick (View aView){
        finish();
    }
}
