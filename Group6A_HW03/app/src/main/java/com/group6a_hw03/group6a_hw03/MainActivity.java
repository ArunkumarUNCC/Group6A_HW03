/*
    Michael Vitulli
    Arunkumar Bagavathi
    Group6A_HW03
*/
package com.group6a_hw03.group6a_hw03;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends AppCompatActivity implements GenericAsyncTask.IData{

    final static String fUPLOAD_DELETE_URL = "http://dev.theappsdr.com/apis/trivia_fall15/deleteAll.php";
    final static String fGROUP_ID = "356f512ffd7616a7f33d3a9bbb41e5b2";
    final static String fGID = "gid";
    final static String fDELETING = "Deleting all questions...";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public void startTrivaOnClick (View aView){
        Intent lStartTrivia = new Intent("com.group6a_hw03.group6a_hw03.intent.action.VIEW");
        startActivity(lStartTrivia);
    }

    public void createQuestionOnClick (View aView){
        Intent lNewQuestion = new Intent("com.group6a_hw03.group6a_hw03.intent.action.CREATE_QUESTION");
        startActivity(lNewQuestion);
    }

    public void deleteQuestionsOnClick (View aView){
        RequestParams lParams = new RequestParams("POST", fUPLOAD_DELETE_URL);
        lParams.addParam(fGID, fGROUP_ID);

        new GenericAsyncTask(this, fDELETING).execute(lParams);
    }

    public void exitAppOnClick (View aView){
        finish();
    }

    @Override
    public Context getContext() {
        return this;
    }
}
