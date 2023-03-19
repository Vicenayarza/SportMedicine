package com.example.proyecto1_das;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.util.Patterns;


import java.util.regex.Pattern;

public class Registro extends AppCompatActivity {

    public EditText et_nombre,et_apellido, et_nacimiento, et_email, et_contraseña, et_contraseña2, et_telefono;
    BaseDatos bdd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        et_nombre = findViewById(R.id.et_nom);
        et_apellido = findViewById(R.id.et_apellido);
        et_email = findViewById(R.id.et_email);
        et_nacimiento = findViewById(R.id.et_nacimiento);
        et_contraseña = findViewById(R.id.et_contraseña);
        et_contraseña2 = findViewById(R.id.et_contraseña2);
        et_telefono = findViewById(R.id.et_telefono);
        bdd = new BaseDatos(getApplicationContext(), "bd_usuarios", null, 2);

    }
    public void buttonSignUp(View view){
        String nombres=et_nombre.getText().toString();
        String apellidos=et_apellido.getText().toString();
        String email=et_email.getText().toString();
        String telefono=et_telefono.getText().toString();
        String contra=et_contraseña.getText().toString();
        String contra2=et_contraseña2.getText().toString();
        String fecha_nacimiento=et_nacimiento.getText().toString();
        //Comprobamos si los campos están bien rellenados
        if (nombres.isEmpty() ||apellidos.isEmpty() ||telefono.isEmpty() || email.isEmpty()  ||contra.isEmpty() ||contra2.isEmpty() ||fecha_nacimiento.isEmpty()){ //si algun campo esta vacio
            Toast.makeText(getApplicationContext(), "Para continuar con el registro llene todos los campos solicitados",
                    Toast.LENGTH_LONG).show(); //mostrando mensaje de campo vacio a traves de un toast
        } else {

                if (NoNumeros(nombres) == false) {
                    Toast.makeText(getApplicationContext(), "El nombre no debe contener numeros",
                            Toast.LENGTH_LONG).show(); //mostrando error de nombre
                } else {
                    if (NoNumeros(apellidos) == false) {
                        Toast.makeText(getApplicationContext(), "El apellido no debe contener numeros",
                                Toast.LENGTH_LONG).show(); //mostrando error de apellido
                    } else {
                        if (validartelefono(telefono) == false) {
                            Toast.makeText(getApplicationContext(), "El numero de télefono debe tener 9 digitos, empezar con 6 o 7 y no tener letras ",
                                    Toast.LENGTH_LONG).show(); //mostrando error de número de telefono
                        } else {
                            Pattern pattern = Patterns.EMAIL_ADDRESS;
                            if (pattern.matcher(email).matches() == false) {
                                Toast.makeText(getApplicationContext(), "Ingrese un Email Valido",
                                        Toast.LENGTH_LONG).show(); //mostrando correo invalido
                            } else {
                                if (contra.length() < 8) {
                                    Toast.makeText(getApplicationContext(), "Ingrese una contraseña mínimo de 8 dígitos",
                                            Toast.LENGTH_LONG).show(); //mostrando mensaje de contraseña invalida
                                } else {
                                    if (validarcontra(contra) == false) {
                                        Toast.makeText(getApplicationContext(), "la contraseña debe tener numeros y letras",
                                                Toast.LENGTH_LONG).show(); //mostrando mensaje de contraseña invalida
                                    } else {
                                        if (contra.equals(contra2)) {
                                            bdd.agregarUsuario(email, contra, nombres, apellidos, telefono, fecha_nacimiento);
                                            Toast.makeText(getApplicationContext(), "Usuario registrado correctamente",
                                                    Toast.LENGTH_LONG).show(); //mostrando mensaje de usuario registrado
                                            finish();
                                            Intent intent = new Intent(this, MainActivity.class);
                                            startActivity(intent);
                                        } else {
                                            Toast.makeText(getApplicationContext(), "Las contraseñas ingresadas no coinciden",
                                                    Toast.LENGTH_LONG).show(); //mostrando un mensaje de error al no poder iniciar
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
//Utilizamos un fragment para la eleccion de la fecha de nacimiento
    public void showDatePickerDialog(View v) {
        DatePickerFragment newFragment = DatePickerFragment.newInstance((datePicker, year, month, day) -> {
            // +1 ya que Enero es 0
            final String selectedDate = twoDigits(day) + "/" + twoDigits(month + 1) + "/" + year;
            et_nacimiento.setText(selectedDate);
        });

        newFragment.show(getSupportFragmentManager(), "datePicker");
    }
    private String twoDigits(int n) {
        return (n<=9) ? ("0"+n) : String.valueOf(n);
    }
//Método para comprobar los nombres y apellidos
    public boolean NoNumeros(String cadena) {
        for (int x = 0; x < cadena.length(); x++) {
            char c = cadena.charAt(x);
            // Si no está entre a y z, ni entre A y Z, ni es un espacio
            if (!((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == ' ' || c =='ñ' || c=='Ñ'
                    || c=='á' || c=='é' || c=='í' || c=='ó' || c=='ú'
                    || c=='Á' || c=='É' || c=='Í' || c=='Ó' || c=='Ú')) {
                return false;
            }
        }
        return true;
    }
    public boolean validartelefono(String telefono) {
        if (telefono.length() != 9){
            return false;
        } else{
            for (int x = 0; x < telefono.length(); x++) {
              char c = telefono.charAt(x);
               //si  no tiene numeros
                if (!(c >= '0' && c <= '9')) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean validarcontra(String password) {
        boolean numeros = false;
        boolean letras = false;
        for (int x = 0; x < password.length(); x++) {
            char c = password.charAt(x);
            // Si no está entre a y z, ni entre A y Z, ni es un espacio
            if (((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')  || c =='ñ' || c=='Ñ'
                    || c=='á' || c=='é' || c=='í' || c=='ó' || c=='ú'
                    || c=='Á' || c=='É' || c=='Í' || c=='Ó' || c=='Ú')) {
                letras = true;
            }
            if ((c >= '0' && c <= '9') ) {
                numeros = true;
            }

        }
        if (numeros == true && letras ==true){
            return true;
        }
        return false;
    }


}
