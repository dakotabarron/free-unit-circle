package com.dakotabarron.freeunitcircle;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class InfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

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

        LinearLayout vc = (LinearLayout)findViewById(R.id.viewContainer);

        for (int j = 0; j < vc.getChildCount(); ++j){
            if (vc.getChildAt(j) instanceof Button){
                Button button = (Button)vc.getChildAt(j);
                if (MainActivity.darkTheme){
                    button.setBackgroundDrawable(getResources().getDrawable(R.drawable.info_button_background_dark_theme));
                    button.setTextColor(getResources().getColor(R.color.colorButtonTextDarkTheme));
                } else {
                    button.setBackgroundDrawable(getResources().getDrawable(R.drawable.info_button_background_light_theme));
                    button.setTextColor(getResources().getColor(R.color.colorButtonTextLightTheme));
                }
            }
            else {
                if (MainActivity.darkTheme){
                    ((TextView)(vc.getChildAt(j))).setTextColor(getResources().getColor(R.color.colorTextDarkTheme));
                } else {
                    ((TextView)(vc.getChildAt(j))).setTextColor(getResources().getColor(R.color.colorTextLightTheme));
                }
            }
        }
    }

    public void viewLicense(View v){
        Intent intent = new Intent(this, LicenseActivity.class);
        startActivity(intent);
    }

    public void goToDeveloperWebsite(View v){
        Uri webpage = Uri.parse(getResources().getString(R.string.developer_website_link));
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public void viewSourceCode(View v){
        Uri webpage = Uri.parse(getResources().getString(R.string.source_code_link));
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public void viewThirdPartyLicenses(View v){
        Intent intent = new Intent(this, ThirdPartyLicensesActivity.class);
        startActivity(intent);
    }
}
