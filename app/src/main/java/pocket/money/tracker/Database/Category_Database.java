package pocket.money.tracker.Database;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Category_Database extends SQLiteOpenHelper {

    // Table Name
    public static final String TABLE_NAME = "Category";

    // Table columns
    public static final String ID = "ID";
    public static final String Category = "Category";
    public static final String Amount = "Amount";
    public static final String Date = "Date";

    // Database Information
    static final String DB_NAME = "Category.DB";

    // database version
    static final int DB_VERSION = 1;

    // Creating table query
    private static final String CREATE_TABLE = "create table " + TABLE_NAME + "(" + ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + Category + " TEXT NOT NULL, " + Amount + " Int NOT NULL, " + Date + " TEXT NOT NULL);";

    public Category_Database(Context context) {
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
