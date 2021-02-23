package com.project.dotamarketplace;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.project.dotamarketplace.model.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class DataHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "dotaMarketplace.db";
    private static final int DATABASE_VERTSION = 1;

    private static final String TABLE_USERS = "USER";
    private static final String TABLE_ITEMS = "ITEM";
    private static final String TABLE_TRANSACTIONS = "TRANSACTIONs";


    private static final String KEY_USER_ID = "USERID";
    private static final String KEY_USER_NAME = "NAME";
    private static final String KEY_USER_USERNAME = "USERNAME";
    private static final String KEY_USER_PASSWORD = "PASSWORD";
    private static final String KEY_USER_PHONE = "PHONENUMBER";
    private static final String KEY_USER_GENDER = "GENDER";
    private static final String KEY_USER_BALANCE = "BALANCE";

    private static final String KEY_ITEM_ID = "ITEMID";
    private static final String KEY_ITEM_NAME = "NAME";
    private static final String KEY_ITEM_PRICE = "PRICE";
    private static final String KEY_ITEM_STOCK = "STOCK";
    private static final String KEY_ITEM_LATITUDE = "LATITUDE";
    private static final String KEY_ITEM_LONGITUDE = "LONGITUDE";


    private  static final String KEY_TRANSACTION_ID = "TRANSACTIONID";
    private  static final String KEY_TRANSACTION_DATE = "TRANSACTIONDATE";
    private  static final String KEY_TRANSACTION_QUANTITY = "QUANTITY";




    public DataHelper(Context context){
        super(context, DATABASE_NAME,null,DATABASE_VERTSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TRANSACTION_TABLE = "CREATE TABLE " + TABLE_TRANSACTIONS +
                "(" +
                KEY_TRANSACTION_ID + " INT PRIMARY KEY," +
                KEY_USER_ID + " INT REFERENCES " + TABLE_USERS + ", " +
                KEY_ITEM_ID + " INT REFERENCES " + TABLE_ITEMS + ", " +
                KEY_TRANSACTION_QUANTITY + " INT" + ", " +
                KEY_TRANSACTION_DATE + " VARCHAR(50)" +
                ")";
        String CREATE_POSTS_TABLE = "CREATE TABLE " + TABLE_ITEMS +
                "(" +
                KEY_ITEM_ID + " INT PRIMARY KEY," + // Define a primary key
                KEY_ITEM_NAME + " TEXT," +
                KEY_ITEM_PRICE + " INT," +
                KEY_ITEM_STOCK + " INT," +
                KEY_ITEM_LATITUDE + " DOUBLE," +
                KEY_ITEM_LONGITUDE + " DOUBLE" +
                ")";

        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS +
                "(" +
                KEY_USER_ID + " INT PRIMARY KEY," +
                KEY_USER_NAME + " VARCHAR(50)," +
                KEY_USER_USERNAME + " VARCHAR(50)," +
                KEY_USER_PASSWORD+ " VARCHAR(50)," +
                KEY_USER_PHONE + " VARCHAR(50)," +
                KEY_USER_GENDER + " VARCHAR(50)," +
                KEY_USER_BALANCE + " INT" +
                ")";

        db.execSQL(CREATE_USERS_TABLE);
        db.execSQL(CREATE_POSTS_TABLE);
        db.execSQL(CREATE_TRANSACTION_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql1 = "DROP TABLE IF EXISTS " + TABLE_ITEMS;
        String sql2 = "DROP TABLE IF EXISTS " + TABLE_USERS;
        String sql3 = "DROP TABLE IF EXISTS " + TABLE_TRANSACTIONS;

        db.execSQL(sql1);
        db.execSQL(sql2);
        db.execSQL(sql3);

        onCreate(db);
    }

    public int getLastID(){
        int lastID = 0;
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT USERID FROM " + TABLE_USERS + " ORDER BY USERID DESC LIMIT 1;", null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    lastID = cursor.getInt(cursor.getColumnIndex(KEY_USER_ID));
                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get posts from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return lastID;
    }

    public int getBalance(String userid){
        int balance = 0;
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[] {KEY_USER_BALANCE},
                KEY_USER_ID + "=?", new String[]{userid},null,null,null);
        try {
            if (cursor != null && cursor.moveToFirst()&& cursor.getCount()>0) {
                //if cursor has value then in user database there is user associated with this given email
                    balance = cursor.getInt(cursor.getColumnIndex(KEY_USER_BALANCE));
                    return balance;


                //Match both passwords check they are same or not

            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get posts from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return balance;
    }

    public User login(String username, String password){
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[] {KEY_USER_ID, KEY_USER_NAME, KEY_USER_USERNAME, KEY_USER_PASSWORD, KEY_USER_PHONE, KEY_USER_GENDER, KEY_USER_BALANCE},
                KEY_USER_USERNAME + "=?", new String[]{username},null,null,null);
        try {
            if (cursor != null && cursor.moveToFirst()&& cursor.getCount()>0) {
                //if cursor has value then in user database there is user associated with this given email
                User user1 = new User(cursor.getInt(cursor.getColumnIndex(KEY_USER_ID)),
                        cursor.getString(cursor.getColumnIndex(KEY_USER_NAME)),
                        cursor.getString(cursor.getColumnIndex(KEY_USER_USERNAME)),
                        cursor.getString(cursor.getColumnIndex(KEY_USER_PASSWORD)),
                        cursor.getString(cursor.getColumnIndex(KEY_USER_PHONE)),
                        cursor.getString(cursor.getColumnIndex(KEY_USER_GENDER)),
                        cursor.getInt(cursor.getColumnIndex(KEY_USER_BALANCE))
                );

                Log.d("Password", password);
                Log.d("PasswordDB", user1.getPassword());

                //Match both passwords check they are same or not
                if (password.equalsIgnoreCase(user1.getPassword())) {


                    return user1;
                }
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get posts from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return null;
    }

    public User getUser(String userID){
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[] {KEY_USER_ID, KEY_USER_NAME, KEY_USER_USERNAME, KEY_USER_PASSWORD, KEY_USER_PHONE, KEY_USER_GENDER, KEY_USER_BALANCE},
                KEY_USER_ID + "=?", new String[]{userID},null,null,null);
        try {
            if (cursor != null && cursor.moveToFirst()&& cursor.getCount()>0) {
                //if cursor has value then in user database there is user associated with this given email
                User user1 = new User(cursor.getInt(cursor.getColumnIndex(KEY_USER_ID)),
                        cursor.getString(cursor.getColumnIndex(KEY_USER_NAME)),
                        cursor.getString(cursor.getColumnIndex(KEY_USER_USERNAME)),
                        cursor.getString(cursor.getColumnIndex(KEY_USER_PASSWORD)),
                        cursor.getString(cursor.getColumnIndex(KEY_USER_PHONE)),
                        cursor.getString(cursor.getColumnIndex(KEY_USER_GENDER)),
                        cursor.getInt(cursor.getColumnIndex(KEY_USER_BALANCE))
                );
                return user1;
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get posts from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return null;
    }

    public Item getItem(String itemID){
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.query(TABLE_ITEMS, new String[] {KEY_ITEM_ID, KEY_ITEM_NAME, KEY_ITEM_STOCK, KEY_ITEM_LATITUDE, KEY_ITEM_LONGITUDE, KEY_ITEM_PRICE},
                KEY_ITEM_ID + "=?", new String[]{itemID},null,null,null);
        try {
            if (cursor != null && cursor.moveToFirst()&& cursor.getCount()>0) {
                //if cursor has value then in user database there is user associated with this given email
                Item item = new Item(cursor.getInt(cursor.getColumnIndex(KEY_ITEM_ID)),
                        cursor.getString(cursor.getColumnIndex(KEY_ITEM_NAME)),
                        cursor.getInt(cursor.getColumnIndex(KEY_ITEM_PRICE)),
                        cursor.getInt(cursor.getColumnIndex(KEY_ITEM_STOCK)),
                        cursor.getDouble(cursor.getColumnIndex(KEY_ITEM_LATITUDE)),
                        cursor.getDouble(cursor.getColumnIndex(KEY_ITEM_LONGITUDE))
                );
                return item;
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get posts from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return null;
    }

    public void insertUser(User user){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues value = new ContentValues();
        value.put(KEY_USER_ID, user.getUserID());
        value.put(KEY_USER_NAME, user.getName());
        value.put(KEY_USER_USERNAME, user.getUserName());
        value.put(KEY_USER_PASSWORD, user.getPassword());
        value.put(KEY_USER_PHONE, user.getPhoneNumber());
        value.put(KEY_USER_GENDER, user.getGender());
        value.put(KEY_USER_BALANCE, user.getBalance());

        db.insert(TABLE_USERS, null, value);
    }

    public boolean updateUserBalance(String id, int balance){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues value = new ContentValues();
        value.put(KEY_USER_BALANCE, balance);

        long result = db.update(TABLE_USERS, value,"USERID = ?", new String[]{id});

        if (result == -1) return false;


        return true;
    }

    public boolean deleteUser(String userID){
        SQLiteDatabase db = getWritableDatabase();

        long result = db.delete(TABLE_USERS, null, null);

        //long result = db.delete(TABLE_USERS,"USERID = ?", new String[]{userID});

        return result != -1;
    }

    public Cursor getAlluser(){
        SQLiteDatabase db = getWritableDatabase();
        Cursor result = db.rawQuery("SELECT * FROM " + TABLE_USERS, null);
        return  result;
    }

    public ArrayList<Item> getAllItem(){
        ArrayList<Item> arrItem = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_ITEMS, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    Item item = new Item(cursor.getInt(cursor.getColumnIndex(KEY_ITEM_ID)),
                            cursor.getString(cursor.getColumnIndex(KEY_ITEM_NAME)),
                            cursor.getInt(cursor.getColumnIndex(KEY_ITEM_PRICE)),
                            cursor.getInt(cursor.getColumnIndex(KEY_ITEM_STOCK)),
                            cursor.getDouble(cursor.getColumnIndex(KEY_ITEM_LATITUDE)),
                            cursor.getDouble(cursor.getColumnIndex(KEY_ITEM_LONGITUDE))
                    );
                    arrItem.add(item);
                } while(cursor.moveToNext());
                return arrItem;
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get posts from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return  null;
    }

    public ArrayList<Transaction> getTransactionHistory(String userid){
        ArrayList<Transaction> arrTransaction = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_TRANSACTIONS +" WHERE USERID = " + userid, null);
        try {
            if(cursor.moveToFirst()){
                do {
                    Transaction  transaction= new Transaction(cursor.getInt(cursor.getColumnIndex(KEY_TRANSACTION_ID)),
                            cursor.getInt(cursor.getColumnIndex(KEY_USER_ID)),
                            cursor.getInt(cursor.getColumnIndex(KEY_ITEM_ID)),
                            cursor.getInt(cursor.getColumnIndex(KEY_TRANSACTION_QUANTITY)),
                            cursor.getString(cursor.getColumnIndex(KEY_TRANSACTION_DATE))
                            );
                    arrTransaction.add(transaction);
                }while (cursor.moveToNext());
                return arrTransaction;
            }
        }catch (Exception e){
            Log.d(TAG, "Error while trying to get posts from database");
        }finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return  null;
    }

    public void insertItem(Item item){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues value = new ContentValues();
        value.put(KEY_ITEM_ID, item.getItemId());
        value.put(KEY_ITEM_NAME, item.getName());
        value.put(KEY_ITEM_PRICE, item.getPrice());
        value.put(KEY_ITEM_STOCK, item.getStock());
        value.put(KEY_ITEM_LATITUDE, item.getLatitude());
        value.put(KEY_ITEM_LONGITUDE, item.getLatitude());

        db.insert(TABLE_ITEMS, null, value);
    }

    public boolean updateItemStock(String id, int stock){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues value = new ContentValues();
        value.put(KEY_ITEM_STOCK, stock);

        long result = db.update(TABLE_ITEMS, value,"ITEMID = ?", new String[]{id});

        if (result == -1) return false;


        return true;
    }

    public int getLastTransactionID(){
        int lastID = 0;
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT TRANSACTIONID FROM " + TABLE_TRANSACTIONS + " ORDER BY TRANSACTIONID DESC LIMIT 1;", null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    lastID = cursor.getInt(cursor.getColumnIndex(KEY_TRANSACTION_ID));
                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get posts from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        lastID += 1;
        return lastID;
    }

    public void insertTransaction(Transaction transaction){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues value = new ContentValues();
        value.put(KEY_TRANSACTION_ID, transaction.getTransactionID());
        value.put(KEY_ITEM_ID, transaction.getItemID());
        value.put(KEY_USER_ID, transaction.getUserID());
        value.put(KEY_TRANSACTION_QUANTITY, transaction.getQuantity());
        value.put(KEY_TRANSACTION_DATE, transaction.getTransactionDate());

        db.insert(TABLE_TRANSACTIONS, null, value);
    }

    public boolean deleteHistory(String userID){
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(TABLE_TRANSACTIONS, KEY_USER_ID + "=?", new String[]{userID})>0;
    }
}
