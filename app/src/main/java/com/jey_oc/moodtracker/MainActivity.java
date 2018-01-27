package com.jey_oc.moodtracker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;

import java.text.SimpleDateFormat;

/**
 * Created by Jey on 27/01/2018.
 */

public class MainActivity extends AppCompatActivity {

    // our scroll View
    private ScrollView scrv = null;
    // an array for the 5s available mood
    private ImageView img[] = new ImageView[5];
    // share's mood array
    private ImageView share_img[] = new ImageView[5];
    // our buttons historical and comment
    private ImageButton hist_btn = null, com_btn = null, share_btn = null;
    // initializing the id as int for the image's number we want to show so by default it's the 1.
    public int num_img = 1;
    // to set up the 5 coordinates in y of our 5 moods
    private int y_img[] = new int[5];
    // the fives mood Description for popup message
    private String mood[] = {"Very happy", "Happy", "Soso", "Disappointed", "Sad"};
    // A DateFormat for each methods that use date as string
    private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    //to set and compare last Mood'image saved
    private int lastImg;
    // the inputText item that will get back user's comment
    private EditText input;
    // a value to keep comment that user will input
    private String mText = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // set the activity_main.xml
        setContentView(com.jey_oc.moodtracker.R.layout.activity_main);

    }



}
