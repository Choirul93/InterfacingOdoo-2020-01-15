package com.ca.interfacingodoo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import de.timroes.axmlrpc.XMLRPCCallback;
import de.timroes.axmlrpc.XMLRPCException;
import de.timroes.axmlrpc.XMLRPCServerException;

public class MainActivity extends AppCompatActivity {

    private OdooUtility odoo;
    private long loginTaskId;

    EditText etServerUrl;
    EditText etDatabase;
    EditText etUserName;
    EditText etPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etServerUrl = findViewById(R.id.etServerUrl);
        etDatabase = findViewById(R.id.etDatabase);
        etUserName = findViewById(R.id.etUserName);
        etPassword = findViewById(R.id.etPassword);

        etServerUrl.setText("http://13.229.202.100:8069");
        etDatabase.setText("and");
        etUserName.setText("admin");
        etPassword.setText("admin");
    }


    public void onLoginOdoo(View view) {

        String password = etPassword.getText().toString();
        String username = etUserName.getText().toString();
        String database = etDatabase.getText().toString();
        String serverUrl = etServerUrl.getText().toString();

        SharedData.setKey(MainActivity.this,"password",password);
        SharedData.setKey(MainActivity.this,"username",username);
        SharedData.setKey(MainActivity.this,"database",database);
        SharedData.setKey(MainActivity.this, "serverAddress",serverUrl);

        odoo = new OdooUtility(serverUrl,"common");
        loginTaskId = odoo.login(listener, database, username, password);

    }


    XMLRPCCallback listener = new XMLRPCCallback() {
        @Override
        public void onResponse(long id, Object result) {
            Looper.prepare();
            if(id ==loginTaskId){
                if(result instanceof Boolean && (Boolean)result == false){
                    odoo.MessageDialog(MainActivity.this,"user tidak ketemukan");
                }
                else{
                    String uid = result.toString();
                    SharedData.setKey(MainActivity.this,"uid",uid);
                    Toast.makeText(MainActivity.this,"Berhasil Login uid ===>"+uid, Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                    intent.putExtra("uid",uid);
                    startActivity(intent);
                }

            }

            Looper.loop();
        }

        @Override
        public void onError(long id, XMLRPCException error) {
            Looper.prepare();
            Toast.makeText(MainActivity.this,"Gagal Login"+error,Toast.LENGTH_LONG).show();
            Looper.loop();

        }

        @Override
        public void onServerError(long id, XMLRPCServerException error) {
            Looper.prepare();
            Toast.makeText(MainActivity.this,"Gagal Login"+error,Toast.LENGTH_LONG).show();
            Looper.loop();
        }
    };
}
