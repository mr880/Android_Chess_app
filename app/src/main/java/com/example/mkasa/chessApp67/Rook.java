package com.example.mkasa.chessApp67;

/**
 * Created by Michael Russo and Mustafa Kasabchy on 12/4/2017.
 */
public class Rook extends ChessPiece{



    public Rook(String color, int x, int y){
        type = "Rook";
        if(color =="white"){
            this.color = color;
            this.x_coord= x;
            this.y_coord = y;
            if(y == 0){
                position = "a1";
            }else{
                position = "h1";
            }
            name = "wR";
        }else{
            this.color = color;
            this.x_coord= x;
            this.y_coord = y;
            if(y ==0){
                position = "a8";
            }else{
                position = "h8";
            }
            name = "bR";
        }
    }

    public boolean movePiece(ChessPiece[][] board, int x, int y){

        int delta_x = Math.abs(this.x_coord- x);
        int delta_y = Math.abs(this.y_coord - y);

        if((delta_x == 0 || delta_y ==0) &&delta_x!=delta_y){
            if(delta_x==0){

                int temp = this.y_coord;

                if(temp>y){
                    temp--;
                    if(temp>-1){
                        while(board[this.x_coord][temp]==null && temp!=y){
                            temp--;
                        }
                    }
                }else{
                    temp++;
                    if(temp<8){
                        while(board[this.x_coord][temp]==null && temp!=y){
                            temp++;
                        }
                    }
                }
                if(temp!=y){

                    return false;
                }
                if(board[x][y]!=null && board[x][y].getColor().compareTo(this.getColor())==0){

                    return false;
                }
                this.checkMove = true;
                return true;

            }else{

                int temp = this.x_coord;
                if(temp>x){
                    temp--;
                    if(temp>-1){
                        while(board[temp][this.y_coord]==null && temp!=x){
                            temp--;
                        }
                    }
                }else{
                    temp++;
                    if(temp<8){
                        while(board[temp][this.y_coord]==null && temp!=x){
                            temp++;
                        }
                    }
                }


                if(temp!=x){
                    return false;
                }


                if(board[x][y]!=null && board[x][y].getColor().compareTo(this.getColor())==0){

                    return false;
                }
                this.checkMove = true;
                return true;

            }
        }else{

            return false;
        }
    }

    public boolean check(ChessPiece[][] board){
        int tempX = this.x_coord-1;
        int tempY = this.y_coord;


        while(tempX>-1 && board[tempX][tempY]==null){
            tempX--;
        }
        if(tempX>-1 && board[tempX][tempY].getType().compareTo("King")==0){
            if(board[tempX][tempY]!=null && this.getColor().compareTo(board[tempX][tempY].getColor())!=0){ return true;}
        }

        tempX = this.x_coord+1;

        while(tempX< 8 && board[tempX][tempY]==null){
            tempX++;
        }
        if(tempX<8 && board[tempX][tempY].getType().compareTo("King")==0){
            if(board[tempX][tempY]!=null && this.getColor().compareTo(board[tempX][tempY].getColor())!=0){ return true;}
        }

        tempX = this.x_coord;
        tempY = this.y_coord-1;
        while(tempY > -1 && board[tempX][tempY]==null){
            tempY--;
        }

        if(tempY>-1 && board[tempX][tempY].getType().compareTo("King")==0){
            if(board[tempX][tempY]!=null && this.getColor().compareTo(board[tempX][tempY].getColor())!=0){ return true;}
        }
        tempX = this.x_coord;
        tempY = this.y_coord+1;

        while(tempY< 8 && board[tempX][tempY]==null){
            tempY++;
        }

        if(tempY<8 && board[this.x_coord][tempY].getType().compareTo("King")==0){
            if(board[tempX][tempY]!=null && this.getColor().compareTo(board[tempX][tempY].getColor())!=0){
                return true;}
        }
        return false;
    }
}

