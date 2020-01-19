package com.ca.interfacingodoo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.timroes.axmlrpc.XMLRPCCallback;
import de.timroes.axmlrpc.XMLRPCException;
import de.timroes.axmlrpc.XMLRPCServerException;

public class SalesOrderDetailActivity extends AppCompatActivity {
    private OdooUtility odoo;
    private String uid;
    private String password;
    private String serverAddress;
    private String database;

    private long searchTaskId, searchSoLineTaskId, createTaskId;


    EditText etDate,
            etCustomer,
            etReferance,
            etWarehouse,
            etTotal;

    RecyclerView recycleviewSoLine;
    SoLineAdapter soLineAdapter;

    String name;
    SalesOrder salesOrder;
    private List<SalesOrderLine> salesOrderLineList = new ArrayList<>();

    String date;

    int partner_id;
    String partner_name;
    int warehouse_id;
    String warehouse_name;

    public static final String activity_name = SalesOrderDetailActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_order_detail);
        uid = SharedData.getKey(this, "uid");
        password = SharedData.getKey(this, "password");
        serverAddress = SharedData.getKey(this, "serverAddress");
        database = SharedData.getKey(this, "database");

        odoo = new OdooUtility(serverAddress, "object");
        etDate = findViewById(R.id.etDate);
        etReferance = findViewById(R.id.etReferance);
        etWarehouse = findViewById(R.id.etWarehouse);
        etCustomer = findViewById(R.id.etCustomer);
        etTotal = findViewById(R.id.etTotal);
        recycleviewSoLine = findViewById(R.id.recycleviewSoLine);
        salesOrder = new SalesOrder();

        name = getIntent().getStringExtra("name");
        if(name != null) searchSalesOrderByName();
        else {

            String soLine = getIntent().getStringExtra("soLine");
            Gson gson = new Gson();
            Type listOfClassObject = new TypeToken<ArrayList<SalesOrderLine>>() {}.getType();
            salesOrderLineList.addAll((Collection<? extends SalesOrderLine>) gson.fromJson(soLine, listOfClassObject));

            Toast.makeText(this,""+salesOrderLineList.size(),Toast.LENGTH_LONG).show();

            date = Helper.getTimeStamp("yyyy-MM-dd HH:mm:ss");
            etDate.setText(date);
            double total = getIntent().getDoubleExtra("total_price",0);
            etTotal.setText(String.valueOf(total));



        }

        soLineAdapter = new SoLineAdapter(this,salesOrderLineList);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recycleviewSoLine.setLayoutManager(layoutManager);

        recycleviewSoLine.setItemAnimator(new DefaultItemAnimator());
        recycleviewSoLine.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        recycleviewSoLine.setAdapter(soLineAdapter);


        etCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SalesOrderDetailActivity.this,PartnerActivity.class);
                intent.putExtra("sourceActivity",activity_name);
                startActivityForResult(intent,2);

            }
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode ==2){
            partner_name = data.getStringExtra("partner_name");
            partner_id = data.getIntExtra("partner_id",0);
            etCustomer.setText(partner_name);
        }
    }

    void fiilSalesOrderDetail() {
        etDate.setText(salesOrder.getDate_order());
        etReferance.setText(salesOrder.getClient_order_ref());
        etWarehouse.setText(salesOrder.getWarehouse_name());
        etCustomer.setText(salesOrder.getPartner_name());
        etTotal.setText(String.valueOf(salesOrder.getAmount_total()));

        searchSalesOrderLineBySoid();
    }

    public void searchSalesOrderByName() {
        List conditions = Arrays.asList(
                Arrays.asList(
                        Arrays.asList("name", "=", name)
                )
        );

        Map fields = new HashMap() {{
            put("fields", Arrays.asList(
                    "id",
                    "name",
                    "partner_id",
                    "date_order",
                    "warehouse_id",
                    "client_order_ref",
                    "amount_total"
            ));
        }};

        searchTaskId = odoo.search_read(listener,
                database,
                uid,
                password, "sale.order",
                conditions,
                fields);
    }

    public void searchSalesOrderLineBySoid() {
        List conditions = Arrays.asList(
                Arrays.asList(
                        Arrays.asList("id", "=", salesOrder.getId())
                )
        );

        Map fields = new HashMap() {{
            put("fields", Arrays.asList(
                    "id",
                    "product_id",
                    "product_uom_id",
                    "product_uom_qty",
                    "price_unit",
                    "price_subtotal"

            ));
        }};
        //[1,"tahu"]

        searchSoLineTaskId = odoo.search_read(listener,
                database,
                uid,
                password, "sale.order.line",
                conditions,
                fields);
    }

    XMLRPCCallback listener = new XMLRPCCallback() {
        @Override
        public void onResponse(long id, Object result) {
            Looper.prepare();
            if (id == searchTaskId) {
                Object[] classObjs = (Object[]) result;
                int length = classObjs.length;

                if (length > 0) {
                    for (int i = 0; i < length; i++) {
                        Map<String, Object> classObj = (Map<String, Object>) classObjs[i];
                        salesOrder.setdata(classObj);
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            fiilSalesOrderDetail();
                        }
                    });

                } else {
                    odoo.MessageDialog(SalesOrderDetailActivity.this, "sales order nott found");
                }


            }

            else if(id == searchSoLineTaskId){
                Object[] classObjs = (Object[]) result;
                int length = classObjs.length;

                if (length > 0) {
                    salesOrderLineList.clear();
                    for (int i = 0; i < length; i++) {
                        SalesOrderLine salesOrderLine = new SalesOrderLine();
                        Map<String, Object> classObj = (Map<String, Object>) classObjs[i];
                        salesOrderLine.setData(classObj);
                        salesOrderLineList.add(salesOrderLine);
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            soLineAdapter.notifyDataSetChanged();
                        }
                    });

                } else {
                    odoo.MessageDialog(SalesOrderDetailActivity.this, "sales order nott found");
                }

            }
            else if(id == createTaskId){
                String crateResult = result.toString();
                if(crateResult != null){
                    odoo.MessageDialog(SalesOrderDetailActivity.this,"Berhasil create"+crateResult);
                    //finish();
                }
                else odoo.MessageDialog(SalesOrderDetailActivity.this,"Gagal delete");
            }

            Looper.loop();

        }

        @Override
        public void onError(long id, XMLRPCException error) {
            Looper.prepare();
            odoo.MessageDialog(SalesOrderDetailActivity.this,error.getMessage());
            Looper.loop();

        }

        @Override
        public void onServerError(long id, XMLRPCServerException error) {
            Looper.prepare();
            odoo.MessageDialog(SalesOrderDetailActivity.this,error.getMessage());
            Looper.loop();

        }
    };

    public void OnCreateSalesOrder(View view) {

        final List salesOrderLine = new ArrayList();
        int size = salesOrderLineList.size();

        //0 = create,
        //1 = update,
        //2 = delete,

        for(int i =0; i<size; i++){
            final int finalI = i;
            salesOrderLine.add(Arrays.asList(
                    0,0,new HashMap(){{
                        put("product_id",salesOrderLineList.get(finalI).getProduct_id());
                        put("product_uom_qty",salesOrderLineList.get(finalI).getProduct_uom_qty());
                        put("product_uom_id",salesOrderLineList.get(finalI).getProduct_uom_id());
                        put("price_unit",salesOrderLineList.get(finalI).getPrice_unit());

                    }}
            ));

        }

        List data = Arrays.asList(new HashMap() {{
            put("date_order", date);
            put("partner_id", partner_id);
            put("order_line",salesOrderLine);
        }});

        createTaskId = odoo.create(listener,
                database,
                uid,
                password,"sale.order",
                data);

    }
}
