package com.mobile.jimsgene;

import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class PopupActivity extends AppCompatActivity {

    Button addToCart, cancel;
    String kg, priceAndKg, fruit_name;
    DatabaseHelper myDb;
    TextView kg_pop, priceAndKg_pop, fruit_name_pop, total_pop;
    ImageView fruit_pic_pop;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pop_activity);

        myDb = new DatabaseHelper(this);

        addToCart = findViewById(R.id.addToCart_pop);
        cancel = findViewById(R.id.cancel_pop);

        //pop
        kg_pop = findViewById(R.id.kg_pop);
        priceAndKg_pop = findViewById(R.id.priceAndKg_pop);
        fruit_name_pop = findViewById(R.id.fruit_name_pop);
        fruit_pic_pop = findViewById(R.id.fruit_pic_pop);
        total_pop = findViewById(R.id.total_pop);


        Bundle extras = getIntent().getExtras();

        if (extras != null) {

            //details
            kg = extras.getString("kg");
            priceAndKg = extras.getString ("priceAndKg"); // slash-aar split hiij avna.
            fruit_name = extras.getString ("fruit_name");
            Resources resources = this.getResources();
            int resourceId = resources.getIdentifier(extras.getString("pic_name"), "drawable", this.getPackageName());
            fruit_pic_pop.setBackground(resources.getDrawable(resourceId));
        }


//        Drawable the_fruit_pic = fruit_pic.getBackground().getCurrent();
        String the_price_text = priceAndKg.toString(),
                the_fruit_name = fruit_name.toString();
        int the_price = new Integer(the_price_text.split("/")[0]),
                the_kg = new Integer(kg);

        //set pops
        total_pop.setText(String.valueOf(the_price * the_kg));
        kg_pop.setText(String.valueOf(the_kg));
        priceAndKg_pop.setText(the_price_text);
        fruit_name_pop.setText(the_fruit_name);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.8), (int)(height*.5));

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = -20;

        getWindow().setAttributes(params);

        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Cursor temp = myDb.getUserIdByUsername() ;
                int user_id = extras.getInt("user_id");
                int fruit_id = extras.getInt("fruit_id");
                int[] temp = myDb.checkFruitExistWithUser(user_id, fruit_id);
                if(temp[0] != -1)
                {
                    myDb.updateCartData(temp[0],user_id,fruit_id,temp[1] + the_kg, false);
                    Toast.makeText(PopupActivity.this,"Амжилттай нэмэгдлээ",Toast.LENGTH_LONG).show();
                }
                else
                {
                    boolean isInserted = myDb.addToCart(user_id, fruit_id, the_kg,false, new Integer(total_pop.getText().toString()));
                    if(isInserted)
                        Toast.makeText(PopupActivity.this,"Амжилттай нэмэгдлээ",Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(PopupActivity.this,"Системд алдаа гарлаа",Toast.LENGTH_LONG).show();
                }

                finish();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
