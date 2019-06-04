package com.waterscheme.meterreading;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;


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
        tv.setText(meter_name);

        submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                save_reading();
            }
        });

    }


    public void extract_bundle(){
        Bundle b = getIntent().getExtras();
        if(b != null) {
            area = "Abbey";//b.getString("area");
            owner = b.getString("owner");
            meter_name = b.getString("meter");
        }

    }

    /**
     * Called when the submit button is pressed.
     * Attempts to write the meter reading to a file.
     */
    public void save_reading(){

        if (isExternalStorageWritable()) {
            // block of code to be executed if the condition is true
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            String date = sdf.format(new Date());
            String meter_reading = reading.getText().toString();

            String meter_reading_entry = meter_name + ","+ meter_name+","+ meter_reading+ ","+ date;
            write_to_file(area, meter_reading_entry);
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




}
