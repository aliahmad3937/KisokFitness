package com.codecoy.fitnesskiodkapp.models

import com.google.gson.annotations.SerializedName

data class ReviewResponse (
    @SerializedName("status"  ) var status  : Boolean? = null,
    @SerializedName("message" ) var message : String?  = null,
    @SerializedName("data"    ) var data    : Data?    = Data()
){
    data class Data (
        @SerializedName("user_id"    ) var userId    : String? = null,
        @SerializedName("class_id"   ) var classId   : String? = null,
        @SerializedName("user_name"  ) var userName  : String? = null,
        @SerializedName("user_email" ) var userEmail : String? = null,
        @SerializedName("phone"      ) var phone     : String? = null,
        @SerializedName("class_type" ) var classType : String? = null,
        @SerializedName("updated_at" ) var updatedAt : String? = null,
        @SerializedName("created_at" ) var createdAt : String? = null,
        @SerializedName("id"         ) var id        : Int?    = null

    )
}