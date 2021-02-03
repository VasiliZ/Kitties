package com.github.rtyvZ.kitties.auth

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.github.rtyvZ.kitties.R
import com.github.rtyvZ.kitties.extentions.hide
import com.github.rtyvZ.kitties.extentions.show
import com.github.rtyvZ.kitties.ui.main.MainActivity
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.launch_activity.*

@AndroidEntryPoint
class LaunchActivity : AppCompatActivity(R.layout.launch_activity) {

    private val viewModel: LaunchViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        progress.show()
        viewModel.getDataForAuth()

        viewModel.getUserUid.observe(this, {
            progress.hide()
            stateLaunch.text = this.getString(R.string.success)
            startMainActivity()
        })

        viewModel.error.observe(this, {
            it?.let { throwable ->
                val charSequence: CharSequence = throwable.message.toString()
                progress.hide()
                stateLaunch.text = this.getString(R.string.empty)
                Snackbar.make(progress, charSequence, Snackbar.LENGTH_LONG).show()
            }
        })

        viewModel
    }

    private fun startMainActivity() {
        val intent = Intent(applicationContext, MainActivity::class.java)
        finish()
        startActivity(intent)
    }
}