package com.dakotabarron.freeunitcircle;

import static java.lang.Math.abs;

/**
 * Encapsulates an angle value and other data associated with the angle
 * Created by dakota on 8/3/17.
 */
public class Theta {
    private double angleInRadians; // always positive
    private double angleInDegrees; // always positive
    private boolean tangentDefined;

    // unicode square root symbol
    private static final String SQRT_SYM = "\u221A";

    // negative prefix used in coordinates
    private static final String NEGATIVE = "- ";

    /*
    whether this angle has a special value such as PI/2, PI, etc...
     */
    private boolean isSpecialAngle;

    /*
    any angle within this amount of degrees of 90 or 270 will be considered
    to have an undefined tangent value.
     */
    private static final double TANGENT_TOLERANCE = 0.1d;

    /*
    enum makes it easier to work with special angles
     */
    public enum SpecialAngle {
        /*
        x and y offsets for displaying text are always
        added (+) on to their respective reference points, so make them
        negative where appropriate
         */
        ZERO(0, 0d, "1", "1", "0", "1", "0", "1", 0, 1),
        PI_OVER_6(30, Math.PI / 6, SQRT_SYM + "3", "2", "1", "2", SQRT_SYM + "3", "3", 1, 6),
        PI_OVER_4(45, Math.PI / 4, SQRT_SYM + "2", "2", SQRT_SYM + "2", "2", "1", "1", 1, 4),
        PI_OVER_3(60, Math.PI / 3, "1", "2", SQRT_SYM + "3", "2", SQRT_SYM + "3", "1", 1, 3),
        PI_OVER_2(90, Math.PI / 2, "0", "1", "1", "1", "1", "0", 1, 2),
        TWO_PI_OVER_3(120, 2 * Math.PI / 3, "1", "2", SQRT_SYM + "3", "2", SQRT_SYM + "3", "1", 2, 3),
        THREE_PI_OVER_4(135, 3 * Math.PI / 4, SQRT_SYM + "2", "2",
                SQRT_SYM + "2", "2", "1", "1", 3, 4),
        FIVE_PI_OVER_6(150, 5 * Math.PI / 6, SQRT_SYM + "3", "2", "1",
                "2", SQRT_SYM + "3", "3", 5, 6),
        PI(180, Math.PI, "1", "1", "0", "1", "0", "1", 1, 1),
        SEVEN_PI_OVER_6(210, 7 * Math.PI / 6, SQRT_SYM + "3", "2", "1",
                "2", SQRT_SYM + "3", "3", 7, 6),
        FIVE_PI_OVER_4(225, 5 * Math.PI / 4, SQRT_SYM + "2", "2",
                SQRT_SYM + "2", "2", "1", "1", 5, 4),
        FOUR_PI_OVER_3(240, 4 * Math.PI / 3, "1", "2", SQRT_SYM + "3",
                "2", SQRT_SYM + "3", "1", 4, 3),
        THREE_PI_OVER_2(270, 3 * Math.PI / 2, "0", "1", "1", "1", "1", "0", 3, 2),
        FIVE_PI_OVER_3(300, 5 * Math.PI / 3, "1", "2", SQRT_SYM + "3",
                "2", SQRT_SYM + "3", "1", 5, 3),
        SEVEN_PI_OVER_4(315, 7 * Math.PI / 4, SQRT_SYM + "2", "2",
                SQRT_SYM + "2", "2", "1", "1", 7, 4),
        ELEVEN_PI_OVER_6(330, 11 * Math.PI / 6, SQRT_SYM + 3, "2", "1", "2", SQRT_SYM + "3", "3", 11, 6);

        private final int degrees;
        private final double radians;

        /*
        the numerator and denominator of the angles' cosine, sine,
         and tangent values
         */
        private final String numeratorCos;
        private final String denominatorCos;
        private final String numeratorSin;
        private final String denominatorSin;
        private final String numeratorTan;
        private final String denominatorTan;

        /*
        the digit before PI in the positive radian value numerator
         */
        private int positiveNumeratorRadians;

        /*
        the digit in the positive radian value denominator
         */
        private int positiveDenominatorRadians;


        SpecialAngle(int degrees, double radians, String numCos,
                     String denomCos, String numSin, String denomSin,
                     String numTan, String denomTan,
                     int posNumRad, int posDenomRad) {
            this.degrees = degrees;
            this.radians = radians;
            this.numeratorCos = numCos;
            this.denominatorCos = denomCos;
            this.numeratorSin = numSin;
            this.denominatorSin = denomSin;
            this.numeratorTan = numTan;
            this.denominatorTan = denomTan;
            this.positiveNumeratorRadians = posNumRad;
            this.positiveDenominatorRadians = posDenomRad;
        }

        /**
         *
         * @return the positive degrees value for this special angle
         * in the range 0 <= x < 360
         */
        public int getDegrees() {
            return degrees;
        }

        /**
         *
         * @return the numerator of the cosine of this angle (always positive)
         */
        public String getNumeratorCos() {
            return numeratorCos;
        }

        /**
         *
         * @return the denominator of the cosine of this angle (always positive)
         */
        public String getDenominatorCos() {
            return denominatorCos;
        }

        /**
         *
         * @return the numerator of the sine of this angle (always positive)
         */
        public String getNumeratorSin() {
            return numeratorSin;
        }

        /**
         *
         * @return the denominator of the sine of this angle (always positive)
         */
        public String getDenominatorSin() {
            return denominatorSin;
        }

        /**
         *
         * @return the numerator of the tangent of this angle (always positive)
         */
        public String getNumeratorTan() {
            return numeratorTan;
        }

        /**
         *
         * @return the denominator of the tangent of this angle (always positive)
         */
        public String getDenominatorTan(){
            return denominatorTan;
        }

        /**
         *
         * @return the digit before PI in the numerator of the positive
         * radians value
         */
        public int getPositiveNumeratorRadians() {
            return positiveNumeratorRadians;
        }

        /**
         * @return the digit in the denominator of the positive radians value
         */
        public int getPositiveDenominatorRadians() {
            return positiveDenominatorRadians;
        }

        /**
         *
         * @return the digit before PI in the numerator of the negative
         * radians value. always returned as a positive integer, client must
         * handle addition of a negative sign if desired
         */
        public int getNegativeNumeratorRadians(){
            if (positiveNumeratorRadians == 0)
                return 0;
            return (2 * positiveDenominatorRadians) -
                    positiveNumeratorRadians;
        }

        /**
         * @return the digit in the denominator of the negative radians value.
         * always returned as a positive integer, client must
         * handle addition of a negative sign if desired
         */
        public int getNegativeDenominatorRadians(){
            // negative denominator same as positive
            return positiveDenominatorRadians;
        }
    }

    /*
    if this Theta is a special angle, its angle measure will be indicated
    by a value of the SpecialAngle enum stored in this field
     */
    private SpecialAngle valueOfSpecialAngle;

    /**
     * ctor initializes a new Theta with angle value of 0.
     */
    public Theta(){
        setAngleInRadians(0);
    }

    /**
     * instantiates a new Theta with identical properties of the argument Theta.
     * @param other the Theta which you want this new Theta to be an identical
     *              copy of
     */
    public Theta(Theta other){
        this.angleInRadians = other.angleInRadians;
        this.angleInDegrees = other.angleInDegrees;
        this.tangentDefined = other.tangentDefined;
        this.isSpecialAngle = other.isSpecialAngle;
        this.valueOfSpecialAngle = other.valueOfSpecialAngle;

        // TODO make sure all fields are included above if you add any more
    }

    /**
     *
     * @return true if this Theta has a special angle value such
     * as PI/2 radians<br> false otherwise
     */
    public boolean isSpecialAngle(){
        return isSpecialAngle;
    }

    /**
     *
     * @return the value of the special angle if this Theta contains a
     * special angle<br>NULL if this Theta does not contain a special angle
     */
    public SpecialAngle getValueOfSpecialAngle(){
        return valueOfSpecialAngle;
    }

    /**
     * returns in terms of positive degrees the angle value
     * encapsulated in this object
     * @return the angle value in positive degrees in the range
     * 0 <= x < 360
     */
    public double getDegreesPositive() {
        return angleInDegrees;
    }

    /**
     * returns in terms of negative degrees the angle value
     * encapsulated in this object
     * @return the angle value in negative degrees in the range
     * -360 < x <= 0
     */
    public double getDegreesNegative(){
        if (angleInDegrees == 0)
            return 0;
        return angleInDegrees - 360;
    }

    /**
     * returns in terms of positive radians the angle value
     * encapsulated in this object
     * @return the angle value in positive radians in the range
     * 0 <= x < 2(PI)
     */
    public double getRadiansPositive(){
        return angleInRadians;
    }

    /**
     * returns in terms of negative radians the angle value
     * encapsulated in this object
     * @return the angle value in negative radians in the range
     * -2(PI) < x <= 0
     */
    public double getRadiansNegative(){
        if (angleInRadians == 0)
            return 0;
        return angleInRadians - (2 * Math.PI);
    }

    /**
     * set the angle value (in radians) to be encapsulated in this object.
     * client should make sure to send in a value in the
     * range -2(PI) <= angle <= +2(PI) or else the old value is kept.
     * will automatically set whether or not the tangent is defined depending
     * on the angle which is passed in.
     * @param angle the angle value (in radians) in the range
     *              -2(PI) <= angle <= +2(PI)
     */
    public void setAngleInRadians(double angle) {
        if ((angle < -2 * Math.PI) || (angle > 2 * Math.PI))
            return;

        // ensure angle is positive
        if (angle >= 0)
            this.angleInRadians = angle;
        else {
            this.angleInRadians = (2 * Math.PI) + angle;
        }

        /*
        only exact special angle that the user can enter in radians is 0
         */
        if (this.angleInRadians == 0){
            isSpecialAngle = true;
            valueOfSpecialAngle = SpecialAngle.ZERO;
        } else {
            isSpecialAngle = false;
            valueOfSpecialAngle = null;
        }

        /*
        always check if tangent is defined for the angle being set
         */
        double deg = Math.toDegrees(this.angleInRadians);
        setTangentDefined(deg);

        angleInDegrees = deg;
    }

    /**
     * sets whether or not the tangent for this angle is defined based on
     * the angle measure argument. note that there is a tolerance within
     * which the tangent of the angle is considered undefined
     * @param degrees the angle measure in degrees
     */
    private void setTangentDefined(double degrees){
        if ((abs(90 - degrees) <= TANGENT_TOLERANCE)
                || (abs(270 - degrees) <= TANGENT_TOLERANCE))
            tangentDefined = false;
        else
            tangentDefined = true;
    }

    /**
     * set the angle value (in degrees) to be encapsulated in this object.
     * client should make sure to send in a value in the
     * range -360 <= angle <= 360 or else the old value is kept.
     * will automatically set whether or not the tangent is defined depending
     * on the angle which is passed in.
     * @param degrees the angle value (in degrees) in the range
     *              -360 <= angle <= 360
     */
    public void setAngleInDegrees(double degrees){
        if ((degrees < -360) || (degrees > 360))
            return;

        if (degrees == 360)
            degrees = 0;

        // make degrees positive
        if (degrees < 0){
            degrees = 360 + degrees;
        }

        /*
        see if angle is any of the special angles
         */
        for (SpecialAngle sa : SpecialAngle.values()){
            if (degrees == (double)sa.degrees){
                isSpecialAngle = true;
                valueOfSpecialAngle = sa;
                angleInRadians = sa.radians;
                angleInDegrees = sa.degrees;
                setTangentDefined(angleInDegrees);
                return;
            }
        }

        /*
        if we get here, we know it is not a special angle
         */

        isSpecialAngle = false;
        valueOfSpecialAngle = null;
        angleInDegrees = degrees;
        angleInRadians = Math.toRadians(angleInDegrees);
        setTangentDefined(degrees);
    }

    /**
     * tells whether or not the tangent of the angle encapsulated in this object
     * is defined or not. Note that there is a tolerance inside of which the
     * tangent is also considered to be undefined.
     * @return false if the tangent of the angle value contained in this object
     * is undefined (i.e. PI/2 and 3PI/2 radians).<br>true otherwise
     */
    public boolean isTangentDefined() {
        return tangentDefined;
    }
}
