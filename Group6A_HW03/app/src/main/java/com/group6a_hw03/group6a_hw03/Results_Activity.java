package com.group6a_hw03.group6a_hw03;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

public class Results_Activity extends AppCompatActivity {

    public ProgressBar fCorrectProgress;
    public TextView fProgressPercent;
    public TextView fMoreText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results_);

        fCorrectProgress = (ProgressBar) findViewById(R.id.progressBarCorrectAnswers);
        fProgressPercent = (TextView) findViewById(R.id.textViewPercentCorrect);
        fMoreText = (TextView) findViewById(R.id.textViewMoreText);
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

    }

    public void quitOnClick (View aView){
        finish();
    }
}
