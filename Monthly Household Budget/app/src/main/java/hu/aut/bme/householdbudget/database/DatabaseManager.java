package hu.aut.bme.householdbudget.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by David on 2016.12.07..
 */

public class DatabaseManager extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION= 1;
    private static final String DATABASE_NAME = "HouseholdMonthlybudget.db";

    private  static final String TEXT_TYPE = " TEXT";
    private  static final String INT_TYPE = " INTEGER";

    private  static final String COMMA_SEP = ",";
    private static   final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + DatabaseProperties.TABLE_NAME;

    private  static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + DatabaseProperties.TABLE_NAME + " (" +
                    "Id" + " INTEGER PRIMARY KEY," +
                    DatabaseProperties.COLUMN_EXPENSEORINCOME_TITLE + TEXT_TYPE + COMMA_SEP +
                    DatabaseProperties.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP+
                    DatabaseProperties.COLUMN_AMOUNT_TITLE + INT_TYPE + COMMA_SEP+
                    DatabaseProperties.COLUMN_DATE_TITLE + TEXT_TYPE + COMMA_SEP+
                    DatabaseProperties.COLUMN_TYPE_TITLE+TEXT_TYPE+" )";




    public DatabaseManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }


}
