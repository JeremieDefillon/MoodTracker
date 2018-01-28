package com.jey_oc.moodtracker;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;

/**
 * Created by Jey on 27/01/2018.
 */

public class Historical extends AppCompatActivity{

    // the 5 colors set to use from happier to sader mood
    private String[] cols ={"#fff9ec4f","#ffb8e986","#a5468ad9","#ff9b9b9b","#ffde3c50"};
    // array that will contains 7 last moods recorded
    private int[] feelings = new int[7];
    // array that will contains 7 last comments recorded
    private String[] comments = new String[7];
    // array that will contains 7 last dates recorded
    private String[] dates = new String[7];

    // All the init of  Historical layout
    private FrameLayout[] bkg;
    private ImageView[] cm;
    private Button[] btn;
    private TextView[] title;

    // A DateFormat for each methods that use date as string
    private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        setContentView(com.jey_oc.moodtracker.R.layout.activity_historical);

        // Getting Arrays saved from Main activity, comments, feelings(moods) & dates
        Intent intent = getIntent();
        comments = intent.getStringArrayExtra("COMMENTS");
        feelings = intent.getIntArrayExtra("MOODS");
        dates = intent.getStringArrayExtra("DATES");

        // splits screen in equals part of width & height for set hisctorical's each row
        int he = (int) (metrics.heightPixels / 7.0f);
        int wi = (int) (metrics.widthPixels / 5.0f);

        //init the 7 FrameLayouts containers
        bkg = new FrameLayout[]{(FrameLayout) findViewById(com.jey_oc.moodtracker.R.id.day7),(FrameLayout) findViewById(com.jey_oc.moodtracker.R.id.day6),(FrameLayout) findViewById(com.jey_oc.moodtracker.R.id.day5),(FrameLayout) findViewById(com.jey_oc.moodtracker.R.id.day4),
                (FrameLayout) findViewById(com.jey_oc.moodtracker.R.id.day3),(FrameLayout) findViewById(com.jey_oc.moodtracker.R.id.day2),(FrameLayout) findViewById(com.jey_oc.moodtracker.R.id.day1)};

        //init the 7 ImageViews showing up comment icon if there's one
        cm = new ImageView[]{(ImageView) findViewById(com.jey_oc.moodtracker.R.id.day7_com),(ImageView) findViewById(com.jey_oc.moodtracker.R.id.day6_com),(ImageView) findViewById(com.jey_oc.moodtracker.R.id.day5_com),(ImageView) findViewById(com.jey_oc.moodtracker.R.id.day4_com),
                (ImageView) findViewById(com.jey_oc.moodtracker.R.id.day3_com),(ImageView) findViewById(com.jey_oc.moodtracker.R.id.day2_com),(ImageView) findViewById(com.jey_oc.moodtracker.R.id.day1_com)};

        //init the 7 Buttons to show up comment(if there's one) into a Toast
        btn = new Button[]{(Button)findViewById(com.jey_oc.moodtracker.R.id.day7_btn),(Button)findViewById(com.jey_oc.moodtracker.R.id.day6_btn),(Button)findViewById(com.jey_oc.moodtracker.R.id.day5_btn),(Button)findViewById(com.jey_oc.moodtracker.R.id.day4_btn),
                (Button)findViewById(com.jey_oc.moodtracker.R.id.day3_btn),(Button)findViewById(com.jey_oc.moodtracker.R.id.day2_btn),(Button)findViewById(com.jey_oc.moodtracker.R.id.day1_btn)};

        //init the 7 Frame's titles to set how long ago that saves exist
        title = new TextView[]{(TextView)findViewById(com.jey_oc.moodtracker.R.id.day7_title),(TextView)findViewById(com.jey_oc.moodtracker.R.id.day6_title),(TextView)findViewById(com.jey_oc.moodtracker.R.id.day5_title),(TextView) findViewById(com.jey_oc.moodtracker.R.id.day4_title),
                (TextView)findViewById(com.jey_oc.moodtracker.R.id.day3_title),(TextView)findViewById(com.jey_oc.moodtracker.R.id.day2_title),(TextView)findViewById(com.jey_oc.moodtracker.R.id.day1_title)};

    }
}
