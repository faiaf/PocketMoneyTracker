package pocket.money.tracker.Activity;

import android.app.Notification;
import android.content.Intent;
import android.database.Cursor;
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
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import pocket.money.tracker.Database.Budget_DBManager;
import pocket.money.tracker.Database.Budget_Database;
import pocket.money.tracker.Database.DBManager;
import pocket.money.tracker.Database.DatabaseHelper;
import pocket.money.tracker.R;

public class Next extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    boolean doubleBackToExitPressedOnce=false;

    Budget_DBManager budget_dbManager;


    DrawerLayout drawerLayout;
    Toolbar toolbar;
    ActionBarDrawerToggle toggle;
    TextView name,email,title;

    ArrayList <String> M_IDs=new ArrayList<>();
    ArrayList <String> Months=new ArrayList<>();
    ArrayList <String> Budget=new ArrayList<>();

    TextView test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);

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

        test=findViewById(R.id.test);
        test.setText("");

        GetAllFromSQL();
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
        Cursor cursor = budget_dbManager.fetch();
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    M_IDs.add(cursor.getString(cursor.getColumnIndex(Budget_Database.ID)));
                    Months.add(cursor.getString(cursor.getColumnIndex(Budget_Database.Month)));
                    Budget.add(cursor.getString(cursor.getColumnIndex(Budget_Database.Amount)));

                    test.append(cursor.getString(cursor.getColumnIndex(Budget_Database.Amount))+"/"+cursor.getString(cursor.getColumnIndex(Budget_Database.Month))+"\n");
                } while (cursor.moveToNext());
            }
        }
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
}
