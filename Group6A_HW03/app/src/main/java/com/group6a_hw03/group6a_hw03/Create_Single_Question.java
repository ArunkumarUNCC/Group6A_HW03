package com.group6a_hw03.group6a_hw03;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.io.IOException;

public class Create_Single_Question extends AppCompatActivity {

    public ImageView fAddAnswer, fSelectedImg;
    public EditText fQuestion, fAnswer;
    public String fUriString;

    public RadioGroup fAnswerGroup;

    final static int fSELECT_PICTURE = 1;

    final static String fUPLOAD_PIC_URL = "http://dev.theappsdr.com/apis/trivia_fall15/uploadPhoto.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create__single__question);

        fAddAnswer = (ImageView) findViewById(R.id.imageViewAddAnswer);
        fQuestion = (EditText) findViewById(R.id.editTextEnterQuestion);
        fAnswer = (EditText) findViewById(R.id.editTextEnterAnswer);
        fSelectedImg = (ImageView) findViewById(R.id.imageViewImageSelected);
        fAnswerGroup = (RadioGroup) findViewById(R.id.radioGroupAnswers);

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
        RadioButton lRadioAnswer = new RadioButton(this);
        lRadioAnswer.setText(fAnswer.getText());
        fAnswerGroup.addView(lRadioAnswer);
        //resets answer text so the same answer can't be put more than once
        fAnswer.setText("");
    }

    public void selectImageOnClick (View aView){
        Intent lPictureIntent = new Intent(Intent.ACTION_PICK);
        lPictureIntent.setType("image/");
        startActivityForResult(lPictureIntent, fSELECT_PICTURE);
    }

    public void submitQuestionOnClick (View aView){
        //1. uploadPhoto API (Returns URL)
        new uploadPicAsyncTask(this).execute(fUPLOAD_PIC_URL, fUriString);
        //2. Submit question content (with new URL)
        //Single semicolon string
    }

    @Override
    protected void onActivityResult(int aRequestCode, int aResultCode, Intent aData) {
        super.onActivityResult(aRequestCode, aResultCode, aData);
        //sets imageView as the image selected
        if(aResultCode == RESULT_OK){
            switch (aRequestCode){
                case fSELECT_PICTURE:
                    Uri lSelectedImgUri = aData.getData();
                    fUriString = lSelectedImgUri.toString();
                    fSelectedImg.setImageURI(lSelectedImgUri);
            }
        }
    }
}
