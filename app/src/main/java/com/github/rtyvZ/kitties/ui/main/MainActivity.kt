package com.github.rtyvZ.kitties.ui.main

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.github.rtyvZ.kitties.R
import com.github.rtyvZ.kitties.common.animations.RotateFabAnimation
import com.github.rtyvZ.kitties.ui.sendPhoto.TakePhotoActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var isRotateFab = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val host = supportFragmentManager
            .findFragmentById(R.id.content_container) as NavHostFragment

        bottomNavigationView.setupWithNavController(host.navController)

        host.navController.addOnDestinationChangedListener { _, destination, _ ->
            val dest: String = try {
                resources.getResourceName(destination.id)
            } catch (e: Resources.NotFoundException) {
                Integer.toString(destination.id)
            }

            Toast.makeText(
                this@MainActivity, "Navigated to $dest",
                Toast.LENGTH_SHORT
            ).show()
            Log.d("NavigationActivity", "Navigated to $dest")
        }

        randomCatFab.setOnClickListener {
            isRotateFab = RotateFabAnimation.rotateFab(it, !isRotateFab)
            if (isRotateFab) {
                RotateFabAnimation.showIn(takeAPhotoFab)
                RotateFabAnimation.showIn(selectPhoto)
            } else {
                RotateFabAnimation.showOut(takeAPhotoFab)
                RotateFabAnimation.showOut(selectPhoto)
            }
        }

        takeAPhotoFab.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_DENIED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                        Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ),
                    CAMERA_REQUEST_CODE
                )
            } else {
                val takeAPhotoActivity = Intent(this, TakePhotoActivity::class.java)
                startActivityForResult(takeAPhotoActivity, ACTIVITY_RESULT_CODE)
            }
        }

        RotateFabAnimation.init(takeAPhotoFab)
        RotateFabAnimation.init(selectPhoto)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_REQUEST_CODE
            && grantResults[0] == PackageManager.PERMISSION_GRANTED
            && grantResults[1] == PackageManager.PERMISSION_GRANTED
            && grantResults[2] == PackageManager.PERMISSION_GRANTED
        ) {
            val takeAPhotoActivity = Intent(this, TakePhotoActivity::class.java)
            startActivityForResult(takeAPhotoActivity, ACTIVITY_RESULT_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == TakePhotoActivity.ACTIVITY_RESULT_CODE && resultCode == RESULT_OK) {

        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    companion object {
        const val CAMERA_REQUEST_CODE = 100
        const val ACTIVITY_RESULT_CODE = 1
    }
}