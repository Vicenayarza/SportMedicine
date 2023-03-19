package com.example.proyecto1_das;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MenuInicio extends AppCompatActivity {
    public TextView textViewNombre;
    String nombreBdd, emailBdd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_inicio);
        //Recepcion de parametros
        Bundle parametrosExtra = getIntent().getExtras();
        if (parametrosExtra != null){
            try {
                //usamos las variables declaradas
                nombreBdd = parametrosExtra.getString("nombreBdd");
                emailBdd = parametrosExtra.getString("emailBdd");

            }catch (Exception ex){//ex recibe el tipo de error
                Toast.makeText(getApplicationContext(), "Error al procesar la solicitud "+ex.toString(),
                        Toast.LENGTH_LONG).show();
            }
        }

        textViewNombre = findViewById(R.id.textViewNombre);

        textViewNombre.setText(getResources().getString(R.string.saludo) +""+ nombreBdd);


    }
    //Dirigirnos a la ventana de citar
    public void buttonCitar(View view) {
        Intent intent = new Intent(this, PedirCitas.class);
        intent.putExtra("emailBdd", emailBdd);
        intent.putExtra("nombreBdd", nombreBdd);
        startActivity(intent);
        finish();
    }
    //Dirigirnos a la ventana de ver mis citas, para poder cancelarlas
    public void buttonCancelar(View view) {
        Intent intent = new Intent(this, MisCitas.class);
        intent.putExtra("emailBdd", emailBdd);
        intent.putExtra("nombreBdd", nombreBdd);
        startActivity(intent);
        finish();

    }
    // Método que dirige a salir de la sesion
    public void buttonSalir(View view) {
        SharedPreferences sharedPref = this.getSharedPreferences("correo_electronico", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();
        editor.apply();

        Intent intent = new Intent(this, MainActivity.class);

        startActivity(intent);
        finish();

    }
    // Método que dirige a la información del Servicio de Medicina
    public void buttonConocenos(View view) {
        Intent intent = new Intent(this, Info.class) ;
        intent.putExtra("emailBdd", emailBdd);
        intent.putExtra("nombreBdd", nombreBdd);
        startActivity(intent);
        finish();
    }


}