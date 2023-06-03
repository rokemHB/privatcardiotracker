package com.example.privatecardiowatch

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.privatcardiowatch.R

class MainActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var heartRateSensor: Sensor? = null
    private lateinit var heartRateTextView: TextView

    // Time between heart rate measurements in milliseconds
    private val measurementInterval: Long = 5000

    // Handler for scheduling the next measurement
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        heartRateTextView = findViewById(R.id.heartRateTextView)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        heartRateSensor = sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE)

        checkBodySensorPermission()
    }

    private fun checkBodySensorPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BODY_SENSORS) !=
            PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.BODY_SENSORS), 0)
        } else {
            scheduleNextMeasurement()
        }
    }

    private fun scheduleNextMeasurement() {
        handler.postDelayed({
            registerHeartRateSensor()
        }, measurementInterval)
    }

    private fun registerHeartRateSensor() {
        heartRateSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_FASTEST)
        }
    }

    override fun onSensorChanged(event: SensorEvent) {
        val heartRate = event.values[0]
        heartRateTextView.text = "Heart Rate: $heartRate bpm"

        // Unregister the sensor listener to save battery
        sensorManager.unregisterListener(this)

        // Schedule the next measurement
        scheduleNextMeasurement()
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
        // Handle changes in sensor accuracy
    }
}
