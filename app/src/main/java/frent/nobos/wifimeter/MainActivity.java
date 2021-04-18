package frent.nobos.wifimeter;


import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
/**
 * wifi signal strength meter app
 * 2021-04-15
 * @author John Maxfield
 * @author Noah Boyers
 * @author Aaron Gibson
 */
public class MainActivity extends AppCompatActivity {
    private final static String TAG = "WifiMeter";    // for Log.d

    // UI elements
    private TextView signalEditText;                  // displays rssi value
    private ProgressBar signalBar;                    // visual signal strength indicator
    private Button resetButton;

    private final static int BAR_GRANULARITY = 4;     // progressbar goes 1-4


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // get the java handles for the GUI widgets
        signalEditText = findViewById(R.id.textRSSI);
        resetButton = findViewById(R.id.buttonReset);
        signalBar = findViewById(R.id.barSignalStrength);

        // progress bar max value
        signalBar.setMax(BAR_GRANULARITY);

        // show some sample data
        signalBar.setProgress(BAR_GRANULARITY - 2);

        // resets the wifi
        resetButton.setOnClickListener(v -> checkWifi());
    }

    /**
     * Method to to fire
     * once the app Opens
     */
    @Override
    public void onStart() {
        super.onStart();
        checkWifi();
    }

    /**
     * Method that checks the wifi
     * and updates the GUI to show
     * the wifi strength
     */
    private void checkWifi() {
        ConnectivityManager cm = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        WifiManager wifiManager;
        NetworkInfo Info = cm.getActiveNetworkInfo();
        int linkSpeed;    // Connection Speed
        int rssi;         // Connection Strength

        //Checks to see if a WIFI connection is there.
        if (Info == null || !Info.isConnectedOrConnecting()) {
            signalEditText.setText(getString(R.string.lost_connection));
            signalBar.setProgress(0);
            Log.d(TAG, "No connection");
        } else {
            int netType = Info.getType();

            if (netType == ConnectivityManager.TYPE_WIFI) {
                wifiManager = (WifiManager) getApplicationContext()
                        .getSystemService(Context.WIFI_SERVICE);
                // Debugging Purpose
                linkSpeed = wifiManager
                        .getConnectionInfo()
                        .getLinkSpeed();

                // Creation of the RSSI from the wifi
                rssi = wifiManager
                        .getConnectionInfo()
                        .getRssi();

                //Updates the text to the display
                signalEditText.setText(getString(R.string.rssi_dbm, rssi));

                //Checks and updates the GUI to display the WIFI connection
                if(rssi <= 0 && rssi >= -50){
                    //Best signal
                    signalBar.setProgress(5);
                } else if (rssi < -50 && rssi >= -70) {
                    //Good signal
                    signalBar.setProgress(3);
                } else if (rssi < -70 && rssi >= -80) {
                    //Low signal
                    signalBar.setProgress(2);
                } else if (rssi < -80 && rssi >= -100) {
                    //Very weak signal
                    signalBar.setProgress(1);
                } else {
                    //No Signal
                    signalBar.setProgress(0);
                    signalEditText.setText(getString(R.string.lost_connection));
                    Log.d(TAG, "No connection");
                }
                //Logging speed and Strength
                Log.d(TAG,"Link Speed: "+ linkSpeed + " RSSI: " + rssi);
            } else {
                // Not wifi but still has internet access.
                signalEditText.setText(getString(R.string.not_wifi));
                signalBar.setProgress(4);
            }
        }
    }
}





