package com.example.gamewiki;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ItemDetailsActivity extends AppCompatActivity {

    private TextView textViewItemName;
    private TextView textViewItemDescription;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);

        textViewItemName = findViewById(R.id.text_item_name);
        textViewItemDescription = findViewById(R.id.text_item_description);
        dbHelper = new DatabaseHelper(this);

        long itemId = getIntent().getLongExtra("itemId", -1);
        displayItemDetails(itemId);
    }

    private void displayItemDetails(long itemId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor itemCursor = db.query(DatabaseHelper.TABLE_ITEMS,
                new String[]{DatabaseHelper.COLUMN_ITEM_NAME},
                DatabaseHelper.COLUMN_ITEM_ID + "=?",
                new String[]{String.valueOf(itemId)},
                null, null, null);

        if (itemCursor.moveToFirst()) {
            String itemName = itemCursor.getString(itemCursor.getColumnIndex(DatabaseHelper.COLUMN_ITEM_NAME));
            textViewItemName.setText(itemName);
        }
        itemCursor.close();

        String itemDescription = dbHelper.getItemDescription(itemId);
        textViewItemDescription.setText(itemDescription);
    }

}
