package com.dakotabarron.freeunitcircle;

import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains all the Views in the main Activity layout except for the
 * SurfaceView. Makes it easier to access and manipulate each View.<br>
 * Created by dakota on 8/1/17.
 */
public class NonSurfaceViewContainer {
    private ConstraintLayout clMainScreenEncompassing;
    private LinearLayout vLLEncompassing;
    private LinearLayout hLLRow1;

    private LinearLayout containerDegrees;
    private TextView labelDegrees;
    private AngleEntry entryDegrees;

    private LinearLayout containerRadians;
    private TextView labelRadians;
    private RadianEntry entryRadians;

    private LinearLayout radianSpecialLayout;
    private View radianLeftBuffer;
    private ImageView radianDataNegSym;
    private LinearLayout containerRadianText;
    private TextView radianTextNumerator;
    private ImageView radianDivBar;
    private TextView radianTextDenominator;
    private View radianRightBuffer;

    private LinearLayout hLLRow2;

    private LinearLayout containerSine;
    private TextView labelSine;
    private TextView sineRegularData;
    private LinearLayout sineSpecialLayout;
    private View sineLeftBuffer;
    private ImageView sinDataNegSym;
    private LinearLayout containerSineText;
    private TextView sineTextNumerator;
    private ImageView sineDivBar;
    private TextView sineTextDenominator;
    private View sineRightBuffer;


    private LinearLayout containerCosine;
    private TextView labelCosine;
    private TextView cosineRegularData;
    private LinearLayout cosineSpecialLayout;
    private View cosineLeftBuffer;
    private ImageView cosDataNegSym;
    private LinearLayout containerCosineText;
    private TextView cosineTextNumerator;
    private ImageView cosineDivBar;
    private TextView cosineTextDenominator;
    private View cosineRightBuffer;


    private LinearLayout hLLRow3;

    private LinearLayout containerTangent;
    private TextView labelTangent;
    private TextView tangentRegularData;
    private LinearLayout tangentSpecialLayout;
    private View tangentLeftBuffer;
    private ImageView tanDataNegSym;
    private LinearLayout containerTangentText;
    private TextView tangentTextNumerator;
    private ImageView tangentDivBar;
    private TextView tangentTextDenominator;
    private View tangentRightBuffer;

    private LinearLayout containerRadianControl;
    private ImageView radianControlLeftArrow;
    private ImageView radianControlRightArrow;

    private LinearLayout hLLRow4 = null;
    private LinearLayout hLLRow5 = null;
    private LinearLayout hLLRow6 = null;

    /* contains all the Views whose background color needs to be set to the
     background color appropriate for the current theme */
    private List<View> allBackgroundColorViews;

    /* contains all the Views whose background color needs to be set to the
     border color appropriate for the current theme */
    private List<View> allBorderColorViews;

    /* contains all the Views whose text color should change depending on
     the current theme */
    private List<TextView> allViewsToSetTextColor;

    /* contains all the Views which need to be set VISIBLE if
       the special layouts should be showing (or set to GONE if the regular
       layout should be showing) */
    private List<View> allSpecialLayoutViews;

    /* contains all the Views which should be set to VISIBLE if
       the regular layout should be showing (or set to GONE if the special
       layout should be showing) */
    private List<View> allRegularDataViews;

    /* all the views in the Special Layouts whose background colors
       should be changed to match the current theme (i.e. negative symbol and
       division bar) */
    private List<View> allSpecialLayoutViewsToChangeBackgroundColor;

    public ConstraintLayout getClMainScreenEncompassing() {
        return clMainScreenEncompassing;
    }

    public void setClMainScreenEncompassing(
            ConstraintLayout clMainScreenEncompassing) {
        this.clMainScreenEncompassing = clMainScreenEncompassing;
    }

    public LinearLayout getvLLEncompassing() {
        return vLLEncompassing;
    }

    public void setvLLEncompassing(LinearLayout vLLEncompassing) {
        this.vLLEncompassing = vLLEncompassing;
    }

    public LinearLayout gethLLRow1() {
        return hLLRow1;
    }

    public void sethLLRow1(LinearLayout hLLRow1) {
        this.hLLRow1 = hLLRow1;
    }

    public LinearLayout getContainerDegrees() {
        return containerDegrees;
    }

    public void setContainerDegrees(LinearLayout containerDegrees) {
        this.containerDegrees = containerDegrees;
    }

    public TextView getLabelDegrees() {
        return labelDegrees;
    }

    public void setLabelDegrees(TextView labelDegrees) {
        this.labelDegrees = labelDegrees;
    }

    public AngleEntry getEntryDegrees() {
        return entryDegrees;
    }

    public void setEntryDegrees(AngleEntry entryDegrees) {
        this.entryDegrees = entryDegrees;
    }

    public LinearLayout getContainerRadians() {
        return containerRadians;
    }

    public void setContainerRadians(LinearLayout containerRadians) {
        this.containerRadians = containerRadians;
    }

    public TextView getLabelRadians() {
        return labelRadians;
    }

    public void setLabelRadians(TextView labelRadians) {
        this.labelRadians = labelRadians;
    }

    public RadianEntry getEntryRadians() {
        return entryRadians;
    }

    public void setEntryRadians(RadianEntry entryRadians) {
        this.entryRadians = entryRadians;
    }

    public LinearLayout gethLLRow2() {
        return hLLRow2;
    }

    public void sethLLRow2(LinearLayout hLLRow2) {
        this.hLLRow2 = hLLRow2;
    }

    public LinearLayout getContainerSine() {
        return containerSine;
    }

    public void setContainerSine(LinearLayout containerSine) {
        this.containerSine = containerSine;
    }

    public TextView getLabelSine() {
        return labelSine;
    }

    public void setLabelSine(TextView labelSine) {
        this.labelSine = labelSine;
    }

    public TextView getSineRegularData() {
        return sineRegularData;
    }

    public void setSineRegularData(TextView sineRegularData) {
        this.sineRegularData = sineRegularData;
    }

    public LinearLayout getContainerCosine() {
        return containerCosine;
    }

    public void setContainerCosine(LinearLayout containerCosine) {
        this.containerCosine = containerCosine;
    }

    public TextView getLabelCosine() {
        return labelCosine;
    }

    public void setLabelCosine(TextView labelCosine) {
        this.labelCosine = labelCosine;
    }

    public TextView getCosineRegularData() {
        return cosineRegularData;
    }

    public void setCosineRegularData(TextView cosineRegularData) {
        this.cosineRegularData = cosineRegularData;
    }

    public LinearLayout gethLLRow3() {
        return hLLRow3;
    }

    public void sethLLRow3(LinearLayout hLLRow3) {
        this.hLLRow3 = hLLRow3;
    }

    public LinearLayout getContainerTangent() {
        return containerTangent;
    }

    public void setContainerTangent(LinearLayout containerTangent) {
        this.containerTangent = containerTangent;
    }

    public TextView getLabelTangent() {
        return labelTangent;
    }

    public void setLabelTangent(TextView labelTangent) {
        this.labelTangent = labelTangent;
    }

    public TextView getTangentRegularData() {
        return tangentRegularData;
    }

    public void setTangentRegularData(TextView tangentRegularData) {
        this.tangentRegularData = tangentRegularData;
    }

    public LinearLayout getContainerRadianControl() {
        return containerRadianControl;
    }

    public void setContainerRadianControl(
            LinearLayout containerRadianControl) {
        this.containerRadianControl = containerRadianControl;
    }

    public ImageView getRadianControlLeftArrow() {
        return radianControlLeftArrow;
    }

    public void setRadianControlLeftArrow(ImageView radianControlLeftArrow) {
        this.radianControlLeftArrow = radianControlLeftArrow;
    }

    public ImageView getRadianControlRightArrow() {
        return radianControlRightArrow;
    }

    public void setRadianControlRightArrow(ImageView radianControlRightArrow) {
        this.radianControlRightArrow = radianControlRightArrow;
    }

    public LinearLayout getRadianSpecialLayout() {
        return radianSpecialLayout;
    }

    public void setRadianSpecialLayout(LinearLayout radianSpecialLayout) {
        this.radianSpecialLayout = radianSpecialLayout;
    }

    public View getRadianLeftBuffer() {
        return radianLeftBuffer;
    }

    public void setRadianLeftBuffer(View radianLeftBuffer) {
        this.radianLeftBuffer = radianLeftBuffer;
    }

    public ImageView getRadianDataNegSym() {
        return radianDataNegSym;
    }

    public void setRadianDataNegSym(ImageView radianDataNegSym) {
        this.radianDataNegSym = radianDataNegSym;
    }

    public LinearLayout getContainerRadianText() {
        return containerRadianText;
    }

    public void setContainerRadianText(LinearLayout containerRadianText) {
        this.containerRadianText = containerRadianText;
    }

    public TextView getRadianTextNumerator() {
        return radianTextNumerator;
    }

    public void setRadianTextNumerator(TextView radianTextNumerator) {
        this.radianTextNumerator = radianTextNumerator;
    }

    public ImageView getRadianDivBar() {
        return radianDivBar;
    }

    public void setRadianDivBar(ImageView radianDivBar) {
        this.radianDivBar = radianDivBar;
    }

    public TextView getRadianTextDenominator() {
        return radianTextDenominator;
    }

    public void setRadianTextDenominator(TextView radianTextDenominator) {
        this.radianTextDenominator = radianTextDenominator;
    }

    public View getRadianRightBuffer() {
        return radianRightBuffer;
    }

    public void setRadianRightBuffer(View radianRightBuffer) {
        this.radianRightBuffer = radianRightBuffer;
    }

    public LinearLayout getSineSpecialLayout() {
        return sineSpecialLayout;
    }

    public void setSineSpecialLayout(LinearLayout sineSpecialLayout) {
        this.sineSpecialLayout = sineSpecialLayout;
    }

    public View getSineLeftBuffer() {
        return sineLeftBuffer;
    }

    public void setSineLeftBuffer(View sineLeftBuffer) {
        this.sineLeftBuffer = sineLeftBuffer;
    }

    public ImageView getSinDataNegSym() {
        return sinDataNegSym;
    }

    public void setSinDataNegSym(ImageView sinDataNegSym) {
        this.sinDataNegSym = sinDataNegSym;
    }

    public LinearLayout getContainerSineText() {
        return containerSineText;
    }

    public void setContainerSineText(LinearLayout containerSineText) {
        this.containerSineText = containerSineText;
    }

    public TextView getSineTextNumerator() {
        return sineTextNumerator;
    }

    public void setSineTextNumerator(TextView sineTextNumerator) {
        this.sineTextNumerator = sineTextNumerator;
    }

    public ImageView getSineDivBar() {
        return sineDivBar;
    }

    public void setSineDivBar(ImageView sineDivBar) {
        this.sineDivBar = sineDivBar;
    }

    public TextView getSineTextDenominator() {
        return sineTextDenominator;
    }

    public void setSineTextDenominator(TextView sineTextDenominator) {
        this.sineTextDenominator = sineTextDenominator;
    }

    public View getSineRightBuffer() {
        return sineRightBuffer;
    }

    public void setSineRightBuffer(View sineRightBuffer) {
        this.sineRightBuffer = sineRightBuffer;
    }

    public LinearLayout getCosineSpecialLayout() {
        return cosineSpecialLayout;
    }

    public void setCosineSpecialLayout(LinearLayout cosineSpecialLayout) {
        this.cosineSpecialLayout = cosineSpecialLayout;
    }

    public View getCosineLeftBuffer() {
        return cosineLeftBuffer;
    }

    public void setCosineLeftBuffer(View cosineLeftBuffer) {
        this.cosineLeftBuffer = cosineLeftBuffer;
    }

    public ImageView getCosDataNegSym() {
        return cosDataNegSym;
    }

    public void setCosDataNegSym(ImageView cosDataNegSym) {
        this.cosDataNegSym = cosDataNegSym;
    }

    public LinearLayout getContainerCosineText() {
        return containerCosineText;
    }

    public void setContainerCosineText(LinearLayout containerCosineText) {
        this.containerCosineText = containerCosineText;
    }

    public TextView getCosineTextNumerator() {
        return cosineTextNumerator;
    }

    public void setCosineTextNumerator(TextView cosineTextNumerator) {
        this.cosineTextNumerator = cosineTextNumerator;
    }

    public ImageView getCosineDivBar() {
        return cosineDivBar;
    }

    public void setCosineDivBar(ImageView cosineDivBar) {
        this.cosineDivBar = cosineDivBar;
    }

    public TextView getCosineTextDenominator() {
        return cosineTextDenominator;
    }

    public void setCosineTextDenominator(TextView cosineTextDenominator) {
        this.cosineTextDenominator = cosineTextDenominator;
    }

    public View getCosineRightBuffer() {
        return cosineRightBuffer;
    }

    public void setCosineRightBuffer(View cosineRightBuffer) {
        this.cosineRightBuffer = cosineRightBuffer;
    }

    public LinearLayout getTangentSpecialLayout() {
        return tangentSpecialLayout;
    }

    public void setTangentSpecialLayout(LinearLayout tangentSpecialLayout) {
        this.tangentSpecialLayout = tangentSpecialLayout;
    }

    public View getTangentLeftBuffer() {
        return tangentLeftBuffer;
    }

    public void setTangentLeftBuffer(View tangentLeftBuffer) {
        this.tangentLeftBuffer = tangentLeftBuffer;
    }

    public ImageView getTanDataNegSym() {
        return tanDataNegSym;
    }

    public void setTanDataNegSym(ImageView tanDataNegSym) {
        this.tanDataNegSym = tanDataNegSym;
    }

    public LinearLayout getContainerTangentText() {
        return containerTangentText;
    }

    public void setContainerTangentText(LinearLayout containerTangentText) {
        this.containerTangentText = containerTangentText;
    }

    public TextView getTangentTextNumerator() {
        return tangentTextNumerator;
    }

    public void setTangentTextNumerator(TextView tangentTextNumerator) {
        this.tangentTextNumerator = tangentTextNumerator;
    }

    public ImageView getTangentDivBar() {
        return tangentDivBar;
    }

    public void setTangentDivBar(ImageView tangentDivBar) {
        this.tangentDivBar = tangentDivBar;
    }

    public TextView getTangentTextDenominator() {
        return tangentTextDenominator;
    }

    public void setTangentTextDenominator(TextView tangentTextDenominator) {
        this.tangentTextDenominator = tangentTextDenominator;
    }

    public View getTangentRightBuffer() {
        return tangentRightBuffer;
    }

    public void setTangentRightBuffer(View tangentRightBuffer) {
        this.tangentRightBuffer = tangentRightBuffer;
    }

    public void sethLLRow4(LinearLayout hLLRow4) {
        this.hLLRow4 = hLLRow4;
    }

    public void sethLLRow5(LinearLayout hLLRow5) {
        this.hLLRow5 = hLLRow5;
    }

    public void sethLLRow6(LinearLayout hLLRow6) {
        this.hLLRow6 = hLLRow6;
    }

    /**
     * this must be called after all of the appropriate setter methods
     * have been called on this object
     */
    public void finishSetup(){
        setupAllBackgroundColorViews();
        setupAllBorderColorViews();
        setupAllViewsToSetTextColor();
        setupAllSpecialLayoutViews();
        setupAllRegularDataViews();
        setupAllSpecialLayoutViewsToChangeBackgroundColor();
    }

    /**
     * creates the relevant List object and then adds the
     * appropriate elements
     */
    private void setupAllBackgroundColorViews(){
        allBackgroundColorViews = new ArrayList<>();
        allBackgroundColorViews.add(clMainScreenEncompassing);
        allBackgroundColorViews.add(containerDegrees);
        allBackgroundColorViews.add(containerRadians);
        allBackgroundColorViews.add(containerSine);
        allBackgroundColorViews.add(containerCosine);
        allBackgroundColorViews.add(containerTangent);
        allBackgroundColorViews.add(containerRadianControl);
    }

    /**
     *
     * @return all the Views whose background color needs to be set to the
     * background color appropriate for the current theme
     */
    public List<View> getAllBackgroundColorViews(){
        return allBackgroundColorViews;
    }

    /**
     * creates the relevant List object and then adds the
     * appropriate elements
     */
    private void setupAllBorderColorViews(){
        allBorderColorViews = new ArrayList<>();

        allBorderColorViews.add(hLLRow1);
        allBorderColorViews.add(hLLRow2);
        allBorderColorViews.add(hLLRow3);

        if (hLLRow4 != null){
            allBorderColorViews.add(hLLRow4);

            // if hLLRow4 is not null, we assume 5 and 6 aren't either
            allBorderColorViews.add(hLLRow5);
            allBorderColorViews.add(hLLRow6);
        }
    }

    /**
     *
     * @return all the Views whose background color needs to be set to the
     * border color appropriate for the current theme
     */
    public List<View> getAllBorderColorViews(){
        return allBorderColorViews;
    }

    /**
     * creates the relevant List object and then adds the
     * appropriate elements
     */
    private void setupAllViewsToSetTextColor(){
        allViewsToSetTextColor = new ArrayList<>();
        allViewsToSetTextColor.add(labelDegrees);
        allViewsToSetTextColor.add(entryDegrees);
        allViewsToSetTextColor.add(labelRadians);
        allViewsToSetTextColor.add(entryRadians);
        allViewsToSetTextColor.add(labelSine);
        allViewsToSetTextColor.add(sineRegularData);
        allViewsToSetTextColor.add(labelCosine);
        allViewsToSetTextColor.add(cosineRegularData);
        allViewsToSetTextColor.add(labelTangent);
        allViewsToSetTextColor.add(tangentRegularData);
        allViewsToSetTextColor.add(radianTextNumerator);
        allViewsToSetTextColor.add(radianTextDenominator);
        allViewsToSetTextColor.add(sineTextNumerator);
        allViewsToSetTextColor.add(sineTextDenominator);
        allViewsToSetTextColor.add(cosineTextNumerator);
        allViewsToSetTextColor.add(cosineTextDenominator);
        allViewsToSetTextColor.add(tangentTextNumerator);
        allViewsToSetTextColor.add(tangentTextDenominator);
    }

    /**
     *
     * @return all the Views whose text color should change depending on
     * the current theme
     */
    public List<TextView> getAllViewsToSetTextColor(){
        return allViewsToSetTextColor;
    }

    /**
     * creates the relevant List object and then adds the
     * appropriate elements
     */
    private void setupAllSpecialLayoutViews(){
        allSpecialLayoutViews = new ArrayList<>();
        allSpecialLayoutViews.add(radianSpecialLayout);
        allSpecialLayoutViews.add(sineSpecialLayout);
        allSpecialLayoutViews.add(cosineSpecialLayout);
        allSpecialLayoutViews.add(tangentSpecialLayout);
        allSpecialLayoutViews.add(radianDivBar);
        allSpecialLayoutViews.add(radianTextDenominator);
        allSpecialLayoutViews.add(sineDivBar);
        allSpecialLayoutViews.add(sineTextDenominator);
        allSpecialLayoutViews.add(cosineDivBar);
        allSpecialLayoutViews.add(cosineTextDenominator);
        allSpecialLayoutViews.add(tangentDivBar);
        allSpecialLayoutViews.add(tangentTextDenominator);
    }

    /**
     * @return all the Views which need to be set VISIBLE if
     * the special layouts should be showing (or set to GONE if the regular
     * layout should be showing)
     */
    public List<View> getAllSpecialLayoutViews(){
        return allSpecialLayoutViews;
    }

    /**
     * creates the relevant List object and then adds the
     * appropriate elements
     */
    private void setupAllRegularDataViews(){
        allRegularDataViews = new ArrayList<>();
        allRegularDataViews.add(entryRadians);
        allRegularDataViews.add(sineRegularData);
        allRegularDataViews.add(cosineRegularData);
        allRegularDataViews.add(tangentRegularData);
    }

    /**
     * @return all the Views which should be set to VISIBLE if
     * the regular layout should be showing (or set to GONE if the special
     * layout should be showing)
     */
    public List<View> getAllRegularDataViews(){
        return allRegularDataViews;
    }

    /**
     * creates the relevant List object and then adds the
     * appropriate elements
     */
    private void setupAllSpecialLayoutViewsToChangeBackgroundColor(){
        allSpecialLayoutViewsToChangeBackgroundColor = new ArrayList<>();
        allSpecialLayoutViewsToChangeBackgroundColor.add(radianDataNegSym);
        allSpecialLayoutViewsToChangeBackgroundColor.add(radianDivBar);
        allSpecialLayoutViewsToChangeBackgroundColor.add(cosDataNegSym);
        allSpecialLayoutViewsToChangeBackgroundColor.add(cosineDivBar);
        allSpecialLayoutViewsToChangeBackgroundColor.add(sinDataNegSym);
        allSpecialLayoutViewsToChangeBackgroundColor.add(sineDivBar);
        allSpecialLayoutViewsToChangeBackgroundColor.add(tanDataNegSym);
        allSpecialLayoutViewsToChangeBackgroundColor.add(tangentDivBar);
    }

    /**
     *
     * @return all the views in the Special Layouts whose background colors
     * should be changed to match the current theme (i.e. negative symbol and
     * division bar)
     */
    public List<View> getAllSpecialLayoutViewsToChangeBackgroundColor(){
        return allSpecialLayoutViewsToChangeBackgroundColor;
    }
}
