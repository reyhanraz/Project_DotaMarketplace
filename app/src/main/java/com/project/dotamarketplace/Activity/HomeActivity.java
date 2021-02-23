package com.project.dotamarketplace.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.project.dotamarketplace.Activity.AddBalanceActivity;
import com.project.dotamarketplace.Activity.BuyActivity;
import com.project.dotamarketplace.AdapterItem;
import com.project.dotamarketplace.DataHelper;
import com.project.dotamarketplace.R;
import com.project.dotamarketplace.model.Item;
import com.project.dotamarketplace.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity implements AdapterItem.onItemListener {
    TextView txtName, txtBalance;
    Button buyItem;
    User user;
    DataHelper dataHelper;
    ArrayList<Item> newArray = new ArrayList<>();
    RecyclerView recyclerView;
    AdapterItem adapter;
    RecyclerView.LayoutManager layoutManager;
    Item item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Intent intent = getIntent();
        user = intent.getParcelableExtra("user");
        dataHelper = new DataHelper(this);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    @Override
    protected void onStart() {
        super.onStart();
        init();
    }

    private void init() {
        newArray.clear();
        recyclerView = findViewById(R.id.RecyleV);
        if (dataHelper.getAllItem() == null){
            try {
                newArray = getList(parseJsonFromUrl("https://bit.ly/3afEy3J"));
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            newArray = dataHelper.getAllItem();
        }

        adapter = new AdapterItem(newArray, this, this);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        txtName = findViewById(R.id.txtName);
        txtBalance = findViewById(R.id.txtBalance);
        buyItem = findViewById(R.id.btnBuy);

        user = dataHelper.getUser(user.getUserID()+"");
        txtName.setText(user.getUserName());
        txtBalance.setText(user.getBalance()+"");
        buyItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), BuyActivity.class);
                intent.putExtra("item", item);
                intent.putExtra("user", user);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuAddBalance:
                Intent intent = new Intent(this, AddBalanceActivity.class);
                Log.d("SENDING", user.getName());
                intent.putExtra("user", user);
                startActivity(intent);
            break;
            case R.id.menuHistory:
                Intent intent2 = new Intent(this, TransactionHistoryActivity.class);
                intent2.putExtra("user", user);
                startActivity(intent2);
            break;
            case R.id.menuLogOut:
                Intent intent1 = new Intent(this, FirstActivity.class);
                startActivity(intent1);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public static JSONArray parseJsonFromUrl(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONArray json = new JSONArray(jsonText);
            return json;
        } finally {
            is.close();
        }
    }
    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public ArrayList<Item> getList(JSONArray jsonArray) throws JSONException {
        ArrayList<Item> arrItem= new ArrayList<>();
        for(int i=0; i<jsonArray.length(); i++){
            JSONObject objct = jsonArray.getJSONObject(i);
            String name = objct.getString("name");
            int price = objct.getInt("price");
            int stock = objct.getInt("stock");
            double latitude = objct.getDouble("latitude");
            double longitude = objct.getDouble("longitude");
            Item item = new Item(i+1,name, price, stock, latitude, longitude);
            if (dataHelper.getItem(i+1+"") == null){
                dataHelper.insertItem(item);
            }
            arrItem.add(item);
        }
        return arrItem;
    }

    @Override
    public void onItemClicked(int position) {
        for(int i = 0; i<newArray.size(); i++){
            Item item = newArray.get(i);
            newArray.remove(i);
            item.setIsVisible(View.INVISIBLE);
            newArray.add(i, item);
        }
        Item item = newArray.get(position);
        newArray.remove(position);
        recyclerView.removeViewAt(position);
        adapter.notifyItemRemoved(position);
        adapter.notifyItemRangeChanged(position, newArray.size());
        if (item.getIsVisible()==View.VISIBLE){
            item.setIsVisible(View.INVISIBLE);
        }else {
            item.setIsVisible(View.VISIBLE);
        }
        newArray.add(position, item);
        adapter.notifyDataSetChanged();

        this.item = item;
    }
}