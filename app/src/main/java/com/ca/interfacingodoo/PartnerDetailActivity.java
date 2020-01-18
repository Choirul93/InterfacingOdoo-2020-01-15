package com.ca.interfacingodoo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.timroes.axmlrpc.XMLRPCCallback;
import de.timroes.axmlrpc.XMLRPCException;
import de.timroes.axmlrpc.XMLRPCServerException;

public class PartnerDetailActivity extends AppCompatActivity {
    private OdooUtility odoo;
    private String uid;
    private String password;
    private String serverAddress;
    private String database;

    EditText etName, etAddress, etPhone, etCountry;
    LinearLayout llBtn;
    Button btnCreate;
    private long searchTaskId,
            updateTaskId,
            deleTaskId,
            createTaskId;


    private String name;
    private Partner partner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partner_detail);

        uid = SharedData.getKey(this,"uid");
        password = SharedData.getKey(this,"password");
        serverAddress = SharedData.getKey(this,"serverAddress");
        database = SharedData.getKey(this,"database");

        odoo = new OdooUtility(serverAddress,"object");

        etName = findViewById(R.id.etName);
        etAddress = findViewById(R.id.etAddress);
        etPhone= findViewById(R.id.etPhone);
        etCountry = findViewById(R.id.etCountry);
        llBtn = findViewById(R.id.llbtn);
        btnCreate = findViewById(R.id.btnCreate);

        name = getIntent().getStringExtra("name");

        if(name != null ){
            etName.setText(name);
            searchPartnerByName();
            llBtn.setVisibility(View.VISIBLE);
        } else{
            btnCreate.setVisibility(View.VISIBLE);

        }


        partner = new Partner();

    }
    public void fiilPartnerDetail(){
        etAddress.setText(partner.getStreet());
        etPhone.setText(partner.getPhone());
        etCountry.setText(partner.getCountry_name());
    }
    public void onUpdate(View view){
        final String name = etName.getText().toString();
        final String street = etAddress.getText().toString();
        final String phone = etPhone.getText().toString();

        List data = Arrays.asList(
                Arrays.asList(partner.getId()),
                new HashMap(){{
                    put("name",name);
                    put("street",street);
                    put("phone",phone);
                }}
        );
        updateTaskId = odoo.update(listener,
                database,
                uid,
                password,
                "res.partner",
                data);

    }



    public void onDelete(View view){
        List id = new ArrayList();
        id.add(partner.getId());

        deleTaskId = odoo.delete(listener,
                database,
                uid,
                password,
                "res.partner",
                id);
    }

    public void onCreate(View view){
        final String name = etName.getText().toString();
        final String street = etAddress.getText().toString();
        final String phone = etPhone.getText().toString();

        List data = Arrays.asList(
                new HashMap(){{
                    put("name",name);
                    put("street",street);
                    put("phone",phone);
                }}
        );
        createTaskId = odoo.create(listener,
                database,
                uid,
                password,
                "res.partner",
                data);

    }

    public void searchPartnerByName(){
        List conditions = Arrays.asList(
                Arrays.asList(
                        Arrays.asList("name","=",name)
                )
        );

        Map fields = new HashMap(){{
            put("fields", Arrays.asList(
                    "id",
                    "name",
                    "street",
                    "country_id",
                    "phone"
            ));
        }};

        searchTaskId = odoo.search_read(listener,
                database,
                uid,
                password,"res.partner",
                conditions,
                fields);
    }

    XMLRPCCallback listener = new XMLRPCCallback() {
        @Override
        public void onResponse(long id, Object result) {
            Looper.prepare();
            if(id == searchTaskId){
                Object[] classObjs = (Object[])result;
                int length  = classObjs.length;

                if(length>0){
                    for(int i= 0; i<length; i++ ){
                        Map<String,Object> classObj = (Map<String,Object>)classObjs[i];
                        partner.setData(classObj);

                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            fiilPartnerDetail();
                        }
                    });


                }
                else {
                    Toast.makeText(PartnerDetailActivity.this,"partner not found",Toast.LENGTH_LONG).show();
                }
            }
            else if(id == updateTaskId){
                final Boolean updateResult = (Boolean)result;

                if(updateResult){
                    odoo.MessageDialog(PartnerDetailActivity.this,"Berhasil update");
                    finish();
                }
                else odoo.MessageDialog(PartnerDetailActivity.this,"Gagal update");
            }

            else if(id == deleTaskId){
                final Boolean deleteteResult = (Boolean)result;

                if(deleteteResult){
                    odoo.MessageDialog(PartnerDetailActivity.this,"Berhasil delete");
                    finish();
                }
                else odoo.MessageDialog(PartnerDetailActivity.this,"Gagal delete");

            }
            else if(id == createTaskId){
                String crateResult = result.toString();
                if(crateResult != null){
                    odoo.MessageDialog(PartnerDetailActivity.this,"Berhasil create"+crateResult);
                    //finish();
                }
                else odoo.MessageDialog(PartnerDetailActivity.this,"Gagal delete");
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

