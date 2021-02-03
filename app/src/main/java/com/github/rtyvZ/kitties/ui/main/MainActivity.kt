package com.github.rtyvZ.kitties.ui.main

import android.Manifest
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.github.rtyvZ.kitties.R
import com.github.rtyvZ.kitties.common.Strings
import com.github.rtyvZ.kitties.common.animations.RotateFabAnimation
import com.github.rtyvZ.kitties.databinding.ActivityMainBinding
import com.github.rtyvZ.kitties.ui.receivers.NoConnectivityMessageReceiver
import com.github.rtyvZ.kitties.ui.sendPhoto.TakePhotoActivity
import com.github.rtyvZ.kitties.ui.services.SendCatService
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var isRotateFab = false
    private val viewModel: MainActivityViewModel by viewModels()
    private val noConnectivityMessageReceiver = NoConnectivityMessageReceiver()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val host = supportFragmentManager
            .findFragmentById(R.id.content_container) as NavHostFragment
        binding.bottomNavigationView.setupWithNavController(host.navController)

        viewModel.getRealPath.observe(this, {
            startService(it)
        })

        binding.randomCatFab.setOnClickListener {
            rotateFab(it)
        }

        binding.takeAPhotoFab.setOnClickListener {
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
                rotateFab(binding.randomCatFab)
            } else {
                startActivityFromUpload()
                rotateFab(binding.randomCatFab)
            }
        }

        binding.selectPhoto.setOnClickListener {
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
                rotateFab(binding.randomCatFab)
            } else {
                rotateFab(binding.randomCatFab)
                startActivityFromGallery()
            }
        }

        RotateFabAnimation.init(binding.takeAPhotoFab)
        RotateFabAnimation.init(binding.selectPhoto)
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
            RotateFabAnimation.showIn(binding.takeAPhotoFab)
            RotateFabAnimation.showIn(binding.selectPhoto)
        } else {
            RotateFabAnimation.showOut(binding.takeAPhotoFab)
            RotateFabAnimation.showOut(binding.selectPhoto)
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