package com.example.gamewiki;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "games.db";
    private static final int DATABASE_VERSION = 2;

    public static final String TABLE_GAMES = "games";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_GAME_NAME = "game_name";

    public static final String TABLE_SECTIONS = "sections";
    public static final String COLUMN_SECTION_ID = "_id";
    public static final String COLUMN_GAME_ID = "game_id";
    public static final String COLUMN_SECTION_NAME = "section_name";

    public static final String TABLE_ITEMS = "items";
    public static final String COLUMN_ITEM_ID = "_id";
    public static final String COLUMN_SECTION_ID_ITEMS = "section_id";
    public static final String COLUMN_ITEM_NAME = "item_name";

    public static final String TABLE_ITEMS_DETAILS = "items_details";
    public static final String COLUMN_ITEM_DETAILS_ID = "_id"; // Исправлено
    public static final String COLUMN_ITEM_DETAILS_ITEM_ID = "item_id"; // Исправлено
    public static final String COLUMN_ITEM_DESCRIPTION = "item_description";

    private static final String DATABASE_CREATE_GAMES = "create table " +
            TABLE_GAMES + "(" + COLUMN_ID +
            " integer primary key autoincrement, " +
            COLUMN_GAME_NAME + " text not null);";

    private static final String DATABASE_CREATE_SECTIONS = "create table " +
            TABLE_SECTIONS + "(" + COLUMN_SECTION_ID +
            " integer primary key autoincrement, " +
            COLUMN_GAME_ID + " integer not null, " +
            COLUMN_SECTION_NAME + " text not null);";

    private static final String DATABASE_CREATE_ITEMS = "CREATE TABLE " +
            TABLE_ITEMS + "(" + COLUMN_ITEM_ID +
            " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_SECTION_ID_ITEMS + " INTEGER NOT NULL, " +
            COLUMN_ITEM_NAME + " TEXT NOT NULL);";

    private static final String DATABASE_CREATE_ITEMS_DETAILS = "CREATE TABLE " +
            TABLE_ITEMS_DETAILS + "(" + COLUMN_ITEM_DETAILS_ID +
            " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_ITEM_DETAILS_ITEM_ID + " INTEGER NOT NULL, " + // Исправлено
            COLUMN_ITEM_DESCRIPTION + " TEXT NOT NULL);";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE_GAMES);
        database.execSQL(DATABASE_CREATE_SECTIONS);
        database.execSQL(DATABASE_CREATE_ITEMS);
        database.execSQL(DATABASE_CREATE_ITEMS_DETAILS);

        insertInitialData(database);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GAMES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SECTIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS_DETAILS);
        onCreate(db);
    }

    private void insertInitialData(SQLiteDatabase database) {
        long valheimId = insertGame(database, "Valheim");
        long genshinImpactId = insertGame(database, "Genshin Impact");

        long valheimWeaponsId = insertSection(database, valheimId, "Оружие");
        long valheimBossesId = insertSection(database, valheimId, "Боссы");
        long genshinCharactersId = insertSection(database, genshinImpactId, "Персонажи");
        long genshinWorldsId = insertSection(database, genshinImpactId, "Миры");

        insertItem(database, valheimWeaponsId, "Меч");
        insertItem(database, valheimWeaponsId, "Топор");
        insertItem(database, valheimBossesId, "Эйктюр");
        insertItem(database, valheimBossesId, "Древень");

        insertItem(database, genshinCharactersId, "Дилюк");
        insertItem(database, genshinCharactersId, "Ке Цин");
        insertItem(database, genshinWorldsId, "Мондштадт");
        insertItem(database, genshinWorldsId, "Ли Юэ");
    }

    private long insertGame(SQLiteDatabase database, String gameName) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_GAME_NAME, gameName);
        return database.insert(TABLE_GAMES, null, values);
    }

    private long insertSection(SQLiteDatabase database, long gameId, String sectionName) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_GAME_ID, gameId);
        values.put(COLUMN_SECTION_NAME, sectionName);
        return database.insert(TABLE_SECTIONS, null, values);
    }

    private long insertItem(SQLiteDatabase database, long sectionId, String itemName) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_SECTION_ID_ITEMS, sectionId);
        values.put(COLUMN_ITEM_NAME, itemName);
        return database.insert(TABLE_ITEMS, null, values);
    }

    public List<String> getSectionsForGame(String gameName) {
        List<String> sections = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursorGameId = db.query(TABLE_GAMES, new String[]{COLUMN_ID},
                COLUMN_GAME_NAME + "=?", new String[]{gameName}, null, null, null);
        if (cursorGameId.moveToFirst()) {
            int gameId = cursorGameId.getInt(cursorGameId.getColumnIndex(COLUMN_ID));

            Cursor cursorSections = db.query(TABLE_SECTIONS, new String[]{COLUMN_SECTION_NAME},
                    COLUMN_GAME_ID + "=?", new String[]{String.valueOf(gameId)}, null, null, null);

            while (cursorSections.moveToNext()) {
                String sectionName = cursorSections.getString(cursorSections.getColumnIndex(COLUMN_SECTION_NAME));
                sections.add(sectionName);
            }
            cursorSections.close();
        }
        cursorGameId.close();
        return sections;
    }

    public List<String> getItemsForSection(String sectionName) {
        List<String> items = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursorSectionId = db.query(TABLE_SECTIONS, new String[]{COLUMN_SECTION_ID},
                COLUMN_SECTION_NAME + "=?", new String[]{sectionName}, null, null, null);
        if (cursorSectionId.moveToFirst()) {
            int sectionId = cursorSectionId.getInt(cursorSectionId.getColumnIndex(COLUMN_SECTION_ID));

            Cursor cursorItems = db.query(TABLE_ITEMS, new String[]{COLUMN_ITEM_NAME},
                    COLUMN_SECTION_ID_ITEMS + "=?", new String[]{String.valueOf(sectionId)}, null, null, null);

            while (cursorItems.moveToNext()) {
                String itemName = cursorItems.getString(cursorItems.getColumnIndex(COLUMN_ITEM_NAME));
                items.add(itemName);
            }
            cursorItems.close();
        }
        cursorSectionId.close();
        return items;
    }

    public long getSectionId(String sectionName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_SECTIONS, new String[]{COLUMN_ID},
                COLUMN_SECTION_NAME + "=?", new String[]{sectionName}, null, null, null);
        if (cursor.moveToFirst())
        {
            long sectionId = cursor.getLong(cursor.getColumnIndex(COLUMN_ID));
            cursor.close();
            return sectionId;
        }
        cursor.close(); // Always close cursor
        return -1; // Return -1 if no section found
    }

    public String getItemDescription(long itemId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String itemDescription = null;

        Cursor cursor = db.query(TABLE_ITEMS_DETAILS,
                new String[]{COLUMN_ITEM_DESCRIPTION},
                COLUMN_ITEM_DETAILS_ITEM_ID + "=?",
                new String[]{String.valueOf(itemId)},
                null, null, null);

        if (cursor.moveToFirst()) {
            itemDescription = cursor.getString(cursor.getColumnIndex(COLUMN_ITEM_DESCRIPTION));
        }
        cursor.close();
        return itemDescription;
    }
}
