package com.example.tienda_oline_couchdb;

import android.os.AsyncTask;
import android.os.Build;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class obtenerDatosServidor extends AsyncTask<String, String, String> {
    HttpURLConnection httpURLConnection;
    private utilidades utilidades;

    @Override
    protected String doInBackground(String... voids) {
        StringBuilder result = new StringBuilder();
        try{
            utilidades utilidades1 = utilidades;
            URL url = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                url = new URL(utilidades.urlConsulta);
            }
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                httpURLConnection.setRequestProperty("Authorization", "Basic "+ utilidades.credencialesCodificadas);
            }

            InputStream inputStream = new BufferedInputStream(httpURLConnection.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            String linea;
            while((linea=bufferedReader.readLine())!=null){
                result.append(linea);
            }
        }catch (Exception e){
            return e.getMessage();
        }finally {
            httpURLConnection.disconnect();
        }
        return result.toString();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}