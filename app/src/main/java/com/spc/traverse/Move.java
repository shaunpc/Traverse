package com.spc.traverse;

/**
 * MOVE - object containing a valid move
 * Created by shaun on 14/05/17.
 */

class Move {
    int fromX, fromY;
    private int incX, incY;
    int toX, toY;
    private int mult;
    Direction direction;
    int distance;

    // initiation with incremental direction and multiplier
    Move (int fromX, int fromY, int incX, int incY, int mult) {

        // DEFAULT BASIC ATTRIBUTES
        this.fromX = fromX;
        this.incX = incX;
        this.fromY = fromY;
        this.incY = incY;
        this.mult = mult;

        // CALCULATED ATTRIBS
        this.toX = fromX + (mult*incX);
        this.toY = fromY + (mult*incY);
        this.distance = mult;
        // this.direction = direction;

    }

    // initiation with specific destination - mainly for pre-game
    Move (int fromX, int fromY, int toX, int toY) {

        // DEFAULT BASIC ATTRIBUTES
        this.fromX = fromX;
        this.incX = 0;
        this.fromY = fromY;
        this.incY = 0;
        this.mult = 0;
        this.toX = toX;
        this.toY = toY;
        this.distance = Math.max(Math.abs(fromX - toX),Math.abs(fromY - toY));
        // this.direction = direction;

    }

    boolean goodDirection(Direction direction) {

        boolean result = false;

        switch (direction) {
            case E2W:
                result = this.toX < this.fromX;
                break;
            case W2E:
                result = this.toX > this.fromX;
                break;
            case N2S:
                result = this.toY > this.fromY;
                break;
            case S2N:
                result = this.toY < this.fromY;
                break;
        }

        return result;
    }

    String log() {
        return "Move from " + this.fromX + "," + this.fromY + " to " + this.toX + "," + this.toY + " (dist=" + this.distance + ")";
    }
}
