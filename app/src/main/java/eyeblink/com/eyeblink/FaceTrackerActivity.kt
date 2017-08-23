package eyeblink.com.eyeblink

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.MultiProcessor
import com.google.android.gms.vision.Tracker
import com.google.android.gms.vision.face.Face
import com.google.android.gms.vision.face.FaceDetector
import eyeblink.com.eyeblink.ui.camera.CameraSourcePreview
import eyeblink.com.eyeblink.ui.camera.GraphicOverlay
import java.io.IOException

class FaceTrackerActivity : AppCompatActivity() {
    lateinit var mCameraSource: CameraSource

    lateinit var mPreview: CameraSourcePreview
    lateinit var mGraphicOverlay: GraphicOverlay

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)

        mPreview = findViewById(R.id.preview) as CameraSourcePreview
        mGraphicOverlay = findViewById(R.id.faceOverlay) as GraphicOverlay
        createCameraSource()
        startCameraSource()
    }

    private fun createCameraSource() {

        val context = getApplicationContext()
        val detector = FaceDetector.Builder(context).setClassificationType(FaceDetector.ALL_CLASSIFICATIONS).build()

        detector.setProcessor(
                MultiProcessor.Builder(GraphicFaceTrackerFactory()).build())

        if (!detector.isOperational()) {
            Log.w(TAG, "Face detector dependencies are not yet available.")
        }

        mCameraSource= CameraSource.Builder(context, detector).setRequestedPreviewSize(640, 480).setFacing(CameraSource.CAMERA_FACING_FRONT).setRequestedFps(30.0f).build()
    }

    protected override fun onPause() {
        super.onPause()
        mPreview!!.stop()
    }

    protected override fun onDestroy() {
        super.onDestroy()
        mCameraSource.release()
    }


    private fun startCameraSource() {

        try {
            mPreview.start(mCameraSource, mGraphicOverlay)
        } catch (e: IOException) {
            Log.e(TAG, "Unable to start camera source.", e)
            mCameraSource.release()
        }

    }

    private inner class GraphicFaceTrackerFactory : MultiProcessor.Factory<Face> {
        override fun create(face: Face): Tracker<Face> {
            return GraphicFaceTracker(mGraphicOverlay)
        }

        override fun hashCode(): Int {
            return super.hashCode()
        }
    }

    private inner class GraphicFaceTracker internal constructor(private val mOverlay: GraphicOverlay) : Tracker<Face>() {
        private val mFaceGraphic: FaceGraphic

        init {
            mFaceGraphic = FaceGraphic(mOverlay)
        }

        override fun onNewItem(faceId: Int, item: Face) {
            mFaceGraphic.setId(faceId)
        }

        override fun onUpdate(detectionResults: Detector.Detections<Face>, face: Face) {
            mOverlay.add(mFaceGraphic)
            mFaceGraphic.updateFace(face)
            BlinkDetector.act( face.getIsLeftEyeOpenProbability() , face.getIsRightEyeOpenProbability())
        }

        override fun onMissing(detectionResults: Detector.Detections<Face>) {
            mOverlay.remove(mFaceGraphic)
        }
        override fun onDone() {
            mOverlay.remove(mFaceGraphic)
        }
    }

    companion object {
        private val TAG = "FaceTracker"
    }
}
