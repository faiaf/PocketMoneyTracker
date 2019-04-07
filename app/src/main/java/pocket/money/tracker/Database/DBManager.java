package pocket.money.tracker.Database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DBManager {

    private DatabaseHelper dbHelper;

    private Context context;

    private SQLiteDatabase database;

    public DBManager(Context c) {
        context = c;
    }

    public DBManager open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public void insert(String category,String budget, String date,String time,int amount) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.Category, category);
        contentValue.put(DatabaseHelper.Budget, budget);
        contentValue.put(DatabaseHelper.Date, date);
        contentValue.put(DatabaseHelper.Time, time);
        contentValue.put(DatabaseHelper.Amount, amount);
        database.insert(DatabaseHelper.TABLE_NAME, null, contentValue);
    }

    public Cursor fetch() {
        String[] columns = new String[] { DatabaseHelper.ID, DatabaseHelper.Category,DatabaseHelper.Budget, DatabaseHelper.Date, DatabaseHelper.Time,DatabaseHelper.Amount };
        Cursor cursor = database.rawQuery("select * from "+DatabaseHelper.TABLE_NAME,null);
        return cursor;
    }

    public int update(long id,String category,String budget, String date,String time,String amount) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.Category, category);
        contentValues.put(DatabaseHelper.Budget, budget);
        contentValues.put(DatabaseHelper.Date, date);
        contentValues.put(DatabaseHelper.Time, time);
        contentValues.put(DatabaseHelper.Amount, amount);

        int i = database.update(DatabaseHelper.TABLE_NAME, contentValues, DatabaseHelper.ID + " = " + id, null);
        return i;
    }

    public void delete(long id) {
        database.delete(DatabaseHelper.TABLE_NAME, DatabaseHelper.ID + "=" + id, null);
    }
    public void deleteAll() {
        database.execSQL("delete from "+ DatabaseHelper.TABLE_NAME);
    }

}
