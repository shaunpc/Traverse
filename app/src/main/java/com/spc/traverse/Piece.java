package com.spc.traverse;

import android.util.Log;

/**
 * Created by shaun on 14/10/15.
 * Object class to hold the position, shape and image of a piece
 */
public class Piece {

    private static final String TAG = "PIECE";

    Integer     posX;
    Integer     posY;
    ShapeType   shape;
    Player      player;
    Boolean     finished;
    int         piecesImageResource;

    public Piece(Player player, Integer posX, Integer posY, ShapeType shape) {
        this.posX = posX;
        this.posY = posY;
        this.shape = shape;
        this.player = player;
        this.finished = false;

        switch (shape) {
            case CIRCLE:
                if (player.getColour() == TeamColour.BLUE) {piecesImageResource = R.drawable.circle_blue;}
                if (player.getColour() == TeamColour.GREEN) {piecesImageResource = R.drawable.circle_green;}
                if (player.getColour() == TeamColour.YELLOW) {piecesImageResource = R.drawable.circle_yellow;}
                if (player.getColour() == TeamColour.RED) {piecesImageResource = R.drawable.circle_red;}
                break;
            case DIAMOND:
                if (player.getColour() == TeamColour.BLUE) {piecesImageResource = R.drawable.diamond_blue;}
                if (player.getColour() == TeamColour.GREEN) {piecesImageResource = R.drawable.diamond_green;}
                if (player.getColour() == TeamColour.YELLOW) {piecesImageResource = R.drawable.diamond_yellow;}
                if (player.getColour() == TeamColour.RED) {piecesImageResource = R.drawable.diamond_red;}
                break;
            case SQUARE:
                if (player.getColour() == TeamColour.BLUE) {piecesImageResource = R.drawable.square_blue;}
                if (player.getColour() == TeamColour.GREEN) {piecesImageResource = R.drawable.square_green;}
                if (player.getColour() == TeamColour.YELLOW) {piecesImageResource = R.drawable.square_yellow;}
                if (player.getColour() == TeamColour.RED) {piecesImageResource = R.drawable.square_red;}
                break;
            case TRIANGLE:
                if (player.getColour() == TeamColour.BLUE) {piecesImageResource = R.drawable.triangle_blue;}
                if (player.getColour() == TeamColour.GREEN) {piecesImageResource = R.drawable.triangle_green;}
                if (player.getColour() == TeamColour.YELLOW) {piecesImageResource = R.drawable.triangle_yellow;}
                if (player.getColour() == TeamColour.RED) {piecesImageResource = R.drawable.triangle_red;}
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

    public Player getPlayer() {
        return player;
    }

    public Boolean getFinished() {
        return finished;
    }

    public void setFinished(Boolean finished) {
        this.finished = finished;
    }

    void logDetails() {
        String msg;
        msg = "Piece at:["+this.posX+"," + this.posY+"]";
        msg = msg + "/Shape:"+this.shape;
        msg = msg + "/Player:"+this.player.id;
        msg = msg + "/Finished:"+this.finished;
        Log.i(TAG, msg);
        // if (this.player != null) {
        //    player.logDetails();
        // }
    }
}
