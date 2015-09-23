package com.group6a_hw03.group6a_hw03;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

public class Create_Single_Question extends AppCompatActivity {

    public ImageView fAddAnswer;
    public EditText fQuestion, fAnswer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        fAddAnswer = (ImageView) findViewById(R.id.imageViewAddAnswer);
        fQuestion = (EditText) findViewById(R.id.editTextEnterQuestion);
        fAnswer = (EditText) findViewById(R.id.editTextEnterAnswer);

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

    public void addAnswerOnClick (View aView){

    }

    public void selectImageOnClick (View aView){

    }

    public void submitQuestionOnClick (View aView){

    }
}
