package com.example.proyecto1_das;

import static android.Manifest.permission.POST_NOTIFICATIONS;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;

import android.content.Intent;


import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

import static java.lang.String.format;
import static com.example.proyecto1_das.R.layout.custom_spiner;

public class PedirCitas extends AppCompatActivity {
    final int STARTHOUR = 9;//Hora que abre el servicio
    final String[] SESSION_TYPE =  {"Consulta", "Tratamiento", "Reconocimiento Básico", "Prueba de Esfuerzo"};// tipos de sesiones que ofrece el servicio
    public Spinner spinnerType, spinnerDate, spinnerTime; //desplegables para elegir cita, hora y fecha
    public TextView textViewType, textViewDate, textViewTime;
    ArrayList<String> spinnerArray;
    BaseDatos bdd;
    String emailBdd;
    String nombreBdd;

    private final static String CHANNEL_ID = "NOTIFICACION";// canal para mostrar la notificación
    private final static int NOTIFICACION_ID = 0;// id de la notificacion


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedir_citas);
        textViewType = findViewById(R.id.textViewType);
        textViewDate = findViewById(R.id.textViewDate);
        textViewTime = findViewById(R.id.textViewTime);

        bdd= new BaseDatos(this,"bd_usuarios",null,2);
        Bundle parametrosExtra = getIntent().getExtras();
        //recibimos los parametros
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
        // Colocar tipo de sesión en el spinner
        fillTypes();
        // Colocar fechas en spinner
        fillDates();
        // Colocar horas en spinner
        fillTime();



    }
//Boton para pedir una cita
    public void buttonSave(View view) {
        if (spinnerValidations()) { //Validar los datos metidos en los spinners
            AlertDialog.Builder builder = new AlertDialog.Builder(this);//si todos ok sacamos una alerta que nos vuelta a indicar si queremos confirmar ña cita
            builder.setTitle(R.string.confirmar);
            builder.setMessage(getDialogText());
            builder.setPositiveButton("Guardar", (dialog, which) -> {
                // Hacer cosas aqui al hacer clic en el boton de aceptar
                try {
                    boolean resultado = insertDate();
                    if (resultado) {
                        Intent intent = new Intent(this, MenuInicio.class); //volvemos al menu inicio
                        intent.putExtra("emailBdd", emailBdd);
                        intent.putExtra("nombreBdd", nombreBdd);
                        startActivity(intent);
                        Toast.makeText(this, getResources().getString(R.string.citaOk), Toast.LENGTH_SHORT).show();// lanzamos mensaje de confirmación
                        createNotificationChannel(); //creamos el canal de la notificacion
                        createNotification();//lanzamos la notificación
                        finish();


                    } else {
                        Toast.makeText(this,getResources().getString(R.string.citaocupada) , Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(this, e + "", Toast.LENGTH_LONG).show();
                }
            });
            builder.setNegativeButton(getResources().getString(R.string.atras), (dialog, which) -> {

            });
            builder.show();
        }
    }
//Agregar la cita a la base de datos
    private boolean insertDate() {

        String type = spinnerType.getSelectedItem().toString();
        String date = spinnerDate.getSelectedItem().toString() + " " + spinnerTime.getSelectedItem().toString();

        boolean id =  bdd.agregarCita(emailBdd, type, date);
        bdd.close();
        finish();

        return id;
    }
// pasar a texto los datos del spinner
    private String getDialogText() {
        String type = spinnerType.getSelectedItem().toString().trim();
        String date = spinnerDate.getSelectedItem().toString().trim();
        String time = spinnerTime.getSelectedItem().toString().trim();

        return "Tipo: " + type + "\n" +
                "Fecha: " + date + "\n" +
                "Hora: " + time;
    }
//validaciones de los spinners
    private boolean spinnerValidations() {

        if (spinnerType.getSelectedItem().toString().trim().equals("<Selecciona un tipo de sesión>")) {
            textViewType.setVisibility(View.VISIBLE);
        } else {
            textViewType.setVisibility(View.GONE);
        }

        if (spinnerDate.getSelectedItem().toString().trim().equals("<Selecciona una fecha>")) {
            textViewDate.setVisibility(View.VISIBLE);
        } else {
            textViewDate.setVisibility(View.GONE);
        }

        if (spinnerTime.getSelectedItem().toString().trim().equals("<Selecciona una hora>")) {
            textViewTime.setVisibility(View.VISIBLE);
        } else {
            textViewTime.setVisibility(View.GONE);
        }

        return textViewType.getVisibility() != View.VISIBLE && textViewDate.getVisibility() != View.VISIBLE && textViewTime.getVisibility() != View.VISIBLE;
    }

    // Métodos privados
    // Método que llena los tipos de sesión
    private void fillTypes() {
        spinnerArray = new ArrayList<>();

        spinnerArray.add("<Selecciona un tipo de sesión>");

        Collections.addAll(spinnerArray, SESSION_TYPE);

        spinnerType = findViewById(R.id.spinnerType);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                custom_spiner,
                spinnerArray
        );
        adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown);
        spinnerType.setAdapter(adapter);


    }

    // Método que llena las fechas

    private void fillDates() {
        Calendar calendar = Calendar.getInstance();
        spinnerArray = new ArrayList<>();

        spinnerArray.add("<Selecciona una fecha>");

        int monthNumber = calendar.get(Calendar.MONTH);

        int i = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.add(Calendar.DATE, 1);

        while (i < calendar.getActualMaximum(Calendar.DAY_OF_MONTH)) {
            String date = twoDigits(calendar.get(Calendar.DAY_OF_MONTH)) + "/" + twoDigits(monthNumber + 1) + "/" + calendar.get(Calendar.YEAR);
            calendar.add(Calendar.DATE, 1);
            spinnerArray.add(date);
            i++;
        }

        spinnerDate = findViewById(R.id.spinnerDate);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                custom_spiner,
                spinnerArray
        );
        adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown);
        spinnerDate.setAdapter(adapter);


    }


//metodo que llena las horas
    private void fillTime() {
        spinnerArray = new ArrayList<>();

        spinnerArray.add("<Selecciona una hora>");

        for (int i = STARTHOUR; i <= 20; i++) {
            String hour = format("%02d", i) + ":00 hrs";
            spinnerArray.add(hour);
        }

        spinnerTime = findViewById(R.id.spinnerTime);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                custom_spiner,
                spinnerArray
        );
        adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown);
        spinnerTime.setAdapter(adapter);


    }

    private String twoDigits(int n) {
        return (n<=9) ? ("0"+n) : String.valueOf(n);
    }
    //Método para crear el canal de notificación que solamente se ejecutará si la version es mayor que la Oreo
    private void createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "Noticacion";
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }
    //Método para crear la Notificación
    private void createNotification(){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID);
        builder.setSmallIcon(R.drawable.ic_sms_black_24dp);
        builder.setContentTitle(getResources().getString(R.string.notificacion));
        builder.setContentText(getResources().getString(R.string.citaOk));
        builder.setColor(Color.BLUE);
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        builder.setLights(Color.MAGENTA, 1000, 1000);
        builder.setVibrate(new long[]{1000,1000,1000,1000,1000});
        builder.setDefaults(Notification.DEFAULT_SOUND);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
        if (ContextCompat.checkSelfPermission(this, POST_NOTIFICATIONS)!=PackageManager.PERMISSION_GRANTED) {
//PEDIR EL PERMISO
            ActivityCompat.requestPermissions(this, new String[]{POST_NOTIFICATIONS}, 11);
        }
        notificationManagerCompat.notify(NOTIFICACION_ID, builder.build());
    }
    public void cerrarPantalla(View vista){ //metodo para cerrar
        Intent intent=new Intent(getApplicationContext(),MenuInicio.class);
        intent.putExtra("emailBdd", emailBdd);
        intent.putExtra("nombreBdd", nombreBdd);
        startActivity(intent); //solicitamos que habra el menu
        finish(); //cerrando la activity

    }
}
