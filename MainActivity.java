package hu.unideb.inf.kettesprojekt0;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    CameraManager cameraManager;
    String cameraID;
    boolean vilagit = false;
    Button ledButton;
    Sensor kozelsegSensor;
    SensorManager sensorManager;
    SensorEventListener sensorEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        ledButton = findViewById(R.id.ledButton);
        //kamera led
        cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            cameraID = cameraManager.getCameraIdList()[0];
        }catch(Exception e){
            e.printStackTrace();
        }
        //közelség szenzor
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        kozelsegSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        sensorEventListener = new SensorEventListener() {
            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) { }
            @Override
            public void onSensorChanged(SensorEvent event) {
                try {
                    cameraManager.setTorchMode(cameraID,false);
                    vilagit = false;
                    ledButton.setText("Vaku bekapcsolása");
                } catch (CameraAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        };

    }
    //szenzorhoz
    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(sensorEventListener, kozelsegSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }
    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(sensorEventListener);
    }

    public void ki_be_kapcsolas(View view) {
        try {
            if (vilagit == true) {
                vilagit = false;
                ledButton.setText("Vaku bekapcsolása");
                cameraManager.setTorchMode(cameraID, false);
            } else {
                vilagit = true;
                ledButton.setText("Vaku kikapcsolása");
                cameraManager.setTorchMode(cameraID, true);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }



}