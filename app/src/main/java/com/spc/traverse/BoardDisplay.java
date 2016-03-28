package com.spc.traverse;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

/**
 * Created by tcottz7 on 27/08/15.
 * This displays the Traverse board...
 */
public class BoardDisplay extends Activity {


    public static final String TAG = "BOARD_DISPLAY";
    public int maxRows = 10;
    public int maxCols = 10;
    public int maxPlayers = 4;
    public Squares[][] board = new Squares[maxRows][maxCols];
    public Squares moveToSquare = null;
    public Squares moveFromSquare = null;
    public Player[] player = new Player[4];
    public Player current_player;

    //Create  arrays of X,Y coords for gathering the valid moves, upto 100
    public int maxValidMoveCount = 100;
    public ValidMoveCoords[] validMoves = new ValidMoveCoords[maxValidMoveCount];
    public int validMoveCount = 0;
    public TextView tv_msg1, tv_msg2;
    public ImageButton btn_red, btn_blue, btn_green, btn_yellow;
    Boolean pre_game = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board_display);
        Log.i(TAG, "Squares display complete, rebuilding gridlayout");

        createBoardGridLayout();

        Log.i(TAG, "...getting the extras....");
        String gametype = "invalid";
        int numplayers = 1;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            gametype = extras.getString("type");
            numplayers = extras.getInt("players", 1);
        }

        tv_msg1 = (TextView) findViewById(R.id.tv_msg1);
        tv_msg2 = (TextView) findViewById(R.id.tv_msg2);

        btn_red = (ImageButton) findViewById(R.id.button_red);
        btn_blue = (ImageButton) findViewById(R.id.button_blue);
        btn_green = (ImageButton) findViewById(R.id.button_green);
        btn_yellow = (ImageButton) findViewById(R.id.button_yellow);

        if (gametype.equals("new")) {
            tv_msg1.setText("PRE-GAME - adjust your starting positions!");
            tv_msg2.setText("Press your coloured button when ready to play!");

            player[0] = new Player(0, TeamColour.RED, Direction.W2E);
            player[0].setButton(btn_red);
            player[1] = new Player(1, TeamColour.BLUE, Direction.N2S);
            player[1].setButton(btn_blue);
            player[2] = new Player(2, TeamColour.GREEN, Direction.E2W);
            player[2].setButton(btn_green);
            player[3] = new Player(3, TeamColour.YELLOW, Direction.S2N);
            player[3].setButton(btn_yellow);


            for (int i = 0; i < maxPlayers; i++) {
                // tag the HUMAN players
                if (i<numplayers) {player[i].setType(PlayerType.HUMAN);}
                // tag the HUMAN players and remove buttons for COMPUTER players
                if (i>=numplayers) {player[i].getButton().setVisibility(View.GONE);}
                // display the pieces for this player
                paintPieces(player[i]);
                // Perform random base-line juggle for COMPUTER players
                if (i>=numplayers) {preGameComputerJuggle(player[i]);}

            }

        }


    }

    public void paintPieces (Player player) {

        for (int p = 0; p < 8; p++) {
            // get the piece position
            int x = player.pieces[p].getPosX();
            int y = player.pieces[p].getPosY();
            // get the ImageButton at that position, and give it the right piece image to display
            board[x][y].getSqImageButton().setImageResource(player.pieces[p].piecesImageResource);
            board[x][y].setSqImageResource(player.pieces[p].piecesImageResource);

            // set square state attributes
            board[x][y].shape=player.pieces[p].getShape();
            board[x][y].setOccupied(true);
            board[x][y].setTeamColour(player.colour);
            board[x][y].setDirection(player.direction);

            // set square state attributes depending on HUMAN or COMPUTER player
            if (player.type==PlayerType.HUMAN) {
                board[x][y].setHuman(true);
                board[x][y].setClickable(true);
            } else {
                board[x][y].setHuman(false);
                board[x][y].setClickable(false);
            }
            // enable or disable the square being pressed
            board[x][y].getSqImageButton().setClickable(board[x][y].getClickable());


        }
    };


    public void createBoardGridLayout() {

        int sqSize = getSquareSize();
        int sqPadding = sqSize / 10;
        Log.i(TAG, "using square size " + sqSize + " (padding " + sqPadding + ") for GridLayout...");

        final GridLayout boardGridLayout = (GridLayout) findViewById(R.id.board);
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
                newButton.setPadding(sqPadding,sqPadding, sqPadding, sqPadding);
                newButton.setMaxHeight(sqSize); // or height
                newButton.setMaxWidth(sqSize);
                newButton.setMinimumHeight(sqSize); // or height
                newButton.setMinimumWidth(sqSize);
                newButton.setBackgroundResource(board[x][y].sqBackgroundResource);
                newButton.setClickable(board[x][y].getClickable());
                if (board[x][y].getSqType() == SquareType.CORNER) {
                    //Log.i(TAG, "not doing the CORNER square image yet");
                    // newButton.setClickable(false);
                    newButton.setImageResource(board[x][y].sqImageResource);
                } else {
                    //Define the handler when this specific button is clicked
                    //newButton.setClickable(true);
                    newButton.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            int id = v.getId(); // you get ID of your dynamic button
                            CharSequence sqDesc = v.getContentDescription();
                            // Toast.makeText(getApplicationContext(), "Button ID "+id+" pressed: "+sqDesc, Toast.LENGTH_SHORT).show();
                            squareClicked(id);
                        }
                    });
                }

                //Add it to the gridlayout
                boardGridLayout.addView(newButton);
                // Store the ImageButton reference for the square
                board[x][y].setSqImageButton(newButton);

            }
        }
    }

    public void colouredButtonClick (View v) {
        int id = v.getId(); // get ID of dynamic button
        switch (id) {
            case R.id.button_red:  // RED player 0
                if (pre_game) {
                    player[0].setStarted(true);
                    player[0].getButton().setVisibility(View.INVISIBLE);
                }
                break;
            case R.id.button_blue:  // BLUE player 1
                if (pre_game) {
                    player[1].setStarted(true);
                    player[1].getButton().setVisibility(View.INVISIBLE);
                }
                break;
            case R.id.button_green:  // GREEN player 2
                if (pre_game) {
                    player[2].setStarted(true);
                    player[2].getButton().setVisibility(View.INVISIBLE);
                }
                break;
            case R.id.button_yellow:  // YELLOW player 3
                if (pre_game) {
                    player[3].setStarted(true);
                    player[3].getButton().setVisibility(View.INVISIBLE);
                }
                break;

        }

        if (pre_game &&   // Check if everyone ready to start
                player[0].isStarted() &&
                player[1].isStarted() &&
                player[2].isStarted() &&
                player[3].isStarted()) {
            // OK, all players are ready...
            pre_game = false;
            tv_msg1.setText("GAME ON!");

            // bit of a fudge to set to YELLOW player
            current_player = player[3];

        }

        if (!pre_game) {    // Set up next player to move
            current_player.getButton().setVisibility(View.INVISIBLE);
            current_player = getNextPlayerTurn (current_player.getId());

            // If a COMPUTER player then make the move automatically...
            while (current_player.getType()==PlayerType.COMPUTER) {
                tv_msg2.setText("Making computer move for " + current_player.colour + " player...");
                makeComputerPlayerMove(current_player);
                current_player = getNextPlayerTurn (current_player.getId());
            }

            tv_msg2.setText("Player " + current_player.colour + " to move...");
            current_player.getButton().setVisibility(View.VISIBLE);
        }
    }

    public void makeComputerPlayerMove (Player cp) {
        // TODO
        showToast("Computer moving "+cp.colour+"...");
    }

    public Player getNextPlayerTurn (int cp) {
        if (cp + 1 == maxPlayers) {
            return player[0];
        } else {
            return player[cp + 1];
        }
    }

    public void squareClicked(int id) {

        Squares actionSquare = sqFindById(id);
        ImageButton mButton = (ImageButton) findViewById(id);

        Log.i(TAG, actionSquare.logDetails());

        if (moveFromSquare == null) {
            // Looks like it is the first square to be pressed this turn...
            // make sure this first selection actually has a piece on it
            if (!board[actionSquare.getPosX()][actionSquare.getPosY()].getOccupied()) {
                showToast("Select an occupied square to begin move");
                return;
            }
            // make sure it is one of the current HUMAN players pieces
            if (!board[actionSquare.getPosX()][actionSquare.getPosY()].getHuman()) {
                showToast("Do not try to move a COMPUTER player piece");
                return;
            }
            // if open-game then ensure matches the current player colour
            if (!pre_game && board[actionSquare.getPosX()][actionSquare.getPosY()].getTeamColour()!= current_player.colour) {
                showToast("Select your own "+current_player.colour+ " team pieces");
                return;
            }

            // and if so, store as a valid from-square
            moveFromSquare = actionSquare;
            mButton.setPressed(true);
            mButton.setActivated(true);
            // set up an animation to flash button image
            Animation mAnimation = new AlphaAnimation(1, 0);
            mAnimation.setDuration(200);
            mAnimation.setInterpolator(new LinearInterpolator());
            mAnimation.setRepeatCount(Animation.INFINITE);
            mAnimation.setRepeatMode(Animation.REVERSE);
            mButton.startAnimation(mAnimation);

            actionSquare.setSelected(true);
            //Log.i(TAG, "new moveFromSquare [" + moveFromSquare.getPosX() + "," + moveFromSquare.getPosY() + "]");
            tv_msg2.setText("Select which square to move this piece to");

        } else {
            // Already have from-square...
            if (moveFromSquare.getSqButtonId() == actionSquare.getSqButtonId()) {
                // Pressed the same button again, so cancel from-selection & stop-flashing
                moveFromSquare.getSqImageButton().clearAnimation();
                moveFromSquare = null;
                mButton.setPressed(false);
                mButton.setActivated(false);
                actionSquare.setSelected(false);
                Log.i(TAG, "cancelled moveFromSquare");
                if (pre_game) {tv_msg2.setText("Press your coloured button when ready to play!");}

            } else {

                    moveToSquare = actionSquare;
                    mButton.setPressed(true);
                    mButton.setActivated(true);
                    actionSquare.setSelected(true);
                    // Log.i(TAG, "new moveToSquare [" + moveToSquare.getPosX() + "," + moveToSquare.getPosY() + "]");

                    // if open-game then make sure this second selection is not actually occupied
                    if (!pre_game && board[actionSquare.getPosX()][actionSquare.getPosY()].getOccupied()) {
                            showToast("You need to select an unoccupied square to move to");
                            return;
                    }

                    // Now try to perform the move...
                    performMove(moveFromSquare, moveToSquare);

                    // clean up the moveTo/From squares and stop flashing
                    moveFromSquare.getSqImageButton().clearAnimation();
                    moveToSquare.getSqImageButton().clearAnimation();
                    moveFromSquare = null;
                    moveToSquare = null;
                    if (pre_game) {tv_msg2.setText("Press your coloured button when ready to play!");}
            }
        }
    }


    public void preGameComputerJuggle(Player player) {
        // TODO
        Direction dir = player.getDirection();
        int startX = player.getStartX();  // for W2E and E2W
        int startY = player.getStartY();  // for N2S and S2N
        Random r = new Random();
        int ri_1, ri_2;

        // perform FOUR random piece swaps for each COMPUTER player
        for (int i = 0; i<4 ; i++) {
            // get the first random integer
            ri_2=0;
            ri_1 = r.nextInt(8)+1;
            Boolean ri_same = true;
            while (ri_same) {  // loop until a second random one is different
                ri_2 = r.nextInt(8) + 1;
                if (ri_1 != ri_2) {
                    ri_same = false;
                }
            }
            if (dir==Direction.N2S || dir==Direction.S2N) {
                // swap pieces at locations [ri_1,startY] and [ri_2,startY]
                performMove(board[ri_1][startY] , board[ri_2][startY]);
            }

            if (dir==Direction.W2E || dir==Direction.E2W) {
                // swap pieces at locations [startX, ri_1] and [startX, ri_2]
                performMove(board[startX][ri_1] , board[startX][ri_2]);
            }

        }
        // COMPUTER player ready to play!
        player.setStarted(true);
    }

    public void performMove(Squares fromSquare, Squares toSquare) {
        String msg;
        int fromX= fromSquare.getPosX();
        int fromY=fromSquare.getPosY();
        int toX=toSquare.getPosX();
        int toY=toSquare.getPosY();
        Direction dir=fromSquare.getDirection();

        msg = "Checking move from "+fromSquare.getShape()+ "[" + fromX + "," + fromY + "] to "+toSquare.getShape()+ "[" + toX + "," + toY + "]";
        Log.i(TAG, msg);

        Boolean valid_move = true;
        // First check it is valid
        if (pre_game) {
            if (dir==Direction.N2S && fromY!=toY) {valid_move=false;}
            if (dir==Direction.S2N && fromY!=toY) {valid_move=false;}
            if (dir==Direction.W2E && fromX!=toX) {valid_move=false;}
            if (dir==Direction.E2W && fromX!=toX) {valid_move=false;}
            if (!valid_move) {
                showToast("During pre-game you may only swap locations within your baseline");
                    return;
            }

        } else {
                // TODO - need to figure out how to determine if move valid....  let them move anywhere for now
                valid_move = isMoveValid(fromSquare, toSquare);
        }

        Log.i(TAG, "Valid Move : " + valid_move);

        if (valid_move) {
            //swap pieces on the actual board
            int tmp_sq_ir = fromSquare.getSqImageResource();
            ShapeType tmp_sq_s = fromSquare.getShape();
            boolean tmp_sq_o = fromSquare.getOccupied();
            TeamColour tmp_sq_tc = fromSquare.getTeamColour();
            boolean tmp_sq_h = fromSquare.getHuman();
            Direction tmp_sq_d = fromSquare.getDirection();

            board[fromX][fromY].setSqImageResource(toSquare.getSqImageResource());
            board[fromX][fromY].setShape(toSquare.getShape());
            board[fromX][fromY].getSqImageButton().setImageResource(toSquare.getSqImageResource());
            board[fromX][fromY].setOccupied(toSquare.getOccupied());
            board[fromX][fromY].setTeamColour(toSquare.getTeamColour());
            board[fromX][fromY].setHuman(toSquare.getHuman());
            board[fromX][fromY].setDirection(toSquare.getDirection());

            board[toX][toY].setSqImageResource(tmp_sq_ir);
            board[toX][toY].setShape(tmp_sq_s);
            board[toX][toY].getSqImageButton().setImageResource(tmp_sq_ir);
            board[toX][toY].setOccupied(tmp_sq_o);
            board[toX][toY].setTeamColour(tmp_sq_tc);
            board[toX][toY].setHuman(tmp_sq_h);
            board[toX][toY].setDirection(tmp_sq_d);
        }

    }

    public boolean isMoveValid (Squares from, Squares to) {
        ShapeType st = from.getShape();
        Direction dir = from.getDirection();


        // Array position ZERO is always the starting FROM square
        validMoveCount = 0;
        validMoves[validMoveCount] = new ValidMoveCoords(from.getPosX(), from.getPosY());

        populateValidMoves ();

        // TODO - what to return?
        return true;
    }

    public void getValidMoves_N () {
        // walk NORTH (decreasing Y) to find valid moves...
        while (y > 0)
    }

    public void populateValidMoves () {
        if (st == ShapeType.CIRCLE || st == ShapeType.SQUARE) {
            getValidMoves_N();
            getValidMoves_S();
            getValidMoves_W();
            getValidMoves_E();
        }

        if (st == ShapeType.CIRCLE || st == ShapeType.DIAMOND) {
            getValidMoves_NW();
            getValidMoves_NE();
            getValidMoves_SW();
            getValidMoves_SE();
        }

        if (st == ShapeType.TRIANGLE) {
            switch (dir) {
                case N2S:
                    getValidMoves_N();
                    getValidMoves_SW();
                    getValidMoves_SE();
                    break;
                case S2N:
                    getValidMoves_S();
                    getValidMoves_NW();
                    getValidMoves_NE();
                    break;
                case W2E:
                    getValidMoves_W();
                    getValidMoves_NE();
                    getValidMoves_SE();
                    break;
                case E2W:
                    getValidMoves_E();
                    getValidMoves_NW();
                    getValidMoves_SW();
                    break;
            }
        }
    }

    public Squares sqFindById(int sqButtonID) {
        for (int y = 0; y < maxRows; y++) {
            for (int x = 0; x < maxCols; x++) {

                if (board[x][y].getSqButtonId()==sqButtonID) {
                    return board[x][y];
                }
            }
        }
        // not found, so return null, and handle in caller (not good, but...)
        return null;
    }

    private void showToast(String msgToDisplay) {
        //Displaying Toast
        Context context = getApplicationContext();
        Toast toast = Toast.makeText(context, msgToDisplay, Toast.LENGTH_SHORT);
        toast.show();
    }

    public int getSquareSize() {
        Log.i(TAG, "determining the square size to use");
        Point size = getDimensions();
        Log.i(TAG, "...retrieved " + size.toString());
        return size.x > size.y ? (size.y / 10) : (size.x / 10);
    }

    public Point getDimensions() {
        Log.i(TAG, "attempting to get dimensions of device... ");
        Display display = ((WindowManager) this.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }
}
