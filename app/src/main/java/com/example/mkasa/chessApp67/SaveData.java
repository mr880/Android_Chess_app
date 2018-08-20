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


public class SaveData {

    private static final String FILENAME = "savedGames.chessApp";

    private SaveData(){};
    public static GameActivity readGames(Context content) throws ClassNotFoundException,IOException {
        ObjectInputStream ois = null;
        FileInputStream ins = null;
        try{
            ins = content.openFileInput(FILENAME);
            ois = new ObjectInputStream(ins);
        }catch(IOException e){
            return null;
        }
        return (GameActivity)ois.readObject();
    }

    public static void saveGames(GameActivity activity, Context content) throws Exception{
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;

        try{
            content.deleteFile(FILENAME);

            fos = content.openFileOutput(FILENAME, Context.MODE_PRIVATE);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(activity);
            oos.flush();
        }catch(Exception e){
            Log.d("Chess", e.getMessage());
        }
    }
}
