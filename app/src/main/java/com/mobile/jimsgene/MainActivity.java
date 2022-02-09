package com.mobile.jimsgene;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ArrayAdapter<String> searchAdapter;
    LinearLayout layout;
    ImageView top_fruit_pic, cart;
    TextView top_fruit_name, top_fruit_calories, top_fruit_price_and_kg;
    ListView searchList;
    DatabaseHelper myDb;
    int user_id;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mymenu, menu);
        MenuItem menuItem = menu.findItem(R.id.search_icon);


        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Хайлт хийх");


        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Working??");
                searchList.setVisibility(View.VISIBLE);
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                System.out.println("close kinda working");
                searchList.setVisibility(View.GONE);
                return false;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                searchAdapter.getFilter().filter(s);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle bundle = getIntent().getExtras();

        Resources resources = this.getResources();

        Button detailButton = findViewById(R.id.detailButton);
        myDb = new DatabaseHelper(this);

        top_fruit_pic = findViewById(R.id.top_fruit_pic);
        top_fruit_name = findViewById(R.id.top_fruit_name);
        top_fruit_calories = findViewById(R.id.top_fruit_calories);
        top_fruit_price_and_kg = findViewById(R.id.top_fruit_price_and_kg);

        Cursor top = myDb.getTopFruit();
        top.moveToNext();
        top_fruit_price_and_kg.setText(top.getInt(2) + "/кг");
        top_fruit_calories.setText(top.getString(3));
        top_fruit_name.setText(top.getString(1));

        cart = findViewById(R.id.cart);

        int resourceIdd = resources.getIdentifier(top.getString(7), "drawable", this.getPackageName());
        top_fruit_pic.setBackground(resources.getDrawable(resourceIdd));

        ActionBar ab = getSupportActionBar();
        ab.setTitle("Сайн уу ");
        searchList = findViewById(R.id.search_list);
        List<String> myFruitList = new ArrayList<>();
        myFruitList.add("Алим");
        myFruitList.add("Гадил");
        myFruitList.add("Тарвас");
        myFruitList.add("Манго");
//        myFruitList.add("Лемон");
//        myFruitList.add("Киви");
//        myFruitList.add("Интоор");
//        myFruitList.add("Тоор");
        searchAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, myFruitList);
        searchList.setAdapter(searchAdapter);

        searchList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = (String) searchList.getItemAtPosition(position);

                Intent detailActivity = new Intent(MainActivity.this, DetailActivity.class);

                Cursor fruits;

                if ("Гадил".equals(item)) {
                    fruits = myDb.getFruitInfoById(1);
                } else if ("Тарвас".equals(item)) {
                    fruits = myDb.getFruitInfoById(4);
                } else if ("Манго".equals(item)) {
                    fruits = myDb.getFruitInfoById(2);
                } else if ("Алим".equals(item)) {
                    fruits = myDb.getFruitInfoById(3);
                } else
                    fruits = myDb.getFruitInfoById(1);
                fruits.moveToNext();
                String name = bundle.getString("username");
                Cursor temporary = myDb.getUserIdByUsername(name);
                temporary.moveToNext();
                int user_id = temporary.getInt(0);
                detailActivity.putExtra("user_id", user_id);
                detailActivity.putExtra("name", fruits.getString(1));
                detailActivity.putExtra("price", fruits.getInt(2));
                detailActivity.putExtra("calories", fruits.getInt(3));
                detailActivity.putExtra("is_top", fruits.getInt(4));
                detailActivity.putExtra("description", fruits.getString(5));
                detailActivity.putExtra("view_count", fruits.getString(6));
                detailActivity.putExtra("pic_name", fruits.getString(7));
                startActivity(detailActivity);

            }
        });
        layout = (LinearLayout) findViewById(R.id.popularList);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(300, 300);
        params.setMargins(0, 0, 60, 10);

        Intent detailActivity = new Intent(MainActivity.this, DetailActivity.class);

        Cursor fruit_infos = myDb.getTopFruit();
        fruit_infos.moveToNext();
        detailActivity.putExtra("name", fruit_infos.getString(1));
        detailActivity.putExtra("price", fruit_infos.getInt(2));
        detailActivity.putExtra("calories", fruit_infos.getInt(3));
        detailActivity.putExtra("is_top", fruit_infos.getInt(4));
        detailActivity.putExtra("description", fruit_infos.getString(5));
        detailActivity.putExtra("view_count", fruit_infos.getString(6));
        detailActivity.putExtra("pic_name", fruit_infos.getString(7));

        Animation bounceAnimation = AnimationUtils.loadAnimation(this, R.anim.bounce);

        Cursor fruits = myDb.getTableData("fruit");

        if (bundle != null) {
            String name = bundle.getString("username");
            Cursor temporary = myDb.getUserIdByUsername(name);
            temporary.moveToNext();
            user_id = temporary.getInt(0);
            detailActivity.putExtra("user_id", user_id);
            ab.setTitle(ab.getTitle() + name);
        }

        detailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(detailActivity);
            }
        });

        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent cartActivity = new Intent(MainActivity.this, CartActivity.class);
                cartActivity.putExtra("user_id", user_id);
                startActivity(cartActivity);
            }
        });

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View btn) {
                Cursor temp = myDb.getFruitInfoById(btn.getId());
                temp.moveToNext();
                detailActivity.putExtra("name", temp.getString(1));
                detailActivity.putExtra("price", temp.getInt(2));
                detailActivity.putExtra("calories", temp.getString(3));
                detailActivity.putExtra("is_top", temp.getInt(4));
                detailActivity.putExtra("description", temp.getString(5));
                detailActivity.putExtra("view_count", temp.getString(6));
                detailActivity.putExtra("pic_name", temp.getString(7));

                switch (btn.getId()) {
                    case 0:
                        break;
                    case 1:
                        startActivity(detailActivity);
                        System.out.println("First");
                        break;
                    case 2:
                        startActivity(detailActivity);
                        System.out.println("Second");
                        break;
                    case 3:
                        startActivity(detailActivity);
                        System.out.println("Third");
                        break;
                    case 4:
                        startActivity(detailActivity);
                        System.out.println("Fourth");
                        break;
                }
            }
        };
        int id = 0;
        while (fruits.moveToNext() && fruits.getCount() != 0) {
            id++;
            int resourceId = resources.getIdentifier(fruits.getString(7), "drawable", this.getPackageName());
            Button imageView = new Button(this);
            imageView.setBackground(resources.getDrawable(resourceId));

            imageView.setId(View.generateViewId());
            imageView.setLayoutParams(params);
            System.out.println("its my generated id : " + id);
            imageView.setId(id);
            imageView.setOnClickListener(listener);
//            if(imageView.getParent() != null)
//                ((ViewGroup)imageView.getParent()).removeView(imageView);
//            CardView tempo = findViewById(R.id.card_view);
//            tempo.setCardBackgroundColor(R.color.white);
//            tempo.addView(imageView);

//            if (imageView.getParent() != null) {
//                ((ViewGroup) imageView.getParent()).removeView(imageView);
//            }
            layout.addView(imageView);
        }
    }
}