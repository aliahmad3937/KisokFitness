package com.codecoy.fitnesskiodkapp.models

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("status"  )     var status  :    Boolean? = null,
    @SerializedName("message" )     var message :    String?  = null,
    @SerializedName("User Data" )   var  user:User? = User()
){
    data class User(
    @SerializedName("id"                ) var id              : Int?    = null,
    @SerializedName("name"              ) var name            : String? = null,
    @SerializedName("email"             ) var email           : String? = null,
    @SerializedName("email_verified_at" ) var emailVerifiedAt : String? = null,
    @SerializedName("created_at"        ) var createdAt       : String? = null,
    @SerializedName("updated_at"        ) var updatedAt       : String? = null,
    @SerializedName("role"              ) var role            : String? = null
    )
}

