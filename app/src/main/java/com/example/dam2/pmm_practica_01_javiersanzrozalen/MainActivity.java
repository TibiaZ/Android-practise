package com.example.dam2.pmm_practica_01_javiersanzrozalen;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    // Declaramos nuestros objetos de clase
    private TextView campoNombre;
    private TextView campoEdad;
    private TextView campoTelefono;
    private TextView campoUbicacionLatitud;
    private TextView campoUbicacionLongitud;
    private Button btnGoogleMaps;
    private Button btnEditar;
    private Intent intento;

    // Constantes que utilizaremos como claves para identificar qué queremos extraer desde otras clases
    public static final String KEY_NOMBRE = "nombre";
    public static final String KEY_EDAD = "edad";
    public static final String KEY_TELEFONO = "telefono";
    public static final String KEY_LOCALIZACION_LATITUD = "latitud";
    public static final String KEY_LOCALIZACION_LONGITUD = "altitud";
    public static final int REQUEST_CODE = 1234;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("INFO", "Iniciando onCreate de MainActivity");
        setContentView(R.layout.activity_main);

        // Inflamos nuestros widgets
        findViewById(R.id.textViewNombre);
        findViewById(R.id.textViewEdad);
        findViewById(R.id.textViewTelefono);
        findViewById(R.id.textViewUbicacion);
        campoNombre = (TextView) findViewById(R.id.textViewNombreVacio);
        campoEdad = (TextView) findViewById(R.id.textViewEdadVacio);
        campoTelefono = (TextView) findViewById(R.id.textViewTelefonoVacio);
        campoUbicacionLatitud = (TextView) findViewById(R.id.textViewUbicacionVacia);
        campoUbicacionLongitud = (TextView) findViewById(R.id.textViewUbicacionVacia2);
        btnGoogleMaps = (Button) findViewById(R.id.buttonVerGoogleMaps);
        btnEditar = (Button) findViewById(R.id.buttonEditar);

        // Hacemos uso de onClick llamando al método de esta clase
        btnGoogleMaps.setOnClickListener(this);
        btnEditar.setOnClickListener(this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK){
            // Sacamos los extras
            if(data.hasExtra(KEY_NOMBRE)){
                campoNombre.setText(data.getExtras().getString(KEY_NOMBRE));
            }
            if(data.hasExtra(KEY_EDAD)){
                campoEdad.setText(String.valueOf(data.getExtras().getInt(KEY_EDAD)));
            }
            if(data.hasExtra(KEY_TELEFONO)){
                campoTelefono.setText(String.valueOf(data.getExtras().getInt(KEY_TELEFONO)));
            }
            if(data.hasExtra(KEY_LOCALIZACION_LATITUD)){
                campoUbicacionLatitud.setText(String.valueOf(data.getExtras().getDouble(KEY_LOCALIZACION_LATITUD)));
            }
            if(data.hasExtra(KEY_LOCALIZACION_LONGITUD)){
                campoUbicacionLongitud.setText(String.valueOf(data.getExtras().getDouble(KEY_LOCALIZACION_LONGITUD)));
            }

            // Si nuestros campos de latitud/longitud están rellenos, activamos el boton
            // Que se ocupará de lanzar un intent con Google Maps
            if(!campoUbicacionLatitud.getText().toString().isEmpty()
                    && !campoUbicacionLongitud.getText().toString().isEmpty()){
                btnGoogleMaps.setEnabled(true);
            }
        }
        // Dejamos preparado para programar en caso de que el resultCode sea canceled
        else if(requestCode == REQUEST_CODE && resultCode == RESULT_CANCELED){}
    }

    @Override
    public void onClick(View v) {
        intento = new Intent(this, SecondActivity.class);

        // Botón para lanzar el intent explícito de google maps
        if(v.getId() == R.id.buttonVerGoogleMaps){
            // Lanzamos Google Maps con la localización recibida y ubicada en los TextView
            String uri = String.format(Locale.ENGLISH, "geo:%f,%f",
                    Double.valueOf(campoUbicacionLatitud.getText().toString()),
                    Double.valueOf(campoUbicacionLongitud.getText().toString()));
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            this.startActivity(intent);
        }

        else if(v.getId() == R.id.buttonEditar){
            // Comprobamos si el campo de texto está vacío, si no lo esta...
            // Añadimos los datos al intent para pasarselos a la Second Activity
            if(!campoNombre.getText().toString().isEmpty()){
                intento.putExtra(KEY_NOMBRE, campoNombre.getText().toString());
            }
            if(!campoEdad.getText().toString().isEmpty()){
                intento.putExtra(KEY_EDAD, campoEdad.getText().toString());
            }
            if(!campoTelefono.getText().toString().isEmpty()){
                intento.putExtra(KEY_TELEFONO, campoTelefono.getText().toString());
            }

            // Sí pulsa Editar, iniciamos el intent que nos lleva a la segunda Activity
            startActivityForResult(intento, 1234);
        }
    }

}
