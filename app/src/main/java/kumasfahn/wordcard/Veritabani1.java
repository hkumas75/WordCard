package kumasfahn.wordcard;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ${PACKAGE_NAME} on 9.7.2017.
 */

public class Veritabani1 extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "DATA_WORD.db";
    private static final String TABLE_NAME = "TABLO_WORD";
    private static final String COL_1 = "ID";
    private static final String COL_2 = "TYPE";
    private static final String COL_3 = "WORD";
    private static final String COL_4 = "MEANING";
    private static final String COL_5 = "EXAMPLE";
    private static final String COL_6 = "PST_ART_SYN";
    private static final String COL_7 = "PRF_PLR_AYN";
    private static final String COL_8 = "COLOR";

    public Veritabani1(Context context) {
        super(context, DATABASE_NAME, null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " +  TABLE_NAME+ " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +COL_2+ " TEXT, "+ COL_3 + " TEXT NOT NULL, "+ COL_4+ " TEXT, "+ COL_5 + " TEXT, " + COL_6 + " TEXT, "+ COL_7+ " TEXT, "+ COL_8 + " TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    boolean insertData(String type, String word, String mean, String exmpl, String partsyn, String pplant) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put (COL_2,type);
        contentValues.put(COL_3,word);
        contentValues.put(COL_4,mean);
        contentValues.put(COL_5,exmpl);
        contentValues.put(COL_6,partsyn);
        contentValues.put(COL_7,pplant);
        contentValues.put(COL_8,"N");
        //Log.d(TABLE_NAME, "Add Data: Adding  "+ad+ "to"+ TABLE_NAME );
        long result = db.insert(TABLE_NAME, null, contentValues);
        return result != -1;
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM TABLO_WORD ", null);
    }

    public boolean updateData(String id, String type, String word, String mean, String exmp, String pas, String ppa) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1,id);
        contentValues.put(COL_2,type);
        contentValues.put(COL_3,word);
        contentValues.put(COL_4,mean);
        contentValues.put(COL_5,exmp);
        contentValues.put(COL_6,pas);
        contentValues.put(COL_7,ppa);

        db.update(TABLE_NAME, contentValues, "ID = ? ", new String[] { id });
        return true;
    }

    public boolean markData(String id1, String mark) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1,id1);
        contentValues.put(COL_8,mark);

        db.update(TABLE_NAME, contentValues, " ID = ? ", new String[] { id1 });
        return true;
    }

    public Integer deleteData (String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "ID = ? ", new String[] {id});
    }

    boolean importData(String type, String word, String mean, String exmpl, String partsyn, String pplant, String color) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put (COL_2,type);
        contentValues.put(COL_3,word);
        contentValues.put(COL_4,mean);
        contentValues.put(COL_5,exmpl);
        contentValues.put(COL_6,partsyn);
        contentValues.put(COL_7,pplant);
        contentValues.put(COL_8,color);
        //Log.d(TABLE_NAME, "Add Data: Adding  "+ad+ "to"+ TABLE_NAME );
        long result = db.insert(TABLE_NAME, null, contentValues);
        return result != -1;
    }
}
