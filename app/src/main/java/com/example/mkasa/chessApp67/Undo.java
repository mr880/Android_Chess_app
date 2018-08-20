package com.example.mkasa.chessApp67;

import java.io.Serializable;

/**
 * Created by Michael Russo and Mustafa Kasabchy on 12/4/2017.
 */

public class Undo implements Serializable {


    private static final long serialVersionUID = 1l;


    private BoardMoves  moves ;

    public Undo(){
        moves = new BoardMoves();

    }


    public void addMove(Board board){
        moves.addTail(board);
    }

    public Board undoMove(){
        int size = moves.size();
        if(size>0){
            Board board= moves.undo();
            return board;
        }
        else{
            return null;
        }
    }

    public boolean hasPrev(){
        if(moves.hasPrev()){
            return true;
        }

        return false;
    }

    public Board initializeBoard(){

        Board gameState = moves.initialize();
        return gameState;
    }



}

