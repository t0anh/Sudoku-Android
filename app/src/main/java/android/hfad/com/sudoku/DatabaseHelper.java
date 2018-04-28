package android.hfad.com.sudoku;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/*
*   A database helper
*   reference: https://medium.com/@valokafor/ultimate-guide-to-android-sqlite-database-44cc8636a4ec
*
* */

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "sudoku.db";
    private static final int DATABASE_VERSION = 1;

    private static DatabaseHelper databaseInstance = null;
    private Context context;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

        public static DatabaseHelper newInstance (Context context) {
        if(databaseInstance == null) {
            databaseInstance = new DatabaseHelper(context.getApplicationContext());
        }
        return databaseInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        try {
            sqLiteDatabase.execSQL("CREATE TABLE Achievement (_id INTEGER PRIMARY KEY AUTOINCREMENT, nickname TEXT, elapsedSeconds INTEGER, difficulty TEXT, date TEXT, note TEXT)");
            sqLiteDatabase.execSQL("CREATE TABLE GameState (id INTEGER PRIMARY KEY AUTOINCREMENT, difficulty INTEGER, status INTEGER, elapsedSeconds INTEGER, solutionString TEXT, gridString TEXT, lastPlaying DateTime DEFAULT (DateTime('now', 'localtime')))");
        }
        catch (SQLException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
