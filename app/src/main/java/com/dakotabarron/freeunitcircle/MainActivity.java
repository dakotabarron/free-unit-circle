package com.dakotabarron.freeunitcircle;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.ColorDrawable;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private MySurfaceView mySurfaceView;
    private NonSurfaceViewContainer nsvc;
    private ThreadPassData tpd;

    // key to retrieve the previous angle, in positive degrees, from the bundle
    private static final String OLD_ANGLE_KEY = "old_angle";

    public enum AngleSign{
        ALL_POS, SPLIT, ALL_NEG
    }

    public static boolean darkTheme;
    public static AngleSign signSetting;

    public static final String DEBUG_TAG = "main_activity_debug";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*
        make sure default preferences are set the very first time
        the application is opened
         */
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        /*
        then initialize settings values based on stored user preferences
        (or default)
         */
        SharedPreferences sharedPref =
                PreferenceManager.getDefaultSharedPreferences(this);
        darkTheme = sharedPref.getBoolean(
                getString(R.string.pref_dark_theme_key), false);

        String angleSignValue = sharedPref.getString(
                getString(R.string.pref_angle_sign_key),
                getString(R.string.pref_angle_sign_value_all_positive));

        if (angleSignValue.equals(getString(
                R.string.pref_angle_sign_value_all_positive))){
            signSetting = AngleSign.ALL_POS;
        } else if (angleSignValue.equals(getString(
                R.string.pref_angle_sign_value_split))){
            signSetting = AngleSign.SPLIT;
        } else { // must be all negative
            signSetting = AngleSign.ALL_NEG;
        }

        tpd = new ThreadPassData();
        Theta theta = new Theta();

        if (savedInstanceState != null){
            double oldAngleDegrees = savedInstanceState.getDouble(OLD_ANGLE_KEY);

            theta.setAngleInDegrees(oldAngleDegrees);
        } else {
            theta.setAngleInDegrees(45); // default angle if no previous value
        }

        tpd.setThetaToDrawNext(theta);

        setContentView(R.layout.activity_main);

        Toolbar mToolbar = (Toolbar)findViewById(R.id.toolbar_main_activity);
        setSupportActionBar(mToolbar);

        nsvc = new NonSurfaceViewContainer();
        setUpNSVC();

        mySurfaceView = (MySurfaceView)findViewById(R.id.mainSurfaceView);
        mySurfaceView.setNsvcToSendIntoWorkerThread(nsvc);
        mySurfaceView.setThreadPassData(tpd);
        SurfaceHolder sh = mySurfaceView.getHolder();
        sh.setFormat(PixelFormat.RGBA_8888); // render 32 bit color
        sh.addCallback(mySurfaceView);
    }

    @Override
    protected void onStart() {
        super.onStart();

        ActionBar ab = getSupportActionBar();
        int color;

        /*
         set the color of the app bar and its text color
         according to the current theme. do this in onStart() instead of
         onCreate() because you want the color to update when the user comes
         back from preferences activity in case the user changed the theme.
          */

        if (darkTheme){
            ab.setBackgroundDrawable(
                    new ColorDrawable(
                            getResources().getColor(
                                    R.color.colorAppBarDarkTheme)));
            color = getResources().getColor(R.color.colorTextDarkTheme);
        } else {
            ab.setBackgroundDrawable(
                    new ColorDrawable(
                            getResources().getColor(
                                    R.color.colorAppBarLightTheme)));
            color = getResources().getColor(R.color.colorTextLightTheme);
        }

        String htmlColor = String.format(Locale.US, "#%06X",
                (0xFFFFFF & Color.argb(0, Color.red(color),
                        Color.green(color), Color.blue(color))));
        ab.setTitle(Html.fromHtml("<font color=\"" + htmlColor + "\">" +
                getString(R.string.app_name) + "</font>"));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putDouble(OLD_ANGLE_KEY,
                tpd.getLastThetaDrawn().getDegreesPositive());
    }

    private void setUpNSVC(){
        nsvc.setClMainScreenEncompassing(
                (ConstraintLayout) findViewById(R.id.clMainScreenEncompassing));
        nsvc.setvLLEncompassing(
                (LinearLayout)findViewById(R.id.vLLEncompassing));
        nsvc.sethLLRow1((LinearLayout)findViewById(R.id.hLLRow1));
        nsvc.setContainerDegrees(
                (LinearLayout)findViewById(R.id.containerDegrees));
        nsvc.setLabelDegrees((TextView)findViewById(R.id.labelDegrees));
        nsvc.setEntryDegrees((AngleEntry) findViewById(R.id.entryDegrees));

        nsvc.setContainerRadians(
                (LinearLayout)findViewById(R.id.containerRadians));
        nsvc.setLabelRadians((TextView)findViewById(R.id.labelRadians));
        nsvc.setEntryRadians((RadianEntry) findViewById(R.id.entryRadians));
        nsvc.setRadianSpecialLayout((LinearLayout)
                findViewById(R.id.radianSpecialLayout));
        nsvc.setRadianLeftBuffer(findViewById(R.id.radianLeftBuffer));
        nsvc.setRadianDataNegSym((ImageView)findViewById(R.id.radianDataNegSym));
        nsvc.setContainerRadianText((LinearLayout)findViewById(R.id.containerRadianText));
        nsvc.setRadianTextNumerator((TextView)findViewById(R.id.radianTextNumerator));
        nsvc.setRadianDivBar((ImageView)findViewById(R.id.radianDivBar));
        nsvc.setRadianTextDenominator((TextView)findViewById(R.id.radianTextDenominator));
        nsvc.setRadianRightBuffer(findViewById(R.id.radianRightBuffer));

        nsvc.sethLLRow2((LinearLayout)findViewById(R.id.hLLRow2));

        nsvc.setContainerSine((LinearLayout)findViewById(R.id.containerSine));
        nsvc.setLabelSine((TextView)findViewById(R.id.labelSine));
        nsvc.setSineRegularData((TextView)findViewById(R.id.sineRegularData));
        nsvc.setSineSpecialLayout((LinearLayout)
                findViewById(R.id.sineSpecialLayout));
        nsvc.setSineLeftBuffer(findViewById(R.id.sineLeftBuffer));
        nsvc.setSinDataNegSym((ImageView)findViewById(R.id.sinDataNegSym));
        nsvc.setContainerSineText((LinearLayout)findViewById(R.id.containerSineText));
        nsvc.setSineTextNumerator((TextView)findViewById(R.id.sineTextNumerator));
        nsvc.setSineDivBar((ImageView)findViewById(R.id.sineDivBar));
        nsvc.setSineTextDenominator((TextView)findViewById(R.id.sineTextDenominator));
        nsvc.setSineRightBuffer(findViewById(R.id.sineRightBuffer));


        nsvc.setContainerCosine(
                (LinearLayout)findViewById(R.id.containerCosine));
        nsvc.setLabelCosine((TextView)findViewById(R.id.labelCosine));
        nsvc.setCosineRegularData((TextView)findViewById(R.id.cosineRegularData));
        nsvc.setCosineSpecialLayout((LinearLayout)
                findViewById(R.id.cosineSpecialLayout));
        nsvc.setCosineLeftBuffer(findViewById(R.id.cosineLeftBuffer));
        nsvc.setCosDataNegSym((ImageView)findViewById(R.id.cosDataNegSym));
        nsvc.setContainerCosineText((LinearLayout)findViewById(R.id.containerCosineText));
        nsvc.setCosineTextNumerator((TextView)findViewById(R.id.cosineTextNumerator));
        nsvc.setCosineDivBar((ImageView)findViewById(R.id.cosineDivBar));
        nsvc.setCosineTextDenominator((TextView)findViewById(R.id.cosineTextDenominator));
        nsvc.setCosineRightBuffer(findViewById(R.id.cosineRightBuffer));

        nsvc.sethLLRow3((LinearLayout)findViewById(R.id.hLLRow3));

        nsvc.setContainerTangent(
                (LinearLayout)findViewById(R.id.containerTangent));
        nsvc.setLabelTangent((TextView)findViewById(R.id.labelTangent));
        nsvc.setTangentRegularData((TextView)findViewById(R.id.tangentRegularData));
        nsvc.setTangentSpecialLayout((LinearLayout)
                findViewById(R.id.tangentSpecialLayout));
        nsvc.setTangentLeftBuffer(findViewById(R.id.tangentLeftBuffer));
        nsvc.setTanDataNegSym((ImageView)findViewById(R.id.tanDataNegSym));
        nsvc.setContainerTangentText((LinearLayout)findViewById(R.id.containerTangentText));
        nsvc.setTangentTextNumerator((TextView)findViewById(R.id.tangentTextNumerator));
        nsvc.setTangentDivBar((ImageView)findViewById(R.id.tangentDivBar));
        nsvc.setTangentTextDenominator((TextView)findViewById(R.id.tangentTextDenominator));
        nsvc.setTangentRightBuffer(findViewById(R.id.tangentRightBuffer));

        nsvc.setContainerRadianControl(
                (LinearLayout)findViewById(R.id.containerRadianControl));
        nsvc.setRadianControlLeftArrow(
                (ImageView)findViewById(R.id.imageLeftArrow));
        nsvc.setRadianControlRightArrow(
                (ImageView)findViewById(R.id.imageRightArrow));

        nsvc.getEntryRadians().setRadianSpecialLayout(
                nsvc.getRadianSpecialLayout());


        View test = findViewById(R.id.hLLRow4);
        if (test != null){
            Log.d(DEBUG_TAG, "row 4 not null");
            nsvc.sethLLRow4((LinearLayout)test);

            // if row 4 is present, then we assume 5 and 6 are as well

            nsvc.sethLLRow5((LinearLayout)findViewById(R.id.hLLRow5));
            nsvc.sethLLRow6((LinearLayout)findViewById(R.id.hLLRow6));
        } else {
            Log.d(DEBUG_TAG, "row 4 null");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * goes to the next special angle in a clockwise rotation from
     * the current angle
     * @param v the left arrow button clicked on
     */
    public void goToPreviousSpecialAngle(View v){
        // if angle is being typed in by user, do nothing
        if (nsvc.getEntryDegrees().isBeingEdited() ||
                nsvc.getEntryRadians().isBeingEdited())
            return;

        double currentDegrees = tpd.getLastThetaDrawn().getDegreesPositive();

        double previousSpecialAngleDegrees;

        if (currentDegrees > 330 || currentDegrees == 0) {
            previousSpecialAngleDegrees = 330;
        } else {
            previousSpecialAngleDegrees = 0;
            Theta.SpecialAngle greaterOrEqualToCurrent =
                    Theta.SpecialAngle.PI_OVER_6;

            while (((double)greaterOrEqualToCurrent.getDegrees()) <
                    currentDegrees){
                previousSpecialAngleDegrees =
                        greaterOrEqualToCurrent.getDegrees();

                greaterOrEqualToCurrent = Theta.SpecialAngle.values()[
                        greaterOrEqualToCurrent.ordinal() + 1];
            }
        }

        Theta theta = new Theta();
        theta.setAngleInDegrees(previousSpecialAngleDegrees);
        tpd.setThetaToDrawNext(theta);
        mySurfaceView.getWorkerThread().wakeUp();
    }

    /**
     * goes to the next special angle in a counter-clockwise rotation from
     * the current angle
     * @param v the right arrow button clicked on
     */
    public void goToNextSpecialAngle(View v){
        // if angle is being typed in by user, do nothing
        if (nsvc.getEntryDegrees().isBeingEdited() ||
                nsvc.getEntryRadians().isBeingEdited())
            return;

        double currentDegrees = tpd.getLastThetaDrawn().getDegreesPositive();

        double nextSpecialAngleDegrees;

        if (currentDegrees >= 330) {
            nextSpecialAngleDegrees = 0;
        } else {
            Theta.SpecialAngle nextSpecialAngle = Theta.SpecialAngle.PI_OVER_6;

            while (nextSpecialAngle.getDegrees() <= currentDegrees){
                nextSpecialAngle = Theta.SpecialAngle.values()[
                        nextSpecialAngle.ordinal() + 1];
            }

            nextSpecialAngleDegrees = nextSpecialAngle.getDegrees();
        }

        Theta theta = new Theta();
        theta.setAngleInDegrees(nextSpecialAngleDegrees);
        tpd.setThetaToDrawNext(theta);
        mySurfaceView.getWorkerThread().wakeUp();
    }

    /**
     * handles the case when the user clicks on the current radians value
     * when a special value is showing
     * @param v the LinearLayout containing the radian special layout
     */
    public void clickedOnSpecialRadians(View v){
        v.setVisibility(View.GONE);

        nsvc.getEntryRadians().setVisibility(View.VISIBLE);

        // simulate DOWN and UP touch events

        Long startTime = SystemClock.uptimeMillis();

        MotionEvent me = MotionEvent.obtain(startTime,
                startTime,
                MotionEvent.ACTION_DOWN, 0, 0, 0);

        nsvc.getEntryRadians().dispatchTouchEvent(me);

        me = MotionEvent.obtain(startTime,
                SystemClock.uptimeMillis(),
                MotionEvent.ACTION_UP, 0, 0, 0);

        nsvc.getEntryRadians().dispatchTouchEvent(me);

        // make sure the keyboard shows
        InputMethodManager manager =
                (InputMethodManager) getSystemService(
                        Context.INPUT_METHOD_SERVICE);
        manager.showSoftInput(nsvc.getEntryRadians(),
                InputMethodManager.SHOW_IMPLICIT);
    }
}
