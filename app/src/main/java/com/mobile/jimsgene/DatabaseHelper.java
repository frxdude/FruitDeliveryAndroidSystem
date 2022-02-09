package com.mobile.jimsgene;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "jimsgene.db";

    public static final String FRUIT_TABLE = "fruit";
    public static final String FRUIT_ID = "id";
    public static final String FRUIT_NAME = "name";
    public static final String FRUIT_PRICE = "price";
    public static final String FRUIT_CALORIES = "calories";
    public static final String FRUIT_IS_TOP = "is_top";
    public static final String FRUIT_DESCRIPTION = "description";
    public static final String FRUIT_VIEW_COUNT = "view_count";

    public static final String USER_CART_TABLE = "user_cart";
    public static final String USER_CART_ID = "id";
    public static final String USER_CART_USER_ID = "user_id";
    public static final String USER_CART_FRUIT_ID = "fruit_id";
    public static final String USER_CART_FRUIT_KG = "fruit_kg";
    public static final String USER_CART_IS_ORDERED = "is_ordered";
    public static final String USER_CART_TOTAL = "total";

    public static final String USER_TABLE = "user";
    public static final String USER_ID = "id";
    public static final String USER_USERNAME = "username";
    public static final String USER_BALANCE = "balance";
    public static final String USER_PASSWORD = "password";

    public static final String ORDER_TABLE = "orders";
    public static final String ORDER_ID = "id";
    public static final String ORDER_CART_ID = "cart_id";
    public static final String ORDER_DONE = "done";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + FRUIT_TABLE + " " +
                "(id INTEGER PRIMARY KEY," +
                " name TEXT, " +
                " price INTEGER, " +
                " calories TEXT, " +
                " is_top BOOLEAN, " +
                " description TEXT, " +
                " view_count INTEGER)");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + USER_TABLE + " " +
                "(id INTEGER PRIMARY KEY AUTOINCREMENT," +
                " username TEXT," +
                " password TEXT," +
                " balance INTEGER)");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + USER_CART_TABLE + " " +
                "(id INTEGER PRIMARY KEY AUTOINCREMENT," +
                " user_id INTEGER, " +
                " fruit_id INTEGER, " +
                " fruit_kg INTEGER, " +
                " is_ordered BOOLEAN," +
                " total INTEGER," +
                " FOREIGN KEY(fruit_id) REFERENCES fruit(id)," +
                " FOREIGN KEY(user_id) REFERENCES user(id))");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + ORDER_TABLE  +
                " (id INTEGER PRIMARY KEY AUTOINCREMENT," +
                " cart_id INTEGER," +
                " is_done BOOLEAN," +
                " FOREIGN KEY(cart_id) REFERENCES user_cart(id))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FRUIT_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + USER_CART_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + ORDER_TABLE);
        onCreate(db);
    }

    public Cursor getTableData(String tableName) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + tableName, null);
        return res;
    }
    public int login(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] args = { username, password };
        Cursor response = db.rawQuery("SELECT * FROM " + USER_TABLE + " WHERE username = ? AND password = ?", args);

        if(response.getCount() > 0 && response.moveToFirst())
            return response.getInt(3);
        else
            return -1;
    }
    public Cursor getFruitInfoById(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] args = { String.valueOf(id) };
        Cursor res = db.rawQuery("SELECT * FROM " + FRUIT_TABLE + " WHERE id = ?", args);
        return res;
    }

    public Cursor getTopFruit() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + FRUIT_TABLE + " WHERE is_top = 1", null);
        return res;
    }

   public boolean addToCart(int user_id, int fruit_id, int fruit_kg, boolean is_ordered, int total) {
       SQLiteDatabase db = this.getWritableDatabase();

       ContentValues contentValues = new ContentValues();
       contentValues.put(USER_CART_USER_ID, user_id);
       contentValues.put(USER_CART_FRUIT_ID, fruit_id);
       contentValues.put(USER_CART_FRUIT_KG, fruit_kg);
       contentValues.put(USER_CART_IS_ORDERED, is_ordered);
       contentValues.put(USER_CART_TOTAL, total);

       long result = db.insert(USER_CART_TABLE,null, contentValues);
       if (result == -1)
           return false;
       else
           return true;
   }

   public Cursor getUserIdByUsername(String username) {
       SQLiteDatabase db = this.getWritableDatabase();
       String[] args = { username };
       Cursor response = db.rawQuery("SELECT id FROM " + USER_TABLE + " WHERE username = ?", args);
       return response;
   }

    public Cursor getFruitIdByName(String fruit_name) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] args = { fruit_name };
        Cursor response = db.rawQuery("SELECT * FROM " + FRUIT_TABLE + " WHERE name = ?", args);
        return response;
    }
    public int[] checkFruitExistWithUser(int user_id, int fruit_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int[] RESPOND = new int[2];
        RESPOND[0] = -1;
        String[] args = { String.valueOf(user_id), String.valueOf(fruit_id) };
        Cursor response = db.rawQuery("SELECT * FROM " + USER_CART_TABLE + " WHERE user_id = ? AND fruit_id = ? AND is_ordered = false", args);
//        response.moveToNext();
        if(response.getCount() > 0 && response.moveToNext())
        {
            RESPOND[0] = response.getInt(0);
            RESPOND[1] = response.getInt(3);
        }
        return RESPOND;
    }
    public boolean updateCartData(int id, int user_id, int fruit_id, int fruit_kg, boolean is_ordered) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_CART_USER_ID, user_id);
        contentValues.put(USER_CART_FRUIT_ID, fruit_id);
        contentValues.put(USER_CART_FRUIT_KG, fruit_kg);
        contentValues.put(USER_CART_IS_ORDERED, is_ordered);
        db.update(USER_CART_TABLE, contentValues, "id = ?", new String[] {String.valueOf(id)});
        return true;
    }

    public Cursor getUserById(int id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] args = { String.valueOf(id) };
        Cursor response = db.rawQuery("SELECT * FROM " + USER_TABLE + " WHERE id = ?", args);
        return response;
    }

    public Cursor getFruitByUserId(int id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] args = { String.valueOf(id) };
        Cursor response = db.rawQuery("SELECT * FROM fruit f INNER JOIN user_cart uc ON f.id = uc.fruit_id WHERE uc.user_id = ?", args);
        return response;
    }
    public int truncateTable(String table_name)
    {
        String[] args = { table_name };
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(table_name,null , null);
    }

/*
    public boolean insertStudentData(String name, String code, String gpa) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(STUDENT_CODE, code);
        contentValues.put(STUDENT_NAME, name);
        contentValues.put(STUDENT_GPA, gpa);
        long result = db.insert(FIRST_TABLE_NAME, null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }

    public Cursor getTableData(String tableName) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + tableName, null);
        return res;
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT s.name, l.name FROM " + THIRD_TABLE_NAME + " li INNER JOIN " +
                FIRST_TABLE_NAME + " s ON li.student_id = s.id INNER JOIN " + SECOND_TABLE_NAME +
                " l ON li.lesson_id = l.id", null);
        return res;
    }

    public boolean updateStudentData(String name, String code, String gpa, String id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(STUDENT_CODE, code);
        contentValues.put(STUDENT_NAME, name);
        contentValues.put(STUDENT_GPA, gpa);
        db.update(FIRST_TABLE_NAME, contentValues, "id = ?", new String[] {id});
        return true;
    }
    public Integer deleteData(String id, String dbName)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(dbName,"id = ?",new String[]{id});
    }
 */
}