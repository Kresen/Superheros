package za.co.superhero.util


import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log


public class ShakeEventManager : SensorEventListener {

    private var sManager: SensorManager? = null
    private var s: Sensor? = null


    private val MOV_COUNTS = 2
    private val MOV_THRESHOLD = 10
    private val ALPHA = 0.8f
    private val SHAKE_WINDOW_TIME_INTERVAL = 500 // milliseconds


    // Gravity force on x,y,z axis
    private val gravity = FloatArray(3)

    private var counter = 0
    private var firstMovTime: Long = 0
    private var listener: ShakeListener? = null

    fun ShakeEventManager() {}

    fun setListener(listener: ShakeListener?) {
        this.listener = listener
    }

    fun init(ctx: Context) {
        sManager = ctx.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        s = sManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        register()
    }

    fun register() {
        sManager!!.registerListener(this, s, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onSensorChanged(sensorEvent: SensorEvent) {
        val maxAcc = calcMaxAcceleration(sensorEvent)
        Log.d("SwA", "Max Acc [$maxAcc]")
        if (maxAcc >= MOV_THRESHOLD) {
            if (counter == 0) {
                counter++
                firstMovTime = System.currentTimeMillis()
                Log.d("SwA", "First mov..")
            } else {
                val now = System.currentTimeMillis()
                if (now - firstMovTime < SHAKE_WINDOW_TIME_INTERVAL) counter++ else {
                    resetAllData()
                    counter++
                    return
                }
                Log.d("SwA", "Mov counter [$counter]")
                if (counter >= MOV_COUNTS) if (listener != null) listener!!.onShake()
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, i: Int) {}

    fun deregister() {
        sManager!!.unregisterListener(this)
    }


    private fun calcMaxAcceleration(event: SensorEvent): Float {
        gravity[0] = calcGravityForce(event.values[0], 0)
        gravity[1] = calcGravityForce(event.values[1], 1)
        gravity[2] = calcGravityForce(event.values[2], 2)
        val accX = event.values[0] - gravity[0]
        val accY = event.values[1] - gravity[1]
        val accZ = event.values[2] - gravity[2]
        val max1 = Math.max(accX, accY)
        return Math.max(max1, accZ)
    }

    // Low pass filter
    private fun calcGravityForce(currentVal: Float, index: Int): Float {
        return ALPHA * gravity[index] + (1 - ALPHA) * currentVal
    }


    private fun resetAllData() {
        Log.d("SwA", "Reset all data")
        counter = 0
        firstMovTime = System.currentTimeMillis()
    }


    interface ShakeListener {
        fun onShake()
    }
}