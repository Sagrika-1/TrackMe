package com.example.sagrika.navigate;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
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

//AsyncTask to verify the credentials of the manager entered in Manager Login Page
// and redirect to TrackPage if correct credentials are entered
public class Retrieve extends AsyncTask<String, Void, String> {

   String login_name, method,login_pass;
    Context ctx;

    Retrieve(Context ctx) {

        this.ctx = ctx;

    }

    String json_url;

    @Override
    protected String doInBackground(String... params)
    {
        String login_url = "http://192.168.0.109:80/TrackMe/login.php";
        method = params[0];
         if (method.equals("login"))
         {
            login_name = params[1];
             login_pass = params[2];

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
    protected void onPostExecute(String response)
    {
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(response);
            String json =  jsonObject.getString("jsonstring");
            switch (json)
            {
                case "p":       //If the password entered is wrong this case is executed
                    Toast.makeText(ctx,"Enter correct Password",Toast.LENGTH_SHORT).show();
                    break;
                case "us":      //If username entered is wronf this case is executed
                    Toast.makeText(ctx, " Username doesn't exist", Toast.LENGTH_SHORT).show();
                    break;
                case "go":      //If correct credentials are entered SpinnerJSON is executed to redirect to TrackPage
                    SpinnerJSON spinnerJSON = new SpinnerJSON(ctx);
                    spinnerJSON.execute(login_name,login_pass);
                    break;
                default:
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
