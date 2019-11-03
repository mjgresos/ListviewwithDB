package com.gresos.listviewact;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;

public class SignupActivity extends AppCompatActivity {

    EditText etUsername, etPassword, etFullname;
    String username, password, fullname;
    int formsuccess, userid;

    DbHelper db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        db = new DbHelper(this);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etFullname = findViewById(R.id.etFullname);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_cancel, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.btnSave:


                formsuccess = 3;
                username = etUsername.getText().toString();
                password = etPassword.getText().toString();
                fullname = etFullname.getText().toString();

                // validate username
                if(username.equals("")) {
                    etUsername.setError("This field is required");
                    formsuccess--;
                }

                // validate password
                if(password.equals("")) {
                    etPassword.setError("This field is required");
                    formsuccess--;
                }

                // validate fullname
                if(fullname.equals("")) {
                    etFullname.setError("This field is required");
                    formsuccess--;
                }
                if(formsuccess == 3) {
                    HashMap<String, String> map_user = new HashMap();
                    map_user.put(db.TBL_USER_USERNAME, username);
                    map_user.put(db.TBL_USER_PASSWORD, password);
                    map_user.put(db.TBL_USER_FULLNAME, fullname);
                    userid = db.createUser(map_user);
                    if(userid < 1) {
                        Toast.makeText(this, "USER SUCCESSFULLY CREATED", Toast.LENGTH_SHORT).show();
                        this.finish();
                    }
                    else {
                        etUsername.setError("Username already existed");
                    }
                }


                break;
            case R.id.btnCancel:
                this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
