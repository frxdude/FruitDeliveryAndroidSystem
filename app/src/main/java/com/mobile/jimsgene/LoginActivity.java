package com.mobile.jimsgene;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    Button loginButton;
    DatabaseHelper myDb;
    EditText username, password;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        myDb = new DatabaseHelper(this);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);

        loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainActivity = new Intent(LoginActivity.this, MainActivity.class);
                String temp_user, temp_pass;
                temp_user = username.getText().toString();
                temp_pass = password.getText().toString();
                if(temp_user.length() < 1 && temp_pass.length() < 1)
                    Toast.makeText(LoginActivity.this,"Хоосон байж болохгүй",Toast.LENGTH_LONG).show();
                else
                {
                    int balance = myDb.login(username.getText().toString(), password.getText().toString());
                    if(balance != -1)
                    {
                        mainActivity.putExtra("balance",balance);
                        mainActivity.putExtra("username",temp_user);
                        startActivity(mainActivity);
                    }
                    else
                        Toast.makeText(LoginActivity.this,"Нэр эсвэл нууц үг тань буруу байна",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}
