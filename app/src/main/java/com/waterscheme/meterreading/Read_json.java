package com.waterscheme.meterreading;


import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

public class Read_json {

    private Context cntx;

    public Read_json(Context cntx){
        this.cntx = cntx;
    }

    public JSONObject read_json_from_inputstream(InputStream in) throws JSONException {
        String everything = "";
        try (BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            everything = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new JSONObject(everything);
    }

    public JSONObject get_json(){
        JSONObject all = null;
        try {
            all = read_json_from_inputstream(this.cntx.getAssets().open("meters.json"));
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
        return all;

    }
    public List<String> get_area_list(){
        JSONObject all = get_json();
        final List<String> area_list = new ArrayList<String>();
        try {
            Log.d("debugging", Arrays.toString(this.cntx.getAssets().list("")));

            Iterator areas = ((JSONObject) all.get("area")).keys();

            while (areas.hasNext()) {
                String key = (String) areas.next();
                area_list.add(key);

            }
            Log.d("debugging", "File read successfully");

        } catch (IOException | JSONException e) {
            Log.d("debugging", "Problem reading file " + e.toString());

            e.printStackTrace();
        }
        return area_list;

    }

    public List<String> get_meters_list(String area){
        JSONObject all = get_json();
        final List<String> meter_list = new ArrayList<String>();

        try {
            Iterator meter_names = all.getJSONObject("area").getJSONObject(area).keys();
            while (meter_names.hasNext()) {
                String key = (String) meter_names.next();
                meter_list.add(key);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return meter_list;

    }
}

