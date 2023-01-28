package com.codecoy.fitnesskiodkapp.models

import com.google.gson.annotations.SerializedName

data class BookingResponse (
    @SerializedName("status"  ) var status  : Boolean? = null,
    @SerializedName("message" ) var message : String?  = null
)