package com.dakotabarron.freeunitcircle;

import android.graphics.PointF;

/**
 * Thread safe container for transferring data between the UI and redraw
 * threads. Extends PointF to facilitate the transfer of coordinates in the
 * case of touch event data.
 * Created by dakota on 8/2/17.
 */
public class ThreadPassData extends PointF {
    private Theta thetaToDrawNext;
    private Theta lastThetaDrawn;

    /**
     * sets the data indicating where the user touched on the screen
     * @param x x-coordinate of where the user touched on the screen
     * @param y y-coordinate of where the user touched on the screen
     */
    public synchronized void setTouchLocation(float x, float y){
        super.set(x, y);
    }

    /**
     * gets the data indicating where the user touched on the screen
     * @return point where the user touched on the screen
     */
    public synchronized PointF getTouchLocation(){
        return new PointF(super.x, super.y);
    }

    /**
     * sets the Theta containing the angle value that should be shown on
     * the unit circle
     * @param t the Theta containing the angle value that should be shown on
     * the unit circle
     */
    public synchronized void setThetaToDrawNext(Theta t){
        if (t != null){
            /*
            instantiate new object because Theta methods are not
            synchronized. this way when one thread accesses the Theta
            stored in this object it cannot interfere with the original
            Theta object passed in by the other thread.
             */
            thetaToDrawNext = new Theta(t);
        } else {
            thetaToDrawNext = null;
        }
    }

    /**
     * gets the Theta containing the angle value that should be shown on
     * the unit circle
     * @return the Theta containing the angle value that should be shown on
     * the unit circle
     */
    public synchronized Theta getThetaToDrawNext(){
        return thetaToDrawNext;
    }

    /**
     *
     * @return the Theta containing the most recent angle drawn on the
     * unit circle
     */
    public synchronized Theta getLastThetaDrawn() {
        return lastThetaDrawn;
    }

    /**
     * @param lastThetaDrawn the Theta containing the most recent angle
     *                       drawn on the unit circle
     */
    public synchronized void setLastThetaDrawn(Theta lastThetaDrawn) {
        /*
        instantiate new object because Theta methods are not
        synchronized. this way when one thread accesses the Theta
        stored in this object it cannot interfere with the original
        Theta object passed in by the other thread.
         */
        this.lastThetaDrawn = new Theta(lastThetaDrawn);
    }
}
