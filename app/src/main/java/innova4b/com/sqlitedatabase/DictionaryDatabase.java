package innova4b.com.sqlitedatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DictionaryDatabase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "dictionary.db";
    private static final String TABLE_DICTIONARY = "dictionary";

    private static final String FIELD_WORD = "word";
    private static final String FIELD_DEFINITION = "definition";

    private static final int DATABASE_VERSION = 1;


    public DictionaryDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_DICTIONARY + "(_id integer PRIMARY KEY, " +
        FIELD_WORD + " TEXT, " + FIELD_DEFINITION + " TEXT);");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void saveRecord(String word, String definition){
        long id = findWordId(word);
        if (id > 0){
            updateRecord(id, word, definition);
        } else {
            addRecord(word, definition);
        }
    }



    private long addRecord(String word, String definition) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FIELD_WORD, word);
        values.put(FIELD_DEFINITION, definition);
        return db.insert(TABLE_DICTIONARY, null, values);
    }

    private int updateRecord(long id, String word, String definition) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("_id", id);
        values.put(FIELD_WORD, word);
        values.put(FIELD_DEFINITION, definition);

        return db.update(TABLE_DICTIONARY, values, "_id = ? and word = '?'", new String[]{String.valueOf(id), word});


    }

    private long findWordId(String word) {
        long returnVal = -1;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT _id FROM " + TABLE_DICTIONARY + " WHERE " + FIELD_WORD + " = ?", new String[]{word});
        if (cursor.getCount() == 1){
            cursor.moveToFirst();
            returnVal = cursor.getInt(0);
        }

        return returnVal;
    }
}
