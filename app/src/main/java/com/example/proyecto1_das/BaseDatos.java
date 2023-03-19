package com.example.proyecto1_das;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.sql.Array;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BaseDatos extends SQLiteOpenHelper {

    private static final String tablaUsuario = "create table usuario(id_email text unique primary key," +
            "password text, nombre text, apellido text, telefono text,fec_nac text )";

    private static final String tablaCita = "create table cita(email text ," +
            "tipo text, fecha_cita text ,primary key(email,fecha_cita),foreign key (email) references usuario (id_email))";
    public BaseDatos(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(tablaUsuario);
        db.execSQL(tablaCita);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS usuario");//eliminacion de la version anterior de la tabla usuarios
        db.execSQL(tablaUsuario); //Ejecucion del codigo para crear la tabla usuaios con su nueva estructura

        db.execSQL("DROP TABLE IF EXISTS cita");//eliminacion de la version anterior de la tabla cita
        db.execSQL(tablaCita); //Ejecucion del codigo para crear la tabla cita con su nueva estructura
    }
    public boolean agregarUsuario( String email,String password, String nombre, String apellido, String telefono, String nacimiento) {
        SQLiteDatabase miBdd = getWritableDatabase(); //llamando a la base de datos en el objeto mi Bdd
        if (miBdd != null) { //validando que la base de datos exista(q no sea nula)
            miBdd.execSQL("insert into usuario(id_email, password, nombre, apellido, telefono, fec_nac) " +
                    "values('"+ email + "','" + password + "','" + nombre+ "','" + apellido +"','" + telefono + "','" + nacimiento + "')");    //ejecutando la sentencia de insercion de SQL
            miBdd.close(); //cerrando la conexion a la base de datos.
            return true; // valor de retorno si se inserto exitosamente.
        }
        return false; //retorno cuando no existe la BDD
    }
 // consultar usuarios por email y password.
    public Cursor obtenerUsuarioPorEmailPassword(String email, String password) {
        SQLiteDatabase miBdd = getWritableDatabase(); // llamado a la base de datos
        //crear un cursor donde inserto la consulta sql y almaceno los resultados en el objeto usuario
        Cursor usuario = miBdd.rawQuery("select * from usuario where " +
                "id_email='" + email + "' and password = '" + password + "';", null);

        //validar si existe o no la consulta
        if (usuario.moveToFirst()) { //metodo movetofirst nueve al primer elemento encontrado si hay el usuario
            return usuario; //retornamos los datos encontrados
        } else {
            //no se encuentra informacion de usuaio -> no existe o porque el email y password son incorrectos
            return null; //devuelvo null si no hay
        }

    }
    //metodo para añadir una cita
    public boolean agregarCita(String email, String tipo, String fecha_cita) {
        SQLiteDatabase miBdd = getWritableDatabase(); //llamando a la base de datos en el objeto mi Ddd
        if (miBdd != null) { //validando que la base de datos exista(q no sea nula)
            boolean hay=buscarCita(fecha_cita);
            if (!hay) {
                miBdd.execSQL("insert into cita(email, tipo, fecha_cita) " +
                        "values('" + email + "','" + tipo + "','" + fecha_cita + "')");    //ejecutando la sentencia de insercion de SQL
                miBdd.close(); //cerrando la conexion a la base de datos.
                return true; // valor de retorno si se inserto exitosamente.
            }
        }
        return false; //retorno cuando no existe la BDD
    }
    //método para obtener las citas asociadas a un mail
    public ArrayList<Cita> obtenerCita(String email){
        SQLiteDatabase miBdd = getWritableDatabase(); // llamado a la base de datos
        ArrayList<Cita> citas = new ArrayList<>();

        //crear un cursor donde inserto la consulta sql y almaceno los resultados
        Cursor c= miBdd.rawQuery("select " +
                "tipo , fecha_cita " +
                "from cita  " +
                "where email =  '"+email+ "';", null);
        //validar si existe o no la consulta
        while (c.moveToNext()) {
            String tipo = c.getString(0);
            String fecha = c.getString(1);
            Cita cita = new Cita(email,tipo, fecha);
            citas.add(cita);
        }
        miBdd.close();
        return citas;
    }
    //metodo para eliminar una cita de la base de datos
    public void eliminarCita(String fecha, String mail) {
        SQLiteDatabase miBdd = getWritableDatabase(); // objeto para manejar la base de datos
        if (miBdd != null) { //validando que la bdd realmente exista
            miBdd.execSQL("DELETE FROM cita WHERE fecha_cita='" + fecha + "' AND email='" + mail + "'"); //ejecucion de la sentencia Sql para eliminar
            miBdd.close();

        }
    }
    //método para buscar una cita dad auna fecha
    public boolean buscarCita(String fecha){
            SQLiteDatabase miBdd = getWritableDatabase();
            boolean haycita=false;
             Cursor c=  miBdd.rawQuery("select * from cita where " +
                     "fecha_cita='" + fecha+ "';", null);
        //validar si existe o no la consulta
        if (c.moveToFirst()) { //metodo movetofirst nueve al primer elemento encontrado si hay
            haycita=true; //retornamos los datos encontrados
        } else {

            haycita=false;
        }

            return haycita;
    }



}
