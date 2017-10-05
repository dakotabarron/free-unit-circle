package com.dakotabarron.freeunitcircle;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.inputmethod.EditorInfo;

/**
 * superclass of the EditTexts which the user will use to enter
 * radian and degree angle values. Had to subclass EditText itself to
 * perform custom input validation.
 * Created by dakota on 8/3/17.
 */
public abstract class AngleEntry extends AppCompatEditText {

    /*
    this will always begin with a space -- ' ' to distinguish
    between computer-generated and user-entered text. Users cannot enter
    spaces on the numeric software keyboard for angle entry.
     */
    private String currentValidText;

    // maximum characters that the user can enter
    private static final int MAX_CHARS_ENTER = 11;

    private WorkerThread workerThread;
    protected ThreadPassData data;

    private static final String DEBUG_TAG = "angle_entry_debug";

    public AngleEntry(Context context){
        super(context);
        setUpHelper(context);
    }

    public AngleEntry(Context context, AttributeSet attrs){
        super(context, attrs);
        setUpHelper(context);
    }

    public void setWorkerThread(WorkerThread wt){
        this.workerThread = wt;
    }

    public void setThreadPassData(ThreadPassData data){
        this.data = data;
    }

    /**
     * utility method containing code common to all constructors.
     * @param context the Context passed into the constructor
     */
    private void setUpHelper(Context context){
        // make it so cut/paste menu cannot pop up
        setCustomSelectionActionModeCallback(new ActionMode.Callback() {
            @Override
            public boolean onCreateActionMode(ActionMode actionMode,
                                              Menu menu) {
                return false;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode actionMode,
                                               Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode actionMode,
                                               MenuItem menuItem) {
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode actionMode) {

            }
        });

        // these 2 below also required to disable copy/paste menu
        setLongClickable(false);
        setCursorVisible(false);
    }

    /**
     * subclasses will override to indicate the maximum value the user can
     * enter in that respective category. (Ex: degrees should be < 360).
     * @return maximum angle value that the user can enter
     */
    public abstract double getMaxAcceptableValue();

    /**
     * subclasses will override to indicate the minimum value the user can
     * enter in that respective category. (Ex: degrees should be > -360).
     * @return minimum angle value that the user can enter
     */
    public abstract double getMinAcceptableValue();

    /**
     * subclasses will override this to set the angle value of a new
     * {@link Theta} object to the angle which the user entered
     * (either in degrees or radians) and then use that newly created
     * Theta object to set the Theta on the ThreadPassData.
     * @param valueEntered the angle value the user entered
     */
    public abstract void setThetaToEnteredValue(double valueEntered);

    /**
     * sets the text which is used to distinguish between human-generated and
     * computer-generated text. this should always be called in the UI thread
     * message handler in the WorkerThread class
     * @param text the new currently valid text
     */
    public void setCurrentValidText(String text){
        currentValidText = text;
    }

    /**
     *
     * @return true if the user is currently editing this EditText<br>
     *     false otherwise
     */
    public boolean isBeingEdited(){
        /*
        if the text currently in the TextView does not match the
        currentValidText then we know the user is editing. See note beside
        the declaration of currentValidText.
         */
        if (!(getText().toString().equals(currentValidText)))
            return true;
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        /*
        run the default implementation so that the keyboard will
        come up properly. cache the result so we can return it later.
         */
        boolean base = super.onTouchEvent(event);

        switch (event.getActionMasked()){
            case MotionEvent.ACTION_DOWN:
                if (getText().toString().equals(currentValidText)){
                    /*
                    if the valid text is currently showing when the user
                    touches down, then erase it
                     */
                    setText("");
                }
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_UP:
                /*
                always make sure the cursor is at the end of the text
                 */
                setSelection(getText().length());
            default:
                return base;
        }
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore,
                                 int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);

        /*
        this stops the user from entering any more digits once they have
        reached the limit. they can always backspace if they want.
         */
        if (isBeingEdited()){
            if (text.length() > MAX_CHARS_ENTER){
                setText(text.subSequence(0, start));
                setSelection(getText().length());
            }
        }
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction,
                                  Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);

        /*
        make sure that if the user clicks off of the TextView while they are
        editing that the currentValidText gets restored
         */
        if (!focused){
            setText(currentValidText);

            if (this instanceof RadianEntry){
                RadianEntry re = (RadianEntry)this;
                re.handleWasSpecialLayoutShowing();
            }
        }
    }

    @Override
    public void onEditorAction(int actionCode) {
        super.onEditorAction(actionCode);

        /*
        looking for when the user presses DONE on the keyboard
         */
        switch (actionCode){
            case EditorInfo.IME_ACTION_DONE:
                /*
                test that the user entered a valid double
                 */
                Double userEntry = null;
                try {
                    userEntry = Double.parseDouble(getText().toString());
                } catch (Exception ex){
                    Log.d(DEBUG_TAG, ex.getMessage());
                    // problem parsing user input, just restore currentValidText
                    setText(currentValidText);

                    if (this instanceof RadianEntry){
                        RadianEntry re = (RadianEntry)this;
                        re.handleWasSpecialLayoutShowing();
                    }

                    return;
                }

                // we get here, we know user entered a valid double

                /*
                make sure input is rounded to the same number it will show
                up as when WorkerThread updates the text data
                 */
                userEntry = Double.parseDouble(
                        String.format(WorkerThread.FORMAT_STRING, userEntry));
                if (userEntry >= getMinAcceptableValue() &&
                        userEntry <= getMaxAcceptableValue()){
                    /*
                    input is valid, send it on.
                     */
                    setThetaToEnteredValue(userEntry);

                    workerThread.wakeUp();
                } else {
                    /*
                    if entry is not within acceptable bounds,
                    just restore currentValidText
                     */
                    setText(currentValidText);

                    if (this instanceof RadianEntry){
                        RadianEntry re = (RadianEntry)this;
                        re.handleWasSpecialLayoutShowing();
                    }
                }
                return;
            default:
                return;
        }
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        /*
        if user hits back button while editing, then just restore
        the currentValidText
         */
        if (keyCode == KeyEvent.KEYCODE_BACK &&
                event.getAction() == KeyEvent.ACTION_UP){
            Log.d(MainActivity.DEBUG_TAG, "BACK");
            setText(currentValidText);

            if (this instanceof RadianEntry){
                RadianEntry re = (RadianEntry)this;
                re.handleWasSpecialLayoutShowing();
            }

            return false;
        }
        return super.onKeyPreIme(keyCode, event);
    }
}
