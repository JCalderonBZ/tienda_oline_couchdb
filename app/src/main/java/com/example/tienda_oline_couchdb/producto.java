package com.example.tienda_oline_couchdb;

public class producto {
    String _id;
    String _rev;
    String idproducto;
    String nombre;
    String direccion;
    String telefono;
    String email;
    String dui;
    String foto;
    public producto(String _id, String _rev, String idproducto, String nombre, String direccion, String telefono, String email, String dui, String foto) {
        this._id = _id;
        this._rev = _rev;
        this.idproducto = idproducto;
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.email = email;
        this.dui = dui;
        this.foto = foto;
    }
    public String get_id() {
        return _id;
    }
    public void set_id(String _id) {
        this._id = _id;
    }
    public String get_rev() {
        return _rev;
    }
    public void set_rev(String _rev) {
        this._rev = _rev;
    }
    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getIdAmigo() {
        return idproducto;
    }

    public void setIdAmigo(String idAmigo) {
        this.idproducto = idAmigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDui() {
        return dui;
    }

    public void setDui(String dui) {
        this.dui = dui;
    }
}