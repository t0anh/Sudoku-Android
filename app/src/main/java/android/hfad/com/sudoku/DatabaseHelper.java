package android.hfad.com.sudoku;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
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

    private DatabaseHelper(Context context) {
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
            sqLiteDatabase.execSQL(context.getString(R.string.CREATE_PLAYER_TABLE));
            sqLiteDatabase.execSQL(context.getString(R.string.CREATE_DIFFICULTY_TABLE));
            sqLiteDatabase.execSQL(context.getString(R.string.CREATE_ACHIEVEMENT_TABLE));
        }
        catch (SQLException e) {
            Log.d("SQL_ERRORS", e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
