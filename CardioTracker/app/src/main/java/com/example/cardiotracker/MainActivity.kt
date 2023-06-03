import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.cardiotracker.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.data.DataType
import com.google.android.gms.fitness.request.DataReadRequest
import java.util.concurrent.TimeUnit

class MainActivity : Activity() {

    companion object {
        private const val REQUEST_OAUTH_REQUEST_CODE = 0x1001
    }

    private lateinit var heartRateTextView: TextView
    private lateinit var startReadingButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        heartRateTextView = findViewById(R.id.heartRateTextView)
        startReadingButton = findViewById(R.id.startReadingButton)

        startReadingButton.setOnClickListener {
            if (hasOAuthPermission()) {
                accessGoogleFit()
            } else {
                requestOAuthPermission()
            }
        }
    }

    private fun hasOAuthPermission(): Boolean {
        return GoogleSignIn.hasPermissions(GoogleSignIn.getLastSignedInAccount(this), DataType.TYPE_HEART_RATE_BPM)
    }

    private fun requestOAuthPermission() {
        GoogleSignIn.requestPermissions(
            this,
            REQUEST_OAUTH_REQUEST_CODE,
            GoogleSignIn.getLastSignedInAccount(this),
            DataType.TYPE_HEART_RATE_BPM)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_OAUTH_REQUEST_CODE) {
                accessGoogleFit()
            }
        }
    }

    private fun accessGoogleFit() {
        val readRequest = DataReadRequest.Builder()
            .read(DataType.TYPE_HEART_RATE_BPM)
            .setTimeRange(1, System.currentTimeMillis(), TimeUnit.MILLISECONDS)
            .build()

        Fitness.getHistoryClient(this, GoogleSignIn.getLastSignedInAccount(this)!!)
            .readData(readRequest)
            .addOnSuccessListener { dataReadResponse ->
                // Handle the dataReadResponse
                // For example, you could iterate over the datasets in the response like this:
                for (dataSet in dataReadResponse.dataSets) {
                    // You need to parse the data set to get the heart rate data.
                    // This is a simplified version, actual data parsing may vary.
                    for (dataPoint in dataSet.dataPoints) {
                        for (field in dataPoint.dataType.fields) {
                            val heartRateBpm = dataPoint.getValue(field)
                            heartRateTextView.text = "Heart Rate: $heartRateBpm bpm"
                        }
                    }
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "There was a problem getting the heart rate data.", Toast.LENGTH_SHORT).show()
            }
    }
}
