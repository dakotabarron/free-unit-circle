package com.dakotabarron.freeunitcircle;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class LicenseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_license);

        // set colors of everything in accordance with the current theme

        LinearLayout topTitle = (LinearLayout)findViewById(R.id.topTitle);
        if (MainActivity.darkTheme){
            topTitle.setBackgroundColor(getResources().getColor(R.color.colorAppBarDarkTheme));
        } else {
            topTitle.setBackgroundColor(getResources().getColor(R.color.colorAppBarLightTheme));
        }

        for (int i = 0; i < topTitle.getChildCount(); ++i){
            TextView tv = (TextView)topTitle.getChildAt(i);
            if (MainActivity.darkTheme){
                tv.setTextColor(getResources().getColor(R.color.colorTextDarkTheme));
            } else {
                tv.setTextColor(getResources().getColor(R.color.colorTextLightTheme));
            }
        }

        ScrollView sv = (ScrollView)findViewById(R.id.scrollView);
        if (MainActivity.darkTheme){
            sv.setBackgroundColor(getResources().getColor(R.color.colorBackgroundDarkTheme));
        } else {
            sv.setBackgroundColor(getResources().getColor(R.color.colorBackgroundLightTheme));
        }

        LinearLayout tvc = (LinearLayout)findViewById(R.id.textViewContainer);

        for (int j = 0; j < tvc.getChildCount(); ++j){
            if (MainActivity.darkTheme){
                ((TextView)(tvc.getChildAt(j))).setTextColor(getResources().getColor(R.color.colorTextDarkTheme));
            } else {
                ((TextView)(tvc.getChildAt(j))).setTextColor(getResources().getColor(R.color.colorTextLightTheme));
            }
        }
    }
}
