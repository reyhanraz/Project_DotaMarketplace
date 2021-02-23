package com.project.dotamarketplace.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.project.dotamarketplace.AdapterItem;
import com.project.dotamarketplace.AdapterTransaction;
import com.project.dotamarketplace.DataHelper;
import com.project.dotamarketplace.R;
import com.project.dotamarketplace.model.Item;
import com.project.dotamarketplace.model.Transaction;
import com.project.dotamarketplace.model.User;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

public class TransactionHistoryActivity extends AppCompatActivity implements AdapterItem.onItemListener{
    User user;
    DataHelper dataHelper;
    ArrayList<Transaction> newArray = new ArrayList<>();
    RecyclerView recyclerView;
    AdapterTransaction adapter;
    RecyclerView.LayoutManager layoutManager;
    Button btnDelete;
    Item item;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_history);

        Intent intent = getIntent();
        user = intent.getParcelableExtra("user");


        init();
    }

    private void init() {
        dataHelper = new DataHelper(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        newArray.clear();
        recyclerView = findViewById(R.id.RecyleV);
        btnDelete = findViewById(R.id.btnDelete);
        user = dataHelper.getUser(user.getUserID()+"");
        if (dataHelper.getTransactionHistory(user.getUserID()+"") != null){
            newArray = dataHelper.getTransactionHistory(user.getUserID()+"");
        }

        adapter = new AdapterTransaction(newArray, this);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dataHelper.deleteHistory(user.getUserID()+"")) {

                    init();
                    Toast.makeText(TransactionHistoryActivity.this, "Delete Success", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onItemClicked(int position) {

    }

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