package pocket.money.tracker.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import pocket.money.tracker.Class.Save;
import pocket.money.tracker.R;

public class MainActivity extends AppCompatActivity {

    static SharedPreferences pref;
    static SharedPreferences.Editor editor;

    EditText name,email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name=findViewById(R.id.name);
        email=findViewById(R.id.email);



        pref = getApplicationContext().getSharedPreferences("Login", 0);
        // 0 - for private mode
        editor = pref.edit();

        if(pref.contains("Name"))
        {
            Collect();
        }
    }

    private void Collect() {

        Save.setName(pref.getString("Name", null));
        Save.setEmail(pref.getString("Email", null));
        startActivity(new Intent(MainActivity.this,Next.class));
    }

    public void Enter(View view) {
        if(!name.getText().toString().equals("") && !email.getText().toString().equals(""))
        {
            editor.putString("Name", name.getText().toString());
            editor.putString("Email", email.getText().toString());
            editor.commit();
            Collect();
        }
        else
            Toast.makeText(this, "Please fill all.", Toast.LENGTH_SHORT).show();

    }


}
