package pocket.money.tracker.Database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class Category_Manager {

    private Category dbHelper;

    private Context context;

    private SQLiteDatabase database;

    public Category_Manager(Context c) {
        context = c;
    }

    public Category_Manager open() throws SQLException {
        dbHelper = new Category(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public void insert(String category) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(Category.Category, category);
        database.insert(Category.TABLE_NAME, null, contentValue);
    }


    public Cursor fetch() {
        String[] columns = new String[] { Category.ID, Category.Category, Category.Category};
        Cursor cursor = database.rawQuery("select * from "+Category.TABLE_NAME,null);
        return cursor;
    }

    public int update(long id,String month) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Category.Category, month);
        int i = database.update(Category.TABLE_NAME, contentValues, Category.ID + " = " + id, null);
        return i;
    }

    public void delete(long id) {
        database.delete(Category.TABLE_NAME, Category.ID + "=" + id, null);
    }
    public void deleteAll() {
        database.execSQL("delete from "+ Category.TABLE_NAME);
    }

}
