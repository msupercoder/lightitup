package lightitup.namo.com;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.widget.Toast;

import java.security.Policy;

public class MainActivity extends AppCompatActivity {

    Switch ls;
    private Camera camera;
    private boolean isFlashOn;
    private boolean hasFlash;
    Parameters params;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ls = (Switch) findViewById(R.id.light_switch);
        ls.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    hasFlash = getApplicationContext().getPackageManager()
                            .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

                    if (!hasFlash) {
                        // device doesn't support flash
                        // Show alert message and close the application
                        AlertDialog alert = new AlertDialog.Builder(MainActivity.this)
                                .create();
                        alert.setTitle("Error");
                        alert.setMessage("Sorry, your device doesn't support flash light!");
                        alert.setButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // closing the application
                                finish();
                            }
                        });
                        alert.show();
                        return;
                    }
                    else {
                        Toast.makeText(getApplicationContext(),"Turning On" , Toast.LENGTH_LONG).show();
                        turnOnFlash();
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(),"Turning Off" , Toast.LENGTH_LONG).show();
                    turnOffFlash();
                }
            }
        });
    }
    private void getCamera() {
        if (camera == null) {
            try {
                camera = Camera.open();
                params = camera.getParameters();
            } catch (RuntimeException e) {
                Log.e("Camera Failed to Open", e.getMessage());
            }
        }
    }

    /*
    * Turning On flash
    */
    private void turnOnFlash() {
        getCamera();
        if (!isFlashOn) {
            if (camera == null || params == null) {
                return;
            }
            // play sound
            //  playSound();

            params = camera.getParameters();
            params.setFlashMode(Parameters.FLASH_MODE_TORCH);
            camera.setParameters(params);
            camera.startPreview();
            isFlashOn = true;
        }

    }
    /*
 * Turning Off flash
 */
    private void turnOffFlash() {
        getCamera();
        if (isFlashOn) {
            if (camera == null || params == null) {
                return;
            }
            // play sound
            //playSound();

            params = camera.getParameters();
            params.setFlashMode(Parameters.FLASH_MODE_OFF);
            camera.setParameters(params);
            camera.stopPreview();
            isFlashOn = false;
        }
    }
}
