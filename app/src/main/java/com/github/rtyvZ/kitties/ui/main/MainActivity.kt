package com.github.rtyvZ.kitties.ui.main

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import com.github.rtyvZ.kitties.R
import com.github.rtyvZ.kitties.common.Strings
import com.github.rtyvZ.kitties.common.animations.RotateFabAnimation
import com.github.rtyvZ.kitties.ui.favoriteCats.FavoriteCatsFragment
import com.github.rtyvZ.kitties.ui.imageCats.RandomCatsFragment
import com.github.rtyvZ.kitties.ui.myCat.MyCatFragment
import com.github.rtyvZ.kitties.ui.sendPhoto.SendCatService
import com.github.rtyvZ.kitties.ui.sendPhoto.TakePhotoActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private var currentSelectedItem = -1
    private var isRotateFab = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        navigation.setOnNavigationItemSelectedListener { item ->
            if (item.itemId == currentSelectedItem) {
                return@setOnNavigationItemSelectedListener false
            }

            when (item.itemId) {

                R.id.list_kitties -> replaceFragment(RandomCatsFragment())

                R.id.favorite_cats -> replaceFragment(FavoriteCatsFragment())

                R.id.my_cats -> replaceFragment(MyCatFragment())

                else -> {
                }
            }

            currentSelectedItem = item.itemId

            return@setOnNavigationItemSelectedListener true
        }

        navigation.selectedItemId = R.id.list_kitties

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
                    CAMERA_REQUEST_CODE
                )
            } else {
                rotateFab(randomCatFab)

                Intent().also {
                    it.type = "image/jpg"
                    it.action = Intent.ACTION_PICK
                    startActivityForResult(Intent.createChooser(it, "select picture"), PICK_IMAGE)
                }
            }
        }

        RotateFabAnimation.init(takeAPhotoFab)
        RotateFabAnimation.init(selectPhoto)
    }


    private fun replaceFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.content_container, fragment)
        transaction.addToBackStack(fragment::class.java.canonicalName)
        transaction.commit()
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
        if (requestCode == TakePhotoActivity.ACTIVITY_RESULT_CODE && resultCode == RESULT_FIRST_USER) {
            data?.let {
                val resultData = it.getStringExtra(Strings.IntentExtras.EXTRA_SEND_UPLOAD)
                resultData?.let { toastMessage ->
                    Toast.makeText(this, toastMessage, Toast.LENGTH_LONG).show()
                }
            }
        }

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            data?.dataString?.let {
                startService(getPathFromUri(it.toUri()))
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

    //todo move to another place
    //todo escape deprecated methods
    private fun getPathFromUri(uri: Uri): String? {
        val cursor: Cursor?
        val columnIndexID: Int
        val projection = arrayOf(DATA)
        var path: String? = ""
        cursor = contentResolver.query(uri, projection, null, null, null)
        cursor?.let {
            it.moveToFirst()
            columnIndexID = cursor.getColumnIndexOrThrow(DATA)
            path = cursor.getString(columnIndexID)
        }
        cursor?.close()
        return path
    }

    companion object {
        const val CAMERA_REQUEST_CODE = 100
        const val ACTIVITY_RESULT_CODE = 1
        const val PICK_IMAGE = 1
        const val DATA = "_data"
    }
}