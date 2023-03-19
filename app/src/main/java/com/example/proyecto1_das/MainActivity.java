package com.example.proyecto1_das;

import static android.Manifest.permission.POST_NOTIFICATIONS;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    public EditText et_email, et_password;

    BaseDatos bdd;//creo objeto tipo bdd



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, POST_NOTIFICATIONS) !=
                    PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new
                        String[]{POST_NOTIFICATIONS}, 11);
            }
        }

        et_email = (EditText) findViewById(R.id.et_email);
        et_password = (EditText) findViewById(R.id.et_password);
        bdd = new BaseDatos(getApplicationContext(), "bd_usuarios", null, 2);

    }
    public void cambiarIdioma(View view) {//Metodo para cambiar idioma
        boolean isChecked = ((Switch) view).isChecked();//si esta seleccionado estaremos en castellano, sino en inglés
        if (isChecked) {
            setLocale("es");
        } else {
            setLocale("en");
        }

    }
    private void setLocale(String languageCode) {//metodo para cambiar la configuración del idioma en funcion de los ficheros srings.xml
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        recreate();
    }

    public void buttonLogin(View view) {

        String email, password;
        email = et_email.getText().toString();
        password = et_password.getText().toString();
        Cursor usuarioEncontrado = bdd.obtenerUsuarioPorEmailPassword(email,password);
        if(usuarioEncontrado != null){
            //Para el caso de que el email y contraseña ingresados sean correctos
            //Obteniendo el valor del email almacenadoe en la BDD
            String emailBdd = usuarioEncontrado.getString(0).toString();
            //Obteniendo el valor del email almacenadoe en la BDD
            String nombreBdd=usuarioEncontrado.getString(2).toString();

            //mostramos la bienvenida
            Toast.makeText(getApplicationContext(), "" +nombreBdd, Toast.LENGTH_LONG).show();


            //creando un objeto para manejar la ventana del menu
            Intent intent = new Intent(getApplicationContext(),MenuInicio.class);
            intent.putExtra("emailBdd", emailBdd); //pasando el mail como parametro
            intent.putExtra("nombreBdd", nombreBdd);//pasando el nombre como parametro
            //abrir el activity del menu de opciones
            startActivity(intent);
            finish();

        }else{
            //para el caso de que el usuarioEncontrado sea nulo se muestra un mensaje informativo al usuario

            et_password.setText(""); //limpiamos el campo de la contraseña
            //Lanzamos una alerta que nos de la posibilidad de registrarnos o volver a intentarlo
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.no_cuenta);
            builder.setMessage(email + getResources().getString(R.string.no_cuenta_mensaje));
            builder.setNegativeButton(R.string.reintentar, (dialog, which) -> {

            });
            builder.setPositiveButton(R.string.registro, (dialog, which) -> {
                // Si pulsas registrar te lleva a la ventana de registro
                Intent intent = new Intent(this, Registro.class);
                startActivity(intent);
            });
            builder.show();
        }
    }
    //Metodo para el boton de Registro, nos conduce al formulario
    public void buttonRegistro(View view) {
        Intent intent = new Intent(this, Registro.class);
        startActivity(intent);
    }

}
