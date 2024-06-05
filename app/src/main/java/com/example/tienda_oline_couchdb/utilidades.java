package com.example.tienda_oline_couchdb;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.Base64;

@RequiresApi(api = Build.VERSION_CODES.O)
public class utilidades {
    static String urlConsulta = "http://192.168.80.84:5984/alfredo/_design/kimberly/_view/kimberly";
    static String urlMto = "http://192.168.80.84:5984/alfredo/";
    static String user = "admin";
    static String passwd = "estudiante";
    static String credencialesCodificadas = Base64.getEncoder().encodeToString((user +":"+ passwd).getBytes());
    public String generarIdUnico(){
        return java.util.UUID.randomUUID().toString();
    }
}
