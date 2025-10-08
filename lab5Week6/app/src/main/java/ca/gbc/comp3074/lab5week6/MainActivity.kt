package ca.gbc.comp3074.lab5week6

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlin.math.sqrt

class MainActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var lightSensor: Sensor? = null
    private var accSensor: Sensor? = null
    private lateinit var lightTV: TextView
    private lateinit var shakeTV: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        lightTV = findViewById(R.id.light_out)
        shakeTV = findViewById(R.id.shake)

        val textView = findViewById<TextView>(R.id.sensors)
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        val deviceSensors: List<Sensor> = sensorManager.getSensorList(Sensor.TYPE_ALL)

        textView.text = ""
        for (s in deviceSensors) {
            textView.append(s.toString() + "\n\n")
        }

        setupLightSensor()
        setupAccSensor()
    }

    private fun setupLightSensor() {
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
        if (lightSensor != null) {
            Log.d("LIGHT SENSOR", "Sensor ref received")
        } else {
            Log.e("LIGHT SENSOR", "Sensor not present")
        }
    }

    private fun setupAccSensor() {
        accSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        if (accSensor != null) {
            Log.d("ACC SENSOR", "Sensor ref received")
        } else {
            Log.e("ACC SENSOR", "Sensor not present")
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        Log.d("LIGHT SENSOR", "Accuracy changed: $accuracy")
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_LIGHT) {
            val light = event.values[0]
            Log.d("LIGHT", "Light value: $light")
            lightTV.text = brightness(light)
        } else if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
            Log.d("ACC", "Acc value: ${event.values[0]}, ${event.values[1]}, ${event.values[2]}")
            if (detectShake(event.values)) {
                shakeTV.text = "SHAKE!!!"
            }
        }
    }

    private var acceleration = 0f
    private var currentAcceleration = 0f
    private var lastAcceleration = 0f

    private fun detectShake(values: FloatArray): Boolean {
        val x = values[0]
        val y = values[1]
        val z = values[2]

        lastAcceleration = currentAcceleration
        currentAcceleration = sqrt((x * x + y * y + z * z).toDouble()).toFloat()
        val delta = currentAcceleration - lastAcceleration
        acceleration = acceleration * 0.9f + delta
        Log.d("SHAKE", "acc=$acceleration")
        return kotlin.math.abs(acceleration) > 10
    }

    private fun brightness(b: Float): String {
        return when (b.toInt()) {
            0 -> "Pitch black"
            in 1..1000 -> "Very dark"
            in 1001..5000 -> "Dim"
            in 5001..10000 -> "Normal indoor light"
            in 10001..20000 -> "Bright"
            else -> "Very bright"
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d("LIGHT-ON", "Sensor listener activated")
        sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL)
        sensorManager.registerListener(this, accSensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
        Log.d("LIGHT-OFF", "Sensor listener deactivated")
    }
}
