package com.example.mkasa.chessApp67;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Michael Russo and Mustafa Kasabchy on 12/4/2017.
 */

public class Board implements Serializable {


    private static final long serialVersionUID = 1l;
    public ChessPiece whitePiece, blackPiece, onAction;
    public ChessPiece[][] chessGrid;
    public boolean gameOver;
    public int callWhiteCheck, callBlackCheck  =0;
    public String currPosition;
    public int prevPieceSelectedID = 0;
    public ChessPiece prePieceSelected = null;
    public final int rows = 8;
    public final int colms = 8;
    public String player1 = "white", player2 = "black";
    public int moveNumber;
    public boolean whiteMove;
    private ArrayList<ChessPiece[][]> prevBoards;
    public ArrayList<ChessPiece> whitePieces;
    public ArrayList<ChessPiece> blackPieces;

    protected Board() {
        whitePieces = new ArrayList<ChessPiece>();
        blackPieces = new ArrayList<ChessPiece>();
        prevBoards = new ArrayList<ChessPiece[][]>();
        tableGrid();
        gameOver = false;
        whiteMove = true;
        moveNumber = 0;

    }

    //return 0 if white wins
    //return 1 if black wins
    //retruns -1 o/w
    protected int gameOver() throws Exception {
        if (whiteMove) {
            if (blackPiece.position == "null") {
                return 0;
            }
        } else {
            if (whitePiece.position == "null") {
                return 1;
            }
        }
        return -1;
    }

    //returns -10 if white wins, -11 if black wins
    //returns 0 for whites turn, returns 1 if  black turn
    public ChessPiece getChessPiece(int i, int j) {
        return chessGrid[i][j];
    }

    public String movePiece(String move, String to, String possiblePromotion) {
        setPreviousBoard();
        String message = "";
        if (whiteMove) {
            checkEnPessant("white");
        } else {
            checkEnPessant("black");
        }
        String warning = "";
        if (whiteMove) {
            if (grid(chessGrid, move, whitePiece)) {
                if (onAction.inValid(chessGrid, to, possiblePromotion)) {
                    if (onAction.check(chessGrid)) {
                        callBlackCheck = 1;

                    } else {
                        callBlackCheck = 0;
                    }

                    if (callBlackCheck == 1) {
                        if (rule(blackPiece, chessGrid, callBlackCheck)) {
                            message = "Black in Checkmate";
                        } else {
                            message = "Black in Check";
                        }
                    } else {
                        message = "valid";
                    }
                    whiteMove = false;
                } else {
                    message = "invalid";
                    warning = "piece is not a valid piece in chessGrid";
                }
            } else {
                message = "invalid";
            }
        } else {
            if (grid(chessGrid, move, blackPiece)) {
                if (onAction.inValid(chessGrid, to, possiblePromotion)) {
                    if (onAction.check(chessGrid)) {
                        callWhiteCheck = 1;
                    } else {
                        callWhiteCheck = 0;
                    }

                    if (callWhiteCheck == 1) {
                        if (rule(whitePiece, chessGrid, callWhiteCheck)) {
                            message = "White in Checkmate";
                        } else {
                            message = "White in Check";
                        }
                    } else {
                        message = "valid";
                    }

                    whiteMove = true;
                } else {
                    message = "invalid";
                    warning = "piece is not a valid black piece";
                }
            } else {
                message = "invalid";
            }
        }

        if (gameOver == true) {
            if (callBlackCheck == 0 && callWhiteCheck == 0) {
                message = "Stalemate";
            }
        }
        if (message.compareToIgnoreCase("invalid") != 0) {
            moveNumber++;
        }
        if (message.compareToIgnoreCase("invalid") == 0) {
            if (prevBoards != null && prevBoards.size() > 0) {
                prevBoards.remove(prevBoards.size() - 1);
            }
        }
        if (whitePiece != null && whitePiece.getPosition().compareTo("null") == 0) {
            message = "Black Wins";
        }
        if (blackPiece != null && blackPiece.getPosition().compareTo("null") == 0) {
            message = "White Wins";
        }

        return message;

    }

    public void setPreviousBoard() {
        ChessPiece[][] previousBoard = new ChessPiece[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {

                if (chessGrid[i][j] != null) {
                    String type = chessGrid[i][j].getType().toLowerCase();
                    String color = chessGrid[i][j].getColor();
                    String location = chessGrid[i][j].getPosition();
                    int fColumn = location.charAt(0) - 97;
                    int fRow = location.charAt(1) - 49;
                    fRow = (fRow - 7) * (-1);
                    switch (type) {
                        case "king":
                            previousBoard[i][j] = new King(color, i, j);
                            previousBoard[i][j].setPosition(getPosition(fRow, fColumn));
                            break;
                        case "queen":
                            previousBoard[i][j] = new Queen(color, i, j);
                            previousBoard[i][j].setPosition(getPosition(fRow, fColumn));
                            break;
                        case "pawn":
                            previousBoard[i][j] = new pawn(color, i, j);
                            previousBoard[i][j].setPosition(getPosition(fRow, fColumn));
                            break;
                        case "knight":
                            previousBoard[i][j] = new Knight(color, i, j);
                            previousBoard[i][j].setPosition(getPosition(fRow, fColumn));
                            break;
                        case "rook":
                            previousBoard[i][j] = new Rook(color, i, j);
                            previousBoard[i][j].setPosition(getPosition(fRow, fColumn));
                            break;
                        case "bishop":
                            previousBoard[i][j] = new Bishop(color, i, j);
                            previousBoard[i][j].setPosition(getPosition(fRow, fColumn));
                            break;

                    }
                } else {
                    previousBoard[i][j] = chessGrid[i][j];
                }
            }
        }
        prevBoards.add(previousBoard);
    }

    private String getPosition(int row, int colm) {
        row = (-1 * row) + 8;
        String row1 = String.valueOf(row);
        String column = "";
        switch (colm) {
            case 0:
                column = "a";
                break;
            case 1:
                column = "b";
                break;
            case 2:
                column = "c";
                break;
            case 3:
                column = "d";
                break;
            case 4:
                column = "e";
                break;
            case 5:
                column = "f";
                break;
            case 6:
                column = "g";
                break;
            case 7:
                column = "h";
                break;
        }

        return column + row1;
    }

    public void undo() {
        if (prevBoards == null || prevBoards.size() == 0 || moveNumber == 0) {
            return;
        }
        ChessPiece[][] previousBoard = prevBoards.remove(prevBoards.size() - 1);
        whitePieces.clear();
        blackPieces.clear();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                chessGrid[i][j] = previousBoard[i][j];
                if (chessGrid[i][j] != null) {
                    chessGrid[i][j].setPosition(getPosition(i, j));
                    if (chessGrid[i][j].getColor().compareToIgnoreCase("white") == 0) {
                        whitePieces.add(chessGrid[i][j]);
                    } else {
                        blackPieces.add(chessGrid[i][j]);
                    }
                    if (chessGrid[i][j].getType().compareToIgnoreCase("king") == 0) {
                        if (chessGrid[i][j].getColor().compareToIgnoreCase("white") == 0) {
                            whitePiece = chessGrid[i][j];
                        } else {
                            blackPiece = chessGrid[i][j];
                        }
                    }
                }
            }
        }

        if (moveNumber != 0) {
            moveNumber--;
        }
        if (moveNumber % 2 == 0 || moveNumber == 0 && moveNumber != 1) {
            whiteMove = true;
        } else {
            whiteMove = false;
        }

    }

    public boolean rule(ChessPiece soldier, ChessPiece[][] chessGrid, int checkMate)
    {

        ChessPiece[][] board;
        int x = soldier.x_coord;
        int y = soldier.y_coord;
        int setNum = 0;
        int roundCount = 8;

        ChessPiece checkPiece;

        if(soldier.getColor().compareTo("white")==0){
            checkPiece = blackPiece;
        }else{
            checkPiece = whitePiece;
        }
        ChessPiece tempPiece = null;
        board = new ChessPiece[8][8];

        for(int i = 0; i<chessGrid.length; i++){
            for(int j =0; j<chessGrid.length; j++){
                board[i][j]= chessGrid[i][j];
            }
        }

        // check in the south west of the king and make sure the king not in check
        x++;
        y--;
        if(y>-1 && x<8){
            ChessPiece tempKing = board[soldier.x_coord][soldier.y_coord];
            if(board[x][y]==null||board[x][y].getColor().compareTo(soldier.getColor())!=0){
                board[x][y] = tempKing;
                board[soldier.x_coord][soldier.y_coord] = null;
                board[checkPiece.x_coord][checkPiece.y_coord]=null;
                boolean theEnd = false;
                int i=0;
                while( i<chessGrid.length && theEnd!=true)
                {
                    int j = 0;
                    while(j < chessGrid.length){
                        tempPiece = board[i][j];
                        if(tempPiece!=null){
                            if(tempPiece.check(board)){
                                setNum++;
                                theEnd = true;
                                break;
                            }
                        }
                        j++;
                    }
                    i++;
                }
                board[checkPiece.x_coord][checkPiece.y_coord] = checkPiece;
                board[x][y]=chessGrid[x][y];
                board[soldier.x_coord][soldier.y_coord]= tempKing;
            }else{
                roundCount--;
            }
        }else{
            roundCount--;
        }

        // check in the west of the king and make sure the king not in check
        x = soldier.x_coord;
        y = soldier.y_coord -1;
        if(y>-1){
            ChessPiece tempKing = board[soldier.x_coord][soldier.y_coord];
            if(board[x][y]==null||board[x][y].getColor().compareTo(soldier.getColor())!=0){
                board[x][y] = tempKing;
                board[soldier.x_coord][soldier.y_coord] = null;
                board[checkPiece.x_coord][checkPiece.y_coord] = null;
                boolean theEnd = false;
                int i=0;
                while( i<chessGrid.length && theEnd!=true)
                {
                    int j = 0;
                    while(j < chessGrid.length){
                        tempPiece = board[i][j];
                        if(tempPiece!=null){
                            if(tempPiece.check(board)){
                                setNum++;
                                theEnd = true;
                                break;
                            }
                        }
                        j++;
                    }
                    i++;
                }
                board[checkPiece.x_coord][checkPiece.y_coord] = checkPiece;
                board[x][y]=chessGrid[x][y];
                board[soldier.x_coord][soldier.y_coord]= tempKing;
            }else{
                roundCount--;
            }
        }else{
            roundCount--;
        }


        // check in the north west of the king and make sure the king not in check
        x = soldier.x_coord -1;
        y = soldier.y_coord -1;
        if(y>-1 && x>-1){
            ChessPiece tempKing = board[soldier.x_coord][soldier.y_coord];
            if(board[x][y]==null||board[x][y].getColor().compareTo(soldier.getColor())!=0){
                board[x][y] = tempKing;
                board[soldier.x_coord][soldier.y_coord] = null;
                board[checkPiece.x_coord][checkPiece.y_coord] = null;
                boolean theEnd = false;
                int i=0;
                while( i<chessGrid.length && theEnd!=true)
                {
                    int j = 0;
                    while(j < chessGrid.length){
                        tempPiece = board[i][j];
                        if(tempPiece!=null){
                            if(tempPiece.check(board)){
                                setNum++;
                                theEnd = true;
                                break;
                            }
                        }
                        j++;
                    }
                    i++;
                }
                board[checkPiece.x_coord][checkPiece.y_coord] = checkPiece;
                board[x][y]=chessGrid[x][y];
                board[soldier.x_coord][soldier.y_coord]= tempKing;
            }else{
                roundCount--;
            }
        }else{
            roundCount--;
        }




        // check in the Noeth of the king and make sure the king not in check
        x = soldier.x_coord -1;
        y = soldier.y_coord;
        if(x>-1){
            ChessPiece tempKing = board[soldier.x_coord][soldier.y_coord];
            if(board[x][y]==null||board[x][y].getColor().compareTo(soldier.getColor())!=0){
                board[x][y] = tempKing;
                board[soldier.x_coord][soldier.y_coord] = null;
                board[checkPiece.x_coord][checkPiece.y_coord] = null;
                boolean theEnd=  false;
                int i=0;
                while( i<chessGrid.length && theEnd!=true)
                {
                    int j = 0;
                    while(j < chessGrid.length){
                        tempPiece = board[i][j];
                        if(tempPiece!=null){
                            if(tempPiece.check(board)){
                                setNum++;
                                theEnd = true;
                                break;
                            }
                        }
                        j++;
                    }
                    i++;
                }
                board[checkPiece.x_coord][checkPiece.y_coord] = checkPiece;
                board[x][y]=chessGrid[x][y];
                board[soldier.x_coord][soldier.y_coord]= tempKing;
            }else{
                roundCount--;
            }
        }else{
            roundCount--;
        }


        // check in the East of the king and make sure the king not in check
        x = soldier.x_coord -1;
        y = soldier.y_coord +1;
        if(y<8 && x>-1){
            ChessPiece tempKing = board[soldier.x_coord][soldier.y_coord];
            if(board[x][y]==null||board[x][y].getColor().compareTo(soldier.getColor())!=0){
                board[x][y] = tempKing;
                board[soldier.x_coord][soldier.y_coord] = null;
                board[checkPiece.x_coord][checkPiece.y_coord] = null;
                boolean theEnd = false;
                int i=0;
                while( i<chessGrid.length && theEnd!=true)
                {
                    int j = 0;
                    while(j < chessGrid.length){
                        tempPiece = board[i][j];
                        if(tempPiece!=null){
                            if(tempPiece.check(board)){
                                setNum++;
                                theEnd = true;
                                break;
                            }
                        }
                        j++;
                    }
                    i++;
                }
                board[checkPiece.x_coord][checkPiece.y_coord] = checkPiece;
                board[x][y]=chessGrid[x][y];
                board[soldier.x_coord][soldier.y_coord]= tempKing;
            }else{
                roundCount--;
            }
        }else{
            roundCount--;
        }

        // check in the south East of the king and make sure the king not in check
        x = soldier.x_coord;
        y = soldier.y_coord+1;
        if(y<8){
            ChessPiece tempKing = board[soldier.x_coord][soldier.y_coord];
            if(board[x][y]==null||board[x][y].getColor().compareTo(soldier.getColor())!=0){
                board[x][y] = tempKing;
                board[soldier.x_coord][soldier.y_coord] = null;
                board[checkPiece.x_coord][checkPiece.y_coord] = null;
                boolean theEnd = false;
                int i=0;
                while( i<chessGrid.length && theEnd!=true)
                {
                    int j = 0;
                    while(j < chessGrid.length){
                        tempPiece = board[i][j];
                        if(tempPiece!=null){
                            if(tempPiece.check(board)){
                                setNum++;
                                theEnd = true;
                                break;
                            }
                        }
                        j++;
                    }
                    i++;
                }
                board[checkPiece.x_coord][checkPiece.y_coord] = checkPiece;
                board[x][y]=chessGrid[x][y];
                board[soldier.x_coord][soldier.y_coord]= tempKing;
            }else{
                roundCount--;
            }
        }else{
            roundCount--;
        }




        // check in the south west of the king and make sure the king not in check
        x = soldier.x_coord+1;
        y = soldier.y_coord +1;
        if(x<8 && y<8){
            ChessPiece tempKing = chessGrid[soldier.x_coord][soldier.y_coord];
            if(board[x][y]==null||board[x][y].getColor().compareTo(soldier.getColor())!=0){
                board[x][y] = tempKing;
                board[soldier.x_coord][soldier.y_coord] = null;
                board[checkPiece.x_coord][checkPiece.y_coord] = null;
                boolean theEnd = false;
                int i=0;
                while( i<chessGrid.length && theEnd!=true)
                {
                    int j = 0;
                    while(j < chessGrid.length){
                        tempPiece = board[i][j];
                        if(tempPiece!=null){
                            if(tempPiece.check(board)){
                                setNum++;
                                theEnd = true;
                                break;
                            }
                        }
                        j++;
                    }
                    i++;
                }
                board[checkPiece.x_coord][checkPiece.y_coord] = checkPiece;
                board[x][y]=chessGrid[x][y];
                board[soldier.x_coord][soldier.y_coord]= tempKing;
            }else{
                roundCount--;
            }
        }else{
            roundCount--;
        }

        // check in the south  of the king and make sure the king not in check
        x = soldier.x_coord+1;
        y = soldier.y_coord;
        if(x<8){
            ChessPiece tempKing = chessGrid[soldier.x_coord][soldier.y_coord];
            if(board[x][y]==null||board[x][y].getColor().compareTo(soldier.getColor())!=0){

                board[x][y] = tempKing;
                board[soldier.x_coord][soldier.y_coord] = null;
                board[checkPiece.x_coord][checkPiece.y_coord] = null;
                boolean theEnd = false;
                int i=0;
                while( i<chessGrid.length && theEnd!=true)
                {
                    int j = 0;
                    while(j < chessGrid.length){
                        tempPiece = board[i][j];
                        if(tempPiece!=null){
                            if(tempPiece.check(board)){
                                setNum++;
                                theEnd = true;
                                break;
                            }
                        }
                        j++;
                    }
                    i++;
                }
                board[checkPiece.x_coord][checkPiece.y_coord] = checkPiece;
                board[x][y]=chessGrid[x][y];
                board[soldier.x_coord][soldier.y_coord]= tempKing;
            }else{
                roundCount--;
            }
        }else{
            roundCount--;
        }

        if(setNum==roundCount && setNum!=0){
            if(checkMate==0){
                Log.d("Chess", "Stalemate");
                //System.exit(0);
                return true;
            }
            Log.d("Chess", "Checkmate!!! :(");
            if(soldier.getColor()=="white"){
                Log.d("Chess", "''''Black Wins''''");
                //System.exit(0);
                whitePiece = null;
            }else{
                Log.d("Chess", "''''White Wins'''");
                blackPiece = null;
                //System.exit(0);
            }
            return true;
        }
        return false;
    }

    // this method will print print all the chess pecies on the grid table
    private void tableGrid(){

        final int rank = 8;
        final int file = 8;

        // in this loop will print all wp and bp in the its coordinate which starting from 2 for wp and 7 for bp
        //then keep papulating until they reach column "a"
        chessGrid = new ChessPiece[rank][file];
        int i= 0;
        while(i< chessGrid.length){
            int j= 0;
            while(j< chessGrid[i].length){

                switch(i) {
                    case 1: chessGrid[i][j]= new pawn("black",1,j);
                        break;
                    case 6: chessGrid[i][j] = new pawn("white",6,j);
                        break;
                    default: chessGrid[i][j]= null;
                        break;
                }
                j++;
            }
            i++;
        }

        // this 2d arry will set all the other chess peices on the write positions
        chessGrid[0][0] = new Rook("black", 0,0);
        chessGrid[0][1] = new Knight("black",0,1);
        chessGrid[0][2] = new Bishop("black",0,2);
        chessGrid[0][3] = new Queen("black", 0,3);
        chessGrid[0][4] = new King("black",0,4);
        blackPiece = chessGrid[0][4];
        chessGrid[0][5] = new Bishop("black",0,5);
        chessGrid[0][6] = new Knight("black",0,6);
        chessGrid[0][7] = new Rook("black",0,7);

        chessGrid[7][0] = new Rook("white",7,0);
        chessGrid[7][1] = new Knight("white",7,1);
        chessGrid[7][2] = new Bishop("white",7,2);
        chessGrid[7][3] = new Queen("white",7,3);
        chessGrid[7][4] = new King("white",7,4);
        whitePiece = chessGrid[7][4];
        chessGrid[7][5] = new Bishop("white",7,5);
        chessGrid[7][6] = new Knight("white",7,6);
        chessGrid[7][7] = new Rook("white",7,7);

        for(int n = 0; n<chessGrid.length; n++){
            whitePieces.add(chessGrid[6][n]);
            blackPieces.add(chessGrid[1][n]);
        }
        for(int n = 0; n<chessGrid.length; n++ ){
            whitePieces.add(chessGrid[7][n]);
            blackPieces.add(chessGrid[0][n]);
        }
    }

    public ArrayList<ChessPiece> getPieces(String color){
        if(color.compareToIgnoreCase("white")==0){
            return whitePieces;
        }else{
            return blackPieces;
        }
    }

    // this method will check for any illiegal move that is made in the input
    public boolean grid(ChessPiece[][] chessGrid, String from, ChessPiece sold){
        int yaxis = from.charAt(0)-97;
        int xaxis = from.charAt(1)-49;

        if(from.length()!=2){

            return false;
        }

        xaxis =  (xaxis-7)*(-1);
        if(xaxis>7 ||xaxis< 0 || yaxis>7 || yaxis<0){
            return false;
        }
        if(chessGrid[xaxis][yaxis]==null){
            return false;
        }

        if(sold == null){
            return false;
        }

        if(chessGrid[xaxis][yaxis].getColor()!= sold.getColor()){

            return false;
        }
        onAction = chessGrid[xaxis][yaxis];
        return true;
    }

    private void checkEnPessant(String color){

        for (int i = 0 ; i< chessGrid[0].length; i ++){

            for (int j = 0 ; j < chessGrid[i].length; j++){


                if (chessGrid[i][j]!=null
                        && chessGrid[i][j].type.equalsIgnoreCase("Pawn")
                        && chessGrid[i][j].color.equalsIgnoreCase(color)){

                    chessGrid[i][j].enPassant = false;
                }
            }
        }


    }

    ChessPiece returnKing(String color){
        if(color.compareToIgnoreCase("white")==0){
            return whitePiece;
        }else{
            return blackPiece;
        }
    }

}