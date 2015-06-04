package scu.edu.moodlogger;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.sql.SQLException;

/**
 * Created by vijay on 5/29/2015.
 */


public class DBUserAdapter
{
    public static final String KEY_ROWID = "_id";

    public static final String KEY_FIRSTNAME= "username";
    public static final String KEY_LASTNAME = "password";

    public static final String KEY_USERNAME= "username";
    public static final String KEY_PASSWORD = "password";
    private static final String TAG = "DBAdapter";

    private static final String DATABASE_NAME = "usersdb";
    private static final int DATABASE_VERSION = 1;

    /* NAME OF TABLES */
    private static final String DATABASE_TABLE_USERS = "users";
    private static final String DATABASE_TABLE_MOODS = "mood";

    private static final String DATABASE_CREATE =
            "create table users (_id integer primary key autoincrement, "
                    + "username text not null, "
                    + "password text not null)";

    private static final String DATABASE_CREATE_MOOD =
            "    CREATE TABLE IF NOT EXISTS `mood` (" +
                    "  `userid` varchar(200) NOT NULL," +
                    "  `moodid` int(11) NOT NULL," +
                    "  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,\n" +
                    "  `picture` varchar(200) NOT NULL," +
                    "  `notes` varchar(1000) NOT NULL," +
                    "  `location` varchar(50) NOT NULL" +
                    ")";


    /*
        Table to log moods
        CREATE TABLE IF NOT EXISTS `mood` (
  `userid` int(11) NOT NULL,
  `moodid` int(11) NOT NULL,
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `picture` varchar(200) NOT NULL,
  `notes` varchar(1000) NOT NULL,
  `location` varchar(50) NOT NULL
)


     */




    /*
    INSERT INTO `test`.`mood` (`userid`, `moodid`, `timestamp`, `picture`, `notes`, `location`) VALUES ('ab', '2', CURRENT_TIMESTAMP, 'Sleepy for a change', 'Sleepy', ''), ('ab', '3', CURRENT_TIMESTAMP, 'Bored', 'Bored', 'Bored');
     */


    private static final String DATABASE_MOOD_SAMPLE =
            "INSERT INTO `mood` (`userid`, `moodid`, `timestamp`, `picture`, `notes`, `location`) VALUES" +
                    "('ab', 1, '2015-06-04 00:57:16', 'Happy', 'Happy', 'Happy')," +
                    "('ab', 1, '2015-06-04 00:57:16', 'Happy Again', 'Always Happy', '')," +
                    "('ab', 2, '2015-06-04 00:57:51', 'Sleepy for a change', 'Sleepy', '')," +
                    "('ab', 3, '2015-06-04 00:57:51', 'Bored', 'Bored', 'Bored');";


    private Context context = null;
    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public DBUserAdapter(Context ctx)
    {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper
    {
        DatabaseHelper(Context context)
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db)
        {
            db.execSQL(DATABASE_CREATE);
            db.execSQL(DATABASE_CREATE_MOOD);
            //mock data for the database.
            db.execSQL(DATABASE_MOOD_SAMPLE);

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
            Log.w(TAG, "Upgrading database from version " + oldVersion
                    + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS users");
            onCreate(db);
        }
    }


    public void open() throws SQLException
    {
        db = DBHelper.getWritableDatabase();
    }


    public void close()
    {
        DBHelper.close();
    }

    public long AddUser(String fname, String lname, String username, String password)
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_FIRSTNAME, fname);
        initialValues.put(KEY_LASTNAME, lname);
        initialValues.put(KEY_USERNAME, username);
        initialValues.put(KEY_PASSWORD, password);
        return db.insert(DATABASE_TABLE_USERS, null, initialValues);

    }

    public boolean Login(String username, String password) throws SQLException
    {
        Cursor mCursor = db.rawQuery("SELECT * FROM " + DATABASE_TABLE_USERS + " WHERE username=? AND password=?", new String[]{username,password});
        if (mCursor != null) {
            if(mCursor.getCount() > 0)
            {
                return true;
            }
        }
        return false;
    }

/**
 * @input : user id
 * @output: data Structure with the number of moods
 *
 */

public DataTypeChart getData(String username) throws SQLException
{

    DataTypeChart data= new DataTypeChart();
    Cursor mCursor = db.rawQuery("SELECT * FROM moods WHERE username='ab'",null);
    if (mCursor != null) {
        if(mCursor.getCount() > 0)
        {
            data.numberOfData=mCursor.getCount();

            int duration = Toast.LENGTH_SHORT;


            Toast toast = Toast.makeText(context, mCursor.getCount(), duration);
            toast.show();
            return data;
        }
    }
    data.numberOfData=0;
    return data;
}




}
