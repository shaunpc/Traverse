package com.spc.traverse;

import android.graphics.drawable.shapes.Shape;
import android.util.Log;

/**
 * Created by shaun on 14/10/15.
 */
public class Piece {

    public static final String TAG = "PIECE";

    Integer     posX;
    Integer     posY;
    ShapeType   shape;
    int         piecesImageResource;

    public Piece(TeamColour colour, Integer posX, Integer posY, ShapeType shape) {
        this.posX = posX;
        this.posY = posY;
        this.shape = shape;

        switch (shape) {
            case CIRCLE:
                if (colour == TeamColour.BLUE) {piecesImageResource = R.drawable.circle_blue;}
                if (colour == TeamColour.GREEN) {piecesImageResource = R.drawable.circle_green;}
                if (colour == TeamColour.YELLOW) {piecesImageResource = R.drawable.circle_yellow;}
                if (colour == TeamColour.RED) {piecesImageResource = R.drawable.circle_red;}
                break;
            case DIAMOND:
                if (colour == TeamColour.BLUE) {piecesImageResource = R.drawable.diamond_blue;}
                if (colour == TeamColour.GREEN) {piecesImageResource = R.drawable.diamond_green;}
                if (colour == TeamColour.YELLOW) {piecesImageResource = R.drawable.diamond_yellow;}
                if (colour == TeamColour.RED) {piecesImageResource = R.drawable.diamond_red;}
                break;
            case SQUARE:
                if (colour == TeamColour.BLUE) {piecesImageResource = R.drawable.square_blue;}
                if (colour == TeamColour.GREEN) {piecesImageResource = R.drawable.square_green;}
                if (colour == TeamColour.YELLOW) {piecesImageResource = R.drawable.square_yellow;}
                if (colour == TeamColour.RED) {piecesImageResource = R.drawable.square_red;}
                break;
            case TRIANGLE:
                if (colour == TeamColour.BLUE) {piecesImageResource = R.drawable.triangle_blue;}
                if (colour == TeamColour.GREEN) {piecesImageResource = R.drawable.triangle_green;}
                if (colour == TeamColour.YELLOW) {piecesImageResource = R.drawable.triangle_yellow;}
                if (colour == TeamColour.RED) {piecesImageResource = R.drawable.triangle_red;}
                break;

        }

    }

    public Integer getPosX() {

        return posX;
    }

    public void setPosX(Integer posX) {
        this.posX = posX;
    }

    public Integer getPosY() {
        return posY;
    }

    public void setPosY(Integer posY) {
        this.posY = posY;
    }

    public ShapeType getShape() {
        return shape;
    }

    public void setShape(ShapeType shape) {
        this.shape = shape;
    }
}
