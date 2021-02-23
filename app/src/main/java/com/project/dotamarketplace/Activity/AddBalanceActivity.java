package com.project.dotamarketplace.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.project.dotamarketplace.DataHelper;
import com.project.dotamarketplace.R;
import com.project.dotamarketplace.model.User;

public class AddBalanceActivity extends AppCompatActivity {
    TextView textName, textSaldo, textNotif;
    EditText  inputBalance, inputPassword;
    Button btnTopup;
    User user;
    DataHelper dbHelper;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_balance);
        intent = getIntent();
        user = intent.getParcelableExtra("user");
        init();

    }

    private void init() {
        dbHelper = new DataHelper(this);

        user = dbHelper.getUser(user.getUserID()+"");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        textName = findViewById(R.id.LblName);
        textSaldo = findViewById(R.id.lblSaldo);
        inputBalance = findViewById(R.id.inputTopup);
        inputPassword = findViewById(R.id.inputPassword);
        textNotif = findViewById(R.id.lblNotif);
        btnTopup = findViewById(R.id.btnTopup);
        updateData();

        btnTopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String balance = inputBalance.getText().toString();
                String password = inputPassword.getText().toString();
                if(validateTopup(user, balance, password)){
                    String id = user.getUserID()+"";
                    int topup = Integer.parseInt(balance);
                    int totalAdded = dbHelper.getBalance(user.getUserID()+"") + topup;
                    if(dbHelper.updateUserBalance(id, totalAdded)){
                        Toast.makeText(getBaseContext(), "Topup success", Toast.LENGTH_LONG).show();
                        updateData();
                    }

                }
            }
        });

    }

    private void updateData() {
        user = dbHelper.getUser(user.getUserID()+"");
        int balance = dbHelper.getBalance(user.getUserID()+"");
        textName.setText(user.getUserName());
        textSaldo.setText(balance+"");
        inputPassword.setText("");
        inputBalance.setText("");
    }

    private boolean validateTopup(User user, String balance, String password) {
        if(balance.isEmpty() && password.isEmpty()){
            textNotif.setText("amount of balance and password must be filled");
            return false;
        }else if(Integer.parseInt(balance)<50000){
            textNotif.setText("amount can't less than 50000");
            return false;
        }else if(!password.equals(user.getPassword())){
            Log.d("TAG", password + " - " + user.getPassword());
            textNotif.setText("Wrong password");
            return false;
        }
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
}