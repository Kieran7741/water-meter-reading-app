package com.waterscheme.meterreading;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;


public class record_reading extends AppCompatActivity {

    String area = "";
    String owner = "";
    String meter_name = ""; // or other values
    EditText reading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_record_reading);
        TextView tv = findViewById(R.id.meter_name);
        reading = findViewById(R.id.meter_reading);
        Button submit = findViewById(R.id.submit_reading);

        extract_bundle();
        tv.setText(meter_name + ": " + owner);

        submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                save_reading();
                go_back();
                   }
        });

    }

    private void go_back() {
        this.finish();
    }


    public void extract_bundle(){
        Bundle b = getIntent().getExtras();
        if(b != null) {
            area = b.getString("area");
            owner = b.getString("owner");
            meter_name = b.getString("meter");
        }

    }

    /**
     * Called when the submit button is pressed.
     * Attempts to write the meter reading to a file.
     */
    public void save_reading(){
        Toast.makeText(this, "Saving meter reading",Toast.LENGTH_LONG).show();

        if (isExternalStorageWritable()) {
            // block of code to be executed if the condition is true
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            String date = sdf.format(new Date());
            String meter_reading = reading.getText().toString();

            String meter_reading_entry = owner + ","+ meter_name+","+ meter_reading+ ","+ date;
            Log.d("debugging", meter_reading_entry);
            write_to_file(area, meter_reading_entry);

            commit_reading(Integer.parseInt(reading.getText().toString()),meter_name, date);
        } else {
            Toast.makeText(this, "Cannot write to file as access not granted by user", Toast.LENGTH_LONG).show();
        }


    }

    /**
     * Append the line to the associated area file.
     * */
    public void write_to_file(String area, String line){

        Log.d("debugging", this.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getPath()+  "/"+area + ".csv");
        Log.d("debugging", line);
//        create_dir();

        try {
            File root = android.os.Environment.getExternalStorageDirectory();

            // See http://stackoverflow.com/questions/3551821/android-write-to-sd-card-folder

            File dir = new File (root.getAbsolutePath() + "/download");
            dir.mkdirs();
            File file = new File(dir, area+".csv");
            Log.d("debugging", "File path: " + file.getAbsolutePath());


            FileOutputStream f = new FileOutputStream(file,true);
            PrintWriter pw = new PrintWriter(f);
            pw.println(line);
            pw.flush();
            pw.close();
            f.close();
            Log.d("debugging", "Does file exist: " + file.exists());
            Log.d("debugging", "Does file exist: " + file.getTotalSpace());


        } catch (IOException e) {
            Log.d("debugging", e.toString());

            e.printStackTrace();
        }


    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    public void create_dir(){
        File file = new File(this.getExternalFilesDir(
                Environment.DIRECTORY_DOWNLOADS), "MeterReadings");
        if (!file.mkdirs()) {
            Log.e("debugging", "Directory not created");
        }
    }

    public void commit_reading(int reading, String meter_name, String date){


        JSONObject json = new JSONObject();
        try {
            json.put("meter-id",meter_name);
            json.put("reading", reading);
            json.put("date", date);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url =" http://192.168.41.110:5000/readings/add";

        // Request a string response from the provided URL.
        JsonObjectRequest jsonReuest = new JsonObjectRequest(Request.Method.POST, url, json, new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {
                        // Display the first 500 characters of the response string.
                        Log.d("debugging", "Response from flask: " + response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("debugging", "Error from flask: "+ error.getMessage());
            }
        });

        // Add the request to the RequestQueue.
        queue.add(jsonReuest);
    }



}
