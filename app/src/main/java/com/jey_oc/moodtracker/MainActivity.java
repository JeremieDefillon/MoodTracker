package com.jey_oc.moodtracker;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    // our scroll View
    private ScrollView scrv = null;
    // an array for the 5s available mood
    private ImageView img[] = new ImageView[5];
    // share's mood array
    private ImageView share_img[]= new ImageView[5];
    // our buttons historical and comment
    private ImageButton hist_btn = null, com_btn = null, share_btn = null;
    // initializing the id as int for the image's number we want to show so by default it's the 1.
    public int num_img=1;
    // to set up the 5 coordinates in y of our 5 moods
    private int y_img[] = new int[5];
    // the fives mood Description for popup message
    private String mood[]={"Very happy","Happy","Soso","Disappointed","Sad"};
    // A DateFormat for each methods that use date as string
    private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    //to set and compare last Mood'image saved
    private int lastImg;
    // the inputText item that will get back user's comment
    private EditText input;
    // a value to keep comment that user will input
    private String mText="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Method to get size of screen
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        //Method to get last save Img
        lastImg = getPreferences(MODE_PRIVATE).getInt("img0", 99);
        //Call to Function Method to get current time

        if(dayPassed()){
            moveSaves();
            num_img=1;
        }else{
            num_img=lastImg==99?1:lastImg;
        }

        // set the activity_main.xml
        setContentView(com.jey_oc.moodtracker.R.layout.activity_main);

        // Init the scrollView
        scrv = (ScrollView) findViewById(com.jey_oc.moodtracker.R.id.scrollView);
        // init the Historical button
        hist_btn = (ImageButton)findViewById(com.jey_oc.moodtracker.R.id.historical_btn);
        // init the Comment button
        com_btn = (ImageButton)findViewById(com.jey_oc.moodtracker.R.id.comment_btn);
        // init the Comment button
        share_btn = (ImageButton)findViewById(com.jey_oc.moodtracker.R.id.share_btn);

        // init the 5 images button
        img = new ImageView[]{(ImageView) findViewById(com.jey_oc.moodtracker.R.id.smiley_0),(ImageView) findViewById(com.jey_oc.moodtracker.R.id.smiley_1),(ImageView) findViewById(com.jey_oc.moodtracker.R.id.smiley_2),
                (ImageView) findViewById(com.jey_oc.moodtracker.R.id.smiley_3),(ImageView) findViewById(com.jey_oc.moodtracker.R.id.smiley_4)};
        share_img = new ImageView[]{(ImageView) findViewById(com.jey_oc.moodtracker.R.id.share_0),(ImageView) findViewById(com.jey_oc.moodtracker.R.id.share_1),(ImageView) findViewById(com.jey_oc.moodtracker.R.id.share_2),
                (ImageView) findViewById(com.jey_oc.moodtracker.R.id.share_3),(ImageView) findViewById(com.jey_oc.moodtracker.R.id.share_4)};

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
                SaveImgChange();
                CheckoutButtonAvailability();
            }
        });

        // Setting method to look for the mood changed
        OnScrollMove();
        // setting on click Comment Listener
        OnClickCommentBTN();
        // setting on click Share Listener
        OnClickShareBTN();
        // setting on click Historical listener
        OnClickHistoricalBTN();

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

    private void Scroll(int num){
        scrv.smoothScrollTo(0, y_img[num]);
        PopupMsg("Mood of the day : \n" + mood[num] + " !");
    }

    private void OnClickCommentBTN(){
        com_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Popup_Dialog_Comment();
            }
        });
    }

    private void OnClickShareBTN(){
        share_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendMood();
            }
        });
    }

    private void SendMood(){
        String text = getPreferences(MODE_PRIVATE).getString("comment0","");
        // get back image from the imageview selectionned
        Uri imageUri = getLocalBitmapUri(share_img[num_img]);
        //create intent and put image + comment if there's one
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, text);
        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        shareIntent.setType("image/*");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        //open share intent
        startActivity(Intent.createChooser(shareIntent, "Share mood of the day"));
    }

    private Uri getLocalBitmapUri(ImageView imageView) {
        // Extract Bitmap from ImageView drawable
        Drawable drawable = imageView.getDrawable();
        Bitmap bmp = null;
        if (drawable instanceof BitmapDrawable){
            bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        } else {
            return null;
        }
        // Store image to default external storage directory
        Uri bmpUri = null;
        try {
            // Use methods on Context to access package-specific directories on external storage.
            // This way, you don't need to request external read/write permission.
            File file =  new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".png");
            FileOutputStream out = new FileOutputStream(file);
            // compress bitmap to png
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }

    private void OnClickHistoricalBTN(){
        hist_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dayPassed()){
                    moveSaves();
                    Scroll(1);
                }
                Intent hist_intent = new Intent(MainActivity.this, Historical.class);
                String dat[]=new String[7];
                String com[]=new String[7];
                int mo[]=new int[7];

                for (int i=0;i<7;i++) {
                    int reverse = 7-i;
                    dat[i]=getPreferences(MODE_PRIVATE).getString("date"+(reverse), "");
                    com[i]=getPreferences(MODE_PRIVATE).getString("comment"+(reverse), "");
                    mo[i]=getPreferences(MODE_PRIVATE).getInt("img"+(reverse), 99);
                }

                hist_intent.putExtra("DATES", dat);
                hist_intent.putExtra("COMMENTS",com );
                hist_intent.putExtra("MOODS", mo);
                setResult(RESULT_OK, hist_intent);
                startActivity(hist_intent);
            }
        });
    }

    private void Popup_Dialog_Comment(){
        ContextThemeWrapper ctw = new ContextThemeWrapper( this, R.style.dialogStyle);
        AlertDialog.Builder builder = new AlertDialog.Builder(ctw);
        builder.setTitle("Comment");
        // Set up the input
        input = new EditText(this);
        // Specify the type of input expected
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        // Changing color of input Text
        input.setTextColor(input.getResources().getColor(android.R.color.black));
        // Set the input into builder
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mText = input.getText().toString();
                CheckoutButtonAvailability();
                SaveComment(mText);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                CheckoutButtonAvailability();
            }
        });

        // Create Dialog for force keyboard to show up
        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(input, InputMethodManager.SHOW_IMPLICIT);
            }
        });

        dialog.show();
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

    private void SaveImgChange(){
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("img0", num_img);
        editor.putString("date0",getCurrentDateStr());
        editor.apply();
    }

    private void SaveComment(String comment){
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("comment0",comment);
        editor.apply();
    }

    //to Get the current Date as a string format
    private String getCurrentDateStr(){
        Calendar cal = Calendar.getInstance();
        String newTime = df.format(cal.getTime());
        return newTime;
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
