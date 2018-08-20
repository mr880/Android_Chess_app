package com.example.mkasa.chessApp67;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * Created by Michael Russo and Mustafa Kasabchy on 12/4/2017.
 */
public class MainActivity extends AppCompatActivity {

    private static final String dirName = "data";
    private static final String fileName = "user.date";
    public GameActivity gameRecord;
    Button newGame;
    Button savedGames;

    public void onCreate(Bundle saveInstanceState) {

        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_main);

        gameRecord = getGameRecords();
        newGame = (Button) findViewById(R.id.startgame);
        newGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, Rules.class);
                startActivity(intent);
            }
        });
        savedGames = (Button) findViewById(R.id.recordedgame);
        savedGames.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SavedGames.class);
                startActivity(intent);
            }
        });

    }
    private GameActivity getGameRecords(){
        GameActivity record = new GameActivity();

        try{
            record = readGames();
            return record;
        }
        catch(FileNotFoundException e){
            return record;
        } catch (Exception e){
            Log.d("Chess", e.getMessage());
        }
        return record;
    }
    private GameActivity readGames() throws FileNotFoundException, IOException, Exception{
        try{
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(dirName + File.separator + fileName));
            GameActivity record =(GameActivity)ois.readObject();
            ois.close();
            return record;
        }catch (Exception e){
            GameActivity records = new GameActivity();
            return records;
        }
    }
}


