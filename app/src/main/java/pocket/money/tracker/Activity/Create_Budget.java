package pocket.money.tracker.Activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;

import pocket.money.tracker.Adapter.Category_Amount_Adapter;
import pocket.money.tracker.Database.Budget_DBManager;
import pocket.money.tracker.Database.Budget_Database;
import pocket.money.tracker.Database.Category_DBManager;
import pocket.money.tracker.Database.Category_Database;
import pocket.money.tracker.Database.Category_Manager;
import pocket.money.tracker.R;

public class Create_Budget extends AppCompatActivity {

    TextView tv_month,budget;
    static RecyclerView recyclerView;

    Budget_DBManager budget_dbManager;
    Category_DBManager category_dbManager;
    Category_Manager category_Manager;

    ArrayList<String> cate=new ArrayList<>();

    DatePicker picker;

    String date="";

    LinearLayout get_date;
    RelativeLayout category;
    static int value=0;

    static TextView amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_budget);


        picker=findViewById(R.id.datePicker1);
        get_date=findViewById(R.id.get_date);
        category=findViewById(R.id.category);

        amount=findViewById(R.id.amount);

        recyclerView=findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));



        // tvw.setText("Selected Date: "+ picker.getDayOfMonth()+"/"+ (picker.getMonth() + 1)+"/"+picker.getYear());

        budget_dbManager=new Budget_DBManager(this);
        budget_dbManager.open();
        category_dbManager=new Category_DBManager(this);
        category_dbManager.open();

        category_Manager=new Category_Manager(this);
        category_Manager.open();


    }


    public void Go(View view) {
       date=picker.getDayOfMonth()+"/"+ (picker.getMonth() + 1)+"/"+picker.getYear();

       GetAllFromSQL();
        value=0;

       get_date.setVisibility(View.GONE);
       category.setVisibility(View.VISIBLE);
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

        Category_Amount_Adapter category_amount_adapter=new Category_Amount_Adapter(getApplicationContext(),cate);
        recyclerView.setAdapter(category_amount_adapter);

        Category_Amount_Adapter.Amount.clear();
        set_Amount();
    }

    public void set_Amount()
    {
        value=0;
        for (int i=0;i<Category_Amount_Adapter.Amount.size();i++)
        {
            value+=Integer.parseInt(Category_Amount_Adapter.Amount.get(i));
        }
        amount.setText(""+value);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                set_Amount();
            }
        }, 1500);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(Create_Budget.this,Next.class));
    }

    public void Enter(View view) {
        value=0;
        for (int i=0;i<Category_Amount_Adapter.Amount.size();i++)
        {
            value+=Integer.parseInt(Category_Amount_Adapter.Amount.get(i));
            category_dbManager.insert(cate.get(i),Integer.parseInt(Category_Amount_Adapter.Amount.get(i)),date);
        }
        budget_dbManager.insert(date,value);

        Next.editor.putString("Current_Budget",date);
        Next.editor.commit();

        startActivity(new Intent(Create_Budget.this,Next.class));


    }
}
