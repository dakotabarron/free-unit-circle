package com.dakotabarron.freeunitcircle;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import static java.lang.Math.PI;

/**
 * EditText where the user will type in angle values in radians
 * Created by dakota on 8/3/17.
 */
public class RadianEntry extends AngleEntry {
    public RadianEntry(Context c){
        super(c);
    }

    public RadianEntry(Context c, AttributeSet a){
        super(c, a);
    }

    private LinearLayout radianSpecialLayout;
    private boolean wasSpecialLayoutShowing = false;

    public void setRadianSpecialLayout(LinearLayout radianSpecialLayout){
        this.radianSpecialLayout = radianSpecialLayout;
    }

    public void setWasSpecialLayoutShowing(boolean wasSpecialLayoutShowing){
        this.wasSpecialLayoutShowing = wasSpecialLayoutShowing;
    }

    /**
     * handles the case in which the special radian layout was previously
     * showing and now needs to be shown again
     */
    void handleWasSpecialLayoutShowing(){
        if (wasSpecialLayoutShowing){
            this.setVisibility(GONE);
            radianSpecialLayout.setVisibility(VISIBLE);
        }
    }

    @Override
    public double getMaxAcceptableValue() {
        return 2 * PI;
    }

    @Override
    public double getMinAcceptableValue() {
        return -2 * PI;
    }

    @Override
    public void setThetaToEnteredValue(double valueEntered) {
        Theta theta = new Theta();
        theta.setAngleInRadians(valueEntered);
        data.setThetaToDrawNext(theta);
    }
}
