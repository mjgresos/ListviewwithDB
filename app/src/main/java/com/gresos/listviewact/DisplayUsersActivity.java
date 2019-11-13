package com.gresos.listviewact;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class DisplayUsersActivity extends AppCompatActivity {

    DbHelper db;
    ListView lvUsers;
    ArrayList<HashMap<String, String>> all_users;
    ListViewAdapter adapter;

    SharedPreferences shared;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_user);

        db = new DbHelper(this);

        shared = getSharedPreferences("MSG", Context.MODE_PRIVATE);

        lvUsers = findViewById(R.id.lvUsers);
        fetch_users();
    }

    private void fetch_users() {
        all_users = db.getAllUsers();
        adapter = new ListViewAdapter(this, R.layout.adapter_users, all_users);
        lvUsers.setAdapter(adapter);
        registerForContextMenu(lvUsers);
    }

    private class ListViewAdapter extends ArrayAdapter {

        LayoutInflater inflater;
        TextView tvFullname , tvUsername;
        ImageView ivEdit, ivDelete;

        ArrayList<HashMap<String, String>> all_users;

        public ListViewAdapter(Context context, int resource, ArrayList<HashMap<String, String>> all_users){
            super(context, resource, all_users);
            inflater = LayoutInflater.from(context);
            this.all_users = all_users;
        }

        @NonNull
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            convertView = inflater.inflate(R.layout.adapter_users, parent,false);
            tvFullname = convertView.findViewById(R.id.tvFullname);
            tvUsername = convertView.findViewById(R.id.tvUsername);
            ivEdit = convertView.findViewById(R.id.ivEdit);
            ivDelete = convertView.findViewById(R.id.ivDelete);

            tvFullname.setText(all_users.get(position).get(db.TBL_USER_FULLNAME));
            tvUsername.setText(all_users.get(position).get(db.TBL_USER_USERNAME));


            ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int userID = Integer.parseInt(all_users.get(position).get(db.TBL_USER_ID));db.deleteUser(userID);
                    Toast.makeText(DisplayUsersActivity.this, "User Successfully Deleted", Toast.LENGTH_SHORT).show();
                    fetch_users();
                }
            });
            ivEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int userID = Integer.parseInt(all_users.get(position).get(db.TBL_USER_ID));

                Intent intent = new Intent(getContext(), EditUserActivity.class);
                intent.putExtra(db.TBL_USER_ID,userID);

                startActivity(intent);
                    }
                });
            return convertView;
            }
        }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.logout, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.btnLogout:
            new AlertDialog.Builder(this)
            .setTitle("Logout")
            .setMessage("Are you sure you want to Logout?")
            .setPositiveButton("YES", new DialogInterface.OnClickListener()
            {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            shared.edit().remove(db.TBL_USER_ID)
                    .commit();
            Toast.makeText(DisplayUsersActivity.this, "You've been Successfully Logout", Toast.LENGTH_SHORT)
                    .show();
            startActivity(new Intent(getBaseContext(), LoginActivity.class));
            DisplayUsersActivity.this.finish();}})
            .setNegativeButton("NO", new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            }
         })
            .show();
            break;
        }
    return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        fetch_users();
        if(!shared.contains(db.TBL_USER_ID)) {
            this.finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
    super.onResume();
    }
}