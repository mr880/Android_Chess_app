package com.example.mkasa.chessApp67;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Michael Russo and Mustafa Kasabchy on 12/4/2017.
 */

public class SavedGames extends AppCompatActivity {

    private ListView list;
    private SavedGamesAdapter adapter;
    private GameActivity game;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved);
        list = findViewById(R.id.list);
        adapter = new SavedGamesAdapter(getApplicationContext(), R.layout.list_item);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String filename = game.recordedGames.get(i).getFileName();
                Intent intent = new Intent(SavedGames.this, ReplayGame.class);
                intent.putExtra("filename", filename);
                startActivity(intent);
            }
        });
        fillList();

    }

    private void fillList() {

        try {
            game = SaveData.readGames(this.getApplicationContext());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(game != null){
            sort(true);
            adapter.addAll(game.recordedGames);
            adapter.notifyDataSetChanged();
        }
    }
    private Menu mOptionsMenu;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inf = new MenuInflater(this);
        inf.inflate(R.menu.saved, menu);
        mOptionsMenu = menu;
        menu.findItem(R.id.sort).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_name:
                mOptionsMenu.findItem(R.id.menu_name).setChecked(true);
                mOptionsMenu.findItem(R.id.menu_date).setChecked(false);
                sort(true);
                break;
            case R.id.menu_date:
                mOptionsMenu.findItem(R.id.menu_name).setChecked(false);
                mOptionsMenu.findItem(R.id.menu_date).setChecked(true);
                sort(false);
                break;
        }
        adapter.clear();
        adapter.addAll(game.recordedGames);
        adapter.notifyDataSetChanged();
        return true;
    }

    private void sort(boolean name){
        if(name){
            Collections.sort(game.recordedGames, new Comparator<RecordMoves>() {
                @Override
                public int compare(RecordMoves recordMoves, RecordMoves t1) {
                    return recordMoves.getFileName().compareTo(t1.getFileName());
                }
            });
        } else {
            Collections.sort(game.recordedGames, new Comparator<RecordMoves>() {
                @Override
                public int compare(RecordMoves recordMoves, RecordMoves t1) {
                    return recordMoves.getCalDate().getTime().compareTo(t1.getCalDate().getTime());
                }
            });
        }
    }

    private class SavedGamesAdapter extends  ArrayAdapter<RecordMoves> {

        public SavedGamesAdapter(@NonNull Context context, int resource) {
            super(context, resource);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View v = super.getView(position, convertView, parent);

            ((TextView)v.findViewById(android.R.id.text1)).setText(getItem(position).getFileName()
                    + " (" + getItem(position).getStrnDate().replace('\n', ' ') + ")" );

            return v;
        }
    }
}
