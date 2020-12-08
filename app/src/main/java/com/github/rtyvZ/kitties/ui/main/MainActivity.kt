package com.github.rtyvZ.kitties.ui.main

import android.Manifest
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.github.rtyvZ.kitties.R
import com.github.rtyvZ.kitties.common.Strings
import com.github.rtyvZ.kitties.common.animations.RotateFabAnimation
import com.github.rtyvZ.kitties.ui.receivers.NoConnectivityMessageReceiver
import com.github.rtyvZ.kitties.ui.sendPhoto.TakePhotoActivity
import com.github.rtyvZ.kitties.ui.services.SendCatService
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity @Inject constructor() : AppCompatActivity(R.layout.activity_main) {

    private var isRotateFab = false

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: MainActivityViewModel
    private val noConnectivityMessageReceiver = NoConnectivityMessageReceiver()

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(
            this,
            viewModelFactory
        ).get(MainActivityViewModel::class.java)

        val host = supportFragmentManager
            .findFragmentById(R.id.content_container) as NavHostFragment

        bottomNavigationView.setupWithNavController(host.navController)

        viewModel.getRealPath.observe(this, {
            startService(it)
        })

        randomCatFab.setOnClickListener {
            rotateFab(it)
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
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ),
                    CAMERA_REQUEST_CODE
                )
                rotateFab(randomCatFab)
            } else {
                startActivityFromUpload()
                rotateFab(randomCatFab)
            }
        }

        selectPhoto.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_DENIED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ),
                    GALLERY_REQUEST_CODE
                )
                rotateFab(randomCatFab)
            } else {
                rotateFab(randomCatFab)
                startActivityFromGallery()
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
        when (requestCode) {
            CAMERA_REQUEST_CODE -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED
                    && grantResults[2] == PackageManager.PERMISSION_GRANTED
                ) {
                    startActivityFromUpload()
                }
            }
            GALLERY_REQUEST_CODE -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED
                ) {
                    startActivityFromGallery()
                }
            }
            else -> {
                Toast.makeText(this, R.string.no_permition, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == TakePhotoActivity.ACTIVITY_RESULT_CODE && resultCode == RESULT_FIRST_USER) {
            data?.let {
                val resultData = it.getStringExtra(Strings.IntentConsts.EXTRA_SEND_UPLOAD)
                resultData?.let { toastMessage ->
                    Toast.makeText(this, toastMessage, Toast.LENGTH_LONG).show()
                }
            }
        }

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            data?.dataString?.let {
                viewModel.getRealPathForImage(it.toUri())
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun rotateFab(view: View) {
        isRotateFab = RotateFabAnimation.rotateFab(view, !isRotateFab)
        if (isRotateFab) {
            RotateFabAnimation.showIn(takeAPhotoFab)
            RotateFabAnimation.showIn(selectPhoto)
        } else {
            RotateFabAnimation.showOut(takeAPhotoFab)
            RotateFabAnimation.showOut(selectPhoto)
        }
    }


    private fun startService(path: String?) {
        Intent(this, SendCatService::class.java).also { intent ->
            path?.let {
                intent.data = path.toUri()
                startService(intent)
            }
        }
    }

    private fun startActivityFromUpload() {
        val takeAPhotoActivity = Intent(this, TakePhotoActivity::class.java)
        startActivityForResult(takeAPhotoActivity, ACTIVITY_RESULT_CODE)
    }

    private fun startActivityFromGallery() = Intent().also {
        it.type = Strings.IntentConsts.INTENT_TYPE_IMAGE
        it.action = Intent.ACTION_PICK
        startActivityForResult(
            Intent.createChooser(
                it,
                getString(R.string.select_picture_label)
            ), PICK_IMAGE
        )
    }

    companion object {
        const val CAMERA_REQUEST_CODE = 100
        const val GALLERY_REQUEST_CODE = 1000
        const val ACTIVITY_RESULT_CODE = 1
        const val PICK_IMAGE = 1
        const val DATA = "_data"
    }

    override fun onResume() {
        super.onResume()
        val filter = IntentFilter(Strings.IntentConsts.SEND_NO_CONNECTIVITY_INTENT_ACTION)
        registerReceiver(noConnectivityMessageReceiver, filter)
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(noConnectivityMessageReceiver)
    }
}