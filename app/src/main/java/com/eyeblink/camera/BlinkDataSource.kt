package com.eyeblink.camera

import android.content.Context
import android.util.Log
import com.eyeblink.BlinkData
import com.eyeblink.MainViewModel
import com.eyeblink.Utility
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.MultiProcessor
import com.google.android.gms.vision.Tracker
import com.google.android.gms.vision.face.Face
import com.google.android.gms.vision.face.FaceDetector
import java.io.IOException
import javax.inject.Inject

public class BlinkDataSource( val model: MainViewModel, val context: Context) {

    private var mStartRequested: Boolean = false
    lateinit var mCameraSource: CameraSource;

    init{
        mStartRequested = false
        createCameraSource()
        startCameraSource()
    }

    private fun createCameraSource() {
        val detector = FaceDetector.Builder(context).setClassificationType(FaceDetector.ALL_CLASSIFICATIONS).build()
        detector.setProcessor(
                MultiProcessor.Builder(GraphicFaceTrackerFactory()).build())

        if (!detector.isOperational()) {
            Log.w(TAG, "Face detector dependencies are not yet available.")
        }

        mCameraSource = CameraSource.Builder(context, detector).setRequestedPreviewSize(640, 480).setFacing(CameraSource.CAMERA_FACING_FRONT).setRequestedFps(30.0f).build()
    }


    @Throws(IOException::class)
    fun start() {
        mStartRequested = true
        startIfReady()

    }

    @Throws(IOException::class)
    private fun startIfReady() {
        if (mStartRequested) {
            mCameraSource.start()
            mStartRequested = false
        }
    }

    private fun startCameraSource() {

        try {
            start()
        } catch (e: IOException) {
            Log.e(TAG, "Unable to start camera source.", e)
            mCameraSource.release()
        }

    }

    private inner class GraphicFaceTrackerFactory : MultiProcessor.Factory<Face> {
        override fun create(face: Face): Tracker<Face> {
            return GraphicFaceTracker()
        }

        override fun hashCode(): Int {
            return super.hashCode()
        }
    }

    private inner class GraphicFaceTracker internal constructor() : Tracker<Face>() {
        override fun onNewItem(faceId: Int, item: Face) {
        }

        override fun onUpdate(detectionResults: Detector.Detections<Face>, face: Face) {
            model.setBlinkData( BlinkData(Utility.getCurrentTime() , face.isLeftEyeOpenProbability, face.isRightEyeOpenProbability))
        }
    }

    companion object {
        private val TAG = "FaceTracker"
    }
}
