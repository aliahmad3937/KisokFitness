package com.codecoy.fitnesskiodkapp.callbacks

import com.codecoy.fitnesskiodkapp.models.Data

interface ReplaceFragment {
    fun showClassDisplayFragment(item: Data)

    fun showEquipmentDisplayFragment(id:Int,clasVideoPath:String? , eqpVideoPath:String? , name:String , desc:String)
}