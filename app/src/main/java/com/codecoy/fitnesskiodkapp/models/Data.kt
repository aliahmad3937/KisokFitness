package com.codecoy.fitnesskiodkapp.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Data (
    @SerializedName("id"             ) var id           : Int?    = null,
    @SerializedName("eqp_name"       ) var eqpName      : String? = null,
    @SerializedName("category_title" ) var category_title: String? = null,
    @SerializedName("clas_qr_img"    ) var qr_img          : String? = null,
    @SerializedName("video_thumb_img") var video_thumb_img : String? = null,
    @SerializedName("icon_img"       ) var icon_img      : String? = null,
    @SerializedName("eqp_img"        ) var eqpImg       : String? = null,
    @SerializedName("eqp_desc"       ) var eqpDesc      : String? = null,
    @SerializedName("desc"           ) var desc      : String? = null,
    @SerializedName("eqp_video_path" ) var eqpVideoPath : String? = null,
    @SerializedName("created_at"     ) var createdAt    : String? = null,
    @SerializedName("updated_at"     ) var updatedAt    : String? = null,
    @SerializedName("eqp_id"          ) var eqpId         : String? = null,
    @SerializedName("clas_name"       ) var clasName      : String? = null,
    @SerializedName("workout_level"   ) var workoutLevel  : String? = null,
    @SerializedName("trainer_name"    ) var trainerName   : String? = null,
    @SerializedName("clas_img"        ) var clasImg       : String? = null,
    @SerializedName("clas_video_path" ) var clasVideoPath : String? = null,
    @SerializedName("class_review") val class_review : String?      = null,
    @SerializedName("difficulty_rating") val difficulty_rating : Int?= null,
    @SerializedName("instructor_rating") val instructor_rating : Int?= null


) : Serializable