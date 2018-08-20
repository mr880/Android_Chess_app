package com.example.mkasa.chessApp67;

import java.io.Serializable;

/**
 * Created by Michael Russo and Mustafa Kasabchy on 12/4/2017.
 */

public class Move implements Serializable {

    private static final long serialVersionUID = 1l;

    String fromIndex;
    String toIndex;
    String promotion;
    Move(String fromIndex, String toIndex){
        this.fromIndex = fromIndex;
        this.toIndex = toIndex;
    }
    public void setPromotion(String promo){
        promotion = promo;
    }


}

