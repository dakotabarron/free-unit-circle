package com.dakotabarron.freeunitcircle;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.TextView;

import java.util.Locale;

import static java.lang.Math.*;

/**
 *  Handles drawing the unit circle to the surface from a separate thread
 *  (aka the "redraw thread") via a reference to the {@link SurfaceHolder} from
 *  {@link MySurfaceView#surfaceCreated(SurfaceHolder)}.<br>
 *
 *  Created by dakota on 7/29/17.
 */
public class WorkerThread extends Thread {
    private SurfaceHolder sh;
    private NonSurfaceViewContainer nsvc;
    private Context context;
    private ThreadPassData data;
    private Theta lastDrawnArcTheta;

    /*
    numbers after decimal point in the text data.
     */
    public static final int DATA_PRECISION = 4;

    /*
    format string used when displaying the text data
     */
    public static final String FORMAT_STRING = "%." + DATA_PRECISION + "f";

    /*
    unicode PI
     */
    private static final String PI_SYM = "\u03C0";

    /*
    handle messages on the UI thread. to update the text data
     */
    private Handler mHandler;

    public static final String DEBUG_TAG = "worker_thread";

    public WorkerThread(SurfaceHolder sh, final NonSurfaceViewContainer nsvc,
                        final Context context,
                        ThreadPassData data){
        this.sh = sh;
        this.nsvc = nsvc;
        this.context = context;
        this.data = data;

        // the code in this Handler will run on the UI thread
        mHandler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                Resources r = context.getResources();

                /*
                make all colors and text colors match the current theme
                 */

                for (View v : nsvc.getAllBackgroundColorViews()){
                    if (MainActivity.darkTheme){
                        v.setBackgroundColor(
                                r.getColor(
                                        R.color.colorBackgroundDarkTheme));
                    } else{
                        v.setBackgroundColor(
                                r.getColor(
                                        R.color.colorBackgroundLightTheme));
                    }
                }
                for (View v : nsvc.getAllBorderColorViews()){
                    if (MainActivity.darkTheme){
                        v.setBackgroundColor(
                                r.getColor(R.color.colorBorderDarkTheme));
                    } else {
                        v.setBackgroundColor(
                                r.getColor(R.color.colorBorderLightTheme));
                    }
                }
                for (TextView v : nsvc.getAllViewsToSetTextColor()){
                    if (MainActivity.darkTheme){
                        v.setTextColor(
                                r.getColor(R.color.colorTextDarkTheme));
                    } else {
                        v.setTextColor(
                                r.getColor(R.color.colorTextLightTheme));
                    }
                }

                for (View v :
                        nsvc.getAllSpecialLayoutViewsToChangeBackgroundColor()){
                    if (MainActivity.darkTheme){
                        v.setBackgroundColor(r.getColor(
                                R.color.colorTextDarkTheme));
                    } else {
                        v.setBackgroundColor(r.getColor(
                                R.color.colorTextLightTheme));
                    }
                }

                if (MainActivity.darkTheme){
                    nsvc.getRadianControlLeftArrow().setBackgroundDrawable(
                            r.getDrawable(R.drawable.rad_arrow_left_white));
                    nsvc.getRadianControlRightArrow().setBackgroundDrawable(
                            r.getDrawable(R.drawable.rad_arrow_right_white));

                } else {
                    nsvc.getRadianControlLeftArrow().setBackgroundDrawable(
                            r.getDrawable(R.drawable.rad_arrow_left_black));
                    nsvc.getRadianControlRightArrow().setBackgroundDrawable(
                            r.getDrawable(R.drawable.rad_arrow_right_black));
                }

                Theta theta = (Theta)msg.obj;

                /*
                now handle all other boxes other than the radian control
                 */

                double radiansToShow;
                double degreesToShow;

                /*
                set the degree values appropriate for the current sign
                setting
                 */
                switch(MainActivity.signSetting){
                    case ALL_POS:
                        radiansToShow = theta.getRadiansPositive();
                        degreesToShow = theta.getDegreesPositive();
                        break;
                    case SPLIT:
                        if (theta.getDegreesPositive() > 180){
                            radiansToShow = theta.getRadiansNegative();
                            degreesToShow = theta.getDegreesNegative();
                        } else {
                            radiansToShow = theta.getRadiansPositive();
                            degreesToShow = theta.getDegreesPositive();
                        }
                        break;
                    case ALL_NEG:
                        radiansToShow = theta.getRadiansNegative();
                        degreesToShow = theta.getDegreesNegative();
                        break;
                    default:
                        // should never get here
                        radiansToShow = theta.getRadiansPositive();
                        degreesToShow = theta.getDegreesPositive();
                        break;
                }

                /*
                the single whitespace added before the radians and degrees text
                is used in the application to tell the difference between
                text generated by the user versus the computer. User keyboard
                will not have a space option. must set the currentValidText
                on the object before calling setText() so that onTextChanged()
                will work correctly in the AngleEntry.
                 */
                nsvc.getEntryDegrees().setCurrentValidText(
                        String.format(" " + FORMAT_STRING,
                                degreesToShow));
                nsvc.getEntryDegrees().setText(
                        String.format(" " + FORMAT_STRING,
                                degreesToShow));
                nsvc.getEntryRadians().setCurrentValidText( //TODO does locale matter?
                        String.format(" " + FORMAT_STRING,
                                radiansToShow));
                nsvc.getEntryRadians().setText(
                        String.format(" " + FORMAT_STRING,
                                radiansToShow));

                if (theta.isSpecialAngle()){
                    Theta.SpecialAngle sa = theta.getValueOfSpecialAngle();

                    for (View v : nsvc.getAllRegularDataViews()){
                        v.setVisibility(View.GONE);
                    }
                    for (View v : nsvc.getAllSpecialLayoutViews()){
                        v.setVisibility(View.VISIBLE);
                    }

                    nsvc.getEntryRadians().setWasSpecialLayoutShowing(true);

                    // take care of radians box
                    switch (MainActivity.signSetting){
                        case ALL_POS:
                            nsvc.getRadianDataNegSym().setVisibility(View.GONE);
                            if (sa.getDegrees() == 0)
                                nsvc.getRadianTextNumerator().setText("0");
                            else if (sa.getPositiveNumeratorRadians() == 1)
                                nsvc.getRadianTextNumerator().setText(PI_SYM);
                            else
                                nsvc.getRadianTextNumerator().setText(
                                        String.format(Locale.US, "%d%s",
                                                sa.getPositiveNumeratorRadians(),
                                                PI_SYM));

                            nsvc.getRadianTextDenominator().setText(
                                    String.format(Locale.US, "%d",
                                            sa.getPositiveDenominatorRadians()));
                            break;
                        case SPLIT:
                            if (theta.getDegreesPositive() <= 180){
                                nsvc.getRadianDataNegSym().setVisibility(View.GONE);
                                if (sa.getDegrees() == 0)
                                    nsvc.getRadianTextNumerator().setText("0");
                                else if (sa.getPositiveNumeratorRadians() == 1)
                                    nsvc.getRadianTextNumerator().setText(PI_SYM);
                                else
                                    nsvc.getRadianTextNumerator().setText(
                                            String.format(Locale.US, "%d%s",
                                                    sa.getPositiveNumeratorRadians(),
                                                    PI_SYM));

                                nsvc.getRadianTextDenominator().setText(
                                        String.format(Locale.US, "%d",
                                                sa.getPositiveDenominatorRadians()));
                            } else {
                                nsvc.getRadianDataNegSym().setVisibility(View.VISIBLE);
                                if (sa.getNegativeNumeratorRadians() == 1)
                                    nsvc.getRadianTextNumerator().setText(PI_SYM);
                                else
                                    nsvc.getRadianTextNumerator().setText(
                                            String.format(Locale.US, "%d%s",
                                                    sa.getNegativeNumeratorRadians(),
                                                    PI_SYM));

                                nsvc.getRadianTextDenominator().setText(
                                        String.format(Locale.US, "%d",
                                                sa.getNegativeDenominatorRadians()));
                            }
                            break;
                        case ALL_NEG:
                            nsvc.getRadianDataNegSym().setVisibility(View.VISIBLE);
                            if (sa.getDegrees() == 0) {
                                nsvc.getRadianDataNegSym().setVisibility(View.GONE);
                                nsvc.getRadianTextNumerator().setText("0");
                            }
                            else if (sa.getNegativeNumeratorRadians() == 1)
                                nsvc.getRadianTextNumerator().setText(PI_SYM);
                            else
                                nsvc.getRadianTextNumerator().setText(
                                        String.format(Locale.US, "%d%s",
                                                sa.getNegativeNumeratorRadians(),
                                                PI_SYM));

                            nsvc.getRadianTextDenominator().setText(
                                    String.format(Locale.US, "%d",
                                            sa.getNegativeDenominatorRadians()));
                            break;
                        default:
                            break;
                    }

                    if ((sa.getDegrees() == 0) || (sa.getDegrees() == 180)) {
                        nsvc.getRadianTextNumerator().setGravity(Gravity.START |
                                Gravity.CENTER_VERTICAL);
                        nsvc.getRadianDivBar().setVisibility(View.GONE);
                        nsvc.getRadianTextDenominator().setVisibility(View.GONE);
                    } else {
                        nsvc.getRadianTextNumerator().setGravity(Gravity.CENTER);
                    }

                    if (nsvc.getRadianDivBar().getVisibility() == View.VISIBLE){
                        nsvc.getRadianTextNumerator().setTextSize(
                                TypedValue.COMPLEX_UNIT_PX,
                                r.getDimension(R.dimen.special_text_size));
                    } else {
                        nsvc.getRadianTextNumerator().setTextSize(
                                TypedValue.COMPLEX_UNIT_PX,
                                r.getDimension(R.dimen.labels_text_size));
                    }

                    // take care of SIN box
                    if (sa.getDegrees() > 180) {
                        nsvc.getSinDataNegSym().setVisibility(View.VISIBLE);
                    } else {
                        nsvc.getSinDataNegSym().setVisibility(View.GONE);
                    }

                    nsvc.getSineTextNumerator().setText(sa.getNumeratorSin());
                    nsvc.getSineTextDenominator().setText(sa.getDenominatorSin());

                    if (sa.getDenominatorSin().equals("1")){
                        nsvc.getSineDivBar().setVisibility(View.GONE);
                        nsvc.getSineTextDenominator().setVisibility(View.GONE);
                        nsvc.getSineTextNumerator().setGravity(Gravity.START |
                                Gravity.CENTER_VERTICAL);
                    } else {
                        nsvc.getSineTextNumerator().setGravity(Gravity.CENTER);
                    }

                    if (nsvc.getSineDivBar().getVisibility() == View.VISIBLE){
                        nsvc.getSineTextNumerator().setTextSize(
                                TypedValue.COMPLEX_UNIT_PX,
                                r.getDimension(R.dimen.special_text_size));
                    } else {
                        nsvc.getSineTextNumerator().setTextSize(
                                TypedValue.COMPLEX_UNIT_PX,
                                r.getDimension(R.dimen.labels_text_size));
                    }

                    // take care of COS box
                    if ((sa.getDegrees() > 90) && (sa.getDegrees() < 270)){
                        nsvc.getCosDataNegSym().setVisibility(View.VISIBLE);
                    } else {
                        nsvc.getCosDataNegSym().setVisibility(View.GONE);
                    }

                    nsvc.getCosineTextNumerator().setText(sa.getNumeratorCos());
                    nsvc.getCosineTextDenominator().setText(sa.getDenominatorCos());

                    if (sa.getDenominatorCos().equals("1")){
                        nsvc.getCosineDivBar().setVisibility(View.GONE);
                        nsvc.getCosineTextDenominator().setVisibility(View.GONE);
                        nsvc.getCosineTextNumerator().setGravity(Gravity.START |
                                Gravity.CENTER_VERTICAL);
                    } else {
                        nsvc.getCosineTextNumerator().setGravity(Gravity.CENTER);
                    }

                    if (nsvc.getCosineDivBar().getVisibility() == View.VISIBLE){
                        nsvc.getCosineTextNumerator().setTextSize(
                                TypedValue.COMPLEX_UNIT_PX,
                                r.getDimension(R.dimen.special_text_size));
                    } else {
                        nsvc.getCosineTextNumerator().setTextSize(
                                TypedValue.COMPLEX_UNIT_PX,
                                r.getDimension(R.dimen.labels_text_size));
                    }

                    // take care of TAN box
                    if (sa.getDenominatorTan().equals("0")){
                        nsvc.getTanDataNegSym().setVisibility(View.GONE);
                        nsvc.getTangentDivBar().setVisibility(View.GONE);
                        nsvc.getTangentTextDenominator().setVisibility(View.GONE);
                        nsvc.getTangentTextNumerator().setText("N/A");
                    } else {
                        if (nsvc.getCosDataNegSym().getVisibility() !=
                                nsvc.getSinDataNegSym().getVisibility()){
                            // tangent is negative
                            nsvc.getTanDataNegSym().setVisibility(View.VISIBLE);

                            if (sa.getNumeratorTan().equals("0")){
                                nsvc.getTanDataNegSym().setVisibility(View.GONE);
                            }
                        } else {
                            nsvc.getTanDataNegSym().setVisibility(View.GONE);
                        }

                        nsvc.getTangentTextNumerator().setText(sa.getNumeratorTan());
                        nsvc.getTangentTextDenominator().setText(sa.getDenominatorTan());

                        if (sa.getDenominatorTan().equals("1")){
                            nsvc.getTangentDivBar().setVisibility(View.GONE);
                            nsvc.getTangentTextDenominator().setVisibility(View.GONE);
                            nsvc.getTangentTextNumerator().setGravity(
                                    Gravity.START | Gravity.CENTER_VERTICAL);
                        } else {
                            nsvc.getTangentTextNumerator().setGravity(Gravity.CENTER);
                        }
                    }

                    if (nsvc.getTangentDivBar().getVisibility() == View.VISIBLE){
                        nsvc.getTangentTextNumerator().setTextSize(
                                TypedValue.COMPLEX_UNIT_PX,
                                r.getDimension(R.dimen.special_text_size));
                    } else {
                        nsvc.getTangentTextNumerator().setTextSize(
                                TypedValue.COMPLEX_UNIT_PX,
                                r.getDimension(R.dimen.labels_text_size));
                    }
                } else {
                    for (View v : nsvc.getAllRegularDataViews()){
                        v.setVisibility(View.VISIBLE);
                    }
                    for (View v : nsvc.getAllSpecialLayoutViews()){
                        v.setVisibility(View.GONE);
                    }

                    nsvc.getEntryRadians().setWasSpecialLayoutShowing(false);

                    nsvc.getSineRegularData().setText(String.format(
                            FORMAT_STRING, sin(theta.getRadiansPositive())));
                    nsvc.getCosineRegularData().setText(String.format(
                            FORMAT_STRING, cos(theta.getRadiansPositive())));
                    if (!theta.isTangentDefined()){
                        nsvc.getTangentRegularData().setText("N/A");
                    } else {
                        nsvc.getTangentRegularData().setText(
                                String.format(FORMAT_STRING,
                                        tan(theta.getRadiansPositive())));
                    }
                }
            }
        };
    }

    /**
     * this method will be called once every time the surface which contains the
     * unit circle is created via
     * {@link MySurfaceView#surfaceCreated(SurfaceHolder)}. Once created,
     * the redraw thread will then immediately {@link #wait()} until woken up
     * (for example by
     * {@link MySurfaceView#surfaceChanged(SurfaceHolder, int, int, int)}) or
     * interrupted (possibly by
     * {@link MySurfaceView#surfaceDestroyed(SurfaceHolder)}).<br><br>
     * Once woken up, the redraw thread will redraw the entire surface, and then
     * immediately wait again (until another thread wakes it up again so that
     * it can redraw). this is synchronized so that other threads can only
     * call notifyAll() on this object via {@link #wakeUp()} when it is certain
     * that the redraw thread has released the monitor on this object via
     * {@link #wait()}
     */
    @Override
    public synchronized void run() {
        super.run();

        while(true){
            try{
                Log.d(MySurfaceView.DEBUG_TAG, "redraw Thread will now wait");
                wait();
            } catch (InterruptedException ex){
                Log.d(MySurfaceView.DEBUG_TAG, "interrupted");

                // just return when interrupted
                return;
            }

            Log.d(MySurfaceView.DEBUG_TAG, "redraw");
            Theta theta = redraw();

            // send data to UI thread to update the text info
            Message message = mHandler.obtainMessage();
            message.obj = theta;
            message.sendToTarget();
        }
    }

    /**
     * redraws the entire SurfaceView using a Canvas
     * @return the Theta that will eventually get sent to the
     * UI handler to update the text data
     */
    private Theta redraw(){
        Canvas c = sh.lockCanvas();
        // TODO c could be null from above

        Log.d(MySurfaceView.DEBUG_TAG, "canvas width: " + c.getWidth() +
                ", canvas height: " + c.getHeight());

        blankWholeCanvas(c);

        int cHeight = c.getHeight();
        int cWidth = c.getWidth();

        float centerX = cWidth / 2f;
        float centerY = cHeight / 2f;

        Log.d(MySurfaceView.DEBUG_TAG, "origin: (" + centerX + ", " + centerY + ")");

        /*
        radius of the circle.
         */
        float r;

        // get stroke widths defined in dimens.xml (adjust them for device-specific density)
        Resources res = context.getResources();
        DisplayMetrics metrics = res.getDisplayMetrics();
        TypedValue outValue = new TypedValue();
        res.getValue(R.dimen.circle_stroke_width, outValue, true);
        float circleStrokeWidth = outValue.getFloat() * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        res.getValue(R.dimen.axes_stroke_width, outValue, true);
        float axesStrokeWidth = outValue.getFloat() * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        res.getValue(R.dimen.arc_stroke_width, outValue, true);
        float arcStrokeWidth = outValue.getFloat() * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);

        /*
        whichever dimension is smaller determines the radius.
        subtract some space to allow for the stroke width.
         */
        if (cWidth <= cHeight)
            r = (cWidth / 2f) - circleStrokeWidth;
        else
            r = (cHeight / 2f) - circleStrokeWidth;

        this.drawCircle(c, centerX, centerY, r, circleStrokeWidth);
        this.drawAxes(c, centerX, centerY, r, axesStrokeWidth);
        Theta theta = this.drawArc(c, centerX, centerY, r,
                arcStrokeWidth);

        sh.unlockCanvasAndPost(c);

        data.setLastThetaDrawn(theta);
        return theta;
    }

    /**
     * draws the unit circle in the center of the Canvas
     * @param c the Canvas on which to draw
     * @param centerX the x coordinate of the center of the Canvas
     * @param centerY the y coordinate of the center of the Canvas
     * @param radius the radius of the circle in device-specific units
     * @param strokeWidth width of the line that makes the circle
     */
    private void drawCircle(Canvas c, float centerX, float centerY,
                            float radius, float strokeWidth){
        Paint p = new Paint();
        p.setStyle(Paint.Style.STROKE);
        Resources res = context.getResources();
        p.setStrokeWidth(strokeWidth);
        if (MainActivity.darkTheme){
            p.setColor(res.getColor(R.color.colorBorderDarkTheme));
        } else {
            p.setColor(res.getColor(R.color.colorBorderLightTheme));
        }

        c.drawCircle(centerX, centerY, radius, p);
    }

    /**
     * draws the x and y axes that divide the unit circle
     * @param c c the Canvas on which to draw
     * @param centerX the x coordinate of the center of the Canvas
     * @param centerY the y coordinate of the center of the Canvas
     * @param radius the radius of the unit circle in device-specific units
     * @param strokeWidth width of the each axis line
     */
    private void drawAxes(Canvas c, float centerX, float centerY,
                          float radius, float strokeWidth){
        Resources res = context.getResources();

        Paint p = new Paint();
        p.setStrokeWidth(strokeWidth);
        if (MainActivity.darkTheme){
            p.setColor(res.getColor(R.color.colorBorderDarkTheme));
        } else {
            p.setColor(res.getColor(R.color.colorBorderLightTheme));
        }

        c.drawLine(centerX, centerY, centerX, centerY + radius, p);
        c.drawLine(centerX, centerY, centerX, centerY - radius, p);
        c.drawLine(centerX, centerY, centerX + radius, centerY, p);
        c.drawLine(centerX, centerY, centerX - radius, centerY, p);
    }

    /**
     * determines whether or not a specific point is the origin of the
     * unit circle. There is a small tolerance around the origin which
     * is considered.
     * @param p the Point in question
     * @param cx the x coordinate of the origin
     * @param cy the y coordinate of the origin
     * @return true if the PointF p is the same as the origin<br>
     *     false otherwise
     */
    private boolean isPointOnOrigin(PointF p, float cx, float cy){
        /*
        if they are very close to the origin, then still count that as
        being on the origin
         */
        final int tolerance = 5;

        if ((abs(p.x - cx) <= tolerance) && (abs(p.y - cy) <= tolerance))
            return true;
        return false;
    }

    /**
     * calculates the angle that should be shown based on a point which the user
     * has touched on screen and puts the resulting angle into a Theta object
     * @param point the point where the user touched on screen
     * @param centerX the x coordinate of the center of the unit circle
     * @param centerY the y coordinate of the center of the unit circle
     * @return the Theta that will eventually get sent to the
     * UI handler to update the text data
     */
    private Theta convertTouchLocationToTheta(PointF point, float centerX,
                                           float centerY){
        /*
        if user clicks right on origin, just draw the same thing as last time.
        it will appear as if nothing happens
         */
        if (isPointOnOrigin(point, centerX, centerY))
            return lastDrawnArcTheta;

        Theta theta = new Theta();

        /*
        test for special cases
         */

        // clicked on top axis
        if ((point.x == centerX) && (point.y < centerY)){
            theta.setAngleInDegrees(90);
            return theta;
        }

        // clicked on bottom axis
        if ((point.x == centerX) && (point.y > centerY)){
            theta.setAngleInDegrees(270);
            return theta;
        }

        // clicked on left axis
        if ((point.y == centerY) && (point.x < centerX)) {
            theta.setAngleInDegrees(180);
            return theta;
        }

        // clicked on right axis
        if ((point.y == centerY) && (point.x > centerX)) {
            theta.setAngleInDegrees(0);
            return theta;
        }

        /*
        if we get here we know the user did not click on one of the four
        axes
         */

        float o = getOpposite(point, centerY);
        float a = getAdjacent(point, centerX);
        double h = getHypotenuse(o, a);

        double sine = o / h;
        double angle; // will be in degrees

        /*
        set angle in degrees depending on the quadrant touched in
         */

        if ((point.y < centerY) && (point.x > centerX)){            // Q1
            angle = toDegrees(asin(sine));
        } else if ((point.y < centerY) && (point.x < centerX)){     // Q2
            angle = toDegrees(PI - asin(sine));
        } else if ((point.y > centerY) && (point.x < centerX)) {    // Q3
            angle = toDegrees(PI + asin(sine));
        } else {                                                    // Q4
            angle = toDegrees(-asin(sine));
        }

        theta.setAngleInDegrees(angle);

        return theta;
    }

    /**
     * draws the arc in the unit circle representing a particular
     * angle
     * @param c the Canvas to draw upon
     * @param centerX the x coordinate of the center of the unit circle
     * @param centerY the y coordinate of the center of the unit circle
     * @param radius the radius length of the unit circle
     * @param strokeWidth the stroke width of the arc
     * @return the Theta that will eventually get sent to the
     * UI handler to update the text data
     */
    private Theta drawArc(Canvas c, float centerX, float centerY,
                          float radius, float strokeWidth){
        Theta theta = data.getThetaToDrawNext();

        if (theta == null){ // we must convert the touch location to a Theta
            theta = convertTouchLocationToTheta(data.getTouchLocation(),
                    centerX, centerY);
        }

        Paint p = new Paint();
        p.setStrokeWidth(strokeWidth);
        Resources res = context.getResources();

        Path path = new Path();

        /*
        the RectF which will define the shape and size of the arc. This is
        defined to be a square bounding the unit circle.
         */
        RectF circleRect = new RectF(centerX - radius, centerY - radius,
                centerX + radius, centerY + radius);

        // start at origin
        path.moveTo(centerX, centerY);

        // always draw starting line
        path.lineTo(centerX + radius, centerY);

        switch (MainActivity.signSetting){
            case ALL_POS:
                path.arcTo(circleRect, 0, (float)-theta.getDegreesPositive());
                break;
            case SPLIT:
                if (theta.getDegreesPositive() <= 180){
                    path.arcTo(circleRect, 0, (float)-theta.getDegreesPositive());
                } else {
                    path.arcTo(circleRect, 0, (float)-theta.getDegreesNegative());
                }
                break;
            case ALL_NEG:
                path.arcTo(circleRect, 0, (float)-theta.getDegreesNegative());
                break;
            default:
                // should never get here
                break;
        }

        path.lineTo(centerX, centerY);
        // new stuff end

        /*
        in order to get different outline and fill colors, we must draw
        the path twice. the first time we fill it and the second time
        we outline it (aka stroke)
         */
        p.setStyle(Paint.Style.FILL);

        if (MainActivity.darkTheme){
            p.setColor(res.getColor(R.color.colorArcInsideDarkTheme));
        } else {
            p.setColor(res.getColor(R.color.colorArcInsideLightTheme));
        }
        c.drawPath(path, p);

        p.setStyle(Paint.Style.STROKE);
        if (MainActivity.darkTheme){
            p.setColor(res.getColor(R.color.colorArcOutlineDarkTheme));
        } else {
            p.setColor(res.getColor(R.color.colorArcOutlineLightTheme));
        }
        c.drawPath(path, p);

        lastDrawnArcTheta = theta;
        return theta;
    }

    /**
     *
     * @param pf represents the point the user touched on screen
     * @param centerY y coordinate of the origin
     * @return length of the side opposite from theta in the triangle formed
     * by the origin, argument, and the other point on the x-axis with the
     * same x-coordinate as the argument. Will always return a positive float
     */
    private float getOpposite(PointF pf, float centerY){
        return abs(pf.y - centerY);
    }

    /**
     *
     * @param pf represents the point the user touched on screen
     * @param centerX x coordinate of the origin
     * @return length of the side adjacent from theta in the triangle formed
     * by the origin, argument, and the other point on the x-axis with the
     * same x-coordinate as the argument. Will always return a positive float
     */
    private float getAdjacent(PointF pf, float centerX){
        return abs(pf.x - centerX);
    }

    /**
     * returns the hypotenuse length of a right triangle as calculated by the
     * pythagorean theorem
     * @param opposite length of the side opposite of theta
     * @param adjacent length of the side adjacent to theta
     * @return length of the hypotenuse of the right triangle with specified
     * opposite and adjacent side lengths
     */
    private double getHypotenuse(float opposite, float adjacent){
        return sqrt(pow(opposite, 2) + pow(adjacent, 2));
    }

    /**
     * erases the whole canvas by making it all one solid color. this gets
     * called before every redraw to prevent artifacts because the Canvas
     * retains its previous state.
     * @param c the Canvas to blank
     */
    private void blankWholeCanvas(Canvas c){
        Resources r = context.getResources();
        if (MainActivity.darkTheme){
            c.drawColor(r.getColor(R.color.colorBackgroundDarkTheme));
        } else {
            c.drawColor(r.getColor(R.color.colorBackgroundLightTheme));
        }
    }

    /**
     * called by other threads to tell the redraw thread to redraw the surface.
     * Is synchronized to make sure that the UI thread is the owner of this
     * object's monitor before calling notifyAll() (as specified in the
     * documentation for {@link #notifyAll()} and also to ensure that the
     * redraw thread is actually in {@link #wait()} and therefore will be
     * woken up
     */
    public synchronized void wakeUp(){
        notifyAll();
    }
}
