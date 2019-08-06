package com.torzini.tictactoeapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * https://www.youtube.com/watch?v=9nVSYkQoV5I
 *
 * 1. take care of orientation to make sure text stays the same
 *      add to the xml TextViews and the 9 numbered btns:
 *          android:freezesText="true"
 * 2. Come back here:
 *      override onSaveInstanceState(Bundle outState) method
 *
 * 3. Override onRestoreInstanceState(Bundle savedInstanceState)
 *       to get back the saved state after orientation change
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //1. create 2D array btns
    private Button[][] buttons = new Button[3][3];

    private boolean player1Turn = true;

    private int roundCount;

    private int player1Points;
    private int player2Points;

    TextView textViewPlayer1;
    TextView textViewPlayer2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //2 ref the tvs
        textViewPlayer1 = findViewById(R.id.tv_p1);
        textViewPlayer2 = findViewById(R.id.tv_p2);

        //3. assign our btn array in a for loop
        // we want to go 3 rounds so i<3
        for (int i = 0; i < 3; i++) {
            // put another for loop
            for (int j = 0; j < 3; j++) {
                // assign our btns
                // loop thru the rows and colns in our 2D array
                String buttonID = "btn_" + i + j;

                // get the resource ID
                int resID = getResources().getIdentifier(buttonID, "id", getPackageName());

                //we usually do findViewById(R.id....) but here we do if dynamically with resID
                // this way we get refs to all our btns without doing them one by one
                buttons[i][j] = findViewById(resID);
                buttons[i][j].setOnClickListener(this);
            }
        }

        //5. assign the reset btn
        Button buttonReset = findViewById(R.id.btn_reset);
        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //vid 4.1
                resetGame();
            }
        });
    }

    //4. auto created when the class implemented onClick listener
    @Override
    public void onClick(View view) {

        // 6. Check if btns have been used
        // if btn is not empty, then it is used, return
        if (!((Button) view).getText().toString().equals("")) {
            return;
        }

        //7. check for player1Turn
        if (player1Turn) {
            ((Button) view).setText("X"); // if player 1, set text to X
        } else {
            ((Button) view).setText("O"); // if not player 1(that is player 2), then set text to 0
        }

        //8. increment round count
        roundCount++;

        // 10 who wins
        if (checkForWin()) {
            if (player1Turn) { // if player1Turn
                player1Wins();
            }
            else { // if player2Turn
                player2Wins();
            }
        }
        else if (roundCount == 9) { // 9 counts finish, then it's a draw
            draw();
        }
        else { // if not a draw
            player1Turn = !player1Turn; // switch turns
        }
    }

    // 9. method to handle checks for win
    private boolean checkForWin() {
        String[][] field = new  String[3][3];

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                field[i][j] = buttons[i][j].getText().toString();
            }
        }

        // check the rows: that the fields have the same letters and not empty
        for (int i = 0; i < 3; i++) {
            if (field[i][0].equals(field[i][1])  //compare the neighboring fields have same letters
            && field[i][0].equals(field[i][2])
            && !field[i][0].equals("")){ // and that the fields are not empty
                return true;

            }
        }

        // check the columns: that the fields have the same letters and not empty
        for (int i = 0; i < 3; i++) {
            if (field[0][i].equals(field[1][i])  //compare the neighboring fields have same letters
                    && field[0][i].equals(field[2][i])
                    && !field[0][i].equals("")){ // and that the fields are not empty
                return true;
            }
        }

        // check the diagonal left to right
        if (field[0][0].equals(field[1][1])  //compare the neighboring fields have same letters
                && field[0][0].equals(field[2][2])
                && !field[0][0].equals("")){ // and that the fields are not empty
            return true;
        }

        // check the diagonal right to left
        if (field[0][2].equals(field[1][1])  //compare the neighboring fields have same letters
                && field[0][2].equals(field[2][0])
                && !field[0][2].equals("")){ // and that the fields are not empty
            return true;
        }

        return false; // if none of the above is true, then we don't have a winner yet
    }

    //vid 3.3
    private void draw() {

        // increment player1 points
        Toast.makeText(this, "It's a Draw!", Toast.LENGTH_LONG).show();
        resetBoard();  // reset the board for a new play
    }

    //vid 3.2
    private void player2Wins() {
        // increment player2 points
        player2Points++;
        Toast.makeText(this, "Player 2 wins!", Toast.LENGTH_LONG).show();
        updatePointsText();  // update the points
        resetBoard();  // reset the board for a new play
    }

    //vid 3.1
    private void player1Wins() {
        // increment player1 points
        player1Points++;
        Toast.makeText(this, "Player 1 wins!", Toast.LENGTH_LONG).show();
        updatePointsText();  // update the points
        resetBoard();  // reset the board for a new play
    }
    //vid 3.4
    private void updatePointsText() {

        //show the points of the players
        textViewPlayer1.setText("Player 1: " + player1Points);
        textViewPlayer2.setText("Player 2: " + player2Points);
    }

    //vid 3.5
    private void resetBoard() {

        // reset all the btns to empty string
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText(""); // set the btns to null
            }
        }

        roundCount = 0;  // set the num of rounds to 0
        player1Turn = true; // player 1 starts again
    }

    //vid 4.2
    private void resetGame() {
        player1Points = 0;
        player2Points = 0;
        updatePointsText();
        resetBoard();

    }

    //vid 4.4 override onSaveInstanceState(Bundle outState)
    // to save the state
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("roundCount", roundCount);         // save the round count
        outState.putInt("player1Points", player1Points);    // save the player 1 points
        outState.putInt("player2Points", player2Points);    // save the player 2 points
        outState.putBoolean("player1Turn", player1Turn);    // save the player 1 turn
    }

    //4.5 get saved values after orientation change
    // Override onRestoreInstanceState(Bundle savedInstanceState)
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        roundCount = savedInstanceState.getInt("roundCount");
        player1Points = savedInstanceState.getInt("player1Points");
        player2Points = savedInstanceState.getInt("player2Points");
        player1Turn = savedInstanceState.getBoolean("player1Turn");
    }

    /*@Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

    }*/
}
