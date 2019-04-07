package pocket.money.tracker.Activity;

import android.app.Notification;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Handler;
import android.os.Process;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;
import pocket.money.tracker.Class.Caust;
import pocket.money.tracker.Database.Budget_DBManager;
import pocket.money.tracker.Database.Budget_Database;
import pocket.money.tracker.Database.Category;
import pocket.money.tracker.Database.Category_Database;
import pocket.money.tracker.Database.Category_Manager;
import pocket.money.tracker.Database.DBManager;
import pocket.money.tracker.Database.DatabaseHelper;
import pocket.money.tracker.R;

public class Next extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    boolean doubleBackToExitPressedOnce=false;
    String id ;
    String category;
    String budget;
    String date;
    String time;
    String amount;

    Budget_DBManager budget_dbManager;
    DBManager dbManager;

    static SharedPreferences pref;
    static SharedPreferences.Editor editor;
    Category_Manager category_Manager;


    DrawerLayout drawerLayout;
    Toolbar toolbar;
    ActionBarDrawerToggle toggle;
    TextView name,email,title;

    ArrayList <String> M_IDs=new ArrayList<>();
    ArrayList <String> Months=new ArrayList<>();
    ArrayList <String> Budget=new ArrayList<>();

    ArrayList<Caust> All_Caust=new ArrayList<>();


    static PieChartView pieChartView;
    static List<SliceValue> pieData = new ArrayList<>();

    ArrayList <String> cat_ID=new ArrayList<>();
    ArrayList <String> cat_Name=new ArrayList<>();
    ArrayList <Integer> cat_cost=new ArrayList<>();
    ArrayList <Integer> cat_percentage=new ArrayList<>();

    Spinner spinner;

    int total_amount=0;

    static String current_budget="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);

        pref = getApplicationContext().getSharedPreferences("Category", 0);
        // 0 - for private mode
        editor = pref.edit();

        category_Manager=new Category_Manager(this);
        category_Manager.open();

        dbManager=new DBManager(this);
        dbManager.open();

//        current_budget=Next.pref.getString("Current_Budget",null);
//
//        GetAll_caust_FromSQL();

        if(!pref.contains("Category"))
        {
            store_category();
        }

        spinner=findViewById(R.id.spinner);

        pieChartView = findViewById(R.id.chart);


        drawerLayout = findViewById(R.id.drawer_layout);
        toolbar=findViewById(R.id.toolbar);
        title=findViewById(R.id.title);

        setSupportActionBar(toolbar);

        title.setText("Dashboard");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView navigationView = findViewById(R.id.navigationId);
        navigationView.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) this);
        View headerView=navigationView.getHeaderView(0);
        name=headerView.findViewById(R.id.name);
        email=headerView.findViewById(R.id.email);

        name.setText(MainActivity.pref.getString("Name", null));
        email.setText(MainActivity.pref.getString("Email", null));

        toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.nav_open,R.string.nav_close);
        drawerLayout.addDrawerListener(toggle);


        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toggle.syncState();


        budget_dbManager=new Budget_DBManager(this);
        budget_dbManager.open();



        GetAllFromSQL();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                current_budget=Months.get(position);
                GetAll_cost_FromSQL();
                GetAll_Category();
                Toast.makeText(Next.this, ""+Months.get(position), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });




//        pieData.add(new SliceValue(15, Color.BLUE));
//        pieData.add(new SliceValue(25, Color.GRAY));
//        pieData.add(new SliceValue(10, Color.RED));
//        pieData.add(new SliceValue(60, Color.MAGENTA));






    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(toggle.onOptionsItemSelected(item))
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        drawerLayout.closeDrawer(GravityCompat.START);
        //Toast.makeText(this, ""+menuItem.getTitle(), Toast.LENGTH_SHORT).show();

        if(menuItem.getTitle().equals("Log Out"))
        {
            MainActivity.editor.clear();
            MainActivity.editor.commit();
            Intent myIntent = new Intent(Next.this, MainActivity.class);
            this.startActivity(myIntent);
            finish();
        }
        else if(menuItem.getTitle().equals("Create a new budget"))
        {
           startActivity(new Intent(Next.this,Create_Budget.class));
        }

        return false;
    }


    private void GetAllFromSQL() {
        M_IDs.clear();
        Months.clear();
        Budget.clear();
        Cursor cursor = budget_dbManager.fetch();
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {

                    M_IDs.add(cursor.getString(cursor.getColumnIndex(Budget_Database.ID)));
                    Months.add(cursor.getString(cursor.getColumnIndex(Budget_Database.Date)));
                    Budget.add(cursor.getString(cursor.getColumnIndex(Budget_Database.Amount)));

                } while (cursor.moveToNext());
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getApplicationContext(),
                android.R.layout.simple_spinner_item,
                Months
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // (4) set the adapter on the spinner
        spinner.setAdapter(adapter);

        int spinnerPosition = adapter.getPosition(Next.pref.getString("Current_Budget",null));
        spinner.setSelection(spinnerPosition);
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        if (doubleBackToExitPressedOnce) {
            Intent intent = new Intent(Next.this, MainActivity.class);
            startActivity(intent);
            moveTaskToBack(true);
            Process.killProcess(Process.myPid());
            System.exit(0);
            finish();
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 1000);
    }

    public void store_category()
    {
        category_Manager.insert("Food");
        category_Manager.insert("Social Life");
        category_Manager.insert("Self-development");
        category_Manager.insert("Transportation");
        category_Manager.insert("Cultural");
        category_Manager.insert("Household");
        category_Manager.insert("Apparel");
        category_Manager.insert("Beauty");
        category_Manager.insert("Health");
        category_Manager.insert("Education");
        category_Manager.insert("Gift");
        category_Manager.insert("Others");

        editor.putString("Category", "OK");
        editor.commit();
    }

    public void Add_Expense(View view) {
       startActivity(new Intent(Next.this,Add_Expense.class));
    }

    private void GetAll_cost_FromSQL() {
        All_Caust.clear();
        total_amount=0;

        Cursor cursor = dbManager.fetch();
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {

                    if(cursor.getString(cursor.getColumnIndex(DatabaseHelper.Budget)).equals(current_budget))
                    {
                        id=cursor.getString(cursor.getColumnIndex(DatabaseHelper.ID));
                        category=cursor.getString(cursor.getColumnIndex(DatabaseHelper.Category));
                        budget=cursor.getString(cursor.getColumnIndex(DatabaseHelper.Budget));
                        date=cursor.getString(cursor.getColumnIndex(DatabaseHelper.Date));
                        time=cursor.getString(cursor.getColumnIndex(DatabaseHelper.Time));
                        amount=cursor.getString(cursor.getColumnIndex(DatabaseHelper.Amount));

                        Log.d("Valid_date",budget+"/"+amount);
                        total_amount+=Integer.parseInt(amount);

                        Caust caust=new Caust(id,category,budget,date,time,amount);
                        All_Caust.add(caust);


                    }

                } while (cursor.moveToNext());
            }
        }

    }

    public void Show_Graph()
    {
        pieData.clear();

        pieData.add(new SliceValue(cat_percentage.get(0), Color.parseColor("#66ffcc")).setLabel(cat_Name.get(0)));
        pieData.add(new SliceValue(cat_percentage.get(1), Color.parseColor("#66ff99")).setLabel(cat_Name.get(1)));
        pieData.add(new SliceValue(cat_percentage.get(2), Color.parseColor("#66ff66")).setLabel(cat_Name.get(2)));
        pieData.add(new SliceValue(cat_percentage.get(3), Color.parseColor("#66ff33")).setLabel(cat_Name.get(3)));
        pieData.add(new SliceValue(cat_percentage.get(4), Color.parseColor("#669900")).setLabel(cat_Name.get(4)));
        pieData.add(new SliceValue(cat_percentage.get(5), Color.parseColor("#cccc00")).setLabel(cat_Name.get(5)));
        pieData.add(new SliceValue(cat_percentage.get(6), Color.parseColor("#cc9900")).setLabel(cat_Name.get(6)));
        pieData.add(new SliceValue(cat_percentage.get(7), Color.parseColor("#ff9900")).setLabel(cat_Name.get(7)));
        pieData.add(new SliceValue(cat_percentage.get(8), Color.parseColor("#cc6600")).setLabel(cat_Name.get(8)));
        pieData.add(new SliceValue(cat_percentage.get(9), Color.parseColor("#ff3300")).setLabel(cat_Name.get(9)));
        pieData.add(new SliceValue(cat_percentage.get(10), Color.parseColor("#ff0000")).setLabel(cat_Name.get(10)));
        pieData.add(new SliceValue(cat_percentage.get(11), Color.parseColor("#cc0000")).setLabel(cat_Name.get(11)));


        PieChartData pieChartData = new PieChartData(pieData);
        pieChartData.setHasLabels(true).setValueLabelTextSize(14);
        pieChartData.setHasCenterCircle(true).setCenterText1("Total cost \n"+total_amount).setCenterText1FontSize(30).setCenterText1Color(Color.parseColor("#008577"));
        pieChartView.setPieChartData(pieChartData);
    }

    private void GetAll_Category() {
        cat_ID.clear();
        cat_Name.clear();
        cat_cost.clear();
        cat_percentage.clear();
        Toast.makeText(this, ""+total_amount, Toast.LENGTH_SHORT).show();

        Cursor cursor = category_Manager.fetch();
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                        cat_ID.add(cursor.getString(cursor.getColumnIndex(Category.ID)));
                        cat_Name.add(cursor.getString(cursor.getColumnIndex(Category.Category)));
                        int a=0;
                        Cursor cursor1 = dbManager.fetch();
                        if (cursor1 != null) {
                            if (cursor1.moveToFirst()) {
                                do {
                                    if(cursor1.getString(cursor1.getColumnIndex(DatabaseHelper.Budget)).equals(current_budget))
                                    {
                                        category=cursor1.getString(cursor1.getColumnIndex(DatabaseHelper.Category));
                                        amount=cursor1.getString(cursor1.getColumnIndex(DatabaseHelper.Amount));
                                        if(cursor.getString(cursor.getColumnIndex(Category.Category)).equals(category))
                                        {
                                            a+=Integer.parseInt(amount);
                                        }
                                    }
                                } while (cursor1.moveToNext());
                            }
                        }
                        cat_cost.add(a);



                        try{
                            if(a!=0  && total_amount!=0)
                            {
                                cat_percentage.add((a/(total_amount/100)));
                                Log.d("KEY_VALUE",cursor.getString(cursor.getColumnIndex(Category.Category))+"/"+a+"/"+(a/(total_amount/100))+"%");
                            }
                            else
                            {
                                cat_percentage.add( 0);
                                Log.d("KEY_VALUE",cursor.getString(cursor.getColumnIndex(Category.Category))+"/"+a+"/"+0+"%");
                            }
                        }
                        catch (Exception e)
                        {

                        }

                } while (cursor.moveToNext());
            }
        }
        selectionSort();
    }

    void selectionSort()
    {
        int i, j;
        int  min_idx=0;

        String value="";

        for (i = 0; i < cat_percentage.size(); i++)
        {

            for (j = 0; j < cat_percentage.size(); j++) {
                if (cat_percentage.get(i) < cat_percentage.get(j))
                {
                    min_idx = cat_percentage.get(i);
                    cat_percentage.set(i,cat_percentage.get(j));
                    cat_percentage.set(j,min_idx);

                    value = cat_Name.get(i);
                    cat_Name.set(i,cat_Name.get(j));
                    cat_Name.set(j,value);

                    value = cat_ID.get(i);
                    cat_ID.set(i,cat_ID.get(j));
                    cat_ID.set(j,value);

                }

            }
        }

        for(i=0;i<cat_percentage.size();i++){
            Log.d("Percentage",""+cat_percentage.get(i));
        }
        try{
            Show_Graph();
        }
        catch (Exception e)
        {

        }
    }
}
