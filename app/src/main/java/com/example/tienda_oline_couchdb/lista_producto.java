package com.example.tienda_oline_couchdb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class lista_producto extends AppCompatActivity {
    Bundle parametros = new Bundle();
    FloatingActionButton btn;
    ListView lts;
    Cursor cAmigos;
    DB dbAmigos;
    producto misAmigos;
    final ArrayList<producto> alAmigos=new ArrayList<producto>();
    final ArrayList<producto> alAmigosCopy=new ArrayList<producto>();
    JSONArray datosJSON;
    JSONObject jsonObject;
    obtenerDatosServidor datosServidor;
    detectarInternet di;
    int posicion=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_producto);

        dbAmigos = new DB(lista_producto.this, "", null, 1);

        btn = findViewById(R.id.btnAbrirNuevosproducto);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parametros.putString("accion", "nuevo");
                abrirActividad(parametros);
            }
        });
        try{
            di = new detectarInternet(getApplicationContext());
            if(di.hayConexionInternet()){
                obtenerDatosAmigosServidor();
            }else{
                obtenerAmigos();//offline
            }
        }catch (Exception e){
            mostrarMsg("Error al detectar si hay conexion "+ e.getMessage());
        }
        buscarAmigos();
    }
    private void obtenerDatosAmigosServidor(){
        try{
            datosServidor = new obtenerDatosServidor();
            String data = datosServidor.execute().get();
            jsonObject = new JSONObject(data);
            datosJSON = jsonObject.getJSONArray("rows");
            mostrarDatosAmigos();
        }catch (Exception e){
            mostrarMsg("Error al obtener datos desde el servidor: "+ e.getMessage());
        }
    }
    private void mostrarDatosAmigos(){
        try{
            if( datosJSON.length()>0 ){
                lts = findViewById(R.id.ltsAmigos);

                alAmigos.clear();
                alAmigosCopy.clear();

                JSONObject misDatosJSONObject;
                for (int i=0; i<datosJSON.length(); i++){
                    misDatosJSONObject = datosJSON.getJSONObject(i).getJSONObject("value");
                    misAmigos = new producto(
                            misDatosJSONObject.getString("_id"),
                            misDatosJSONObject.getString("_rev"),
                            misDatosJSONObject.getString("idAmigo"),
                            misDatosJSONObject.getString("nombre"),
                            misDatosJSONObject.getString("direccion"),
                            misDatosJSONObject.getString("telefono"),
                            misDatosJSONObject.getString("email"),
                            misDatosJSONObject.getString("dui"),
                            misDatosJSONObject.getString("urlCompletaFoto")
                    );
                    alAmigos.add(misAmigos);
                }
                adaptadorImagenes adImagenes = new adaptadorImagenes(getApplicationContext(), alAmigos);
                lts.setAdapter(adImagenes);
                alAmigosCopy.addAll(alAmigos);

                registerForContextMenu(lts);
            }else{
                mostrarMsg("No hay datos que mostrar.");
            }
        }catch (Exception e){
            mostrarMsg("Error al mostrar los datos: "+e.getMessage());
        }
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mimenu, menu);
        try {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            posicion = info.position;
            menu.setHeaderTitle(datosJSON.getJSONObject(posicion).getJSONObject("value").getString("nombre"));
        }catch (Exception e){
            mostrarMsg("Error al mostrar el menu: "+ e.getMessage());
        }
    }
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        try {
            if(item.getItemId()==R.id.mnxAgregar) {
                parametros.putString("accion", "nuevo");
                abrirActividad(parametros);
            }
                if (item.getItemId()==R.id.mnxModificar) {
                    parametros.putString("accion","modificar");
                parametros.putString("amigos", datosJSON.getJSONObject(posicion).toString());
            }
                if (item.getItemId()==R.id.mnxEliminar){
                    eliminarAmigos();
                }


            return true;
        }catch (Exception e){
            mostrarMsg("Error en menu: "+ e.getMessage());
            return super.onContextItemSelected(item);
        }
    }
    private void eliminarAmigos(){
        try {
            AlertDialog.Builder confirmacion = new AlertDialog.Builder(lista_producto.this);
            confirmacion.setTitle("Esta seguro de Eliminar a: ");
            confirmacion.setMessage(datosJSON.getJSONObject(posicion).getJSONObject("value").getString("nombre"));
            confirmacion.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    try {
                        String respuesta = dbAmigos.administrar_amigos("eliminar", new String[]{"", "", datosJSON.getJSONObject(posicion).getJSONObject("value").getString("idProducto")});
                        for (String s : new String[]{"", "", datosJSON.getJSONObject(posicion).getJSONObject("value").getString("idAmigo")}) {

                        }

                        if (respuesta.equals("ok")) {
                            mostrarMsg("Amigo eliminado con exito.");
                            obtenerAmigos();
                        } else {
                            mostrarMsg("Error al eliminar amigo: " + respuesta);
                        }
                    } catch (Exception e) {
                        mostrarMsg("Error al eliminar Datos: " + e.getMessage());
                    }
                }
            }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            confirmacion.create().show();
        }catch (Exception e){
            mostrarMsg("Error al eliminar: "+ e.getMessage());
        }
    }
    private void abrirActividad(Bundle parametros){
        Intent abriVentana = new Intent(getApplicationContext(), MainActivity.class);
        abriVentana.putExtras(parametros);
        startActivity(abriVentana);
    }
    private void obtenerAmigos(){
        try{
            cAmigos = dbAmigos.consultar_producto();
            if ( cAmigos.moveToFirst() ){
                datosJSON = new JSONArray();
                do{
                    jsonObject = new JSONObject();
                    JSONObject jsonObjectValue = new JSONObject();
                    jsonObject.put("_id",cAmigos.getString(0));
                    jsonObject.put("_rev",cAmigos.getString(1));
                    jsonObject.put("idAmigo",cAmigos.getString(2));
                    jsonObject.put("nombre",cAmigos.getString(3));
                    jsonObject.put("direccion",cAmigos.getString(4));
                    jsonObject.put("telefono",cAmigos.getString(5));
                    jsonObject.put("email",cAmigos.getString(6));
                    jsonObject.put("dui",cAmigos.getString(7));
                    jsonObject.put("urlfotoCompleta",cAmigos.getString(8));

                    jsonObjectValue.put("value", jsonObject);
                    datosJSON.put(jsonObjectValue);
                }while(cAmigos.moveToNext());
                mostrarDatosAmigos();
            }else{
                mostrarMsg("No hay amigos que mostrar");
            }
        }catch (Exception e){
            mostrarMsg("Error al obtener amigos: "+ e.getMessage());
        }
    }
    private void mostrarMsg(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }
    private void buscarAmigos(){
        TextView tempVal;
        tempVal = findViewById(R.id.txtBuscarproducto);
        tempVal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    alAmigos.clear();
                    String valor = tempVal.getText().toString().trim().toLowerCase();
                    if( valor.length()<=0 ){
                        alAmigos.addAll(alAmigosCopy);
                    }else{
                        for( producto amigos : alAmigosCopy ){
                            String nombre = amigos.getNombre();
                            String direccion = amigos.getDireccion();
                            String tel = amigos.getTelefono();
                            String email = amigos.getEmail();
                            if( nombre.toLowerCase().trim().contains(valor) ||
                                    direccion.toLowerCase().trim().contains(valor) ||
                                    tel.trim().contains(valor) ||
                                    email.trim().toLowerCase().contains(valor) ){
                                alAmigos.add(amigos);
                            }
                        }
                        adaptadorImagenes adImagenes = new adaptadorImagenes(getApplicationContext(), alAmigos);
                        lts.setAdapter(adImagenes);
                    }
                }catch (Exception e){
                    mostrarMsg("Error al buscar: "+e.getMessage() );
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}
