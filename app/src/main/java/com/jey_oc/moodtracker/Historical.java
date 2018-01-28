package com.jey_oc.moodtracker;

import android.support.v7.app.AppCompatActivity;
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
}
