package com.jey_oc.moodtracker;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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

        // Method to get size of screen
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        // set the activity_main.xml
        setContentView(R.layout.activity_main);

        // Init the scrollView
        scrv = (ScrollView) findViewById(R.id.scrollView);
        // init the Historical button
        hist_btn = (ImageButton)findViewById(R.id.historical_btn);
        // init the Comment button
        com_btn = (ImageButton)findViewById(R.id.comment_btn);
        // init the Comment button
        share_btn = (ImageButton)findViewById(R.id.share_btn);

        // init the 5 images button
        img = new ImageView[]{(ImageView) findViewById(R.id.smiley_0),(ImageView) findViewById(R.id.smiley_1),(ImageView) findViewById(R.id.smiley_2),
                (ImageView) findViewById(R.id.smiley_3),(ImageView) findViewById(R.id.smiley_4)};
        share_img = new ImageView[]{(ImageView) findViewById(R.id.share_0),(ImageView) findViewById(R.id.share_1),(ImageView) findViewById(R.id.share_2),
                (ImageView) findViewById(R.id.share_3),(ImageView) findViewById(R.id.share_4)};

        // set the 5's images height to screen height
        for(int i=0; i<5; i++){
            img[i].getLayoutParams().height = metrics.heightPixels;
        }

        //init on happy_smiley by default
        scrv.post(new Runnable(){
            @Override
            public void run() {
                // Locate image view
                for(int i=0; i<5; i++){
                    y_img[i] = img[i].getTop();
                }
                // Scroll to Image View.
                Scroll(num_img);
            }
        });
    }

    private void OnScrollMove(){
        scrv.setOnTouchListener(new ScrollView.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent event) {
                CheckoutButtonAvailability();
                // looking for the closer mood after release touch screen
                if(event.getAction() == android.view.MotionEvent.ACTION_UP ){
                    Log.d("ON SCROLL", "TRUE");
                    scrv.postDelayed(new Runnable(){
                        @Override
                        public void run() {
                            int h = scrv.getScrollY();
                            Log.d("ON SCROLL Y ", String.valueOf(h));
                            for(int i=0; i<5; i++){
                                if(i!=4){
                                    if(h<=0){
                                        // secure the zero position
                                        num_img=0;
                                        break;
                                    }else if(h>y_img[i] && h<y_img[i+1]) {
                                        if (h - y_img[i] > y_img[i + 1] - h) {
                                            num_img = i + 1;
                                        } else {
                                            num_img = i;
                                        }
                                        break;
                                    }
                                }else{
                                    // secure the last position
                                    num_img=4;
                                }
                            }
                            Log.d("ON SCROLL", String.valueOf(num_img));
                            if(dayPassed()){
                                moveSaves();
                            }else{
                                SaveImgChange();
                            }
                            CheckoutButtonAvailability();
                            // Scroll to Image View.
                            Scroll(num_img);
                        }
                    },300);
                }
                return false;
            }
        });
    }

    private void SaveImgChange(){
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("img0", num_img);
        editor.putString("date0",getCurrentDateStr());
        editor.apply();
    }

    private void Scroll(int num){
        scrv.smoothScrollTo(0, y_img[num]);
    }

    //to know if the Day of the last save passed
    protected boolean dayPassed(){
        try
        {
            String myTime = getPreferences(MODE_PRIVATE).getString("date0", "");
            Date lastD = df.parse(myTime);
            Calendar lastCal = Calendar.getInstance();
            lastCal.setTime(lastD);
            // init last date to 0:00:00 and advance to next day to compare with actual time
            lastCal.set(Calendar.HOUR_OF_DAY,0);
            lastCal.set(Calendar.MINUTE,0);
            lastCal.set(Calendar.SECOND,0);
            lastCal.set(Calendar.MILLISECOND,0);
            lastCal.add(Calendar.DAY_OF_MONTH, 1);

            // the actual time
            Calendar nowCal = Calendar.getInstance();
            // the boolean that compare if actual time passed last one at midnight
            boolean pass = nowCal.after(lastCal);
            return pass;
        }
        catch(ParseException e)
        {
            System.out.println(" Parsing Exception");
        }
        return false;
    }

    //to Get the current Date as a string format
    private String getCurrentDateStr(){
        Calendar cal = Calendar.getInstance();
        String newTime = df.format(cal.getTime());
        return newTime;
    }

    private void CheckoutButtonAvailability(){
        com_btn.setEnabled(true);
        com_btn.setVisibility(View.VISIBLE);
        share_btn.setEnabled(true);
        share_btn.setVisibility(View.VISIBLE);
        // Disable or Enable Historical button if necessary
        if(getPreferences(MODE_PRIVATE).getInt("img1", 99)!=99){
            HistBtnOn();
        }else{
            HistBtnOff();
        }
    }

    private void moveSaves(){
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        for (int i=7;i>0;i-- ){
            editor.putInt("img"+i, getPreferences(MODE_PRIVATE).getInt("img"+(i-1), 99));
            editor.putString("date"+i,getPreferences(MODE_PRIVATE).getString("date"+(i-1), ""));
            editor.putString("comment"+i,getPreferences(MODE_PRIVATE).getString("comment"+(i-1), ""));
            editor.apply();
        }
        editor.putInt("img0", num_img);
        editor.putString("date0",getCurrentDateStr());
        editor.putString("comment0","");
        editor.apply();
    }

    //Make a Toast to show wich mood selected
    private void PopupMsg(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    // to put Historical BTN Off
    private void HistBtnOff(){
        hist_btn.setVisibility(View.INVISIBLE);
        hist_btn.setEnabled(false);
    }

    // to put Historical BTN On
    private void HistBtnOn(){
        hist_btn.setVisibility(View.VISIBLE);
        hist_btn.setEnabled(true);
    }
}
