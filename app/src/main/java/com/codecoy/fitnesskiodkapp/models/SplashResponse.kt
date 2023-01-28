package com.codecoy.fitnesskiodkapp.models

import com.google.gson.annotations.SerializedName

data class SplashResponse(
    @SerializedName("status"  ) var status  : Boolean?        = null,
    @SerializedName("message" ) var message : String?         = null,
    @SerializedName("data"    ) var data    : Data            = Data()
)
{
    data class Data (
        @SerializedName("id"                ) var id              : Int?    = null,
        @SerializedName("splash_video_path" ) var splashVideoPath : String? = null,
        @SerializedName("qr_img"            ) var qr_img          : String? = null,
        @SerializedName("logo"              ) var logo            : String? = null,
        @SerializedName("logo_text"         ) var logoText        : String? = null,
        @SerializedName("created_at"        ) var createdAt       : String? = null,
        @SerializedName("updated_at"        ) var updatedAt       : String? = null
    )
}