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


public class ShakeActivity extends AppCompatActivity {

    private SensorManager senSensorManager;
    private Sensor senAccelerometer;
    private float accval;
    private float acclast;
    private float shake;

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
            shake=shake*.7f + delta;
            if(shake>16)
            {
                Toast.makeText(getApplicationContext(),"sms sent",Toast.LENGTH_SHORT).show();
                startService(new Intent(getApplication(),LocationService.class));
            }
        }
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

}
