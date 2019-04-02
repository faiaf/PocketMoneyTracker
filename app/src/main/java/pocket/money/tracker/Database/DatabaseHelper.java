package pocket.money.tracker.Database;


import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Table Name
    public static final String TABLE_NAME = "Pocket_Money";

    // Table columns
    public static final String ID = "ID";
    public static final String Category = "Category";
    public static final String Month = "Month";
    public static final String Date = "Date";
    public static final String Time = "Time";
    public static final String Amount = "Amount";

    // Database Information
    static final String DB_NAME = "Wallet.DB";

    // database version
    static final int DB_VERSION = 1;

    // Creating table query
    private static final String CREATE_TABLE = "create table " + TABLE_NAME + "(" + ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + Category + " TEXT NOT NULL, " + Month + " TEXT NOT NULL," + Date + " TEXT NOT NULL," + Time + " TEXT NOT NULL," + Amount +" TEXT NOT NULL);";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

}
