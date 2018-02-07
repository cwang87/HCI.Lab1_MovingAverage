package lab1.cwang87.lab1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.ViewGroup;
import android.widget.TextView;

import com.affectiva.android.affdex.sdk.Frame;
import com.affectiva.android.affdex.sdk.detector.CameraDetector;
import com.affectiva.android.affdex.sdk.detector.Detector;
import com.affectiva.android.affdex.sdk.detector.Face;

import java.util.List;



public class MainActivity extends AppCompatActivity
        implements CameraDetector.CameraEventListener, CameraDetector.ImageListener {

    private static final int maxProcessingRate = 10;

    private static final float threshold = (float) 60.0;

    private NumSlidingWindow numSlidingWindow;
    private TimeSlidingWindow timeSlidingWindow;
    private SurfaceView cameraDetectorSurfaceView;
    private CameraDetector cameraDetector;

    private TextView joyDataView;
    private TextView smaView;
    private TextView wmaView;
    private TextView timeSmaView;


//    TextView outputView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        numSlidingWindow = new NumSlidingWindow();
        timeSlidingWindow = new TimeSlidingWindow();

        cameraDetectorSurfaceView = (SurfaceView)findViewById(R.id.cameraDetectorSurfaceView);
        joyDataView = findViewById(R.id.joyData);
        smaView = findViewById(R.id.SMA);
        wmaView = findViewById(R.id.WMA);
        timeSmaView = findViewById(R.id.timeSMA);

        cameraDetector = new CameraDetector(this, CameraDetector.CameraType.CAMERA_FRONT, cameraDetectorSurfaceView);
        cameraDetector.setMaxProcessRate(maxProcessingRate);
        cameraDetector.setImageListener((Detector.ImageListener) this);
        cameraDetector.setOnCameraEventListener((CameraDetector.CameraEventListener) this);
        cameraDetector.setDetectAllEmotions(true);
        cameraDetector.start();
    }


    @Override
    public void onCameraSizeSelected(int cameraHeight, int cameraWidth, Frame.ROTATE rotate) {
        ViewGroup.LayoutParams params = cameraDetectorSurfaceView.getLayoutParams();
        params.height = cameraHeight;
        params.width = cameraWidth;
        cameraDetectorSurfaceView.setLayoutParams(params);
    }

    @Override
    public void onImageResults(List<Face> faces, Frame frame, float timeStamp) {
        if (faces == null)
            return; //frame was not processed

        if (faces.size() == 0)
            return; //no face found

        Face face = faces.get(0);
        float joy = face.emotions.getJoy();
        joyDataView.setText("Joy: " + joy);

        numSlidingWindow.update(joy);
        showNumWinRes();

        timeSlidingWindow.add(joy, timeStamp);
        showTimeWinRes();


    }

    public void showNumWinRes() {
        if (numSlidingWindow.getSize() < 100) {
            smaView.setText("Only " + numSlidingWindow.getSize() + " datapoints...");
            wmaView.setText("Only " + numSlidingWindow.getSize() + " datapoints...");
            return;
        }

        float sma = numSlidingWindow.getSMA();
        String smaRes = "SMA of last 100 datapoints is " + sma;

        if (sma > threshold) {
            smaRes = smaRes + "\n" + "You have reached the threshold using SMA!";
        }
        smaView.setText(smaRes);

        float wma = numSlidingWindow.getWMA();
        String wmaRes = "WMA of last 100 datapoints is " + wma;

        if (wma > threshold) {
            wmaRes = wmaRes + "\n" + "You have reached the threshold using WMA!";
        }
        wmaView.setText(wmaRes);
    }

    public void showTimeWinRes() {
        if (!timeSlidingWindow.reachWindow()) {
            timeSmaView.setText("Not reach 10 seconds yet...");
            return;
        }

        float sma = timeSlidingWindow.getSMA();
        String smaRes = "SMA of last 10 seconds is " + sma;
        if (sma > threshold) {
            smaRes = smaRes + "\n" + "You have reached the threshold using SMA using timestamps!";
        }
        timeSmaView.setText(smaRes);
    }

}
