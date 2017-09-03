package com.eyeblink

import android.Manifest
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import eyeblink.com.eyeblink.R


class LoadingActivity : AppCompatActivity() {

    var cameraPermission =false;
    var gmsAvailabe=false;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val rc = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        if (rc == PackageManager.PERMISSION_GRANTED) {
            cameraPermission=true;
        } else {
            requestCameraPermission()
        }
        checkForGMS()

    }

    fun checkAndStartNextActivity(){
        if(gmsAvailabe && cameraPermission) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    fun checkForGMS(){
        val code = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(
                getApplicationContext())
        if (code != ConnectionResult.SUCCESS) {
            val dlg = GoogleApiAvailability.getInstance().getErrorDialog(this, code, RC_HANDLE_GMS)
            dlg.show()
        }else{
            gmsAvailabe=true;
            checkAndStartNextActivity()

        }
    }
    protected override fun onResume() {
        super.onResume()
        checkForGMS()
    }

    private fun requestCameraPermission() {
        val permissions = arrayOf(Manifest.permission.CAMERA)

        if (!ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CAMERA)) {
            ActivityCompat.requestPermissions(this, permissions, RC_HANDLE_CAMERA_PERM)
            return
        }

        val thisActivity = this

        ActivityCompat.requestPermissions(thisActivity, permissions,
                RC_HANDLE_CAMERA_PERM)


    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode != RC_HANDLE_CAMERA_PERM) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            return
        }

        if (grantResults.size != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            cameraPermission=true;
            checkAndStartNextActivity()
            return
        }
        val listener = DialogInterface.OnClickListener { dialog, id -> finish() }

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Dare to Stare").setMessage(R.string.no_camera_permission).setPositiveButton(R.string.ok, listener).show()
    }

    companion object {
        private val RC_HANDLE_GMS = 9001
        private val RC_HANDLE_CAMERA_PERM = 2
    }


}
