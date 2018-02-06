package lab1.cwang87.lab1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.ViewGroup;

import com.affectiva.android.affdex.sdk.Frame;
import com.affectiva.android.affdex.sdk.detector.CameraDetector;
import com.affectiva.android.affdex.sdk.detector.Detector;
import com.affectiva.android.affdex.sdk.detector.Face;

import java.util.List;


public class MainActivity extends AppCompatActivity
        implements CameraDetector.CameraEventListener, CameraDetector.ImageListener{

    SurfaceView cameraDetectorSurfaceView;
    CameraDetector cameraDetector;

    int maxProcessingRate = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cameraDetectorSurfaceView = (SurfaceView)findViewById(R.id.cameraDetectorSurfaceView);
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
        float anger = face.emotions.getAnger();
        float surprise = face.emotions.getSurprise();

        System.out.println("Joy: " + joy);
        System.out.println("Anger: " + anger);
        System.out.println("Surprise: " + surprise);
    }


    public void compute_SMA() {

    }
}
