package com.waterscheme.meterreading;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;


public class meter extends AppCompatActivity {

    Read_json json;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.meter_activity);

        json = new Read_json(this);
        Bundle b = getIntent().getExtras();
        String area = ""; // or other values
        if(b != null)
            area = b.getString("Area");

//        Toast.makeText(this,area,Toast.LENGTH_LONG).show();
        TextView tv = findViewById(R.id.meter_text_view);
        tv.setText("Select meter in " + area);
        ListView lv = (ListView) findViewById(R.id.meter_list);

        // Instanciating an array list (you don't need to do this,
        // you already have yours).
        final List<String> your_array_list = json.get_meters_list(area);

        // This is the array adapter, it takes the context of the activity as a
        // first parameter, the type of list view as a second parameter and your
        // array as a third parameter.
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                your_array_list);

        lv.setAdapter(arrayAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(meter.this,record_reading.class);
                //based on item add info to intent

                Bundle b = new Bundle();
                b.putString("meter", your_array_list.get(position)); //Your id
                intent.putExtras(b); //Put your id to your next Intent
                startActivity(intent);
            }
        });


    }

}
