package scu.edu.moodlogger;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


/**
 * Need to modify whole file
 */




public class DBAdapter {

    private static final String TAG = "DBAdapter"; //used for logging database version changes

    // Field Names:
    public static final String KEY_ROWID = "_id";
    public static final String KEY_CAPTION = "caption";
    public static final String KEY_PATH = "path";
    public static final String KEY_DATE = "date";
    public static final String KEY_LOCATION = "location";
    public static final String[] ALL_KEYS = new String[]{KEY_ROWID, KEY_CAPTION, KEY_PATH, KEY_DATE, KEY_LOCATION};

    // DataBase info:
    public static final String DATABASE_NAME = "dbOne";
    public static final String DATABASE_TABLE = "dailyPhoto";
    public static final int DATABASE_VERSION = 1; // The version number must be incremented each time a change to DB structure occurs.

    //SQL statement to create database
    private static final String DATABASE_CREATE_SQL =
            "CREATE TABLE " + DATABASE_TABLE
                    + " (" + KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + KEY_CAPTION + " TEXT,"
                    + KEY_PATH + " TEXT NOT NULL,"
                    + KEY_DATE + " TEXT NOT NULL,"
                    + KEY_LOCATION + " TEXT"
                    + ");";

    private final Context context;
    private DatabaseHelper myDBHelper;
    private SQLiteDatabase db;


    public DBAdapter(Context ctx) {
        this.context = ctx;
        myDBHelper = new DatabaseHelper(context);
    }

    // Open the database connection.
    public DBAdapter open() {
        db = myDBHelper.getWritableDatabase();
        return this;
    }

    // Close the database connection.
    public void close() {
        myDBHelper.close();
    }

    // Add a new set of values to be inserted into the database.
    public long insertRow(String CAPTION, String path, String date, String location) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_CAPTION, CAPTION);
        initialValues.put(KEY_PATH, path);
        initialValues.put(KEY_DATE, date);
        initialValues.put(KEY_LOCATION, location);

        // Insert the data into the database.
        return db.insert(DATABASE_TABLE, null, initialValues);
    }

    // Delete a row from the database, by rowId (primary key)
    public boolean deleteRow(long rowId) {
        String where = KEY_ROWID + "=" + rowId;
        return db.delete(DATABASE_TABLE, where, null) != 0;
    }

    // Delete a row from the database, by date
    public boolean deleteRow(String date) {
        String where = KEY_DATE + "=" + date;
        return db.delete(DATABASE_TABLE, where, null) != 0;
    }

    public void deleteAll() {
        Cursor c = getAllRows();
        long rowId = c.getColumnIndexOrThrow(KEY_ROWID);
        if (c.moveToFirst()) {
            do {
                deleteRow(c.getLong((int) rowId));
            } while (c.moveToNext());
        }
        c.close();
    }

    // Return all data in the database.
    public Cursor getAllRows() {
        String where = null;
        Cursor c = db.query(true, DATABASE_TABLE, ALL_KEYS, where, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    // Get a specific row (by rowId)
    public Cursor getRow(long rowId) {
        String where = KEY_ROWID + "=" + rowId;
        Cursor c = db.query(true, DATABASE_TABLE, ALL_KEYS,
                where, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    // Get a specific row (by rowId)
    public Cursor getRow(String date) {
        String where = KEY_DATE + "=" + date;
        Cursor c = db.query(true, DATABASE_TABLE, ALL_KEYS,
                where, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    // get image
//    public ImageDetails getImage(String sDate) {
//
//        ImageDetails images = null;
//        String where = KEY_DATE + "=" + sDate;
//        String orderBy = KEY_ROWID + " DESC";
//        Cursor c = db.query(true, DATABASE_TABLE, ALL_KEYS, where, null, null, null, orderBy, null);
//
//        int cRow;
//        String cCaption, cPath, cDate, cLocation;
//
//        if (c != null && (c.getCount() > 0)) {
//            c.moveToFirst();
//            cRow = c.getInt(c.getColumnIndex(KEY_ROWID));
//            cCaption = c.getString(c.getColumnIndex(KEY_CAPTION));
//            cPath = c.getString(c.getColumnIndex(KEY_PATH));
//            cDate = c.getString(c.getColumnIndex(KEY_DATE));
//            cLocation = c.getString(c.getColumnIndex(KEY_LOCATION));
//            images = new ImageDetails();
//            images.setRow_id(cRow);
//            images.setCaption(cCaption);
//            images.setPath(cPath);
//            images.setDate(cDate);
//            images.setLocation(cLocation);
//
//        }
//
//        return images;
//    }

    // get image
    public String[] getImageDetails(String sDate) {
        String where = KEY_DATE + "=" + sDate;
        Cursor c = db.query(true, DATABASE_TABLE, ALL_KEYS, where, null, null, null, null, null);

        String[] imageDetails;

        int iRow = c.getColumnIndex(KEY_ROWID);
        int iPath = c.getColumnIndex(KEY_PATH);
        int iDate = c.getColumnIndex(KEY_DATE);
        int iCaption = c.getColumnIndex(KEY_CAPTION);
        int iLocation = c.getColumnIndex(KEY_LOCATION);

        String cRow = null;
        String cPath = null;
        String cCaption = null;
        String cLocation = null;
        String cDate = null;

        if (c != null) {
            for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {

                cRow = c.getString(iRow);
                cCaption = c.getString(iCaption);
                cPath = c.getString(iPath);
                cDate = c.getString(iDate);
                cLocation = c.getString(iLocation);
            }

        }
        imageDetails = new String[]{cRow, cCaption, cPath, cDate, cLocation};

        return imageDetails;
    }


    // Change an existing row to be equal to new data.
    public boolean updateRow(long rowId, String CAPTION, String path, String date, String location) {
        String where = KEY_ROWID + "=" + rowId;
        ContentValues newValues = new ContentValues();
        newValues.put(KEY_CAPTION, CAPTION);
        newValues.put(KEY_PATH, path);
        newValues.put(KEY_DATE, date);
        newValues.put(KEY_LOCATION, location);
        // Insert it into the database.
        return db.update(DATABASE_TABLE, newValues, where, null) != 0;
    }

    // Change an existing row to be equal to new data.
    public boolean updateCaption(long rowId, String CAPTION) {
        String where = KEY_ROWID + "=" + rowId;
        ContentValues newValues = new ContentValues();
        newValues.put(KEY_CAPTION, CAPTION);

        // Insert it into the database.
        return db.update(DATABASE_TABLE, newValues, where, null) != 0;
    }


    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase _db) {
            _db.execSQL(DATABASE_CREATE_SQL);
        }

        @Override
        public void onUpgrade(SQLiteDatabase _db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading application's database from version " + oldVersion
                    + " to " + newVersion + ", which will destroy all old data!");

            // Destroy old database:
            _db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);

            // Recreate new database:
            onCreate(_db);
        }
    }


}



