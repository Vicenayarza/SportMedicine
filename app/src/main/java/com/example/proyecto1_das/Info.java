package com.example.proyecto1_das;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class Info extends AppCompatActivity {
    String nombreBdd, emailBdd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
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
    }
    public void cerrarPantalla(View vista){ //metodo para cerrary volver a la ventana de Menú.
        Intent intent=new Intent(getApplicationContext(),MenuInicio.class);
        intent.putExtra("emailBdd", emailBdd);
        intent.putExtra("nombreBdd", nombreBdd);

        startActivity(intent);
        finish(); //cerrando la activity

    }
    public void VerTitulos(View vista){ //metodo para acceder a la ventana donde se mostrará el RecyclerView+CardView sobre las titulaciones de la médico.
        Intent intent=new Intent(getApplicationContext(),Titulos.class);
        startActivity(intent);
        finish();

    }

}