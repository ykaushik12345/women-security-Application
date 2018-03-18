package com.sheield.myapplication;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;


public class Main7Activity extends AppCompatActivity {

    private SensorManager senSensorManager;
    private Sensor senAccelerometer;
    private float accval;
    private float acclast;
    public float Sensitivity= MainActivity.shake;
    private static float shake;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senSensorManager.registerListener(sensorListener, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        accval=SensorManager.GRAVITY_EARTH;
        acclast=SensorManager.GRAVITY_EARTH;
        shake=0.00f;
    }
    private final SensorEventListener sensorListener= new SensorEventListener(){
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];
            acclast=accval;
            accval=(float) Math.sqrt((double) (x*x+y*y+z*z));
            float delta=accval-acclast;
            shake=shake*.9f + delta;
            if(shake>Sensitivity)
            {
                String s = String.valueOf(shake);
                String s1 = String.valueOf(delta);
                String s2 = String.valueOf(Sensitivity);
                Toast.makeText(getApplicationContext(),"value of shake is "+s+" and delta is "+s1+" with sensitivity "+s2,Toast.LENGTH_LONG).show();
                startService(new Intent(getApplication(),LocationService.class));
                shake=0;
                finish();
            }
        }
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

}
