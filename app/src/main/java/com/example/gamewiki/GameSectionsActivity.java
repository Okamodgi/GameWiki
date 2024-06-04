// GameSectionsActivity.java
package com.example.gamewiki;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;

public class GameSectionsActivity extends AppCompatActivity {

    private ListView listViewSections;
    private DatabaseHelper dbHelper;
    private ArrayAdapter<String> adapter;
    private String gameName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_sections);

        listViewSections = findViewById(R.id.list_sections);
        dbHelper = new DatabaseHelper(this);

        // Получаем название игры, переданное из MainActivity
        gameName = getIntent().getStringExtra("gameName");

        displaySections();
        setupListViewClickListener();
    }

    private void displaySections() {
        List<String> sections = dbHelper.getSectionsForGame(gameName);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, sections);
        listViewSections.setAdapter(adapter);
    }

    private void setupListViewClickListener() {
        listViewSections.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedSection = (String) parent.getItemAtPosition(position);
                Intent intent = new Intent(GameSectionsActivity.this, SectionItemsActivity.class);
                intent.putExtra("sectionName", selectedSection);
                startActivity(intent);
            }
        });
    }
}
