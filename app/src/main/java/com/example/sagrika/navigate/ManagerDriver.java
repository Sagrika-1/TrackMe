package com.example.sagrika.navigate;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

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

/**
 * Created by Sagrika on 20/6/2016.
 */
public class ManagerDriver extends AsyncTask<String,Void,String> {
    String method, veh_id, manager_url, driver_id, userName, vehicle;
    int len;
    Context ctx;

    ManagerDriver(Context ctx) {
        this.ctx = ctx;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        manager_url = "http://sagrika.netau.net/json_driver.php";
        driver_id = "http://sagrika.netau.net/manager.php";
        method = params[0];
        if (method.equals("manager_id")) {
            String new_manager = params[1];
            String new_driver = params[2];
            try {
                URL url = new URL(driver_id);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String info = URLEncoder.encode("new_manager", "UTF-8") + "=" + URLEncoder.encode(new_manager, "UTF-8") + "&" +
                        URLEncoder.encode("new_driver", "UTF-8") + "=" + URLEncoder.encode(new_driver, "UTF-8");
                bufferedWriter.write(info);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();


                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String response_d = "";
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    response_d += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return response_d;


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return null;
    }


    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String response_d) {

        // Toast.makeText(ctx, response_d, Toast.LENGTH_SHORT).show();

        JSONObject jsonObject;
        JSONArray jsonArray;
        //String json_string = response_d;
            try{
                jsonObject = new JSONObject(response_d);
                jsonArray = jsonObject.getJSONArray("jsonstring");
                int count = 0;
                while(count<jsonArray.length()){
                    JSONObject JO = jsonArray.getJSONObject(count);
                    userName = JO.getString("userName");
                    //   vehicle = JO.getString("vehicle");
                    count++;

                }
                Toast.makeText(ctx, "Welcome " + userName , Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ctx, Options.class);
                ctx.startActivity(intent);


            } catch (JSONException e) {
                e.printStackTrace();
            }


            //  super.onPostExecute(response_d);
    }
}



