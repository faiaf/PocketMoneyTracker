package pocket.money.tracker.Activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

import pocket.money.tracker.Adapter.Category_Amount_Adapter;
import pocket.money.tracker.Class.Save;
import pocket.money.tracker.Database.Category_Database;
import pocket.money.tracker.Database.Category_Manager;
import pocket.money.tracker.Database.DBManager;
import pocket.money.tracker.R;

public class Add_Expense extends AppCompatActivity {

    DatePickerDialog picker;

    RecyclerView recyclerView;
    TextView date;
    Category_Manager category_Manager;

    ArrayList <String> cate=new ArrayList<>();

    public static int value=0;
    public static TextView total;

    DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        recyclerView=findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        date=findViewById(R.id.date);
        total=findViewById(R.id.total);

        category_Manager=new Category_Manager(this);
        category_Manager.open();

        dbManager=new DBManager(this);
        dbManager.open();

        date.setText(Save.getDate());

        GetAllFromSQL();


    }

    private void GetAllFromSQL() {
        Cursor cursor = category_Manager.fetch();
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    cate.add(cursor.getString(cursor.getColumnIndex(Category_Database.Category)));

                } while (cursor.moveToNext());
            }
        }

        Category_Amount_Adapter.Amount.clear();
        Category_Amount_Adapter category_amount_adapter=new Category_Amount_Adapter(getApplicationContext(),cate);
        recyclerView.setAdapter(category_amount_adapter);

        set_Amount();
    }
    public void set_Amount()
    {
        value=0;
        for (int i=0;i<Category_Amount_Adapter.Amount.size();i++)
        {
            //Toast.makeText(this, ""+Category_Amount_Adapter.Amount.size(), Toast.LENGTH_SHORT).show();
            value+=Integer.parseInt(Category_Amount_Adapter.Amount.get(i));
        }
        total.setText(""+value);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                set_Amount();
            }
        }, 1500);
    }

    public void Get_Date(View view) {
        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
        // date picker dialog
        picker = new DatePickerDialog(Add_Expense.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        date.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                    }
                }, year, month, day);
        picker.show();
    }


    public void OK(View view) {
        for (int i=0;i<Category_Amount_Adapter.Amount.size();i++)
        {
            if(!Category_Amount_Adapter.Amount.get(i).equals("0"))
            {
                dbManager.insert(cate.get(i),Next.pref.getString("Current_Budget",null),Save.getDate(),Save.getTime(), Integer.parseInt(Category_Amount_Adapter.Amount.get(i)));
            }
        }
        startActivity(new Intent(Add_Expense.this,Next.class));
    }
}
