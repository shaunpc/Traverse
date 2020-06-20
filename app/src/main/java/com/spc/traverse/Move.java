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
    float score;

    // initiation with incremental direction and multiplier
    Move (int fromX, int fromY, int incX, int incY, int mult, Direction ply_dir) {

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
        this.direction = ply_dir;

        this.score = calculateMoveScore();
    }

    // initiation with specific destination - for pre-game
    Move (int fromX, int fromY, int toX, int toY) {

        // DEFAULT BASIC ATTRIBUTES
        this.fromX = fromX;
        this.incX = 0;
        this.fromY = fromY;
        this.incY = 0;
        this.mult = 0;
        this.toX = toX;
        this.toY = toY;
        this.distance = 0;
        this.direction = null;
        this.score = 0f;
    }

    private float calculateMoveScore () {
        float calc = 0;

        // FACTORS

        switch (this.direction) {
            case E2W:
                calc += this.toX - this.fromX;                      // Forwards
                calc += 0.5 * Math.abs((this.toY - this.fromY));    // Sideways
                if (this.toX == 0) {calc += 1;}                     // End zone
                break;
            case W2E:
                calc += this.fromX - this.toX;                      // Forwards
                calc += 0.5 * Math.abs((this.toY - this.fromY));    // Sideways
                if (this.toX == 9) {calc += 1;}                     // End zone
                break;
            case N2S:
                calc += this.toY - this.fromY;                      // Forwards
                calc += 0.5 * Math.abs((this.toX - this.fromX));    // Sideways
                if (this.toY == 9) {calc += 1;}                     // End zone
                break;
            case S2N:
                calc += this.fromY - this.toY;                      // Forwards
                calc += 0.5 * Math.abs((this.toX - this.fromX));    // Sideways
                if (this.toY == 0) {calc += 1;}                     // End zone
                break;
        }

        return calc;
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
        return "move from " + this.fromX + "," + this.fromY + " to " + this.toX + "," + this.toY +
                " (dist=" + this.distance + "/score=" + Float.toString(this.score) +") " + this.direction ;
    }
}
