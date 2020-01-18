package com.ca.interfacingodoo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MenuActivity extends AppCompatActivity {
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        uid = getIntent().getStringExtra("uid");

    }

    public void onPartnerSync(View view) {
        startActivity(new Intent(MenuActivity.this, PartnerActivity.class));

    }

    public void onSOSync(View view) {
        startActivity(new Intent(this,SalesOrderActivity.class));
    }

    public void onProduct(View view) {
        startActivity(new Intent(this,ProductListActivity.class));
    }
}
