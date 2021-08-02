package com.jar.demo.base

import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.jar.demo.R

/**
 * Parent class for all activity classes.
 */
abstract class BaseActivity : AppCompatActivity(), IBaseActionListener {

    private var alertDialogCancellable: AlertDialog? = null

    /**
     * Show toast using given message and toast length.
     * This gets called mostly from fragments.
     */
    override fun showToast(message: String, length: Int) {
        Toast.makeText(this, message, length).show()
    }

    /**
     * Shows cancellable progress dialog.
     */
    override fun showLoadingCancellable() {
        if (alertDialogCancellable == null) {
            alertDialogCancellable = AlertDialog.Builder(this).apply {
                setView(
                    LayoutInflater.from(this@BaseActivity).inflate(R.layout.layout_loading, null)
                )
                setCancelable(true)
            }.create()
        }
        alertDialogCancellable?.show()
    }

    /**
     * Hides cancellable progress dialog.
     */
    override fun hideLoadingCancellable() {
        if (alertDialogCancellable != null) {
            alertDialogCancellable?.dismiss()
        }
    }
}