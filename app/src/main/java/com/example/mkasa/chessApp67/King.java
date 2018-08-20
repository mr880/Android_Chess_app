package com.example.mkasa.chessApp67;

/**
 * Created by Michael Russo and Mustafa Kasabchy on 12/4/2017.
 */

public class King extends ChessPiece {

    public King(String color, int x, int y){
        type = "King";
        if(color =="white"){
            this.color = color;
            this.x_coord = x;
            this.y_coord = y;
            position = "e1";
            name = "wK";
        }else{
            this.color = color;
            this.x_coord = x;
            this.y_coord = y;
            position = "e8";
            name = "bK";
        }
    }

    public  boolean movePiece(ChessPiece[][] board, int x, int y){

        int deltaX = Math.abs(this.x_coord - x);
        int deltaY = Math.abs(this.y_coord - y);


        if(/*this.checkMove==false &&*/ (deltaY==2) && deltaX==0){
            char sideToMove;

            if(this.y_coord>y){
                if(board[this.x_coord][0] == null || board[this.x_coord][0].checkMove==true){
                    return false;
                }
                sideToMove = 'l';
            }else{
                if(board[this.x_coord][7] == null || board[this.x_coord][7].checkMove==true){
                    return false;
                }
                sideToMove = 'r';
            }
            return castling(sideToMove, board);
        }

        if(deltaX>1 || deltaY>1){
            return false;
        }else if(this.y_coord ==y && this.x_coord == x){

            return false;
        }else{

            if(board[x][y]!=null &&board[x][y].getColor()==this.getColor()){

                return false;
            }else{
                this.checkMove = true;
                return true;
            }
        }
    }

    public boolean check(ChessPiece[][] board){

        int tempX = this.x_coord;
        int tempY = this.y_coord;



        tempX++;
        tempY--;
        if(tempY>-1 && tempX<8){
            ChessPiece tempPiece = board[tempX][tempY];
            if(tempPiece!=null && tempPiece.getType()=="King" && this.getColor()!=tempPiece.getColor()){ return true;}
        }



        tempY = this.y_coord -1;
        tempX = this.x_coord;
        if(tempY>-1){
            ChessPiece tempPiece = board[tempX][tempY];
            if(tempPiece!=null && tempPiece.getType()=="King" && this.getColor()!=tempPiece.getColor()){ return true;}
        }


        tempY = this.y_coord -1;
        tempX = this.x_coord -1;
        if(tempY>-1 && tempX>-1){
            ChessPiece tempPiece = board[tempX][tempY];
            if(tempPiece!=null && tempPiece.getType()=="King" && this.getColor()!=tempPiece.getColor()){ return true;}
        }




        tempY = this.y_coord;
        tempX = this.x_coord -1;
        if(tempX>-1){
            ChessPiece tempPiece = board[tempX][tempY];
            if(tempPiece!=null && tempPiece.getType()=="King" && this.getColor()!=tempPiece.getColor()){ return true;}
        }



        tempX = this.x_coord -1;
        tempY = this.y_coord +1;
        if(tempY<8 && tempX>-1){
            ChessPiece tempPiece = board[tempX][tempY];
            if(tempPiece!=null && tempPiece.getType()=="King" && this.getColor()!=tempPiece.getColor()){ return true;}
        }


        tempX = this.x_coord;
        tempY = this.y_coord+1;
        if(tempY<8){
            ChessPiece tempPiece = board[tempX][tempY];
            if(tempPiece!=null && tempPiece.getType()=="King" && this.getColor()!=tempPiece.getColor()){ return true;}
        }



        tempX = this.x_coord+1;
        tempY = this.y_coord +1;
        if(tempX<8 && tempY<8){
            ChessPiece tempPiece = board[tempX][tempY];
            if(tempPiece!=null && tempPiece.getType()=="King" && this.getColor()!=tempPiece.getColor()){ return true;}
        }


        tempY = this.y_coord;
        tempX = this.x_coord+1;
        if(tempX<8){
            ChessPiece tempPiece = board[tempX][tempY];
            if(tempPiece!=null && tempPiece.getType()=="King" && this.getColor()!=tempPiece.getColor()){ return true;}
        }
        return false;
    }

    private boolean castling(char castl, ChessPiece[][] board){

        int x = this.x_coord;
        int y;
        int count  = this.y_coord;
        if(castl == 'l'){

            while(count>0){
                if(castlingCheck(board, count)){
                    return false;
                }
                if(board[this.x_coord][--count]!=null){
                    if(count==0){
                        continue;
                    }
                    return false;
                }
            }
            y = this.y_coord - 2;
            board[x][y+1] = board[x][0];
            board[x][0] = null;
            if(this.x_coord==7){

                board[x][y+1].position = "d1";
                board[x][y+1].x_coord = 7;
                board[x][y+1].y_coord = 3;
            }else{

                board[x][y+1].position = "d8";
                board[x][y+1].x_coord = 0;
                board[x][y+1].y_coord = 3;
            }

            board[x][y+1].checkMove = true;
        }else{

            while(count<7){
                if(castlingCheck(board, count)){
                    return false;
                }
                if(board[this.x_coord][++count]!=null){
                    if(count==7){
                        continue;
                    }
                    return false;
                }
            }

            y = this.y_coord + 2;
            board[x][y-1] = board[x][y+1];
            board[x][y+1] = null;
            if(this.x_coord==7){

                board[x][y-1].position = "f1";
                board[x][y-1].x_coord = 7;
                board[x][y-1].y_coord = 5;
            }else{

                board[x][y-1].position = "f8";
                board[x][y-1].x_coord = 0;
                board[x][y-1].y_coord = 5;
            }
            board[x][y-1].checkMove=true;
        }

        this.checkMove = true;
        return true;

    }

    private boolean castlingCheck(ChessPiece[][] board, int currColumn){


        ChessPiece tempPiece = null;
        ChessPiece[][] tempBoard = new ChessPiece[8][8];
        //////////////////////
        int i = 0;
        while(i<board.length){
            int j =0;
            while(j<board.length){
                tempBoard[i][j]= board[i][j];
                j++;
            }
            i++;
        }
        /////////////////////////////

        tempBoard[this.x_coord][currColumn] = tempBoard[this.x_coord][this.y_coord];

        if(currColumn!=this.y_coord){
            tempBoard[this.x_coord][this.y_coord]=null;
        }
        //////////
        int n = 1;
        while(n<tempBoard.length){
            for(int j=0; j<tempBoard.length; j++){
                tempPiece = tempBoard[n][j];
                if(tempPiece!=null){
                    if(tempPiece.check(tempBoard)){
                        return true;
                    }
                }
            }
            n++;
        }
        ///////////
        return false;

    }
}
