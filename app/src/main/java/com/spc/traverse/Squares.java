package com.spc.traverse;


import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;

/**
 * Created by tcottz7 on 30/08/15.
 * Class for each square on the board...
 */
class Squares {

    private static final String TAG = "SQUARES";

    // BASIC attributes
    private int posX;
    private int posY;
    private SquareType sqType;
    private SquareSubType sqSubType;
    // INTERACTION attributes
    private int sqButtonId;
    private ImageButton sqImageButton;
    int sqBackgroundResource;
    private int sqImageResource;
    // STATE attributes
    Piece piece;    // either has a piece on it, or not




    Squares(int posX, int posY) {

        // DEFAULT BASIC ATTRIBUTES
        this.posX = posX;
        this.posY = posY;
        this.sqType = SquareType.MAIN;
        this.sqSubType = SquareSubType.MAIN_DARK;
        // DEFAULT INTERACTION attributes
        this.sqButtonId = ((posX * 10) + posY + 1);
        this.sqBackgroundResource = R.drawable.sel_sq_main_dark;
       // this.sqImageResource = null;

        // DEFAULT STATE ATTRIBUTES
        this.piece = null;      // assume NO PIECE ON SQUARE

        // CALCULATE SQUARE-SPECIFIC BASIC and INTERACTION ATTRIBUTES
        if (posY == 0) {
            // This will be overwritten shortly if a corner-square
            this.sqType = SquareType.BASE;
            this.sqSubType = SquareSubType.BASE_N;
            this.sqBackgroundResource = R.drawable.sel_sq_base_n;
        }

        if (posY == 9) {
            // This will be overwritten shortly if a corner-square
            this.sqType = SquareType.BASE;
            this.sqSubType = SquareSubType.BASE_S;
            this.sqBackgroundResource = R.drawable.sel_sq_base_s;
        }

        if (posX == 0) {
            if (posY == 0) {
                this.sqType = SquareType.CORNER;
                this.sqSubType = SquareSubType.CORNER_NW;
                this.sqBackgroundResource = R.drawable.sel_sq_corner;
                this.sqImageResource = R.drawable.circle_corner;
            } else if (posY == 9) {
                this.sqType = SquareType.CORNER;
                this.sqSubType = SquareSubType.CORNER_SW;
                this.sqBackgroundResource = R.drawable.sel_sq_corner;
                this.sqImageResource = R.drawable.square_corner;
            } else {
                this.sqType = SquareType.BASE;
                this.sqSubType = SquareSubType.BASE_W;
                this.sqBackgroundResource = R.drawable.sel_sq_base_w;
            }
        }

        if (posX == 9) {
            if (posY == 0) {
                this.sqType = SquareType.CORNER;
                this.sqSubType = SquareSubType.CORNER_NE;
                this.sqBackgroundResource = R.drawable.sel_sq_corner;
                this.sqImageResource = R.drawable.triangle_corner;
            } else if (posY == 9) {
                this.sqType = SquareType.CORNER;
                this.sqSubType = SquareSubType.CORNER_SE;
                this.sqBackgroundResource = R.drawable.sel_sq_corner;
                this.sqImageResource = R.drawable.diamond_corner;
            } else {
                this.sqType = SquareType.BASE;
                this.sqSubType = SquareSubType.BASE_E;
                this.sqBackgroundResource = R.drawable.sel_sq_base_e;
            }
        }

        if (posX > 0 && posX < 9 && posY > 0 && posY < 9) {
            if ((isOdd(posX) && !isOdd(posY)) ||
                    (!isOdd(posX) && isOdd(posY))) {
                this.sqSubType = SquareSubType.MAIN_LIGHT;
                this.sqBackgroundResource = R.drawable.sel_sq_main_light;
            }

        }

    }

    private boolean isOdd(int val) {
        return (val & 0x01) != 0;
    }

    // DEFAULT BASIC ATTRIBUTES
    int getPosX() {
        return posX;
    }
    int getPosY() {
        return posY;
    }

    SquareType getSqType() {
        return sqType;
    }

    boolean isCorner() {
        return this.sqType == SquareType.CORNER;
    }

    boolean isBase () {
        return this.sqType == SquareType.BASE;
    }

    boolean isPlayerEndBase(Direction dir) {
        // return true if a BASE, but is not this players end row/col
        return ((this.sqSubType == SquareSubType.BASE_N && dir==Direction.S2N) ||
                (this.sqSubType == SquareSubType.BASE_S && dir==Direction.N2S) ||
                (this.sqSubType == SquareSubType.BASE_E && dir==Direction.W2E) ||
                (this.sqSubType == SquareSubType.BASE_W && dir==Direction.E2W));

    }


    // INTERACTION attributes
    int getSqButtonId() {
        return sqButtonId;
    }

    ImageButton getSqImageButton() {
        return sqImageButton;
    }
    void setSqImageButton(ImageButton sqImageButton) {
        this.sqImageButton = sqImageButton;
    }

    int getSqImageResource() {
        return sqImageResource;
    }
    void setSqImageResource(int sqImageResource) {
        this.sqImageResource = sqImageResource;
    }


    // STATE attributes
    Boolean isOccupied() {
        return piece!=null;
    }
    void setOccupied(Piece piece) {
        this.piece = piece;
    }

    void logDetails() {
        String msg;
        msg = "Square:["+this.posX+"," + this.posY+"]";
        msg = msg + "/Type:["+this.sqType+"/"+this.sqSubType+"]";
        msg = msg + "/ID:"+this.sqButtonId;
        if (this.piece != null) {
            msg = msg + "/Piece:OCCUPIED";
        } else {
            msg = msg + "/Piece:EMPTY";
        }
        Log.i(TAG, msg);
        if (this.piece != null) {
                piece.logDetails();
        }
    }


    void highlightSquare(Boolean on) {

        if (on) {  // turn it on
            this.getSqImageButton().setPressed(true);
            this.getSqImageButton().setActivated(true);
            // set up an animation to flash image image
            Animation mAnimation = new AlphaAnimation(1, 0);
            mAnimation.setDuration(200);
            mAnimation.setInterpolator(new LinearInterpolator());
            mAnimation.setRepeatCount(Animation.INFINITE);
            mAnimation.setRepeatMode(Animation.REVERSE);
            this.getSqImageButton().startAnimation(mAnimation);
        } else {  // turn it off
            this.getSqImageButton().clearAnimation();
            this.getSqImageButton().setPressed(false);
            this.getSqImageButton().setActivated(false);
        }
        //Log.i(TAG, "highlightSquare="+on+" ["+this.posX+","+this.posY+"]");
        //this.logDetails();
    }

    void highlightChoice(Boolean on) {

        if (on) {  // turn it on
            //if (this.sqSubType == SquareSubType.MAIN_DARK)
            //this.getSqImageButton().setBackgroundResource(R.drawable.sq_main_light_border);
            this.getSqImageButton().setPressed(true);
            this.getSqImageButton().setActivated(true);
            this.getSqImageButton().setClickable(true);
            // set up an animation to flash image image
            //Animation mAnimation = new AlphaAnimation(1, 0);
            //mAnimation.setDuration(200);
            //mAnimation.setInterpolator(new LinearInterpolator());
            //mAnimation.setRepeatCount(Animation.INFINITE);
            //mAnimation.setRepeatMode(Animation.REVERSE);
            //this.getSqImageButton().startAnimation(mAnimation);
        } else {  // turn if off
            //this.getSqImageButton().clearAnimation();
            this.getSqImageButton().setPressed(false);
            this.getSqImageButton().setActivated(false);
            //this.getSqImageButton().setClickable(false);
        }
        //Log.i(TAG, "highlightChoice="+on+" ["+this.posX+","+this.posY+"]");
        //this.logDetails();
    }
}