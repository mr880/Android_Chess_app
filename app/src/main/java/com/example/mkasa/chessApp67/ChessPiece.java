package com.example.mkasa.chessApp67;

import java.io.Serializable;

/**
 * Created by Michael Russo and Mustafa Kasabchy on 12/4/2017.
 */

public abstract class ChessPiece implements Serializable {


    private static final long serialVersionUID = 1l;
    public String[] column = {"a","b","c","d","e","f","g","h"};
    public String[] rows = {"1","2","3","4","5","6","7","8"};
    public boolean enPassant = false;
    public boolean exchange =false;
    public String color, position, name, type, change;
    public boolean checkMove = false;
    public int x_coord, y_coord;


    public abstract boolean movePiece(ChessPiece[][]chessGrid, int verticalMove, int horizantalMove);

    public abstract boolean check(ChessPiece[][]chessGrid);
    //this will return the white or block chess piece
    public String getColor(){
        return color;
    }
    //this will print the position of every piece and it will update with every new position
    public String getPosition(){return position;}

    public void setPosition(String position){this.position = position;}
    //this will return the name of the chess piece
    public void getName(){
        System.out.print(name+ " ");
    }
    // this will return a specific chess piece
    public String getType(){
        return type;
    }

    // this method will check the validity of each chess piece then will move the price to new postion if valid
    // also will check all illegal movement
    public boolean inValid(ChessPiece[][] chessGrid, String checkValidity, String soldier){

        if(checkValidity.length()!=2 ){
            return false;
        }
        int y_pos = checkValidity.charAt(0)-97;
        int x_pos = checkValidity.charAt(1)-49;

        this.change = soldier;
        x_pos =  (x_pos-7)*(-1);
        if(x_pos>7 ||x_pos< 0 || y_pos>7 || y_pos<0){
            return false;
        }

        //this method will safe the new chess piece position and delete the old position by setting to null
        ChessPiece[][] board = chessGrid;

        if(this.movePiece(board, x_pos,y_pos)){
            int x = this.x_coord;
            int y = this.y_coord;

            this.y_coord = y_pos;
            this.x_coord = x_pos;
            this.position = checkValidity;
            if(chessGrid[x_pos][y_pos]!=null){
                chessGrid[x_pos][y_pos].position = "null";
            }

            chessGrid[x_pos][y_pos]= chessGrid[x][y];
            chessGrid[x][y]=null;
            return true;
        }


        return false;

    }




}
