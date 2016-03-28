package com.spc.traverse;

import android.util.Log;
import android.widget.ImageButton;

/**
 * Created by shaun on 14/10/15.
 */
public class Player {
    public static final String TAG = "PLAYER";


    int id;
    TeamColour  colour;
    Direction   direction;
    PlayerType  type;
    int     startX, startY;
    int     endX, endY;
    Piece   pieces[] = new Piece[8];
    boolean started;
    boolean finished;
    ImageButton button;

    public Player(int id, TeamColour colour, Direction direction) {
        this.id = id;
        this.colour = colour;
        this.direction = direction;
        this.type = PlayerType.COMPUTER;  // default this on initial creation
        this.started = false;
        this.finished = false;

        switch (direction) {
            case N2S:
                this.startY = 0;
                this.endY = 9;
                break;
            case S2N:
                this.startY = 9;
                this.endY = 0;
                break;
            case W2E:
                this.startX = 0;
                this.endX = 9;
                break;
            case E2W:
                this.startX = 9;
                this.endX = 0;
                break;

        }

        Log.i(TAG, "...creating a new player....");

        if (direction==Direction.N2S || direction==Direction.S2N) {
            this.pieces[0] = new Piece(colour, 1, startY, ShapeType.TRIANGLE);
            this.pieces[1] = new Piece(colour,2, startY, ShapeType.SQUARE);
            this.pieces[2] = new Piece(colour,3, startY, ShapeType.DIAMOND);
            this.pieces[3] = new Piece(colour,4, startY, ShapeType.CIRCLE);
            this.pieces[4] = new Piece(colour,5, startY, ShapeType.TRIANGLE);
            this.pieces[5] = new Piece(colour,6, startY, ShapeType.SQUARE);
            this.pieces[6] = new Piece(colour,7, startY, ShapeType.DIAMOND);
            this.pieces[7] = new Piece(colour,8, startY, ShapeType.CIRCLE);
        } else {
            this.pieces[0] = new Piece(colour,startX,1 , ShapeType.TRIANGLE);
            this.pieces[1] = new Piece(colour,startX,2 , ShapeType.SQUARE);
            this.pieces[2] = new Piece(colour,startX,3 , ShapeType.DIAMOND);
            this.pieces[3] = new Piece(colour,startX,4 , ShapeType.CIRCLE);
            this.pieces[4] = new Piece(colour,startX,5 , ShapeType.TRIANGLE);
            this.pieces[5] = new Piece(colour,startX,6 , ShapeType.SQUARE);
            this.pieces[6] = new Piece(colour,startX,7 , ShapeType.DIAMOND);
            this.pieces[7] = new Piece(colour,startX,8 , ShapeType.CIRCLE);

        };

    }

    public int getId() {
        return id;
    }

    public PlayerType getType() {
        return type;
    }

    public void setType(PlayerType type) {
        this.type = type;
    }

    public ImageButton getButton() {
        return button;
    }

    public void setButton(ImageButton button) {
        this.button = button;
    }

    public Direction getDirection() {
        return direction;
    }

    public int getStartX() {
        return startX;
    }

    public int getStartY() {
        return startY;
    }

    public int getEndX() {
        return endX;
    }

    public int getEndY() {
        return endY;
    }

    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }
}
