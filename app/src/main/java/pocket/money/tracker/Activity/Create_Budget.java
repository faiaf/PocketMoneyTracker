package pocket.money.tracker.Activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.rackspira.kristiawan.rackmonthpicker.RackMonthPicker;
import com.rackspira.kristiawan.rackmonthpicker.listener.DateMonthDialogListener;
import com.rackspira.kristiawan.rackmonthpicker.listener.OnCancelMonthDialogListener;

import java.text.DateFormatSymbols;
import java.util.Calendar;

import pocket.money.tracker.Database.Budget_DBManager;
import pocket.money.tracker.R;

public class Create_Budget extends AppCompatActivity {

    TextView tv_month,budget;

    Budget_DBManager budget_dbManager;

    String Month_name="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_budget);

        tv_month=findViewById(R.id.month);
        budget=findViewById(R.id.budget);

        budget_dbManager=new Budget_DBManager(this);
        budget_dbManager.open();

    }


    public void Date_Picker(View view) {
         new RackMonthPicker(this)
                .setPositiveButton(new DateMonthDialogListener() {
                    @Override
                    public void onDateMonth(int month, int startDate, int endDate, int year, String monthLabel) {
                        DateFormatSymbols dfs = new DateFormatSymbols();
                        String[] months = dfs.getMonths();

                        Month_name=months[month-1];
                        tv_month.setText(""+months[month-1]);
                    }
                })
                .setNegativeButton(new OnCancelMonthDialogListener() {
                    @Override
                    public void onCancel(android.support.v7.app.AlertDialog alertDialog) {
                        alertDialog.dismiss();
                    }
                }).show();
    }

    public void Go(View view) {
        if (!budget.getText().toString().equals("") && !Month_name.equals(""))
        {
            budget_dbManager.insert(Month_name,Integer.parseInt(budget.getText().toString()));
            startActivity(new Intent(Create_Budget.this,Next.class));
        }
        else
            Toast.makeText(this, "Please fill all.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(Create_Budget.this,Next.class));
    }
}
