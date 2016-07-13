package com.example.sagrika.navigate;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class SpinnerJSON extends AsyncTask<String, Void, String>
{

    String username,password;
    ArrayList<String> vehicleList = new ArrayList<String>();
    Context ctx;

    SpinnerJSON(Context ctx)
    {
        this.ctx = ctx;
    }

    @Override
    protected String doInBackground(String... params) {
        String get_url = "http://192.168.0.109:80/TrackMe/get_id.php";
        username = params[0];
        password = params[1];


        try {
            URL url = new URL(get_url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);

            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String data = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8");
            bufferedWriter.write(data);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();


            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
            String response = "";
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                response += line;
            }
            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();
            return response;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String args) {
        try {
            JSONObject object = new JSONObject(args);
            JSONArray arr = object.getJSONArray("server_response");
            vehicleList.add("Select a vehicle");
            for (int i = 0; i < arr.length(); i++) {
                String o = arr.getString(i);
                // Populate spinner with IDs
                vehicleList.add(o);
                Log.e("vehicle", vehicleList.get(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Intent i = new Intent(ctx, TrackFleet.class);
        i.putStringArrayListExtra("vehicleList",vehicleList);
        i.putExtra("username",username);
        i.putExtra("password",password);
        ctx.startActivity(i);
    }
}