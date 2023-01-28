package com.codecoy.fitnesskiodkapp.utils

import android.content.Context
import com.codecoy.fitnesskiodkapp.MyApp
import com.codecoy.fitnesskiodkapp.R
import com.kaopiz.kprogresshud.KProgressHUD

object ProgressUtils {

    private var hud: KProgressHUD? = null
    @JvmStatic
    fun hudProgress(context: Context,msg:String=context.getString(R.string.please_wait)) {
        hud = KProgressHUD.create(context)
            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
            .setLabel(msg)
            .setCancellable(true)
            .setAnimationSpeed(2)
            .setDimAmount(0.5f)
            .show()
    }


    @JvmStatic
    fun hideProgress(){
        hud?.let {
            it.dismiss()
            hud = null
        }
    }




}