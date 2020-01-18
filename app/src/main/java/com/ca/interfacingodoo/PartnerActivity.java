package com.ca.interfacingodoo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.timroes.axmlrpc.XMLRPCCallback;
import de.timroes.axmlrpc.XMLRPCException;
import de.timroes.axmlrpc.XMLRPCServerException;

public class PartnerActivity extends AppCompatActivity {
    private OdooUtility odoo;
    private String uid;
    private String password;
    private String serverAddress;
    private String database;

    private long searchTaskId;

    ListView listViewPartner;
    List arrayListPartner;

    EditText etKeyword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partner);

        uid = SharedData.getKey(this,"uid");
        password = SharedData.getKey(this,"password");
        serverAddress = SharedData.getKey(this,"serverAddress");
        database = SharedData.getKey(this,"database");

        odoo = new OdooUtility(serverAddress,"object");

        arrayListPartner = new ArrayList();

        listViewPartner = findViewById(R.id.listViewPartner);
        etKeyword = findViewById(R.id.etKeyword);

        etKeyword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                onChangeText(charSequence.toString());


            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!etKeyword.getText().toString().isEmpty()){
            onChangeText(etKeyword.getText().toString());
        }
    }

    public void OnCreate(View view){

        startActivity(new Intent(this,PartnerDetailActivity.class));
    }

    public void onSync(View view) {
        String keyword = etKeyword.getText().toString();
        arrayListPartner.clear();
        //listViewPartner.notifyAll();

        List conditions = Arrays.asList(
                Arrays.asList(
                        Arrays.asList("name","ilike",keyword)
                )
        );

        //[[["name","ilike","arif"]]]

        Map fields = new HashMap(){{
            put("fields", Arrays.asList(
                    "id",
                    "name"
            ));
        }};

        searchTaskId = odoo.search_read(listener,
                database,
                uid,
                password,"res.partner",
                conditions,
                fields);

    }

    public void onChangeText(String key){
        List conditions = Arrays.asList(
                Arrays.asList(
                        Arrays.asList("name","ilike",key)
                )
        );

        //[[["name","ilike","arif"]]]

        Map fields = new HashMap(){{
            put("fields", Arrays.asList(
                    "id",
                    "name"
            ));
        }};

        searchTaskId = odoo.search_read(listener,
                database,
                uid,
                password,"res.partner",
                conditions,
                fields);

    }

    public void fillListPartner(){
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                arrayListPartner);

        listViewPartner.setAdapter(adapter);
        listViewPartner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int position = i;
                String name = (String)arrayListPartner.get(i);
                Toast.makeText(PartnerActivity.this,"name ===> "+name,Toast.LENGTH_LONG).show();

                Intent intent = new Intent(PartnerActivity.this, PartnerDetailActivity.class);
                intent.putExtra("name",name);
                startActivity(intent);
            }
        });
    }

    XMLRPCCallback listener = new XMLRPCCallback() {
        @Override
        public void onResponse(long id, Object result) {
            Looper.prepare();
            if(id ==searchTaskId){
                Object[] classObjs = (Object[])result;
                int length  = classObjs.length;
                //[["id":1, "disaplay_name":"arif"],["id":2, "disaplay_name":"arif 2"]]

                if(length>0){
                    Toast.makeText(PartnerActivity.this,"partner ada ===>"+length,Toast.LENGTH_LONG).show();
                    arrayListPartner.clear();
                    for(int i =0; i<length; i++){
                        Map<String,Object> classObj = (Map<String,Object>) classObjs[i];
                        arrayListPartner.add(classObj.get("name"));
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            fillListPartner();
                        }
                    });
                } else {
                    arrayListPartner.clear();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            fillListPartner();
                        }
                    });
                    Toast.makeText(PartnerActivity.this,"partner tidak ada ===>",Toast.LENGTH_LONG).show();

                }

            }

            Looper.loop();

        }

        @Override
        public void onError(long id, XMLRPCException error) {
            Looper.prepare();

            Looper.loop();

        }

        @Override
        public void onServerError(long id, XMLRPCServerException error) {
            Looper.prepare();
            Looper.loop();

        }
    };
}
