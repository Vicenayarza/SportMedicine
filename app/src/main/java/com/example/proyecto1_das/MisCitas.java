package com.example.proyecto1_das;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static com.example.proyecto1_das.R.layout.custom_spiner;

public class MisCitas extends AppCompatActivity {
    public TextView textViewTitle, textViewEmpty;
    public Spinner spinnerSelectDate;
    public Button buttonCancelDate;
    ArrayList<String> listaCitas;
    ArrayList<Cita> citasList;
    String emailBdd;
    String nombreBdd;
    String datee;
    BaseDatos bdd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_citas);
        bdd= new BaseDatos(this,"bd_usuarios",null,2);
        textViewTitle = findViewById(R.id.textViewTitle);
        textViewEmpty = findViewById(R.id.textViewEmpty);
        spinnerSelectDate = findViewById(R.id.spinnerSelectDate);
        buttonCancelDate = findViewById(R.id.buttonCancelDate);
        buttonCancelDate.setEnabled(false);
//Parametros recibidos de otra ventana
        Bundle parametrosExtra = getIntent().getExtras();
        if (parametrosExtra != null){
            try {
                //usamos las variables declaradas
                emailBdd = parametrosExtra.getString("emailBdd");
                nombreBdd = parametrosExtra.getString("nombreBdd");

            }catch (Exception ex){//ex recibe el tipo de error
                Toast.makeText(getApplicationContext(), "Error al procesar la solicitud "+ex.toString(),
                        Toast.LENGTH_LONG).show();
            }
        }
//Cargamos las citas que tiene un email en su base de datos, mostrando el botón de cacelarlas siempre que haya
        if (checkDates()) {
            fillDates();
            ArrayAdapter<String> adaptador = new ArrayAdapter<>(
                    this,
                    custom_spiner,
                    listaCitas
            );

            adaptador.setDropDownViewResource(R.layout.custom_spinner_dropdown);
            spinnerSelectDate.setAdapter(adaptador);

            spinnerSelectDate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long idl) {

                    if (position != 0) {
                        buttonCancelDate.setEnabled(true);
                        textViewEmpty.setVisibility(View.INVISIBLE);


                        buttonCancelDate.setVisibility(View.VISIBLE);

                        buttonCancelDate.setVisibility(View.VISIBLE);
                         datee=citasList.get(position -1).getFecha();

                    } else {
                        buttonCancelDate.setEnabled(false);

                        buttonCancelDate.setVisibility(View.INVISIBLE);

                        buttonCancelDate.setVisibility(View.INVISIBLE);

                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

        } else {
            textViewEmpty.setVisibility(View.VISIBLE);
        }
    }
    //Método para rellenar las citas
    private void fillDates() {
        textViewTitle.setVisibility(View.VISIBLE);
        spinnerSelectDate.setVisibility(View.VISIBLE);

        spinnerSelectDate = findViewById(R.id.spinnerSelectDate);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                custom_spiner,
                listaCitas
        );
        adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown);
        spinnerSelectDate.setAdapter(adapter);
    }
    //Comprobamos si hay citas en la base de datos asociadas a un email
    private boolean checkDates() {
        boolean b = false;
        try{
            citasList=bdd.obtenerCita(emailBdd);
            if(citasList!=null || citasList.isEmpty()){
                b=true;
            }
            getList(citasList);
        }
        catch (Exception e) {
            e.printStackTrace();

        }
        return b;
    }
    private void getList(ArrayList<Cita>cit) {
        listaCitas = new ArrayList<>();
        listaCitas.add("<Seleccione una fecha>");
        for(int i=0; i < cit.size(); i++){
            listaCitas.add(cit.get(i).getTipo() + " - " + cit.get(i).getFecha());
        }
    }


//Método para cancelar la cita del usuario
    public void eliminarUsuario(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);//Mostramos una alerta para ver si está seguro de cancelarla
        builder.setTitle(R.string.pregunta_cancelar);
        builder.setMessage(R.string.borrar_cita);
        builder.setNegativeButton(R.string.cancelar, (dialog, which) -> {

        });
        builder.setPositiveButton(R.string.confirmar, (dialog, which) -> {

            try {
                //eliminamos de la base de dtaos la cita asociada a un email
                bdd.eliminarCita(datee ,emailBdd);
                Toast.makeText(getApplicationContext(),"La cita fue cancelada", Toast.LENGTH_LONG).show();
                spinnerSelectDate.setSelection(0);
                Intent intent = getIntent();
                intent.putExtra("emailBdd", emailBdd);
                intent.putExtra("nombreBdd", nombreBdd);
                startActivity(intent);
                bdd.close();
                finish();
            } catch (Exception e) {
                Toast.makeText(this, e + "", Toast.LENGTH_SHORT).show();
            }
        });
        builder.show();
    }
    public void cerrarPantalla(View vista){ //metodo para cerrar
        Intent intent=new Intent(getApplicationContext(),MenuInicio.class);
        intent.putExtra("emailBdd", emailBdd);
        intent.putExtra("nombreBdd", nombreBdd);
        startActivity(intent); //solicitamos que habra el menu
        finish(); //cerrando la activity

    }
}