package com.example.mkasa.chessApp67;

import android.util.Log;

/**
 * Created by Michael Russo and Mustafa Kasabchy on 12/4/2017.
 */

public class  Bishop extends ChessPiece {

    private static final long serialVersionUID = 1l;

    public Bishop(String color, int x , int y){


        this.color = color;
        this.x_coord = x;
        this.y_coord = y;
        this.type = "Bishop";

        this.name = color.equalsIgnoreCase("White")? "wB" : "bB";

        this.position = column[y]+rows[Math.abs((rows.length-1)-x)];
    }



    public boolean movePiece(ChessPiece[][] board, int x, int y) {


        int delta_x = Math.abs(this.x_coord - x);
        int delta_y = Math.abs(this.y_coord- y);

        if (delta_x != delta_y){

            Log.d("Chess", "Illegal Move, try again..");
            return false;
        }

        boolean upRight =false, upLeft = false, downRight = false, downLeft = false;

        upRight = x < this.x_coord && y >this.y_coord ? true : false;
        upLeft = x < this.x_coord && y < this.y_coord? true : false;

        downRight =  x> this.x_coord && y >this.y_coord ? true : false;
        downLeft =  x > this.x_coord && y <this.y_coord;


        if (upRight){

            directionsSort condition = new directionsSort(){

                @Override
                public boolean apply(int x, int y, int fx, int fy) {
                    if(x>=fx+1 &&y<=fy-1){
                        return true;
                    }
                    return false;
                }
                };
            return filter(board, this.x_coord-1, this.y_coord+1, "-+", condition, x, y);

        }
        else if (upLeft){



            directionsSort condition = new directionsSort() {
                @Override
                public boolean apply(int x, int y, int fx, int fy) {
                    if(x>=fx+1 && y>=fy+1){
                        return true;
                    }
                    return false;
                }
            };
            //directionsSort condition = (cordinatex, cordinatey, finalx, finaly) -> cordinatex >= finalx+1 && cordinatey >=finaly+1 ;

            return filter(board, this.x_coord-1, this.y_coord-1, "--", condition, x, y);

        }

        else if (downRight){

            directionsSort condition = new directionsSort() {
                @Override
                public boolean apply(int x, int y, int fx, int fy) {
                    if(x<=fx-1 && y<=fy-1){
                        return true;
                    }
                    return false;
                }
            };
            //directionsSort condition = (cordinatex, cordinatey, finalx, finaly) -> cordinatex <= finalx-1 && cordinatey <=finaly-1 ;

            return filter(board, this.x_coord+1, this.y_coord+1, "++", condition, x, y);

        }

        else if(downLeft){


            directionsSort condition = new directionsSort() {
                @Override
                public boolean apply(int x, int y, int fx, int fy) {
                    if(x<=fx-1 && y>=fy+1){
                        return true;
                    }
                    return false;
                }
            };
            //directionsSort condition = (cordinatex, cordinatey, finalx, finaly) -> cordinatex <= finalx-1 && cordinatey >=finaly+1 ;

            return filter(board, this.x_coord+1, this.y_coord-1, "+-", condition, x, y);


        }

        Log.d("Chess", "Illegal move, try again.");
        return false;

    }



    public boolean check(ChessPiece[][] board) {

        boolean upRight = false, upLeft = false , downRight = false, downLeft = false;
        int xpath,ypath;




        upLeft = this.x_coord >0 && this.y_coord>0 ? true: false;
        downRight = this.x_coord< 7 && this.y_coord <7? true : false;

        upRight = this.x_coord >0 && this.y_coord<7 ? true :false;
        downLeft = this.x_coord <7 && this.y_coord >0 ;

        if (upLeft){

            xpath = this.x_coord-1;
            ypath = this.y_coord-1;

            while (xpath >= 0 && ypath >= 0){

                if (board[xpath][ypath]== null){}

                else if (!board[xpath][ypath].type.equalsIgnoreCase("king")){  break;}

                else if (!board[xpath][ypath].color.equalsIgnoreCase(this.color)){ return true;}

                xpath --;
                ypath--;
            }

        }

        if (downRight){

            xpath = this.x_coord+1;
            ypath = this.y_coord+1;


            while (xpath <= 7 && ypath <= 7){

                if (board[xpath][ypath]== null){}

                else if (!board[xpath][ypath].type.equalsIgnoreCase("king")){ break;}

                else if (!board[xpath][ypath].color.equalsIgnoreCase(this.color)){ return  true; }

                xpath ++;
                ypath++;
            }

        }

        if (upRight){

            xpath = this.x_coord-1;
            ypath = this.y_coord+1;

            while(xpath >=0 && ypath <=7){

                if (board[xpath][ypath]== null){}

                else if (!board[xpath][ypath].type.equalsIgnoreCase("king")){  break;}

                else if (!board[xpath][ypath].color.equalsIgnoreCase(this.color)){ return true;}

                xpath --;
                ypath++;
            }

        }

        if (downLeft){


            xpath = this.x_coord+1;
            ypath = this.y_coord-1;

            while(xpath <=7 && ypath >=0){

                if (board[xpath][ypath]== null){}

                else if (!board[xpath][ypath].type.equalsIgnoreCase("king")){  break;}

                else if (!board[xpath][ypath].color.equalsIgnoreCase(this.color)){ return true;}

                xpath ++;
                ypath--;

            }
        }

        return false;

    }


    private boolean filter(ChessPiece[][] board, int pathX , int pathY , String sings, directionsSort function, int x, int y){





        while (function.apply(pathX, pathY, x, y)){


            if (board[pathX][pathY] !=null){

                Log.d("Chess", "Illegal move, try again.");
                return false;
            }
            pathX = sings.charAt(0) == '+'? pathX +1 : pathX-1;
            pathY = sings.charAt(1) == '+'? pathY+1 : pathY-1;
        }

        return  (board[x][y] == null)
                || (board[x][y]!= null && (!board[x][y].color.equalsIgnoreCase(this.color)));
    }
}

interface directionsSort{


    boolean apply(int x , int y, int fx , int fy);
}

