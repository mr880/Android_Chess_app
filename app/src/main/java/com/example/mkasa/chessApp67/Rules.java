package com.example.mkasa.chessApp67;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Michael Russo and Mustafa Kasabchy on 12/4/2017.
 */

public class Rules extends AppCompatActivity {

    Button undo, aI, draw, resign;
    Board gameBoard;
    protected String moveFrom;
    protected String moveTo;
    protected TextView origin = null;
    protected TextView destiny = null;
    protected String prevOrigin = null;
    protected String prevDestiny = null;
    protected TextView oldPieceSelected = null;
    public Undo undoPlay;
    public RecordMoves gameMoves;
    public GameActivity gameStream;
    protected ChessPiece prevPiece;
    protected String promotePawn;
    private boolean drawOf = false;

    public void onCreate(Bundle saveInstanceState) {

        super.onCreate(saveInstanceState);
        setContentView(R.layout.chess_board);
        try{
            GameActivity game;
            game = SaveData.readGames(this.getApplicationContext());
            if(game == null){
                this.gameStream = new GameActivity();
                SaveData.saveGames(this.gameStream, this.getApplicationContext());
            }else{
                this.gameStream = game;
            }
        }catch(Exception e){
            Log.d("Chess", e.getMessage());
        }
        gameMoves = new RecordMoves();
        undoPlay = new Undo();
        gameBoard = new Board();

        drawBoard();

        undo = (Button) findViewById(R.id.undoButton);
        undo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    undo();
                } catch (Exception e) {
                    Log.d("Chess", e.getMessage());
                }
            }
        });
        aI = (Button) findViewById(R.id.autoButton);
        aI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ArrayList<ChessPiece> pieces = new ArrayList<ChessPiece>();

                if (gameBoard.whiteMove) {
                    ArrayList<ChessPiece> whitePieces = gameBoard.whitePieces;
                    Collections.shuffle(whitePieces);
                    for (ChessPiece piece : whitePieces) {
                        if (piece != null && piece.getPosition().compareToIgnoreCase("null") != 0) {
                            pieces.add(piece);
                        }
                    }

                } else {
                    ArrayList<ChessPiece> blackPieces = gameBoard.blackPieces;
                    Collections.shuffle(blackPieces);
                    for (ChessPiece piece : blackPieces) {
                        if (piece != null && piece.getPosition().compareToIgnoreCase("null") != 0) {
                            pieces.add(piece);
                        }
                    }
                }

                boolean done = false;
                int g = 0;
                Collections.shuffle(pieces);
                while(!done && g<pieces.size()) {
                    ChessPiece piece = pieces.get(g);

                    String randomMove = randomMove(piece);
                    if (randomMove.compareToIgnoreCase("invalid") != 0) {


                        move(randomMove);
                        done = true;

                    }
                    g++;
                }
                if(!done){
                    showToast("There no valid moves");
                }

            }
        });

        draw = (Button) findViewById(R.id.drawButton);
        draw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = "You sure you want to end game in a Draw?";
                dialogMessage(message, 1);

            }


        });

        resign = (Button) findViewById(R.id.resignButton);
        resign.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                String message = "You sure you want to resign?";
                dialogMessage(message,2);
            }
        });

    }
    public void dialogMessage(String message, int caseNum){

        switch(caseNum){
            case 1:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(message)
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {


                                String playerDrawing;

                                if(gameBoard.whiteMove)
                                    playerDrawing = "White";
                                else
                                    playerDrawing= "Black";

                                String confirmation = playerDrawing+" Would you like to Draw?";
                                dialogMessage(confirmation,3);
                                dialog.cancel();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
                break;

            //One of the players resigns
            case 2:
                AlertDialog.Builder builderResign = new AlertDialog.Builder(this);
                builderResign.setMessage(message)
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                String save = "Save this game?";
                                dialogMessage(save,8);
                                dialog.cancel();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertResign = builderResign.create();
                alertResign.show();
                break;

            //Confirmation of Draw request to the other player
            case 3:
                drawOf = true;
                AlertDialog.Builder builderConfirmation = new AlertDialog.Builder(this);
                builderConfirmation.setMessage(message)
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                String save = "Save this game?";
                                dialogMessage(save,4);
                                dialog.cancel();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertConfirmation = builderConfirmation.create();
                alertConfirmation.show();
                break;

            //Save game question
            case 4:
                AlertDialog.Builder builderSave = new AlertDialog.Builder(this);
                builderSave.setMessage(message)
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                String messageSave = "Please Enter a Name:  ";
                                dialogMessage(messageSave,5);
                                dialog.cancel();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                if(drawOf){
                                    gameEnd(3);
                                } else {
                                    finish();
                                }
                            }
                        });
                AlertDialog alertSave = builderSave.create();
                alertSave.show();
                break;


            //File name dialog
            case 5:

                AlertDialog.Builder builderFileName = new AlertDialog.Builder(this);
                builderFileName.setMessage(message);

                final EditText input = new EditText(this);
                builderFileName.setView(input);

                builderFileName.setPositiveButton("Save", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        Editable value = input.getText();
                        String newGameName = value.toString();

                        if(newGameName.equals("")){
                            String messageResign = "Please Enter a Name: ";
                            dialogMessage(messageResign,5);
                        }

                        else{
                            RecordMoves content = Rules.this.gameMoves;
                            content.setFileName(value.toString());
                            GameActivity savedContent= Rules.this.gameStream;
                            savedContent.recordedGames.add(content);
                            try {
                                SaveData.saveGames(gameStream,Rules.this.getApplicationContext());
                            } catch (Exception e) {
                                Log.d("Chess", e.getMessage());
                            }

                            dialog.cancel();
                            if(drawOf){

                                gameEnd(3);
                            }
                        }
                    }
                });

                AlertDialog alertFileName = builderFileName.create();
                alertFileName.show();
                break;

            //Notification dialog when one of the players resigns
            case 6:
                AlertDialog.Builder builderNotification = new AlertDialog.Builder(this);
                builderNotification.setMessage(message)
                        .setCancelable(false)
                        .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                Rules.this.finish();
                            }
                        });
                AlertDialog alertNotification = builderNotification.create();
                alertNotification.show();
                break;

            case 7:
                AlertDialog.Builder builderNotification2 = new AlertDialog.Builder(this);
                builderNotification2.setMessage(message)
                        .setCancelable(false)
                        .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertNotification2 = builderNotification2.create();
                alertNotification2.show();
                break;


            case 8:

                AlertDialog.Builder builderSave2 = new AlertDialog.Builder(this);
                builderSave2.setMessage(message)
                        .setCancelable(false)

                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                String messageSave = "Please Enter a Name: ";
                                dialogMessage(messageSave,9);
                                dialog.cancel();
                            }
                        })

                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();

                                gameEnd(6);
                            }
                        });

                AlertDialog alertSave2 = builderSave2.create();
                alertSave2.show();
                break;


            case 9:

                AlertDialog.Builder builderFileName2 = new AlertDialog.Builder(this);
                builderFileName2.setMessage(message);

                final EditText input2 = new EditText(this);
                builderFileName2.setView(input2);

                builderFileName2.setPositiveButton("Save", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {

                        Editable value = input2.getText();
                        String newGameName = value.toString();

                        if(newGameName.equals("")){
                            String messageResign = "Please Enter a Name: ";
                            dialogMessage(messageResign,5);
                        }

                        else{
                            RecordMoves content = Rules.this.gameMoves;
                            content.setFileName(value.toString());
                            GameActivity save = Rules.this.gameStream;
                            save.recordedGames.add(content);
                            try {
                                SaveData.saveGames(gameStream,Rules.this.getApplicationContext());
                            } catch (Exception e) {
                                Log.d("Chess", e.getMessage());
                            }
                            dialog.cancel();

                            gameEnd(7);
                        }
                    }
                });

                AlertDialog alertFileName2 = builderFileName2.create();
                alertFileName2.show();
                break;
        }
    }


    protected void showPromotion(){
        final String[] items  ={"Queen", "Rook", "Bishop", "Knight"};

        AlertDialog.Builder promo = new AlertDialog.Builder(this);
        promo.setTitle("What would you like to promote your Pawn to?");
        promo.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int selection) {

                promotePawn = items[selection];
                switch(promotePawn){
                    case "Rook":
                        promotePawn = "r";
                        break;
                    case "Bishop":
                        promotePawn = "b";
                        break;
                    case "Knight":
                        promotePawn = "n";
                        break;
                }
                String hear = gameBoard.movePiece(moveFrom, moveTo, promotePawn);
                if (hear.compareToIgnoreCase("invalid") == 0) {
                    String errorMessage = "not a valid move";
                    showToast(errorMessage);
                } else {
                    move(hear);
                    prevPiece.checkMove = false;
                    prevPiece = null;
                    oldPieceSelected = null;
                }
            }
        });
        AlertDialog alert = promo.create();
        alert.setCancelable(false);
        alert.show();
    }

    void addMoveToUndo() throws Exception{
        Board gameState = null;
        gameState = (Board)(ObjectStream.copyAll(gameBoard));
        undoPlay.addMove(gameState);
    }

    protected void gameEnd(int status){
        String message ="";
        String title="GAME OVER";
        switch(status){
            case 0:
                title = "CHECKMATE:";
                message ="White won";
                break;
            case 1:
                title = "CHECKMATE";
                message = "Black won";
                break;
            case 2:
                title = "STALEMATE";
                message = "There is no winner";
                break;
            case 3:
                title = "DRAW";
                message = "Draw, No Body won";
                break;
            case 4:
                message = "White Won";
                break;
            case 5:
                message = "Black Won";
                break;
            case 6:
                title = "RESIGN";
                message = "Resign";
                break;
            case 7:
                title = "RESIGN";
                message = "Resign and Saved";
                break;
            case 8:
                title = "GAME EXIT";
                message = "You sure you want to exit the game?";
                break;
        }
        showEndGameMessage(title, message);
    }
    public void showEndGameMessage(String title,String message){
        if(!title.contains("GAME")){
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle(title)
                    .setMessage(message)
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.cancel();
                            Rules.this.finish();
                        }
                    });
            AlertDialog alertConfirmation = alert.create();
            alertConfirmation.show();
        } else {

            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle(title)
                    .setMessage(message)
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.cancel();
                            finish();

                        }
                    });
            AlertDialog alertConfirmation = alert.create();
            alertConfirmation.show();

            AlertDialog.Builder builderConfirmation = new AlertDialog.Builder(this);
            //Would the player like to save game after it ends
            builderConfirmation.setMessage(message)
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            String save = "Would you like to save this game?";
                            dialogMessage(save, 4);
                            dialog.cancel();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            Rules.this.finish();
                        }
                    });
            AlertDialog confirmation = builderConfirmation.create();
            confirmation.show();
        }
    }
    public void backPressed(){
        AlertDialog.Builder confimation = new AlertDialog.Builder(this);
        confimation.setMessage("Are you sure that you want to exit current game?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.cancel();
                        Rules.this.finish();
                    }
                });

    }
    private String randomMove(ChessPiece piece){
        String message = "invalid";

        ChessPiece holdPiece = piece;
        String from = holdPiece.getPosition();
        moveFrom = from;
        prevPiece = piece;
        int fColumn = from.charAt(0)-97;
        int fRow = from.charAt(1)-49;
        fRow =  (fRow-7)*(-1);
        ArrayList<String> moves = legalMoves(holdPiece);
        Collections.shuffle(moves);
        int i= 0;
        boolean done = false;
        while(i<moves.size() && !done){
            String[]tok;
            String currMove = moves.get(i);
            i++;
            tok = currMove.split(",");
            int row = Integer.parseInt(tok[1])+fRow;
            int colm = Integer.parseInt(tok[0])+fColumn;

            if(row>7 || colm >7 ||row<0 ||colm<0) {
                ;
            }else{

                String moveLocation = getPosition(row, colm);
                moveTo = moveLocation;
                String promo = null;
                boolean promoted = false;
                if (holdPiece.getType().compareToIgnoreCase("pawn") == 0) {

                    String returnMessage = gameBoard.movePiece(from, moveLocation, promo);

                    if (returnMessage.compareToIgnoreCase("invalid") == 0) {
                        Log.d("Chess", "Invalid move from " + from + " to " + moveLocation);
                        continue;
                    }
                    Log.d("Chess", "Valid move from " + from + " to " + moveLocation);
                    gameBoard.undo();
                    if (holdPiece.getColor().compareToIgnoreCase("white") == 0) {
                        if (row == 0) {
                            showPromotion();
                            promoted = true;
                            done = true;
                        }
                    } else {
                        if (row == 7) {
                            showPromotion();
                            promoted = true;
                            done = true;
                        }
                    }
                    message = returnMessage;
                }
                if(!promoted) {
                    String returnMessage = gameBoard.movePiece(from, moveLocation, promo);
                    if (returnMessage.compareToIgnoreCase("invalid") != 0) {
                        holdPiece.setPosition(getPosition(row, colm));
                        message = returnMessage;
                        done = true;
                        return message;
                    }
                }
            }
        }



        return message;
    }
    private ArrayList<String> legalMoves(ChessPiece piece){

        ArrayList<String> moves = new ArrayList<String>();
        String type = piece.getType();
        //column(a-h), row(8-1)
        if(type.compareToIgnoreCase("pawn")==0){
            if(piece.getColor().compareToIgnoreCase("black")==0) {
                moves.add("0,1");//move up
                moves.add("0,2");//move up two space
                moves.add("1,1");//moves to right for attacking
                moves.add("-1,1");//moves to left to attack
            }else{
                moves.add("0,-1");//move down
                moves.add("0,-2");//move down two space
                moves.add("1,-1");//moves down right for attacking
                moves.add("-1,-1");//moves down left to attack
            }
        }else if(type.compareToIgnoreCase("king")==0){
            moves.add("1,0");//move up
            moves.add("1,1");//moves to right and up
            moves.add("1,0");//moves to right
            moves.add("2,0");//right castling
            moves.add("-1,1");//moves to left and up
            moves.add("-1,0"); //move left
            moves.add("-2,0");//left casting
            moves.add("0,-1");//move down
            moves.add("1,-1");//moves down right for attacking
            moves.add("-1,-1");//moves down left to attack
        }else if(type.compareToIgnoreCase("knight")==0){
            moves.add("-1,2");//moves up 2 and to the left
            moves.add("1,2");//moves up 2 and to the right
            moves.add("2,1");//moves right two and to the left
            moves.add("-2,1");
            moves.add("2,-1");
            moves.add("-2,-1");
            moves.add("-1,-2");
            moves.add("1,-2");
        }
        else if(type.compareToIgnoreCase("queen")==0){
            int i = 1;
            while(i<8){
                String strn = i+",0";//moves to the right
                moves.add(strn);
                i++;
            }
            i = -1;
            while(i>-8){
                String strn = i+",0";//moves to the left
                moves.add(strn);
                i--;
            }
            i = 1;
            while(i<8){
                String strn = "0,"+i;//moves up
                moves.add(strn);
                i++;
            }
            i = -1;
            while(i>-8){
                String strn = "0,"+i;//moves down
                moves.add(strn);
                i--;
            }
            i = 1;
            //diagnol moves
            int j = 1;
            while(i<8){
                String strn = i+","+j;//moves to the right
                moves.add(strn);
                i++;
                j++;
            }
            i = -1;
            j= -1;
            while(i>-8){
                String strn = i+","+j;//moves to the left and down
                moves.add(strn);
                i--;
                j--;
            }
            i = 1;
            j = -1;
            while(i<8){
                String strn = i+","+j;//moves up and left
                moves.add(strn);
                i++;
                j--;
            }
            i = -1;
            j = -1;
            while(i>-8){
                String strn = j+","+i;//moves down
                moves.add(strn);
                i--;
                j--;
            }
        }else if(type.compareToIgnoreCase("bishop")==0){
            int i = 1;
            int j = 1;
            while(i<8){
                String strn = i+","+j;//moves to the right
                moves.add(strn);
                i++;
                j++;
            }
            i = -1;
            j= -1;
            while(i>-8){
                String strn = i+","+j;//moves to the left and down
                moves.add(strn);
                i--;
                j--;
            }
            i = 1;
            j = -1;
            while(i<8){
                String strn = i+","+j;//moves up and left
                moves.add(strn);
                i++;
                j--;
            }
            i = -1;
            j = -1;
            while(i>-8){
                String strn = j+","+i;//moves down
                moves.add(strn);
                i--;
                j--;
            }
        }
        else {//rook
            int i = 1;
            while(i<8){
                String strn = i+",0";//moves to the right
                moves.add(strn);
                i++;
            }
            i = -1;
            while(i>-8){
                String strn = i+",0";//moves to the left
                moves.add(strn);
                i--;
            }
            i = 1;
            while(i<8){
                String strn = "0,"+i;//moves up
                moves.add(strn);
                i++;
            }
            i = -1;
            while(i>-8){
                String strn = "0,"+i;//moves down
                moves.add(strn);
                i--;
            }
        }
        return moves;
    }

    protected void drawBoard(){
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/Symbola.ttf");
        ChessPiece holdPiece = null;
        int i,j;

        for(i = 0; i<gameBoard.chessGrid.length; i++){
            for(j = 0; j<gameBoard.chessGrid.length; j++){
                holdPiece = gameBoard.chessGrid[i][j];

                TextView textView = (TextView) findViewById(getCellID(i+1,j+1));;
                if(holdPiece!=null){
                    textView.setTypeface(font);
                    textView.setTextColor(Color.BLACK);
                    String type = holdPiece.getType();

                    if(type.compareToIgnoreCase("king")==0){
                        if(holdPiece.getColor().compareToIgnoreCase("white")==0) {
                            textView.setText(getPieceImage(R.drawable.whiteking));
                        }else{
                            textView.setText(getPieceImage(R.drawable.blackking));
                        }
                    }else if(type.compareToIgnoreCase("queen")==0){
                        if(holdPiece.getColor().compareToIgnoreCase("white")==0) {
                            textView.setText(getPieceImage(R.drawable.whitequeen));
                        }else{
                            textView.setText(getPieceImage(R.drawable.blackqueen));
                        }
                    }else if(type.compareToIgnoreCase("rook")==0){
                        if(holdPiece.getColor().compareToIgnoreCase("white")==0) {
                            textView.setText(getPieceImage(R.drawable.whiterook));
                        }else{
                            textView.setText(getPieceImage(R.drawable.blackrook));
                        }
                    }else if(type.compareToIgnoreCase("bishop")==0){
                        if(holdPiece.getColor().compareToIgnoreCase("white")==0) {
                            textView.setText(getPieceImage(R.drawable.whitebishop));
                        }else{
                            textView.setText(getPieceImage(R.drawable.blackbishop));
                        }
                    }else if(type.compareToIgnoreCase("knight")==0){
                        if(holdPiece.getColor().compareToIgnoreCase("white")==0) {
                            textView.setText(getPieceImage(R.drawable.whiteknight));
                        }else{
                            textView.setText(getPieceImage(R.drawable.blackknight));
                        }
                    }else if(type.compareToIgnoreCase("pawn")==0){
                        if(holdPiece.getColor().compareToIgnoreCase("white")==0) {
                            textView.setText(getPieceImage(R.drawable.whitepawn));
                        }else{
                            textView.setText(getPieceImage(R.drawable.blackpawn));
                        }
                    }
                    holdPiece.checkMove=false;
                    if(holdPiece.getColor().compareToIgnoreCase("white")==0){
                        textView.setShadowLayer(3.0f, 0.0f, 0.0f, Color.WHITE);
                    }
                    textView.setTag(holdPiece.getPosition());
                    addlistener(textView, getCellID(i+1,j+1), i+1, j+1);
                }
                else{
                    textView.setBackgroundColor(00000000);
                    textView.setShadowLayer(0.0f, 0.0f, 0.0f, Color.WHITE);
                    textView.setTypeface(font);
                    textView.setText(getPieceImage(R.drawable.clear));
                    textView.setTextColor(00000000);
                    textView.setTag(getPosition(i,j));
                    addlistener(textView,getCellID(i+1,j+1),i+1,j+1);

                }
            }
        }
    }



    @NonNull
    private Spannable getPieceImage(int id) {
        ImageSpan is = new ImageSpan(this, id);
        final Spannable text = new SpannableString("x");
        text.setSpan(is, 0, 1, 0);
        return text;
    }

    private void addlistener(final TextView textView, final int id, final int row, final int column){

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ChessPiece holdPiece = gameBoard.chessGrid[row-1][column-1];
                String possiblePromotion = null;
                boolean promotion = false;
                if (holdPiece != null) {
                    if (gameBoard.whiteMove) {
                        //deselected piece
                        if (oldPieceSelected != null && prevPiece.getType().compareToIgnoreCase("pawn") == 0) {
                            if (row-1 == 0) {
                                promotion = true;
                            }
                        }
                        if (oldPieceSelected != null && oldPieceSelected == textView) {
                            v.setBackgroundColor(00000000);
                            changefontColor(id, holdPiece);
                            holdPiece.checkMove = false;
                            origin = null;
                            oldPieceSelected = null;
                            prevPiece = null;
                            moveFrom = "";

                        } else if (oldPieceSelected == null) {
                            if(holdPiece.getColor().compareToIgnoreCase("white") != 0){
                                showToast("Error this is not your piece");
                            }else {

                                v.setBackgroundColor(Color.BLACK);
                                v.getBackground().setAlpha(50);
                                changefontColor(id, holdPiece);

                                TextView cell = (TextView) v;
                                holdPiece.checkMove = true;
                                origin = cell;
                                oldPieceSelected = textView;
                                moveFrom = holdPiece.getPosition();
                                prevPiece = holdPiece;
                            }
                        } else {

                            moveTo = holdPiece.getPosition();
                            if (promotion) {
                                showPromotion();
                                possiblePromotion = promotePawn;
                            }else {

                                String message = gameBoard.movePiece(moveFrom, moveTo, possiblePromotion);
                                if (message.compareToIgnoreCase("invalid") == 0) {

                                    String errorMessage = "Error, invalid move";
                                    showToast(errorMessage);
                                } else {
                                    // not valid move
                                    move(message);

                                    prevPiece.checkMove = false;
                                    prevPiece = null;
                                    oldPieceSelected = null;
                                }
                            }
                        }
                    } else {//black turns
                        if (oldPieceSelected != null && prevPiece.getType().compareToIgnoreCase("pawn") == 0) {
                            if ((row-1) == 7) {
                                promotion = true;
                            }
                        }
                        if (oldPieceSelected != null && oldPieceSelected == textView) {
                            v.setBackgroundColor(00000000);
                            changefontColor(id, holdPiece);
                            origin = null;
                            holdPiece.checkMove = false;
                            oldPieceSelected = null;
                            prevPiece = null;
                            moveFrom = "";
                        } else if (oldPieceSelected == null) {
                            if(holdPiece.getColor().compareToIgnoreCase("black") == 0) {

                                holdPiece.checkMove = true;
                                v.setBackgroundColor(Color.BLACK);
                                v.getBackground().setAlpha(50);
                                changefontColor(id, holdPiece);
                                TextView cell = (TextView) v;
                                origin = cell;
                                oldPieceSelected = textView;
                                moveFrom = holdPiece.getPosition();
                                prevPiece = holdPiece;
                            }else{
                                showToast("Error, this is not your piece");
                            }
                        } else {

                            moveTo = holdPiece.getPosition();
                            if (promotion) {
                                showPromotion();
                                possiblePromotion = promotePawn;
                            }else {
                                String message = gameBoard.movePiece(moveFrom, moveTo, possiblePromotion);
                                if (message.compareToIgnoreCase("invalid") == 0) {
                                    String eMessage = "Error, invalid move";
                                    showToast(eMessage);
                                } else {
                                    move(message);

                                    prevPiece.checkMove = false;
                                    prevPiece = null;
                                    oldPieceSelected = null;
                                }
                            }
                        }
                    }

                } else {
                    if (oldPieceSelected != null) {
                        boolean promoFailed = true;
                        if (prevPiece.getType().compareToIgnoreCase("pawn") == 0) {
                            if (gameBoard.whiteMove) {
                                if (row == 1) {
                                    moveTo = getPosition(row - 1, column - 1);
                                    showPromotion();
                                    promoFailed = false;
                                }
                            } else {
                                if (row == 8) {
                                    moveTo = getPosition(row - 1, column - 1);
                                    showPromotion();
                                    promoFailed = false;
                                }
                            }
                        }
                        if(promoFailed){
                            moveTo = getPosition(row - 1, column - 1);
                            String hear = gameBoard.movePiece(moveFrom, moveTo, possiblePromotion);
                            if (hear.compareToIgnoreCase("invalid") == 0) {
                                String errorMessage = "Error, invalid move";
                                showToast(errorMessage);
                            } else {
                                move(hear);

                                prevPiece.checkMove = false;
                                prevPiece = null;
                                oldPieceSelected = null;
                            }
                        }
                    } else {
                        String errorMessage = "Error, no prior piece was checkMove";
                        showToast(errorMessage);
                    }
                }
            }
        });
    }

    private void move(String message){

        Move move = new Move(moveFrom, moveTo);
        if(origin!=null) {
            origin.setBackgroundColor(00000000);
        }
        origin = null;
        destiny = null;

        String[]tok;
        tok = message.split("\\s+");
        if(prevPiece!=null && prevPiece.getType().compareToIgnoreCase("pawn")==0){
            if(promotePawn !=null && promotePawn.length()>1){
                move.setPromotion(promotePawn);
                promotePawn = "";
            }
        }
        if(tok.length==1){
            if(tok[0].compareToIgnoreCase("stalemate")==0){
                gameEnd(2);
                return;
            }
        }
        if(tok.length==2){
            if(tok[1].compareTo("Wins")==0){
                if(tok[0].compareTo("White")==0){
                    gameEnd(4);
                }else{
                    gameEnd(5);
                }
                return;
            }
        }

        if(tok.length==3){
            if(tok[2].compareToIgnoreCase("check")==0){
                if(tok[0].compareToIgnoreCase("white")==0){
                    showToast(message);
                }else{
                    showToast(message);
                }
            }else if(tok[2].compareToIgnoreCase("checkmate")==0){
                if(gameBoard.whiteMove){
                    gameEnd(0);
                }else{
                    gameEnd(1);
                }
                return;
            }

        }
        addMoveToMoves(move);
        try {
            addToUndo();
        }catch(Exception e){
            Log.d("Chess", e.getMessage());
        }
        drawBoard();
    }
    private void addMoveToMoves(Move move){
        gameMoves.addMove(move);
    }
    private void addToUndo() throws  Exception{
        Board boardState = null;
        boardState = (Board)(ObjectStream.copyAll(gameBoard));
        undoPlay.addMove(boardState);
    }

    protected void undo() throws Exception{
        if(gameBoard.moveNumber!=0){
            if(prevOrigin!=null)
                removeTrace(prevOrigin, prevDestiny);
            gameBoard.undo();
            gameMoves.removeLastMove();
            origin = null;
            destiny = null;
            prevPiece = null;
            oldPieceSelected = null;
            moveFrom = null;
            moveTo = null;
            Board recovState = undoPlay.undoMove();

            drawBoard();
        }else{
            showToast("There is nothing to undo");
        }
    }

    private void removeTrace(String mvFro, String mvTo){

    }
    private String getPosition(int row, int colm){
        row = (-1*row)+8;
        String row1 = String.valueOf(row);
        String column = "";
        switch(colm){
            case 0:
                column= "a";
                break;
            case 1:
                column="b";
                break;
            case 2:
                column="c";
                break;
            case 3:
                column="d";
                break;
            case 4:
                column="e";
                break;
            case 5:
                column="f";
                break;
            case 6:
                column="g";
                break;
            case 7:
                column="h";
                break;
        }

        return column+ row1;
    }

    private void changefontColor(int id, ChessPiece piece){
        if(!piece.checkMove){
            TextView txt = (TextView)findViewById(id);
            txt.setTextColor(Color.WHITE);
        }else{
            TextView txt = (TextView)findViewById(id);
            txt.setTextColor(Color.BLACK);
        }
    }
    private int getCellID(int row, int colm){
        if(colm ==1){
            switch(row){
                case 1:
                    return R.id.A8;
                case 2:
                    return R.id.A7;
                case 3:
                    return R.id.A6;
                case 4:
                    return R.id.A5;
                case 5:
                    return R.id.A4;
                case 6:
                    return R.id.A3;
                case 7:
                    return R.id.A2;
                case 8:
                    return R.id.A1;
            }

        }else if(colm ==2){
            switch(row){
                case 1:
                    return R.id.B8;
                case 2:
                    return R.id.B7;
                case 3:
                    return R.id.B6;
                case 4:
                    return R.id.B5;
                case 5:
                    return R.id.B4;
                case 6:
                    return R.id.B3;
                case 7:
                    return R.id.B2;
                case 8:
                    return R.id.B1;
            }
        }else if(colm== 3){
            switch(row){
                case 1:
                    return R.id.C8;
                case 2:
                    return R.id.C7;
                case 3:
                    return R.id.C6;
                case 4:
                    return R.id.C5;
                case 5:
                    return R.id.C4;
                case 6:
                    return R.id.C3;
                case 7:
                    return R.id.C2;
                case 8:
                    return R.id.C1;
            }
        }else if(colm == 4){
            switch(row){
                case 1:
                    return R.id.D8;
                case 2:
                    return R.id.D7;
                case 3:
                    return R.id.D6;
                case 4:
                    return R.id.D5;
                case 5:
                    return R.id.D4;
                case 6:
                    return R.id.D3;
                case 7:
                    return R.id.D2;
                case 8:
                    return R.id.D1;
            }
        }else if(colm == 5){
            switch(row){
                case 1:
                    return R.id.E8;
                case 2:
                    return R.id.E7;
                case 3:
                    return R.id.E6;
                case 4:
                    return R.id.E5;
                case 5:
                    return R.id.E4;
                case 6:
                    return R.id.E3;
                case 7:
                    return R.id.E2;
                case 8:
                    return R.id.E1;
            }
        }else if(colm ==6){
            switch(row){
                case 1:
                    return R.id.F8;
                case 2:
                    return R.id.F7;
                case 3:
                    return R.id.F6;
                case 4:
                    return R.id.F5;
                case 5:
                    return R.id.F4;
                case 6:
                    return R.id.F3;
                case 7:
                    return R.id.F2;
                case 8:
                    return R.id.F1;
            }
        }else if(colm ==7){
            switch(row){
                case 1:
                    return R.id.G8;
                case 2:
                    return R.id.G7;
                case 3:
                    return R.id.G6;
                case 4:
                    return R.id.G5;
                case 5:
                    return R.id.G4;
                case 6:
                    return R.id.G3;
                case 7:
                    return R.id.G2;
                case 8:
                    return R.id.G1;
            }
        }else if(colm ==8){
            switch(row){
                case 1:
                    return R.id.H8;
                case 2:
                    return R.id.H7;
                case 3:
                    return R.id.H6;
                case 4:
                    return R.id.H5;
                case 5:
                    return R.id.H4;
                case 6:
                    return R.id.H3;
                case 7:
                    return R.id.H2;
                case 8:
                    return R.id.H1;
            }
        }
        return 0;
    }

    private void showToast(String message){

        Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP, -30, 50);
        toast.show();

    }

    public void onBackPressed(){
        drawOf = false;
        gameEnd(8);

    }
}


