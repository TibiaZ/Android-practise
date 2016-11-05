package com.example.dam2.pmm_practica_01_javiersanzrozalen;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.Locale;

public class SecondActivity extends AppCompatActivity implements View.OnClickListener,
        SeekBar.OnSeekBarChangeListener,
        CompoundButton.OnCheckedChangeListener {

    // Declaramos nuestros objetos como privados
    private EditText campoNombreSecond;
    private SeekBar seekBarEdad;
    private TextView campoEdadSecond;
    private EditText campoTelefonoSecond;
    private TextView campoLatitud;
    private TextView campoLongitud;
    private ToggleButton toggleGPS;
    private Button btnAceptar;
    private Intent intento;
    private Bundle bundle;

    // Declaración de objetos para el uso de la geolocalización
    private LocationManager locationManager;
    private LocationListener locationListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("INFO", "Iniciando onCreate de SecondActivity");
        setContentView(R.layout.activity_second);

        // Inflamos nuestros widgets
        findViewById(R.id.textViewNombreSecond);
        findViewById(R.id.textViewEdadSecond);
        findViewById(R.id.textViewTelefonoSecond);
        findViewById(R.id.textLatitud);
        findViewById(R.id.textViewLongitud);
        campoNombreSecond = (EditText) findViewById(R.id.editTextNombre);
        seekBarEdad = (SeekBar) findViewById(R.id.seekBarEdad);
        campoEdadSecond = (TextView) findViewById(R.id.textViewSeekBarEdad);
        campoTelefonoSecond = (EditText) findViewById(R.id.editTextTelefono);
        toggleGPS = (ToggleButton) findViewById(R.id.toggleButtonCoordenadas);
        btnAceptar = (Button) findViewById(R.id.buttonAceptar);
        campoLatitud = (TextView) findViewById(R.id.textCampoLatitud);
        campoLongitud = (TextView) findViewById(R.id.textViewCampoLongitud);

        // Hacemos uso del onClick llamando al método implementado en esta clase
        seekBarEdad.setOnSeekBarChangeListener(this);
        toggleGPS.setOnCheckedChangeListener(this);
        btnAceptar.setOnClickListener(this);

        // Obtenemos los datos del intent y los ponemos en el EditText en caso de recibirlos
        bundle = getIntent().getExtras();
        if (getIntent().hasExtra(MainActivity.KEY_NOMBRE)) {
            campoNombreSecond.setText(bundle.getString(MainActivity.KEY_NOMBRE));
        }
        if (getIntent().hasExtra(MainActivity.KEY_EDAD)) {
            campoEdadSecond.setText(bundle.getString(MainActivity.KEY_EDAD));
            seekBarEdad.setProgress(Integer.valueOf(campoEdadSecond.getText().toString()) - 18);
        }
        if (getIntent().hasExtra(MainActivity.KEY_TELEFONO)) {
            campoTelefonoSecond.setText(bundle.getString(MainActivity.KEY_TELEFONO));
        }

        // Instanciación y uso de los objetos para la geolocalización
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                campoLatitud.setText(String.valueOf(location.getLatitude()));
                campoLongitud.setText(String.valueOf(location.getLongitude()));
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {}

            @Override
            public void onProviderEnabled(String provider) {}

            @Override
            public void onProviderDisabled(String provider) {}
        };
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i("INFO", "Iniciando onPause de SecondActivity");

        // Si nuestra actividad entra en onPause, detenemos el GPS
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.removeUpdates(locationListener);

        // Dejamos como OFF nuestro toggle button
        toggleGPS.setChecked(false);
    }

    @Override
    public void onClick(View v) {
        // Preparamos el intent
        intento = new Intent();

        // Comprobamos el id del boton al que vamos a escuchar
        if (v.getId() == R.id.buttonAceptar) {

            // Si los EditText NO están vacíos, almacenamos el extra en una variable y ponemos el extra en el intent
            if (!campoNombreSecond.getText().toString().isEmpty()) {
                intento.putExtra(MainActivity.KEY_NOMBRE, campoNombreSecond.getText().toString());
            }
            if (!campoEdadSecond.getText().toString().isEmpty()) {
                intento.putExtra(MainActivity.KEY_EDAD, Integer.valueOf(seekBarEdad.getProgress() + 18));
            }
            if (!campoTelefonoSecond.getText().toString().isEmpty()) {
                intento.putExtra(MainActivity.KEY_TELEFONO, Integer.valueOf(campoTelefonoSecond.getText().toString()));
            }
            if (!campoLatitud.getText().toString().isEmpty()
                    && !campoLongitud.getText().toString().isEmpty()) {
                intento.putExtra(MainActivity.KEY_LOCALIZACION_LATITUD, Double.valueOf(campoLatitud.getText().toString()));
                intento.putExtra(MainActivity.KEY_LOCALIZACION_LONGITUD, Double.valueOf(campoLongitud.getText().toString()));
            }

            // Lanzamos el intento con nuestros datos
            setResult(RESULT_OK, intento);
            finish();
        }
    }

    // Métodos de la interfaz SeekBar.OnSeekBarChangeListener

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        // Establecemos un máximo de edad
        seekBar.setMax(35-18);

        // Ponemos en el TextView el número que sacamos del SeekBar
        campoEdadSecond.setText("" + (seekBar.getProgress() + 18) + " años");
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {}

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {}

    // Métodos de la interfaz CompoundButton.OnCheckedChangeListener

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        // Comprobamos el id del boton al que vamos a escuchar
        if (buttonView.getId() == R.id.toggleButtonCoordenadas) {
            if (toggleGPS.isChecked()) {
                // Si el toggle button está activado mostrará coordenadas
                campoLongitud.setVisibility(View.VISIBLE);
                campoLatitud.setVisibility(View.VISIBLE);
                findViewById(R.id.textLatitud).setVisibility(View.VISIBLE);
                findViewById(R.id.textViewLongitud).setVisibility(View.VISIBLE);

                // Escuchamos al GPS obteniendo las coordenadas
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

            } else {
                // Si el toggle button NO está activado, esconderá las coordenadas
                campoLongitud.setVisibility(View.INVISIBLE);
                campoLatitud.setVisibility(View.INVISIBLE);
                findViewById(R.id.textLatitud).setVisibility(View.INVISIBLE);
                findViewById(R.id.textViewLongitud).setVisibility(View.INVISIBLE);

                // Si el ToggleButton está en OFF, no hará uso del GPS
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                locationManager.removeUpdates(locationListener);
            }
        }
    }
}
