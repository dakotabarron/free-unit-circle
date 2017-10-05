package com.dakotabarron.freeunitcircle;

import android.content.Context;
import android.util.AttributeSet;

/**
 * EditText where the user will type in angle values in degrees
 * Created by dakota on 8/3/17.
 */
public class DegreeEntry extends AngleEntry {
    public DegreeEntry(Context c){
        super(c);
    }

    public DegreeEntry(Context c, AttributeSet a){
        super(c, a);
    }

    @Override
    public double getMaxAcceptableValue() {
        return 360;
    }

    @Override
    public double getMinAcceptableValue() {
        return -360;
    }

    @Override
    public void setThetaToEnteredValue(double valueEntered) {
        Theta theta = new Theta();
        theta.setAngleInDegrees(valueEntered);
        data.setThetaToDrawNext(theta);
    }
}
