package com.spc.traverse;

import android.graphics.drawable.Drawable;
import android.util.Log;

import java.util.Random;

/**
 * Created by shaun on 14/10/15.
 * Object class to hold a player, with colour, direction and all their pieces...
 */
public class Player {

    public static final String TAG = "PLAYER";

    int id;
    TeamColour colour;
    Direction direction;
    PlayerType type;
    int startX, startY;
    int offsetX, offsetY;
    int endX, endY;
    Piece pieces[] = new Piece[8];
    boolean started;
    boolean finished;
    Drawable image;

    public int getOffsetX() {
        return offsetX;
    }

    public int getOffsetY() {
        return offsetY;
    }

    public Player(int id, TeamColour colour, Direction direction) {
        this.id = id;
        this.colour = colour;
        this.direction = direction;
        this.type = PlayerType.COMPUTER;  // default this on initial creation
        this.started = false;
        this.finished = false;

        switch (direction) {
            case N2S:
                this.startX = -1;
                this.startY = 0;
                this.endX = -1;
                this.endY = 9;
                this.offsetX = 0;
                this.offsetY = 1;
                break;
            case S2N:
                this.startX = -1;
                this.startY = 9;

                this.endX = -1;
                this.endY = 0;
                this.offsetX = 0;
                this.offsetY = -1;
                break;
            case W2E:
                this.startX = 0;
                this.startY = -1;
                this.endX = 9;
                this.endY = -1;
                this.offsetX = 1;
                this.offsetY = 0;
                break;
            case E2W:
                this.startX = 9;
                this.startY = -1;
                this.endX = 0;
                this.endY = -1;
                this.offsetX = -1;
                this.offsetY = 0;
                break;
        }

        Log.i(TAG, "...creating a new player....");

        if (direction == Direction.N2S || direction == Direction.S2N) {
            this.pieces[0] = new Piece(this, 1, startY, ShapeType.TRIANGLE);
            this.pieces[1] = new Piece(this, 2, startY, ShapeType.SQUARE);
            this.pieces[2] = new Piece(this, 3, startY, ShapeType.DIAMOND);
            this.pieces[3] = new Piece(this, 4, startY, ShapeType.CIRCLE);
            this.pieces[4] = new Piece(this, 5, startY, ShapeType.TRIANGLE);
            this.pieces[5] = new Piece(this, 6, startY, ShapeType.SQUARE);
            this.pieces[6] = new Piece(this, 7, startY, ShapeType.DIAMOND);
            this.pieces[7] = new Piece(this, 8, startY, ShapeType.CIRCLE);
        } else {
            this.pieces[0] = new Piece(this, startX, 1, ShapeType.TRIANGLE);
            this.pieces[1] = new Piece(this, startX, 2, ShapeType.SQUARE);
            this.pieces[2] = new Piece(this, startX, 3, ShapeType.DIAMOND);
            this.pieces[3] = new Piece(this, startX, 4, ShapeType.CIRCLE);
            this.pieces[4] = new Piece(this, startX, 5, ShapeType.TRIANGLE);
            this.pieces[5] = new Piece(this, startX, 6, ShapeType.SQUARE);
            this.pieces[6] = new Piece(this, startX, 7, ShapeType.DIAMOND);
            this.pieces[7] = new Piece(this, startX, 8, ShapeType.CIRCLE);

        }

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

    public boolean isHuman() {
        return this.type == PlayerType.HUMAN;
    }

    public Drawable getImage() {
        return image;
    }

    public void setImage(Drawable image) {
        this.image = image;
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

    public TeamColour getColour() {
        return colour;
    }

    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }

    public boolean inEndZone(int x, int y) {
        return ((this.direction == Direction.E2W && this.endX == x) ||
                (this.direction == Direction.W2E && this.endX == x) ||
                (this.direction == Direction.N2S && this.endY == y) ||
                (this.direction == Direction.S2N && this.endY == y) );
    }

    public boolean allInEndZone() {
        for (int i=0; i<8; i++) {
            if (!this.pieces[i].getFinished()) {
                return false;
            }
        }
        this.finished = true;
        return true;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    void logDetails() {
        String msg;
        msg = "Player:" + id;
        msg = msg + "/Type:" + this.type;
        msg = msg + "/Colour:" + this.colour;
        msg = msg + "/Direction:" + this.direction;
        msg = msg + "/StartX:" + this.startX;
        msg = msg + "/StartY:" + this.startY;
        msg = msg + "/EndX:" + this.endX;
        msg = msg + "/EndY:" + this.endY;
        msg = msg + "/Started:" + this.started;
        msg = msg + "/Finished:" + this.finished;
        Log.i(TAG, msg);
    }

    public void jugglePieces() {
        Random r = new Random();
        int ri_1, ri_2;

        // perform EIGHT random piece swaps for each COMPUTER player
        for (int i = 0; i < 8; i++) {
            // get the first random integer
            ri_2 = 0;
            ri_1 = r.nextInt(8);
            Boolean ri_same = true;
            while (ri_same) {  // loop until a second random one is different
                ri_2 = r.nextInt(8);
                if (ri_1 != ri_2) {
                    ri_same = false;
                }
            }
            // swap pieces at locations [ri_1,startY] and [ri_2,startY]
            int tmp_x = this.pieces[ri_1].getPosX();
            int tmp_y = this.pieces[ri_2].getPosY();
            this.pieces[ri_1].setPosX(this.pieces[ri_2].getPosX());
            this.pieces[ri_1].setPosX(this.pieces[ri_2].getPosX());
            this.pieces[ri_2].setPosX(tmp_x);
            this.pieces[ri_2].setPosY(tmp_y);

        }
    }
}