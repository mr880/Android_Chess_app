package com.example.mkasa.chessApp67;

import android.util.Log;

/**
 * Created by Michael Russo and Mustafa Kasabchy on 12/4/2017.
 */



public class Queen extends ChessPiece {

    String[] column = {"a","b","c","d","e","f","g","h"};
    String[] rows = {"1","2","3","4","5","6","7","8"};
    public Queen(String color, int x, int y){
        type = "Queen";
        if(color =="white"){
            this.color = color;
            this.x_coord = x;
            this.y_coord = y;

            name = "wQ";
        }else{
            this.color = color;
            this.x_coord = x;
            this.y_coord = y;

            name = "bQ";
        }

        this.position = column[y]+rows[Math.abs((rows.length-1)-x)];
    }
    public boolean movePiece(ChessPiece[][] board, int x, int y) {

        int newX = Math.abs(this.x_coord - x);
        int newY = Math.abs(this.y_coord - y);

        if(newX==0 && newY==0){
            return false;
        }
        if(newX ==0 || newY==0){
            if(newX==0){

                int temp = this.y_coord;

                if(temp>y){

                    temp--;
                    if(temp>-1){
                        while(board[this.x_coord][temp]==null && temp!=y){temp--;}
                    }

                }else{

                    temp++;
                    if(temp<8){
                        while(board[this.x_coord][temp]==null && temp!=y){temp++;}
                    }

                }

                if(temp!=y){
                    Log.d("Chess", "there is a abustructive stucture in path, invalid move");
                    return false;
                }

                if(board[x][y]!=null && board[x][y].getColor()==this.getColor()){
                    Log.d("Chess", "your piece is there");
                    return false;
                }
                this.checkMove = true;
                return true;

            }else{

                int temp = this.x_coord;

                if(temp>x){
                    temp--;
                    if(temp>-1){
                        while(board[temp][this.y_coord]==null && temp!=x){temp--;}
                    }
                }else{

                    temp++;
                    if(temp<8){
                        while(board[temp][this.y_coord]==null && temp!=x){temp++;}
                    }

                }

                if(temp!=x){
                    Log.d("Chess", "these is a abustructive stucture in path");
                    return false;
                }

                if(board[x][y]!=null && board[x][y].getColor()==this.getColor()){
                    Log.d("Chess", "your piece is there");
                    return false;
                }
                this.checkMove = true;
                return true;

            }
        }else if(newY==newX){
            int tempX = this.x_coord;
            int tempY = this.y_coord;
            if(tempX>x){
                if(tempY>y){

                    tempY--;
                    tempX--;
                    while(board[tempX][tempY]==null && tempY!=y){
                        tempX--;
                        tempY--;
                    }
                }else{
                    tempY++;
                    tempX--;
                    while(board[tempX][tempY]==null && tempY!=y){
                        tempX--;
                        tempY++;
                    }

                }
            }else{
                if(tempY>y){

                    tempY--;
                    tempX++;
                    while(board[tempX][tempY]==null && tempY!=y){
                        tempX++;
                        tempY--;
                    }
                }else{

                    tempY++;
                    tempX++;
                    while(board[tempX][tempY]==null && tempY!=y){
                        tempX++;
                        tempY++;
                    }
                }
            }
            if(tempY!=y){

                return false;
            }
            if(board[x][y]!=null && board[x][y].getColor()==this.getColor()){

                return false;
            }
            this.checkMove = true;
            return true;
        }else{
            return false;
        }
    }

    public boolean check(ChessPiece[][] board){
        int tempY = this.y_coord;
        int tempX = this.x_coord;
        ChessPiece tempPiece;

        tempY--;
        tempX++;
        while(tempY>-1 && tempX<8 &&board[tempX][tempY]==null){
            tempY--;
            tempX++;
        }
        if(tempY>=0 && tempX<=7){
            tempPiece= board[tempX][tempY];
            if(tempPiece.getType().compareTo("King")==0 &&tempPiece.getColor().compareTo(this.getColor())!=0){
                return true;
            }
        }


        tempY = this.y_coord;
        tempX = this.x_coord+1;
        while(tempX<8 && board[tempX][this.y_coord]==null){
            tempX++;
        }
        if(tempX<=7){
            tempPiece= board[tempX][this.y_coord];
            if(tempPiece.getType().compareTo("King")==0 &&tempPiece.getColor().compareTo(this.getColor())!=0){
                return true;
            }
        }



        tempX = this.x_coord +1;
        tempY = this.y_coord +1;
        while(tempY<8 && tempX<8 &&board[tempX][tempY]==null){
            tempY++;
            tempX++;
        }
        if(tempY<8 && tempX<8){
            tempPiece= board[tempX][tempY];
            if(tempPiece.getType().compareTo("King")==0 &&tempPiece.getColor().compareTo(this.getColor())!=0){
                return true;
            }
        }



        tempX = this.x_coord;
        tempY = this.y_coord+1;
        while(tempY<8 &&board[this.x_coord][tempY]==null){tempY++;}
        if(tempY<8){
            tempPiece= board[this.x_coord][tempY];
            if(tempPiece.getType().compareTo("King")==0 &&tempPiece.getColor().compareTo(this.getColor())!=0){
                return true;
            }
        }


        tempX = this.x_coord -1;
        tempY = this.y_coord +1;
        while(tempY<8 && tempX>-1 &&board[tempX][tempY]==null){
            tempY++;
            tempX--;
        }
        if(tempY<8 && tempX>-1){
            tempPiece= board[tempX][tempY];
            if(tempPiece.getType().compareTo("King")==0 &&tempPiece.getColor().compareTo(this.getColor())!=0){
                return true;
            }
        }



        tempY = this.y_coord;
        tempX = this.x_coord-1;
        while(tempX>-1 && board[tempX][this.y_coord]==null){
            tempX--;
        }
        if(tempX>-1){
            tempPiece= board[tempX][this.y_coord];
            if(tempPiece.getType().compareTo("King")==0 &&tempPiece.getColor().compareTo(this.getColor())!=0){
                return true;
            }
        }



        tempX = this.x_coord -1;
        tempY = this.y_coord -1;
        while(tempY>-1 && tempX>-1 &&board[tempX][tempY]==null){
            tempY--;
            tempX--;
        }
        if(tempY>-1 && tempX>-1){
            tempPiece= board[tempX][tempY];
            if(tempPiece.getType().compareTo("King")==0 &&tempPiece.getColor().compareTo(this.getColor())!=0){
                return true;
            }
        }




        tempX = this.x_coord;
        tempY = this.y_coord-1;
        while(tempY>-1 &&board[this.x_coord][tempY]==null){tempY--;}
        if(tempY>-1){
            tempPiece= board[this.x_coord][tempY];
            if(tempPiece.getType().compareTo("King")==0 &&tempPiece.getColor().compareTo(this.getColor())!=0){
                return true;
            }
        }

        return false;
    }



}
