package com.github.rtyvZ.kitties.auth

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.github.rtyvZ.kitties.R
import com.github.rtyvZ.kitties.extentions.hide
import com.github.rtyvZ.kitties.extentions.show
import com.github.rtyvZ.kitties.ui.main.MainActivity
import com.google.android.material.snackbar.Snackbar
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.launch_activity.*
import javax.inject.Inject

class LaunchActivity : AppCompatActivity(R.layout.launch_activity) {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var sessionStorage: ProvideUserContract
    private lateinit var viewModel: LaunchViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory).get(LaunchViewModel::class.java)
        progress.show()

        if (sessionStorage.hasSession()) {
            progress.hide()
            startMainActivity()
        } else {
            stateLaunch.text = this.getString(R.string.auth)
            viewModel.requestUid()
        }

        viewModel.getUserUid.observe(this, {
            progress.hide()
            stateLaunch.text = this.getString(R.string.success)
            if (!sessionStorage.hasSession())
                sessionStorage.saveSession(UserSession(it))
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
    }

    private fun startMainActivity() {
        val intent = Intent(applicationContext, MainActivity::class.java)
        finish()
        startActivity(intent)
    }
}