package com.example.mkasa.chessApp67;

import android.util.Log;

/**
 * Created by Michael Russo and Mustafa Kasabchy on 12/4/2017.
 */

public class Knight extends ChessPiece{
    String[] column = {"a","b","c","d","e","f","g","h"};
    String[] rows = {"1","2","3","4","5","6","7","8"};

    public Knight(String color, int x, int y) {

        this.type = "Knight";
        this.x_coord = x;
        this.y_coord = y;
        this.color = color;

        this.name = color.equalsIgnoreCase("White")? "wN" : "bN";
        this.position = column[y]+rows[Math.abs((rows.length-1)-x)];
    }


    public boolean check(ChessPiece[][] board) {
        boolean passDown = false, passUp = false, rightSide = false, leftSide = false;

        int downJump = this.x_coord +2;
        passDown = downJump <=7 ? true :false;

        int upJump = this.x_coord -2;
        passUp = upJump >= 0 ? true : false;


        int rightJump = this.y_coord +2 ;
        rightSide = rightJump <= 7 ? true : false;

        int leftJump = this.y_coord -2;
        leftSide = leftJump >=0 ? true : false;


        if (passDown){



            if (this.y_coord < 7) {



                if (checkJumps(board,downJump, y_coord+1, 0)){

                    return true;
                }

            }
            if (this.y_coord >0 ){


                if (checkJumps(board,downJump, y_coord-1, 0)){

                    return true;
                }
            }

        }

        if (passUp){


            if (this.y_coord <7){



                if (checkJumps(board,upJump, y_coord+1, 0)){

                    return true;
                }

            }

            if (this.y_coord > 0){


                if (checkJumps(board,upJump, y_coord-1, 0)){

                    return true;
                }

            }


        }

        if (rightSide){

            if (this.x_coord >0){



                if (checkJumps(board,rightJump, x_coord-1, 1)){

                    return true;
                }

            }
            if (this.x_coord<7){


                if (checkJumps(board,rightJump, x_coord+1, 1)){

                    return true;
                }
            }
        }

        if (leftSide){


            if (this.x_coord >0){



                if (checkJumps(board,leftJump, x_coord-1, 1)){

                    return true;
                }
            }
            if (this.x_coord<7){



                if (checkJumps(board,leftJump, x_coord+1, 1)){

                    return true;
                }


            }

        }


        return false;
    }

    public boolean movePiece(ChessPiece[][] board, int x, int y) {

        boolean moveVertical = false, moveHorizontal = false;

        int differenceX = Math.abs(this.x_coord - x);
        int differenceY = Math.abs(this.y_coord - y);

        if (differenceX ==1 && differenceY == 2){

            moveHorizontal = true;
        }
        else if (differenceX == 2 && differenceY == 1){

            moveVertical = true;
        }


        boolean output = false;

        if (moveHorizontal || moveVertical){


            output = (board[x][y] == null )
                    ||((board[x][y]!= null) && !( board[x][y].color.equalsIgnoreCase(this.color)));
        }

        if (!output){

            Log.d("Chess", "Illegal move, try again");
        }

        return output;
    }



    public boolean checkJumps(ChessPiece[][] board,int jump , int cordinate, int decisionVariable ){


        if (decisionVariable == 0){


            if (board[jump][cordinate] != null
                    && board[jump][cordinate].type.equalsIgnoreCase("King")
                    && !(board[jump][cordinate].color.equalsIgnoreCase(this.color))){

                return true;
            }

        }

        else{


            if (board[cordinate][jump]!=null
                    && board[cordinate][jump].type.equalsIgnoreCase("King")
                    && (!board[cordinate][jump].color.equalsIgnoreCase(this.color))){

                return true;
            }


        }

        return false;
    }




}

