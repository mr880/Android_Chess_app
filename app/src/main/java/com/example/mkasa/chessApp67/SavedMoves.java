package com.example.mkasa.chessApp67;

import android.content.Context;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
/**
 * Created by Michael Russo and Mustafa Kasabchy on 12/4/2017.
 */

public class SavedMoves {

    static String FILENAME = "gameState";

    private SavedMoves(){}

    static public Object copyAll(Object oldObject, Context content) throws Exception{
        ObjectOutputStream oos = null;
        ObjectInputStream ois = null;

        try
        {
            FileInputStream fin;


            FileOutputStream fos;
            fos = content.openFileOutput(FILENAME, Context.MODE_PRIVATE);
            oos = new ObjectOutputStream(fos);
            // serialize and pass the object
            oos.writeObject(oldObject);
            oos.flush();
            fin = content.openFileInput(FILENAME);
            ois = 	new ObjectInputStream(fin);

        }
        catch(IOException e){
            e.getMessage();
        }
        catch(Exception e)
        {
            Log.d("Chess", "Exception in ObjectCloner = " + e);
            throw(e);
        }
        // return the new object
        return (Board)ois.readObject();

    }
}

