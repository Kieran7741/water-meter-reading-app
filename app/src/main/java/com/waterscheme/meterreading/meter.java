package com.waterscheme.meterreading;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;


public class meter extends AppCompatActivity {

    Read_json json;
    String area = ""; // or other values


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.meter_activity);
        HashMap<String, String> hmap = new HashMap<String, String>();

        json = new Read_json(this);
        Bundle b = getIntent().getExtras();
        if(b != null)
            area = b.getString("Area");

//        Toast.makeText(this,area,Toast.LENGTH_LONG).show();
        TextView tv = findViewById(R.id.meter_text_view);
        tv.setText("Select meter in " + area);
        ListView lv = (ListView) findViewById(R.id.meter_list);

        // Instanciating an array list (you don't need to do this,
        // you already have yours).
        final LinkedHashMap<String, List> owner_meter_map = json.get_meters_list(area);

        Log.d("debugging", "Owner Keys: " + owner_meter_map.keySet().toString());
        final List<String> owners = new ArrayList<>(owner_meter_map.keySet());
        final List<String> list_view_items = new ArrayList<>();

        for (String owner: owners) {
            for (String meter: (List<String>)Objects.requireNonNull(owner_meter_map.get(owner))) {

                list_view_items.add(meter + ":" + owner);
            }
        }
        // This is the array adapter, it takes the context of the activity as a
        // first parameter, the type of list view as a second parameter and your
        // array as a third parameter.
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                list_view_items);

        lv.setAdapter(arrayAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(meter.this,record_reading.class);
                //based on item add info to intent

                Bundle b = new Bundle();
                b.putString("area", area);
                b.putString("meter", list_view_items.get(position).split(":")[0]); //Your id
                b.putString("owner", list_view_items.get(position).split(":")[1]);
                intent.putExtras(b); //Put your id to your next Intent
                startActivity(intent);
            }
        });


    }

}
