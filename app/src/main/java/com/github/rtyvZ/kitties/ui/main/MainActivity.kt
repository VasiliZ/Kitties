package com.github.rtyvZ.kitties.ui.main

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.github.rtyvZ.kitties.R
import com.github.rtyvZ.kitties.common.animations.RotateFabAnimation
import com.github.rtyvZ.kitties.ui.favoriteCats.FavoriteCatsFragment
import com.github.rtyvZ.kitties.ui.imageCats.RandomCatsFragment
import com.github.rtyvZ.kitties.ui.myCat.MyCatFragment
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
        if (requestCode == TakePhotoActivity.ACTIVITY_RESULT_CODE && resultCode == RESULT_OK) {
            data?.let {
                val resultData = it.getStringExtra("result")
                resultData?.let { toastMessage ->
                    Toast.makeText(this, toastMessage, Toast.LENGTH_LONG).show()
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    companion object {
        const val CAMERA_REQUEST_CODE = 100
        const val ACTIVITY_RESULT_CODE = 1
    }
}