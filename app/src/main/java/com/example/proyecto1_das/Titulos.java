package com.example.proyecto1_das;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Titulos extends AppCompatActivity {
    RecyclerView rv1;
    List<ListaTitulos> lista_titulos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_titulos);
//Creamos el array donde meteremos los titulos con su año
        lista_titulos= new ArrayList<> ();
        lista_titulos.add(new ListaTitulos(getResources().getString(R.string.t1),1987) );
        lista_titulos.add(new ListaTitulos(getResources().getString(R.string.t2),1993) );
        lista_titulos.add(new ListaTitulos(getResources().getString(R.string.t3),1999) );
        lista_titulos.add(new ListaTitulos(getResources().getString(R.string.t4),2005) );
        lista_titulos.add(new ListaTitulos(getResources().getString(R.string.t5),2010) );
        rv1=findViewById(R.id.rv1);
//crearmos el Recycler view
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        rv1.setLayoutManager(linearLayoutManager);

        rv1.setAdapter(new AdaptadorPersona());

    }

    private class AdaptadorPersona extends RecyclerView.Adapter<AdaptadorPersona.AdaptadorPersonaHolder> {

        @NonNull
        @Override
        public AdaptadorPersonaHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new AdaptadorPersonaHolder(getLayoutInflater().inflate(R.layout.layout_titulos,parent,false));
        }

        @Override
        public void onBindViewHolder(@NonNull AdaptadorPersonaHolder holder, int position) {
            holder.imprimir(position);
        }

        @Override
        public int getItemCount() {
            return lista_titulos.size();
        }

        class AdaptadorPersonaHolder extends RecyclerView.ViewHolder {

            TextView tv1,tv2;
            public AdaptadorPersonaHolder(@NonNull View itemView) {
                super(itemView);

                tv1=itemView.findViewById(R.id.tvnombre);
                tv2=itemView.findViewById(R.id.tvedad);
            }

//Imprimimos cada uno de los Cards View
            public void imprimir(int position) {

                tv1.setText(lista_titulos.get(position).getTitulo());
                tv2.setText(""+lista_titulos.get(position).getAño());
            }
        }
    }
}
