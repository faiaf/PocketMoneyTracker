package pocket.money.tracker.Database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class Category_DBManager {

    private Category_Database dbHelper;

    private Context context;

    private SQLiteDatabase database;

    public Category_DBManager(Context c) {
        context = c;
    }

    public Category_DBManager open() throws SQLException {
        dbHelper = new Category_Database(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public void insert(String category,int amount,String date) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(Category_Database.Category, category);
        contentValue.put(Category_Database.Amount, amount);
        contentValue.put(Category_Database.Date, date);
        database.insert(Category_Database.TABLE_NAME, null, contentValue);
    }


    public Cursor fetch() {
        String[] columns = new String[] { Category_Database.ID, Category_Database.Category, Category_Database.Category, Category_Database.Amount,Category_Database.Date };
        Cursor cursor = database.rawQuery("select * from "+Category_Database.TABLE_NAME,null);
        return cursor;
    }

    public int update(long id,String month,int amount,String date) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Category_Database.Category, month);
        contentValues.put(Category_Database.Amount, amount);
        contentValues.put(Category_Database.Date, date);


        int i = database.update(Category_Database.TABLE_NAME, contentValues, Category_Database.ID + " = " + id, null);
        return i;
    }

    public void delete(long id) {
        database.delete(Category_Database.TABLE_NAME, Category_Database.ID + "=" + id, null);
    }
    public void deleteAll() {
        database.execSQL("delete from "+ Category_Database.TABLE_NAME);
    }

}
