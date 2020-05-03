package com.spc.traverse;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

/**
 * Created by tcottz7 on 27/08/15.
 * This displays the Traverse board...
 */
public class BoardDisplay extends Activity {


    public static final String TAG = "BOARD_DISPLAY";
    public static final int INVALID  = -1;
    public static final int OCCUPIED = 0;
    public static final int EMPTY    = 1;

    public String msg;
    public int maxRows = 10;
    public int maxCols = 10;
    public int maxPlayers = 4;
    public Squares[][] board = new Squares[maxRows][maxCols];
    public Squares moveToSquare = null;
    public Squares moveFromSquare = null;
    public Player[] player = new Player[4];
    public int current_player;
    ArrayList<Move> legalMoves = new ArrayList<>();
    public Move best_comp_move;

    public TextView tv_msg1, tv_msg2;
    public ImageButton btn_finished_moving_image;
    GridLayout gl_board;
    Boolean newgame = true;
    Boolean pre_game = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board_display);
        Log.i(TAG, "Squares display complete, rebuilding gridlayout");

        // Create and display the board
        createBoardGridLayout();

        Log.i(TAG, "...getting the extras....");

        int numplayers = 1;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            newgame = extras.getBoolean("newgame", true);
            numplayers = extras.getInt("players", 1);
        }

        tv_msg1 = findViewById(R.id.tv_msg1);
        tv_msg2 = findViewById(R.id.tv_msg2);
        gl_board = findViewById(R.id.board);
        btn_finished_moving_image = findViewById(R.id.finished_moving_image);

        if (newgame) {
            pre_game = true;
            current_player = 0;
            tv_msg1.setText(getString(R.string.pregame));
            tv_msg2.setText(getString(R.string.whenready));

            player[0] = new Player(0, TeamColour.RED, Direction.W2E);
            player[0].setImage(getResources().getDrawable(R.drawable.button_red));
            player[1] = new Player(1, TeamColour.BLUE, Direction.N2S);
            player[1].setImage(getResources().getDrawable(R.drawable.button_blue));
            player[2] = new Player(2, TeamColour.GREEN, Direction.E2W);
            player[2].setImage(getResources().getDrawable(R.drawable.button_green));
            player[3] = new Player(3, TeamColour.YELLOW, Direction.S2N);
            player[3].setImage(getResources().getDrawable(R.drawable.button_yellow));

            for (int i = 0; i < maxPlayers; i++) {
                if (i < numplayers) {
                    player[i].setType(PlayerType.HUMAN);                 // tag the HUMAN players
                } else {
                    player[i].jugglePieces();       // juggle COMPUTER pieces
                    player[i].setStarted(true);    // COMPUTER player ready to play!
                }
                paintPieces(player[i]);
            }

            btn_finished_moving_image.setImageDrawable(player[current_player].getImage());
            resetBoardClickables();
        }
    }

    public void paintPieces(Player player) {
        for (int p = 0; p < 8; p++) {
            // get the piece position
            int x = player.pieces[p].getPosX();
            int y = player.pieces[p].getPosY();
            // get the ImageButton at that position, and give it the right piece image to display
            board[x][y].getSqImageButton().setImageResource(player.pieces[p].piecesImageResource);
            board[x][y].setSqImageResource(player.pieces[p].piecesImageResource);
            // set square state attributes
            board[x][y].setOccupied(player.pieces[p]);
            // set square image-clickable attributes depending on HUMAN or COMPUTER player
            if (player.isHuman()) {
                board[x][y].getSqImageButton().setClickable(true);
            } else {
                board[x][y].getSqImageButton().setClickable(false);
            }
        }
    }

    public void resetBoardClickables() {
        for (int y = 0; y < maxRows; y++) {
            for (int x = 0; x < maxCols; x++) {
                if ( board[x][y].isOccupied()  &&          // IF it it occupied
                    (board[x][y].piece.player.isHuman()) && // AND it is human
                    (board[x][y].piece.player.getId() == current_player) && // AND it is current player
                    (!board[x][y].piece.getFinished())) {   // AND it is not in the endzone
                    board[x][y].getSqImageButton().setClickable(true);
                } else {
                    board[x][y].getSqImageButton().setClickable(false);
                }
            }
        }
    }

    public void createBoardGridLayout() {

        int sqSize = getSquareSize();
        int sqPadding = sqSize / 10;
        Log.i(TAG, "using square size " + sqSize + " (padding " + sqPadding + ") for GridLayout...");

        final GridLayout boardGridLayout = findViewById(R.id.board);
        boardGridLayout.removeAllViews();
        boardGridLayout.setRowCount(maxRows);
        boardGridLayout.setColumnCount(maxCols);

        for (int y = 0; y < maxRows; y++) {
            for (int x = 0; x < maxCols; x++) {

                // Create a new 'square' object to hold the basic details
                board[x][y] = new Squares(x, y);

                // Create the imagebutton for this square
                ImageButton newButton = new ImageButton(this);
                newButton.setId(board[x][y].getSqButtonId());
                newButton.setContentDescription(x + "/" + y);
                newButton.setAdjustViewBounds(true);
                newButton.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                newButton.setPadding(sqPadding, sqPadding, sqPadding, sqPadding);
                newButton.setMaxHeight(sqSize); // or height
                newButton.setMaxWidth(sqSize);
                newButton.setMinimumHeight(sqSize); // or height
                newButton.setMinimumWidth(sqSize);
                newButton.setBackgroundResource(board[x][y].sqBackgroundResource);
                // set everything non-clickable to start with, HUMAN pieces will clickable
                newButton.setClickable(false);  // and LEGALMOVE squares will become clickable
                if (board[x][y].getSqType() == SquareType.CORNER) {
                    //Log.i(TAG, "not doing the CORNER square image yet");
                    newButton.setImageResource(board[x][y].getSqImageResource());
                } else {
                    //Define the handler when this specific image is clicked (except CORNERS)
                    newButton.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            int id = v.getId(); // you get ID of your dynamic image
                            // CharSequence sqDesc = v.getContentDescription();
                            // Toast.makeText(getApplicationContext(), "Button ID "+id+" pressed: "+sqDesc, Toast.LENGTH_SHORT).show();
                            squareClicked(id);
                        }
                    });
                }

                //Add it to the gridlayout
                boardGridLayout.addView(newButton);
                // Store the ImageButton reference for the square
                board[x][y].setSqImageButton(newButton);
                board[x][y].getSqImageButton().setClickable(false);

            }
        }
    }

    public void finishedMoving(View v) {

        if (pre_game) {     // image indicates HUMAN finished juggling
            // Setup PRE-GAME for next player
            Log.i(TAG, "Finishing PRE-GAME for player "+current_player);
            player[current_player].setStarted(true);
            getNextPlayerTurn();
        }

        if (pre_game &&   // Check if everyone ready to start
                player[0].isStarted() &&
                player[1].isStarted() &&
                player[2].isStarted() &&
                player[3].isStarted()) {
            // OK, all players are ready...
            Log.i(TAG, "Finished PRE-GAME for all players");
            pre_game = false;
            tv_msg1.setText(getString(R.string.gameon));

            // bit of a fudge to set to YELLOW player, so that next section moves to RED!
            current_player = 3;
        }

        if (!pre_game) {    // Set up next player to move
            getNextPlayerTurn();    // moves until next human
            msg = "Player " + player[current_player].getColour() + " to move...";
            tv_msg2.setText(msg);
        }

        // clean up the moveTo/From squares and stop flashing if hit button in middle of move
        if (moveFromSquare!= null) {
            moveFromSquare.highlightSquare(false);
            if (moveToSquare != null) moveToSquare.highlightSquare(false);
            resetLegalMoves();  // clear out the current list
            moveFromSquare = null;
            moveToSquare = null;
            if (pre_game) tv_msg2.setText(getString(R.string.whenready));
        }

        resetBoardClickables();

    }

    public void makeComputerPlayerMove() {
        msg = "Computer Player " + player[current_player].getColour() + " moved";
        tv_msg2.setText(msg);
        // pick a random piece
        Piece comp_piece;
        Random r = new Random();
        do {
            //pick a random piece, but ensure still in play
            comp_piece = player[current_player].pieces[r.nextInt(8)];
            comp_piece.logDetails();
        }
        while (comp_piece.getFinished());

        Squares actionSquare = board[comp_piece.posX][comp_piece.posY];
        // now get best move for that piece
        legalMoves = new ArrayList<>();
        getLegalJumpsFrom(actionSquare);
        if (legalMoves.isEmpty()) {
            // errr....
            tv_msg2.setText(getString(R.string.novalidmoves));
            Log.i(TAG, "no valid moves for moveFromSquare");
        } else {
            // light selected up
            actionSquare.highlightSquare(true);
            moveFromSquare = actionSquare;  // store it as "FROM" square

            // find best move in array...
            best_comp_move = legalMoves.get(0);  // assume first for now
            int best_distance = legalMoves.get(0).distance;
            for (Move move : legalMoves ) {
                // TODO - FIND THE BEST MOVE! JUST TAKE biggest distance for now
                // but really need to account for beneficial direction
                if (move.distance > best_distance && move.goodDirection(player[current_player].direction)) {
                    best_distance = move.distance;
                    best_comp_move = move;
                }
            }


            moveToSquare = board[best_comp_move.toX][best_comp_move.toY];
            performMove(moveFromSquare, moveToSquare);

            // clean up the moveTo/From squares and stop flashing
            if (moveFromSquare!= null) {moveFromSquare.highlightSquare(false);}
            if (moveToSquare != null) {moveToSquare.highlightSquare(false);}
            resetLegalMoves ();  // clear out the current list
            moveFromSquare = null;
            moveToSquare = null;
        }

    }

    public void getNextPlayerTurn() {
        if (current_player + 1 == maxPlayers) {
            current_player = 0;
        } else {
            current_player = current_player + 1;
        }
        btn_finished_moving_image.setImageDrawable(player[current_player].getImage());
        if (moveFromSquare != null) moveFromSquare.highlightSquare(false);
        if (moveToSquare != null) moveToSquare.highlightSquare(false);
        resetBoardClickables();
        resetLegalMoves();  // clear out the current list
        moveFromSquare = null;
        moveToSquare = null;
        if (pre_game) tv_msg2.setText(getString(R.string.whenready));

        // If a COMPUTER player then make the move automatically...
        if (!pre_game) {
            while (player[current_player].getType() == PlayerType.COMPUTER && !player[current_player].isFinished()) {
                tv_msg2.setText(getResources().getString(R.string.computer_move, player[current_player].getColour()));
                makeComputerPlayerMove();
                getNextPlayerTurn();
            }
        }
    }

    public void squareClicked(int id) {

        Squares actionSquare = sqFindById(id);
        boolean continue_move = true;
        actionSquare.logDetails();  // log info of square pressed

        if (moveFromSquare == null) {

            // clear any existing legalmove trail, get fresh legalmoves, and flash valid target squares
            legalMoves = new ArrayList<>();
            getLegalJumpsFrom(actionSquare);   // also lights up options
            if (legalMoves.isEmpty()) {
                // errr....
                tv_msg2.setText(getString(R.string.novalidmoves));
                Log.i(TAG, "no valid moves for moveFromSquare");
            } else {
                // light selected up
                actionSquare.highlightSquare(true);
                moveFromSquare = actionSquare;  // store it as "FROM" square
                tv_msg2.setText(getString(R.string.selectsquare));
            }
        } else {
            // Already have from-square...
            if (moveFromSquare.getSqButtonId() == actionSquare.getSqButtonId()) {
                // Pressed the same image again, so cancel from-selection & stop-flashing
                Log.i(TAG, "same image pressed, cancelling moveFromSquare");
                if (pre_game) {tv_msg2.setText(getString(R.string.whenready));}
            } else {

                // Now try to perform the move...
                moveToSquare = actionSquare;
                performMove(moveFromSquare, moveToSquare);
                continue_move = true;
                // If not in PRE-GAME then check if any more moves?
                if (!pre_game) {
                    if ((Math.abs(moveFromSquare.getPosX()-moveToSquare.getPosX()) < 2) &&
                            (Math.abs(moveFromSquare.getPosY()-moveToSquare.getPosY()) < 2)) {
                        continue_move = false;
                    } else {
                        // TODO is there another valid jump for this one?
                        getAnotherJump(moveToSquare);
                    }
                }
            }
            // clean up the moveTo/From squares and stop flashing
            if (moveFromSquare!= null) {moveFromSquare.highlightSquare(false);}
            if (moveToSquare != null) {moveToSquare.highlightSquare(false);}
            resetBoardClickables();
            resetLegalMoves ();  // clear out the current list
            moveFromSquare = null;
            moveToSquare = null;
            if (!continue_move) getNextPlayerTurn();
            if (pre_game) {tv_msg2.setText(getString(R.string.whenready));}
        }
    }


    public void getAnotherJump (Squares startSquare) {
        // TODO - just printing details for now...
        Log.i(TAG,"TODO: Check if more moves for:");
        startSquare.logDetails();
    }

    public void performMove(Squares fromSquare, Squares toSquare) {
        String msg;
        int fromX = fromSquare.getPosX();
        int fromY = fromSquare.getPosY();
        int toX = toSquare.getPosX();
        int toY = toSquare.getPosY();

        msg = "Checking move from ("+fromX+","+fromY+") to ("+toX+","+toY+")";
        Log.i(TAG, msg);
        Log.i(TAG, "BEFORE:FROM");
                board[fromX][fromY].logDetails();
        Log.i(TAG, "BEFORE:TO");
                board[toX][toY].logDetails();

        // assume move valid - swap pieces on the actual board
        int tmp_sq_ir = fromSquare.getSqImageResource();
        Piece tmp_sq_p = fromSquare.piece;

        Log.i(TAG, "TEMP PIECE");
        tmp_sq_p.logDetails();

        board[fromX][fromY].setSqImageResource(toSquare.getSqImageResource());
        board[fromX][fromY].getSqImageButton().setImageResource(toSquare.getSqImageResource());
        board[fromX][fromY].setOccupied(toSquare.piece);
        if (board[fromX][fromY].piece !=null) { // might be unoccupied
            board[fromX][fromY].piece.setPosX(fromX);
            board[fromX][fromY].piece.setPosY(fromY);
        }


        board[toX][toY].setSqImageResource(tmp_sq_ir);
        board[toX][toY].getSqImageButton().setImageResource(tmp_sq_ir);
        board[toX][toY].setOccupied(tmp_sq_p);
        if (board[toX][toY].piece !=null) { // might be unoccupied
            board[toX][toY].piece.setPosX(toX);
            board[toX][toY].piece.setPosY(toY);
        }

        Log.i(TAG, "AFTER:FROM");
        board[fromX][fromY].logDetails();
        Log.i(TAG, "AFTER:TO");
        board[toX][toY].logDetails();

        // should force the board display to repaint...
        gl_board.invalidate();

        // Check if this piece ends up in EndZone for the player
        if (board[toX][toY].piece.getPlayer().inEndZone(toX,toY)) {
            board[toX][toY].piece.setFinished(Boolean.TRUE);
            //check if this player has now all pieces in end-zone
            if (board[toX][toY].piece.getPlayer().allInEndZone()) {
                board[toX][toY].piece.getPlayer().setFinished(true);
                // TODO - probably more here...
                Log.i(TAG, "...we have a winner");
                Intent intent = new Intent(this, MessageDisplay.class);
                msg = "Congratulations!\n" + player[current_player].getColour() + " WINS!";
                Log.d(TAG,msg);
                intent.putExtra("Message", msg);
                startActivity(intent);
                //this.finish();
            }
        }
    }



    public Squares sqFindById(int sqButtonID) {
        for (int y = 0; y < maxRows; y++) {
            for (int x = 0; x < maxCols; x++) {
                if (board[x][y].getSqButtonId() == sqButtonID) {
                    return board[x][y];
                }
            }
        }
        // not found, so return null, and handle in caller (not good, but...)
        return null;
    }

    public int getSquareSize() {
        Point size = getDimensions();
        Log.i(TAG, "Dimensions of device are " + size.toString());
        return size.x > size.y ? (size.y / 10) : (size.x / 10);
    }

    public Point getDimensions() {
        Display display = ((WindowManager) Objects.requireNonNull(this.getSystemService(Context.WINDOW_SERVICE))).getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }

    // Populates the legalMoves array of valid moves from the square passed. If no such
    // jumps are possible, null is returned.
    public void getLegalJumpsFrom(Squares fromHere) {
        Direction direction = fromHere.piece.player.getDirection();    // ... and which way are they heading?
        ShapeType shape = fromHere.piece.getShape();        // ... and which shape is it?
        int posX = fromHere.getPosX();
        int posY = fromHere.getPosY();

        if (pre_game) {
            for (int i = 1; i < 9; i++) {// anything in the same base-section is valid (avoids CORNERS)
                if ((direction == Direction.E2W || direction == Direction.W2E) && (i != posY)) {
                    legalMoves.add(new Move(posX, posY, posX, i)); // avoid same Y
                    board[posX][i].highlightChoice(true);
                }
                if ((direction == Direction.N2S || direction == Direction.S2N) && (i != posX)) {
                    legalMoves.add(new Move(posX, posY, i, posY)); // avoid same X
                    board[i][posY].highlightChoice(true);
                }
            }
            return;
        }

        switch (shape) {
            case CIRCLE:
                getLegalJumpsDirection(posX, posY, -1, +1);  // SW
                getLegalJumpsDirection(posX, posY, -1, -1);  // NW
                getLegalJumpsDirection(posX, posY, +1, +1);  // SE
                getLegalJumpsDirection(posX, posY, +1, -1);  // NE
                getLegalJumpsDirection(posX, posY, +1,  0);  // E
                getLegalJumpsDirection(posX, posY, -1,  0);  // W
                getLegalJumpsDirection(posX, posY,  0, +1);  // S
                getLegalJumpsDirection(posX, posY,  0, -1);  // N
                break;
            case DIAMOND:
                getLegalJumpsDirection(posX, posY, -1, +1);  // SW
                getLegalJumpsDirection(posX, posY, -1, -1);  // NW
                getLegalJumpsDirection(posX, posY, +1, +1);  // SE
                getLegalJumpsDirection(posX, posY, +1, -1);  // NE
                break;
            case SQUARE:
                getLegalJumpsDirection(posX, posY, +1,  0);  // E
                getLegalJumpsDirection(posX, posY, -1,  0);  // W
                getLegalJumpsDirection(posX, posY,  0, +1);  // S
                getLegalJumpsDirection(posX, posY,  0, -1);  // N
                break;
            case TRIANGLE:
                // do something special depending on player direction
                if (direction == Direction.E2W || direction == Direction.N2S)
                    getLegalJumpsDirection(posX, posY, -1, +1);  // SW
                if (direction == Direction.E2W || direction == Direction.S2N)
                    getLegalJumpsDirection(posX, posY, -1, -1);  // NW
                if (direction == Direction.W2E || direction == Direction.N2S)
                    getLegalJumpsDirection(posX, posY, +1, +1);  // SE
                if (direction == Direction.W2E || direction == Direction.S2N)
                    getLegalJumpsDirection(posX, posY, +1, -1);  // NE
                if (direction == Direction.E2W)
                    getLegalJumpsDirection(posX, posY, +1,  0);  // E
                if (direction == Direction.W2E)
                    getLegalJumpsDirection(posX, posY, -1,  0);  // W
                if (direction == Direction.S2N)
                    getLegalJumpsDirection(posX, posY,  0, +1);  // S
                if (direction == Direction.N2S)
                    getLegalJumpsDirection(posX, posY,  0, -1);  // N
                break;
        }

        Log.i(TAG,"LEGALMOVES - ending getLegalJumpsFrom:"+legalMoves.toString());
        Log.i(TAG,"LEGALMOVES - SIZE="+legalMoves.size());
        Log.i(TAG,"LEGALMOVES - EMPTY="+legalMoves.isEmpty());

    }  // end getLegalJumpsFrom()

    void getLegalJumpsDirection(int posX, int posY, int incX, int incY) {
        // array of integers representing the square states in the direction of travel
        int[] squareState = new int[9];
        // Log.i(TAG, "Trajectory = ["+incX+","+incY+"]");
        for (int i=1; i<9; i++) {
            squareState[i] = squareState(posX+(i*incX),posY+(i*incY));
            //Log.i(TAG, "squareState[" + i + "]=" + squareState[i]);
        }

        if (squareState[1]==EMPTY) {
            legalMoves.add(new Move(posX, posY, incX, incY, 1));
            board[posX+(incX)][posY+(incY)].highlightChoice(true);
        }

        if (squareState[1]==OCCUPIED &&
            squareState[2]==EMPTY) {
            legalMoves.add(new Move(posX, posY, incX, incY, 2));
            board[posX+(2*incX)][posY+(2*incY)].highlightChoice(true);
        }

        if (    squareState[1]==EMPTY &&
                squareState[2]==OCCUPIED &&
                squareState[3]==EMPTY &&
                squareState[4]==EMPTY) {
            legalMoves.add(new Move(posX, posY, incX, incY, 4));
            board[posX+(4*incX)][posY+(4*incY)].highlightChoice(true);
        }

        if (    squareState[1]==EMPTY &&
                squareState[2]==EMPTY &&
                squareState[3]==OCCUPIED &&
                squareState[4]==EMPTY &&
                squareState[5]==EMPTY &&
                squareState[6]==EMPTY) {
            legalMoves.add(new Move(posX, posY, incX, incY, 6));
            board[posX+(6*incX)][posY+(6*incY)].highlightChoice(true);
        }

        if (    squareState[1]==EMPTY &&
                squareState[2]==EMPTY &&
                squareState[3]==EMPTY &&
                squareState[4]==OCCUPIED &&
                squareState[5]==EMPTY &&
                squareState[6]==EMPTY &&
                squareState[7]==EMPTY &&
                squareState[8]==EMPTY) {
            legalMoves.add(new Move(posX, posY, incX, incY, 8));
            board[posX+(8*incX)][posY+(8*incY)].highlightChoice(true);
        }
    }

    private int squareState(int x2, int y2) {
        // check if actually on the board
        if (x2 < 0 || x2 > 9 || y2 < 0 || y2 > 9) return INVALID;  // off the board
        if (board[x2][y2].isCorner())             return INVALID;  // in the corner
        if (board[x2][y2].isOccupied())           return OCCUPIED; // already contains a piece.

        // TODO - prevents any first jump into a BASE square (except the players end row/col)
        if (board[x2][y2].isBase() &&       // in a base square, but not my END row/col
                !board[x2][y2].isPlayerEndBase(player[current_player].direction)) return INVALID;
        return EMPTY; //  empty
    }  // end canJump()

    void resetLegalMoves () {
        int x, y;
        for (int i=0; i < legalMoves.size(); i++) {
            x = legalMoves.get(i).toX;
            y = legalMoves.get(i).toY;
            Log.i(TAG, "resetLegalMoves ["+i+"]- ("+x+","+y+") is being reset");
            board[x][y].highlightChoice(false);
        }
    }// resetLegalMoves

}
