package com.project.dotamarketplace.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import android.Manifest;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.project.dotamarketplace.DataHelper;
import com.project.dotamarketplace.R;
import com.project.dotamarketplace.model.Item;
import com.project.dotamarketplace.model.Transaction;
import com.project.dotamarketplace.model.User;

public class BuyActivity extends AppCompatActivity {
    TextView itemName, itemStock, itemPrice, totalPrice, buyNotif, txtSaldo;
    ImageView itemImage;
    EditText inputQty;
    Button btnShowLocation, btnCheckout;
    Item item;
    User user;
    DataHelper dataHelper;
    int totalPricce, qty;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);
        Intent intent = getIntent();
        item = intent.getParcelableExtra("item");
        user = intent.getParcelableExtra("user");
        requestSendReceiveSMS();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();

    }

    private void init() {
        dataHelper = new DataHelper(this);
        itemName = findViewById(R.id.itemName);
        itemStock = findViewById(R.id.itemStock);
        itemPrice = findViewById(R.id.itemPrice);
        itemImage = findViewById(R.id.itemImage);
        totalPrice = findViewById(R.id.totalPrice);
        inputQty = findViewById(R.id.inputQuantity);
        buyNotif = findViewById(R.id.buyNotif);
        btnCheckout = findViewById(R.id.btnCheckout);
        btnShowLocation = findViewById(R.id.btnScowLocation);
        txtSaldo = findViewById(R.id.lblSaldo);

        loadData();



        btnShowLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SellerLocationActivity.class);
                intent.putExtra("name", item.getName());
                intent.putExtra("latitude", item.getLatitude());
                intent.putExtra("longitude", item.getLogitude());
                startActivity(intent);
            }
        });

        inputQty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().isEmpty()){
                   qty = 0;
                }else{
                    qty = Integer.parseInt(editable.toString());
                }
                totalPricce = qty*item.getPrice();
                totalPrice.setText("Total Price: " + totalPricce);

            }
        });

        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (inputQty.getText().toString().isEmpty()){
                    buyNotif.setText("Please fill quantity");
                    buyNotif.setVisibility(View.VISIBLE);
                }else if(qty>item.getStock()){
                    buyNotif.setText("Your qty is out of stock");
                    buyNotif.setVisibility(View.VISIBLE);
                }else if(totalPricce>user.getBalance()){
                    buyNotif.setText("Please Top up your balance");
                    buyNotif.setVisibility(View.VISIBLE);
                }else {
                    int transactionId = dataHelper.getLastTransactionID() + 1;
                    Transaction transaction = new Transaction(transactionId, user.getUserID(), item.getItemId(), qty);
                    dataHelper.insertTransaction(transaction);
                    dataHelper.updateItemStock(item.getItemId()+"", item.getStock()-qty);
                    dataHelper.updateUserBalance(user.getUserID()+"", user.getBalance()-totalPricce);
                    String message = "Transaction Success";
                    SmsManager.getDefault().sendTextMessage("5554", null, message, null,null);

                    NotificationCompat.Builder builder = new NotificationCompat.Builder(BuyActivity.this, "Dota")
                            .setContentTitle("Transaksi Berhasil")
                            .setContentText("Pembelian " + item.getName() + " sebanyak" + qty + "telah berhasil.")
                            .setSmallIcon(R.drawable.ic_launcher_foreground)
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT);
                    NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.notify(0, builder.build());
                    Toast.makeText(BuyActivity.this, "Transaction Success", Toast.LENGTH_SHORT).show();


                    finish();


                }

            }
        });
    }

    private void requestSendReceiveSMS(){
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECEIVE_SMS, Manifest.permission.SEND_SMS}, 0);
    }

    private void loadData(){
        user = dataHelper.getUser(user.getUserID()+"");
        item = dataHelper.getItem(item.getItemId()+"");
        itemName.setText(item.getName());
        itemPrice.setText(item.getPrice()+"");
        itemStock.setText(item.getStock()+"");
        String imageName = "image"+item.getItemId();
        int resID = getResources().getIdentifier(imageName, "drawable", getPackageName());
        itemImage.setImageResource(resID);
        inputQty.setText("");
        txtSaldo.setText("Your Balance: " + user.getBalance());
    }
}