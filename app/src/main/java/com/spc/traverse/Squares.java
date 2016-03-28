package com.spc.traverse;

import android.util.Log;
import android.widget.ImageButton;

/**
 * Created by tcottz7 on 30/08/15.
 * Class for each square on the board...
 */
public class Squares {

    public static final String TAG = "SQUARES";

    // BASIC attributes
    int posX;
    int posY;
    SquareType sqType;
    SquareSubType sqSubType;
    // INTERACTION attributes
    int sqButtonId;
    ImageButton sqImageButton;
    int sqBackgroundResource;
    int sqImageResource;
    Boolean clickable;      // FALSE for corners, and for COMPUTER pieces //
    // STATE attributes
    Boolean occupied;       // is square occupied...?
    TeamColour teamColour;  // ... if so, which team?
    Boolean human;          // ... by a human player?
    Direction direction;    // ... and which way are they heading?
    ShapeType shape;        // ... and which shape is it?
    Boolean selected;



    public Squares(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;

        // Log.i(TAG, "Creating new square details for " + posX + "," + posY);

        // DEFAULT BASIC ATTRIBUTES
        this.sqType = SquareType.MAIN;
        this.sqSubType = SquareSubType.MAIN_DARK;
        this.sqButtonId = ((posX * 10) + posY + 1);
        this.sqBackgroundResource = R.drawable.sel_sq_main_dark;
       // this.sqImageResource = null;

        // DEFAULT STATE ATTRIBUTES
        this.selected = false;
        this.shape = null;      // assume NO PIECE ON SQUARE
        this.occupied = false;   // hence, not occupied
        this.human = false;     // hence, not human
        this.teamColour = null; // hence, not in a team
        this.direction = null;  // hence, not going anywhere
        this.clickable = true;  // not for corners or computer pieces

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
                this.clickable = false;
            } else if (posY == 9) {
                this.sqType = SquareType.CORNER;
                this.sqSubType = SquareSubType.CORNER_SW;
                this.sqBackgroundResource = R.drawable.sel_sq_corner;
                this.sqImageResource = R.drawable.square_corner;
                this.clickable = false;
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
                this.clickable = false;
            } else if (posY == 9) {
                this.sqType = SquareType.CORNER;
                this.sqSubType = SquareSubType.CORNER_SE;
                this.sqBackgroundResource = R.drawable.sel_sq_corner;
                this.sqImageResource = R.drawable.diamond_corner;
                this.clickable = false;
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

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public SquareType getSqType() {
        return sqType;
    }

    public SquareSubType getSqSubType() {
        return sqSubType;
    }

    public int getSqButtonId() {
        return sqButtonId;
    }

    public void setSqButtonId(int sqButtonId) {
        this.sqButtonId = sqButtonId;
    }

    public ImageButton getSqImageButton() {
        return sqImageButton;
    }

    public void setSqImageButton(ImageButton sqImageButton) {
        this.sqImageButton = sqImageButton;
    }

    public Boolean getHuman() {
        return human;
    }

    public void setHuman(Boolean human) {
        this.human = human;
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    public Boolean getOccupied() {
        return occupied;
    }

    public void setOccupied(Boolean occupied) {
        this.occupied = occupied;
    }

    public ShapeType getShape() {
        return shape;
    }

    public void setShape(ShapeType shape) {
        this.shape = shape;
    }

    public TeamColour getTeamColour() {
        return teamColour;
    }

    public void setTeamColour(TeamColour teamColour) {
        this.teamColour = teamColour;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public Boolean getClickable() {
        return clickable;
    }

    public void setClickable(Boolean clickable) {
        this.clickable = clickable;
    }

    public int getSqImageResource() {
        return sqImageResource;
    }

    public void setSqImageResource(int sqImageResource) {
        this.sqImageResource = sqImageResource;
    }

    public String logDetails() {
        String msg;
        msg = "Square: ["+this.getPosX()+"," + this.getPosY()+"]";
        msg = msg + "/Occupied:"+this.getOccupied();
        msg = msg + "/Human:"+this.getHuman();
        msg = msg + "/Shape:"+this.getShape();
        msg = msg + "/Direction:"+this.getDirection();
        return msg;
    }
}


