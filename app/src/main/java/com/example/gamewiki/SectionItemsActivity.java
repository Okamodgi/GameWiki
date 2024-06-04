// SectionItemsActivity.java
package com.example.gamewiki;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.SimpleCursorAdapter;

public class SectionItemsActivity extends AppCompatActivity {

    private ListView listViewItems;
    private DatabaseHelper dbHelper;
    private SimpleCursorAdapter adapter;
    private long sectionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sections_items);

        listViewItems = findViewById(R.id.list_items);
        dbHelper = new DatabaseHelper(this);

        sectionId = getIntent().getLongExtra("sectionId", -1);

        displayItems(sectionId);
    }

    private void displayItems(long sectionId) {
        Cursor cursor = dbHelper.getReadableDatabase().query(DatabaseHelper.TABLE_ITEMS,
                new String[]{DatabaseHelper.COLUMN_ID, DatabaseHelper.COLUMN_ITEM_NAME},
                DatabaseHelper.COLUMN_SECTION_ID_ITEMS + "=?",
                new String[]{String.valueOf(sectionId)},
                null, null, null);

        adapter = new SimpleCursorAdapter(
                this,
                android.R.layout.simple_list_item_1,
                cursor,
                new String[]{DatabaseHelper.COLUMN_ITEM_NAME},
                new int[]{android.R.id.text1},
                0);
        listViewItems.setAdapter(adapter);

        listViewItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                long itemId = cursor.getLong(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID));
                openItemDetailsActivity(itemId);
            }
        });
    }

    private void openItemDetailsActivity(long itemId) {
        Intent intent = new Intent(this, ItemDetailsActivity.class);
        intent.putExtra("itemId", itemId);
        startActivity(intent);
    }
}
