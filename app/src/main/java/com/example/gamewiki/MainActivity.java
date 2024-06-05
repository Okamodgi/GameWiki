package com.example.gamewiki;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.SimpleCursorAdapter;
import android.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private DatabaseHelper dbHelper;
    private SimpleCursorAdapter adapter;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.list_game);
        dbHelper = new DatabaseHelper(this);

        displayGames();
        setupListViewClickListener();
    }

    private void displayGames() {
        Cursor cursor = dbHelper.getReadableDatabase().query(DatabaseHelper.TABLE_GAMES,
                new String[]{DatabaseHelper.COLUMN_ID, DatabaseHelper.COLUMN_GAME_NAME},
                null, null, null, null, null);

        adapter = new SimpleCursorAdapter(
                this,
                android.R.layout.simple_list_item_1,
                cursor,
                new String[] { DatabaseHelper.COLUMN_GAME_NAME },
                new int[] { android.R.id.text1 },
                0);
        listView.setAdapter(adapter);
    }

    private void setupListViewClickListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                String selectedGame = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_GAME_NAME));
                openGameSectionsActivity(selectedGame);
            }
        });
    }

    private void openGameSectionsActivity(String gameName) {
        Intent intent = new Intent(this, GameSectionsActivity.class);
        intent.putExtra("gameName", gameName);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbHelper.close();
    }
}
