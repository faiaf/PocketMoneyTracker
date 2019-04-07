package pocket.money.tracker.Database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class Budget_DBManager {

    private Budget_Database dbHelper;

    private Context context;

    private SQLiteDatabase database;

    public Budget_DBManager(Context c) {
        context = c;
    }

    public Budget_DBManager open() throws SQLException {
        dbHelper = new Budget_Database(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public void insert(String date ,int amount) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(Budget_Database.Date, date);
        contentValue.put(Budget_Database.Amount, amount);
        database.insert(Budget_Database.TABLE_NAME, null, contentValue);
    }

    public Cursor fetch() {
        String[] columns = new String[] { Budget_Database.ID, Budget_Database.Date ,Budget_Database.Amount };
        Cursor cursor = database.rawQuery("select * from "+Budget_Database.TABLE_NAME,null);
        return cursor;
    }

    public int update(long id,String month,int amount) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Budget_Database.Date, month);
        contentValues.put(Budget_Database.Amount, amount);


        int i = database.update(Budget_Database.TABLE_NAME, contentValues, Budget_Database.ID + " = " + id, null);
        return i;
    }

    public void delete(long id) {
        database.delete(Budget_Database.TABLE_NAME, Budget_Database.ID + "=" + id, null);
    }
    public void deleteAll() {
        database.execSQL("delete from "+ Budget_Database.TABLE_NAME);
    }

}
