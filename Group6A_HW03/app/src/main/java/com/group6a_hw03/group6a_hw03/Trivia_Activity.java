package com.group6a_hw03.group6a_hw03;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;

public class Trivia_Activity extends AppCompatActivity {

    public RadioButton fAnswerOne, fAnswerTwo, fAnswerThree, fAnswerFour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trivia_activity);

        fAnswerOne = (RadioButton) findViewById(R.id.radioButtonAnswer1);
        fAnswerTwo = (RadioButton) findViewById(R.id.radioButtonAnswer2);
        fAnswerThree = (RadioButton) findViewById(R.id.radioButtonAnswer3);
        fAnswerFour = (RadioButton) findViewById(R.id.radioButtonAnswer4);
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

    public void nextOnClick (View aView){

    }

    public void quitOnClick (View aView){
        finish();
    }
}


