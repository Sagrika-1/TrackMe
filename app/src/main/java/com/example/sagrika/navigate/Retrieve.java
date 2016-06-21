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
public class Retrieve extends AsyncTask<String, Void, String> {


    String method, name, username = null, password;
    int len;
    Context ctx;

    Retrieve(Context ctx) {

        this.ctx = ctx;

    }

    String JSON_STRING;
    String json_url;


    @Override
    protected void onPreExecute() {
        // alertDialog = new AlertDialog.Builder(ctx).create();
        // alertDialog.setTitle("Login information");
        // super.onPreExecute();

    }

    @Override
    protected String doInBackground(String... params) {
        json_url = "http://sagrika.netau.net/json_get.php";
        String reg_url = "http://sagrika.netau.net/logindata.php";
        String login_url = "http://sagrika.netau.net/login.php";
        method = params[0];
         if (method.equals("login")) {
            String login_name = params[1];
            String login_pass = params[2];

            try {
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data = URLEncoder.encode("login_name", "UTF-8") + "=" + URLEncoder.encode(login_name, "UTF-8") + "&" +
                        URLEncoder.encode("login_pass", "UTF-8") + "=" + URLEncoder.encode(login_pass, "UTF-8");
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

        }

        return null;
    }


    @Override
    protected void onProgressUpdate(Void... values) {

        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String response) {

        //  Toast.makeText(ctx,response, Toast.LENGTH_SHORT).show();
      JSONObject jsonObject;
        JSONArray jsonArray;
        // InfoAdapter infoAdapter = null;
        String json_string = response;
        try {
            jsonObject = new JSONObject(json_string);
            jsonArray = jsonObject.getJSONArray("jsonstring");
            int count = 0;

            while (count < jsonArray.length()) {
                JSONObject JO = jsonArray.getJSONObject(count);
                //  name = JO.getString("name");
                username = JO.getString("username");
                len = username.length();
                if (len == 0) {
                    Toast.makeText(ctx, "Invalid Username or Password", Toast.LENGTH_SHORT).show();
                }

                //    Info info = new Info(name,username);
                // infoAdapter.add(info);

                else {
                    count++;
                    Toast.makeText(ctx, "Welcome " + username, Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(ctx, Verify.class);
                    intent.putExtra("id", username);
                    ctx.startActivity(intent);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
