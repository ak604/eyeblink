package com.eyeblink

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.MultiProcessor
import com.google.android.gms.vision.Tracker
import com.google.android.gms.vision.face.Face
import com.google.android.gms.vision.face.FaceDetector
import eyeblink.com.eyeblink.R
import kotlinx.android.synthetic.main.fragment_user_face.*
import java.io.IOException

class UserFace : Fragment() {

    lateinit private var model: MainViewModel

    lateinit var mCameraSource: CameraSource
    lateinit var mPreview: CameraSourcePreview
    lateinit var mGraphicOverlay: GraphicOverlay

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        model = ViewModelProviders.of(activity).get(MainViewModel::class.java)
        mPreview = preview
        mGraphicOverlay = faceOverlay
        createCameraSource()
        startCameraSource()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_user_face, container, false)
    }

    private fun createCameraSource() {

        val context = activity.getApplicationContext()
        val detector = FaceDetector.Builder(context).setClassificationType(FaceDetector.ALL_CLASSIFICATIONS).build()

        detector.setProcessor(
                MultiProcessor.Builder(GraphicFaceTrackerFactory()).build())

        if (!detector.isOperational()) {
            Log.w(TAG, "Face detector dependencies are not yet available.")
        }

        mCameraSource= CameraSource.Builder(context, detector).setRequestedPreviewSize(640, 480).setFacing(CameraSource.CAMERA_FACING_FRONT).setRequestedFps(30.0f).build()
    }

    override fun onPause() {
        super.onPause()
        mPreview.stop()
    }

    override fun onDestroy() {
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
            model.setBlinkData(Pair(face.isLeftEyeOpenProbability, face.isRightEyeOpenProbability))
            if(model.getBlink().value!! )
                (activity as MainActivity).onUserBlink()
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
