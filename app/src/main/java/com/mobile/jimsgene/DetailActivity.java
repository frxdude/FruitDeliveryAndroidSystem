package com.mobile.jimsgene;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParseException;

public class DetailActivity extends AppCompatActivity {
    Button increase, decrease, addToCart;
    DatabaseHelper myDb;
    ImageView fruit_pic;
    TextView kg, priceAndKg, fruit_name, calories, description;
    String username = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity);
        increase = findViewById(R.id.increase);
        decrease = findViewById(R.id.decrease);
        kg = findViewById(R.id.kg);
        addToCart = findViewById(R.id.addToCart);
        fruit_pic = findViewById(R.id.fruit_pic);
        priceAndKg = findViewById(R.id.priceAndKg);
        fruit_name = findViewById(R.id.fruit_name);
        calories = findViewById(R.id.fruit_calories);
        description = findViewById(R.id.fruit_description);

        myDb = new DatabaseHelper(this);


        Bundle extras = getIntent().getExtras();
        Bundle extras1 = new Bundle();

        username = extras.getString("username");

        Resources resources = this.getResources();
        int resourceId = resources.getIdentifier(extras.getString("pic_name"), "drawable", this.getPackageName());
        fruit_pic.setBackground(resources.getDrawable(resourceId));

        priceAndKg.setText(extras.getInt("price") + priceAndKg.getText().toString());
        fruit_name.setText(extras.getString("name"));
        calories.setText(String.valueOf(extras.getInt("calories")));
        description.setText(extras.getString("description"));



        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("THIS SHOULD BE THE NAME OF THE FRUIT : " + extras.getString("name"));
                String fruitname = extras.getString("name");
                Cursor response = myDb.getFruitIdByName(fruitname);
                response.moveToNext();
                int fruit_id = response.getInt(0);

                Intent popActivity = new Intent(DetailActivity.this, PopupActivity.class);
                extras1.putString ("kg", kg.getText().toString());
                extras1.putString ("priceAndKg", priceAndKg.getText().toString());
                extras1.putString ("fruit_name", fruit_name.getText().toString());
                extras1.putString ("pic_name", extras.getString("pic_name"));
                extras1.putInt ("user_id", extras.getInt("user_id"));
                extras1.putInt ("fruit_id", fruit_id);
                popActivity.putExtras(extras1);
//                fruit_pic.startAnimation(R.anim.bounce);
                startActivity(popActivity);
            }
        });
        increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentKG = new Integer(kg.getText().toString());
                kg.setText(String.valueOf(++currentKG));
            }
        });
        decrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentKG = new Integer(kg.getText().toString());
                kg.setText(String.valueOf(--currentKG));
            }
        });
    }
}