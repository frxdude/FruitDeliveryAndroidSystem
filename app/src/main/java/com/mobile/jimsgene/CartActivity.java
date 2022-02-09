package com.mobile.jimsgene;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.media.Image;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.text.ParseException;

public class CartActivity extends AppCompatActivity {
    LinearLayout user_cart_image_list, user_cart_detail_list;
    Button clear, order;
    DatabaseHelper myDb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_activity);
        user_cart_image_list = findViewById(R.id.user_cart_image_list);
//        user_cart_detail_list = findViewById(R.id.user_cart_detail_list);
        Bundle extras = getIntent().getExtras();
        int user_id = extras.getInt("user_id");

        clear = findViewById(R.id.clear);
        order = findViewById(R.id.order);
        myDb = new DatabaseHelper(this);

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDb.truncateTable("user_cart");
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        });

        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDb.truncateTable("user_cart");
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        });

        Resources resources = this.getResources();

        Cursor temp = myDb.getUserById(user_id);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(300, 300);
        params.setMargins(0, 0, 60, 20);

        temp.moveToFirst();

        String username = temp.getString(1);
        Cursor fruit_list = myDb.getFruitByUserId(user_id);

        while(fruit_list.moveToNext())
        {
            TextView fruit_name = new TextView(CartActivity.this);
            TextView fruit_price = new TextView(CartActivity.this);
            TextView fruit_total = new TextView(CartActivity.this);
            ImageView fruit_img = new ImageView(CartActivity.this);
            LinearLayout layout = new LinearLayout(CartActivity.this);
            layout.setOrientation(LinearLayout.HORIZONTAL);

            layout.setLayoutParams(params);

            int resourceIdd = resources.getIdentifier(fruit_list.getString(7), "drawable", this.getPackageName());
            fruit_img.setBackground(resources.getDrawable(resourceIdd));

            fruit_name.setText(fruit_list.getString(1));

            fruit_total.setText(fruit_list.getString(13));

            fruit_price.setText(fruit_list.getString(2));

            layout.addView(fruit_img);
            layout.addView(fruit_name);
            layout.addView(fruit_price);
            layout.addView(fruit_total);
            user_cart_image_list.addView(layout);

//            user_cart_detail_list.addView(layout);

        }
    }
}