package com.jey_oc.moodtracker;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

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

        //Set each row of Historical
        for (int i = 0; i < 7; i++) {
            // build only if mood is registered
            if(feelings[i]!=99){
                //build title with timeAgo method to get clean title instead of date
                title[i].setText(timeAgo(dates[i]));
                // set everything with appropriate dimensions
                btn[i].getLayoutParams().height = he;
                btn[i].getLayoutParams().width = metrics.widthPixels;
                bkg[i].getLayoutParams().height = he;
                // calculating how much part of the screen depending on mood
                int len = (5 - feelings[i]) < 0 ? 0 : 5 - feelings[i];
                int framelen = wi * len;
                //setting it up
                bkg[i].getLayoutParams().width = framelen;
                int ic = (int) (metrics.widthPixels * 0.1f);
                cm[i].getLayoutParams().width = ic;
                // set margins
                FrameLayout.LayoutParams fl = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
                fl.setMargins(framelen - ic, 0, 0, 0);
                cm[i].setLayoutParams(fl);
                //set color of background depending on mood
                bkg[i].setBackgroundColor(Color.parseColor(cols[feelings[i]]));
                // checking if comment is available to show or not
                boolean comm = !(comments[i] == "" || comments[i] == " " || comments[i] == null || comments[i].length() == 0);
                btn[i].setEnabled(comm);
                if (comm) {
                    cm[i].setVisibility(View.VISIBLE);
                    // init the button listener only if a comment exist
                    InitBtn(i);
                } else {
                    // here it doesn't so button listener isn't called and indicator icon is masked
                    cm[i].setVisibility(View.INVISIBLE);
                }
            }
        }
    }

    // Toast message to show up comment when called
    private void PopupMsg(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    // Init Button's listener if got a comment to show up
    private void InitBtn(final int i){
        btn[i].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMsg(comments[i]);
            }
        });
    }

    // Determinate and return how long ago for titles
    private String timeAgo(String time){
        try
        {
            // parse the returned date string into date
            Date date = df.parse(time);
            // create 2 calendars one last and one now to compare
            Calendar lastCal = Calendar.getInstance();
            lastCal.setTime(date);
            Calendar nowCal = Calendar.getInstance();

            // setting up last and now calendars to 0:00:00 ( we need to compare days between)
            lastCal.set(Calendar.HOUR_OF_DAY,0);
            lastCal.set(Calendar.MINUTE,0);
            lastCal.set(Calendar.SECOND,0);
            lastCal.set(Calendar.MILLISECOND, 0);

            nowCal.set(Calendar.HOUR_OF_DAY,0);
            nowCal.set(Calendar.MINUTE,0);
            nowCal.set(Calendar.SECOND,0);
            nowCal.set(Calendar.MILLISECOND, 0);

            // converting everything in millisecond and get the subtract of now calendar by last calendar
            long msr = TimeUnit.MILLISECONDS.toDays(
                    Math.abs(nowCal.getTimeInMillis() - lastCal.getTimeInMillis()));

            // the reported msr value as int
            int days = (int)msr;

            // int array for check if got year, month, week or day between , respectively
            int counter[] ={(days/365),(days/30),(days/7),days};
            //init int to save the select value
            int select=99;
            // checking for each counter from year to day
            for(int i=0; i<counter.length;i++){
                Log.d("Counter "+i+" ", "n= "+counter[i]);
                if(counter[i]>0){
                    select=i;
                    break;
                }
            }
            // the result that will be returned by this method
            String result;
            // to determinate if it's singular or plural
            String s = select>4?"":counter[select]>1?"s":"";
            // checking for each counters
            switch (select){
                case 0 :
                    result=counter[select]+" year"+s+" ago";
                    break;
                case 1 :
                    result=counter[select]+" month"+s+" ago";
                    break;
                case 2 :
                    result=counter[select]+" week"+s+" ago";
                    break;
                case 3 :
                    result=counter[select]==1?"Yesterday":counter[select]+" days ago";
                    break;
                default :
                    result="";
                    break;
            }
            // return the result
            return result;
        }
        catch(ParseException e)
        {
            System.out.println(" Parsing Exception");
        }
        return "";
    }
}


