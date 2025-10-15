package ca.gbc.comp3074.lab6week7

import android.content.Context
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class PressureActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var pressureSensor: Sensor? = null
    private lateinit var pressureText: TextView
    private lateinit var layout: ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_pressure)
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        layout = findViewById(R.id.main)
        pressureText = findViewById(R.id.pressureValue)
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        pressureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE)

        if (pressureSensor != null) {
            Log.d("PRESSURE SENSOR", "Sensor ref received")
        } else {
            Log.e("PRESSURE SENSOR", "Sensor not present")
            pressureText.text = "Pressure sensor not available"
        }
    }

    override fun onResume() {
        super.onResume()
        pressureSensor?.also {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_PRESSURE) {
            val pressure = event.values[0]
            pressureText.text = "Pressure: %.2f hPa".format(pressure)
            updateBackground(pressure)
        }
    }

    private fun updateBackground(pressure: Float) {
        when {
            pressure < 950 -> layout.setBackgroundColor(Color.GRAY)
            pressure in 950.0..1010.0 -> layout.setBackgroundColor(Color.BLUE)
            else -> layout.setBackgroundColor(Color.YELLOW)
        }
    }
}
