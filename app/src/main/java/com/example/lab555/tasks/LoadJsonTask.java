package com.example.lab555.tasks;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoadJsonTask extends AsyncTask<String,String,String> {

    private MyJsonResponseListener mListener;

    public LoadJsonTask(MyJsonResponseListener jsonListener){
        this.mListener = jsonListener;
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            StringBuilder response = new StringBuilder();
            //pobieramy url z którego chcemy pobrać nasze dane
            URL u = new URL(params[0]);
            HttpURLConnection conn = (HttpURLConnection) u.openConnection();
            //sprawdzamy czy kod odpowiedzi http sie zgadza
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                //inicjalizujemy buffor danych, w naszym przypadku wiemy że może być mały
                BufferedReader input = new BufferedReader(new InputStreamReader(conn.getInputStream()), 1024);
                String line = null;
                while ((line = input.readLine()) != null) {
                    response.append(line);
                }
                input.close();
            }
            return response.toString();
        }catch (Exception e){
            Log.e("ab04", e.getMessage());
            return "Error"; //zwracamy błąd w przypadku problemów
        }

    }
    @Override
    protected void onPostExecute(String s) {
        mListener.onJsonResponseChange(s);

        try {
            JSONObject json = new JSONObject(s);
            String response = json.getString("response");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
