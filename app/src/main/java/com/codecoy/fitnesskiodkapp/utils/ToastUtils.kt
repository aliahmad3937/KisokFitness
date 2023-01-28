package com.codecoy.fitnesskiodkapp.utils

import android.content.Context
import android.widget.Toast

object ToastUtils {
    @JvmStatic
    fun showToast(context: Context, message: String) {
        Toast.makeText(context, "" + message, Toast.LENGTH_LONG).show()
    }

}