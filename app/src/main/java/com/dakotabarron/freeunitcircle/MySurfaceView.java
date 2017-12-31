package com.dakotabarron.freeunitcircle;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.SurfaceHolder;

/**
 * The SurfaceView which the user will touch and interact with and on which
 * the unit circle will be drawn.
 * Created by dakota on 7/27/17.
 */
class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    public static final String DEBUG_TAG = "mysurfaceview_debug";

    private WorkerThread workerThread;
    private ThreadPassData data;

    /* only purpose is to send into WorkerThread constructor
    when a new surface is created;
     */
    private NonSurfaceViewContainer nsvcToSendIntoWorkerThread = null;

    public MySurfaceView(Context context){
        super(context);
    }

    public MySurfaceView(Context context, AttributeSet attrs){
        super(context, attrs);
    }

    public void setNsvcToSendIntoWorkerThread(NonSurfaceViewContainer nsvc){
        nsvcToSendIntoWorkerThread = nsvc;
    }

    public void setThreadPassData(ThreadPassData data){
        this.data = data;
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        workerThread = new WorkerThread(surfaceHolder,
                nsvcToSendIntoWorkerThread, getContext(), data);

        nsvcToSendIntoWorkerThread.getEntryDegrees().setThreadPassData(data);
        nsvcToSendIntoWorkerThread.getEntryDegrees().setWorkerThread(
                workerThread);
        nsvcToSendIntoWorkerThread.getEntryRadians().setThreadPassData(data);
        nsvcToSendIntoWorkerThread.getEntryRadians().setWorkerThread(
                workerThread);

        // start the redraw thread (thread responsible for drawing the surface)
        workerThread.start();

        /*
         make sure redraw thread is waiting before surfaceChanged()
         gets called automatically after this method returns. If we do not
         ensure that the redraw thread is waiting, then surfaceChanged() will
         call WorkerThread.wakeUp() before the redraw thread is waiting. Then
         when the redraw thread finally waits it will just keep waiting and not
         get woken up the very first time to draw initially like it is supposed
         to
          */
        while (workerThread.getState() != Thread.State.WAITING);
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder,
                               int i, int i1, int i2) {
        // wake up the redraw thread so it will redraw the surface
        workerThread.wakeUp();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        workerThread.interrupt();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()){
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_UP:

                /*
                if user is editing angle values, do not respond to touches
                on the unit circle
                 */
                if (nsvcToSendIntoWorkerThread.getEntryDegrees().isBeingEdited()
                        || nsvcToSendIntoWorkerThread.getEntryRadians()
                        .isBeingEdited()){
                    return true;
                }

                /*
                want redraw thread to read touch location, not a Theta
                 */
                data.setThetaToDrawNext(null);

                data.setTouchLocation(event.getX(), event.getY());

                workerThread.wakeUp();
                return true;
            default:
                return super.onTouchEvent(event);
        }
    }

    public WorkerThread getWorkerThread() {
        return workerThread;
    }
}
