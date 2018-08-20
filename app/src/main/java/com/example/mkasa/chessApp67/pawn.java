package com.example.mkasa.chessApp67;

/**
 * Created by Michael Russo and Mustafa Kasabchy on 12/4/2017.
 */

public class pawn extends ChessPiece {

    private static final long serialVersionUID = 1l;

    String[] column = {"a","b","c","d","e","f","g","h"};
    String[] rows = {"1","2","3","4","5","6","7","8"};

    public pawn(String color, int x, int y){
        this.type = "Pawn";
        this.color = color;
        this.x_coord = x;
        this.y_coord = y;

        if(color.equals("white")){
            this.name = "wP";
            this.position = column[y]+2;

        }
        else if(color.equals("black")){
            this.name = "bp";
            this.position = column[y]+ 7;
        }

    }




    @Override
    public boolean movePiece(ChessPiece[][] board, int x, int y) {

        if(this.getColor().compareToIgnoreCase("white")==0){
            if((this.x_coord - x)<1){
                return false;
            }
        }else{
            if((this.x_coord - x)>-1){
                return false;
            }
        }

        boolean move_one = false;
        boolean move_two = false;
        boolean valid_move = false;
        int x_coord = Math.abs(this.x_coord - x);
        int y_coord = Math.abs(this.y_coord- y);

        if ( y_coord == 0 || x_coord == 2){
            move_one = false;
            move_two = true;

            if(x_coord == 2){
                //check if this is first move of pawn
                if(this.getColor().compareToIgnoreCase("white")==0){
                    //white pawn should be on x==6
                    if (this.x_coord != 6) {
                        return false;
                    }
                } else {
                    //black pawn should be on x==1
                    if (this.x_coord != 1) {
                        return false;
                    }
                }
                valid_move = true;

                /*move_one = false;
                move_two = true;*/
            } else  if(y_coord == 0 && x_coord == 1 ) {
                move_one = true;
                move_two = false;
                if(board[x][y] == null){
                    valid_move = false;
                } else {
                    return false;
                }
            }



        } else if (y_coord == 1 && x_coord == 1){
            move_one = true;
            move_two = false;

            if(board[x][y] != null && !board[x][y].color.equalsIgnoreCase(this.color)) {
                //usual move
                valid_move = true;

            } else if(board[this.x_coord][y] != null && !board[this.x_coord][y].color.equalsIgnoreCase(this.color)
                    && board[this.x_coord][y].enPassant){
                //en passant
                valid_move = true;
            } else {
                return false;
            }

        } else{
            return false;
        }


        boolean output = false;




        if (move_one){
            output = board[x][y] == null ;
            this.exchange =  (this.color.equalsIgnoreCase("black") && x== 7);  // checking if its black and reaches all the to the white territoyr

            if (!this.exchange){

                this.exchange =  (this.color.equalsIgnoreCase("white") && x == 0); // checking if its white and reaches all the to the black territory
            }
        } else if (move_two){
            output = /*(this.checkMove == false) &&*/	 (board[x][y]== null); //all condition matches
            enPassant = true;

        }


        if (valid_move){


            if ((this.color.equalsIgnoreCase("black")&& this.x_coord == 4)|| (this.color.equalsIgnoreCase("white")&& this.x_coord == 3)){

                output = ( board[x][y] ==null ) && ((board[this.x_coord][y] != null)&& (board[this.x_coord][y].type.equalsIgnoreCase("Pawn")
                        && (!board[this.x_coord][y].color.equalsIgnoreCase(this.color))&& (board[this.x_coord][y].enPassant== true/*enPassant*/)) );

                if (output){

                    board[this.x_coord][y] = null;
                }

            } if(!output){

                output = (board[x][y] !=null ) && (!(board[x][y].color.equalsIgnoreCase(this.color)));
            }
            this.exchange =  (this.color.equalsIgnoreCase("black") && x == 7);

            if (!this.exchange){

                this.exchange =  (this.color.equalsIgnoreCase("white") && x == 0);

            }

        }



        if (output){

            this.checkMove = true;

            if(this.exchange){




                if (this.change == null){

                    board[this.x_coord][this.y_coord]= new Queen(this.color,x,y);
                    return output;
                }
                /////////////////////////////
                change = change.toLowerCase();

                if ("n".equals(change)) {
                    board[this.x_coord][this.y_coord] = (new Knight(this.color, x, y));
                } else if ("b".equals(change)) {
                    board[this.x_coord][this.y_coord] = (new Bishop(this.color, x, y));
                } else if ("r".equals(change)) {
                    board[this.x_coord][this.y_coord] = (new Rook(this.color, x, y));
                } else {
                    board[this.x_coord][this.y_coord] = new Queen(this.color, x, y);
                }
                ///////////////////////////////////////

            }

            if(move_one){
                enPassant = false;
            }

        }


        return output;
    }




    public boolean check(ChessPiece[][] board) {


        int xCordinate = this.x_coord;
        int yCordinate = this.y_coord;

        if (yCordinate <7 ) {


            if (this.color.equalsIgnoreCase("White") && xCordinate >0){ // checking for white side

                if (board[xCordinate -1][yCordinate +1] != null && board[xCordinate -1][yCordinate +1].type.equalsIgnoreCase("King") &&
                        (!board[xCordinate -1][yCordinate +1].color.equalsIgnoreCase(this.color))){

                    return true;
                }

            }else if (this.color.equalsIgnoreCase("black")&& xCordinate <7){ // checking for black side

                if (board[xCordinate +1][yCordinate +1] != null && board[xCordinate +1][yCordinate +1].type.equalsIgnoreCase("King") &&
                        (!board[xCordinate +1][yCordinate +1].color.equalsIgnoreCase(this.color))){

                    return true;
                }

            }
        }


        if (yCordinate > 0) {



            if (this.color.equalsIgnoreCase("White") && xCordinate <0){


                if (board[xCordinate -1][yCordinate -1] != null && board[xCordinate -1][yCordinate -1].type.equalsIgnoreCase("King") &&
                        (!board[xCordinate -1][yCordinate -1].color.equalsIgnoreCase(this.color))){

                    return true;

                }
            }
            else if (this.color.equalsIgnoreCase("black")&& xCordinate <7){

                if (board[xCordinate +1][yCordinate -1] != null && board[xCordinate +1][yCordinate -1].type.equalsIgnoreCase("King") &&
                        (!board[xCordinate +1][yCordinate -1].color.equalsIgnoreCase(this.color))){

                    return true;
                }

            }

        }
        return false;

    }
}
