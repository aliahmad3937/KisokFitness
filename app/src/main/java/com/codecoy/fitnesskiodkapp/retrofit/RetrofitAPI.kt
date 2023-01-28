package com.codecoy.fitnesskiodkapp.retrofit

import com.codecoy.fitnesskiodkapp.models.*
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded

import retrofit2.http.GET

import retrofit2.http.POST

import retrofit2.http.Query
import java.util.concurrent.TimeUnit


interface RetrofitAPI {

    @FormUrlEncoded
    @POST("login")
    fun checkUser(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>



    @FormUrlEncoded
    @POST("ratings")
    suspend fun reviewClass(
        @Field("user_id") email: Int,
        @Field("class_id") classId: Int,
        @Field("class_review") review: String,
        @Field("difficulty_rating") diffi: Int,
        @Field("instructor_rating") instruc: Int,
    ): Response<ReviewResponse>


    @FormUrlEncoded
    @POST("booking_details")
    suspend fun bookSession(
        @Field("user_id") id: Int,
        @Field("class_id") classId: Int,
        @Field("user_name") name: String,
        @Field("user_email") email: String,
        @Field("phone") phn: String,
        @Field("class_type") type: String,
    ): Response<BookingResponse>


    @GET("eqp_details")
    fun getEquipmentsDetail(): Call<EquipmentResponse>


    @GET("splash_screen")
    suspend fun splashData():  Response<SplashResponse>

    @GET("eqp_details")
    suspend fun getEquipmentsDetail2(): Response<EquipmentResponse>

    @GET("category_classes")
    suspend fun getClassDetail(
        @Query("user_id") user_id: Int,
        @Query("cat_id") ctgr_id: Int
    ): Response<EquipmentResponse>


    @GET("category")
    suspend fun getClassCategories(): Response<EquipmentResponse>

    @GET("qr_image")
    suspend fun getQRImage(): Response<SplashResponse>

    @GET("recomended_video")
    suspend fun getRecomendedEquipments(
        @Query("user_id") user_id:Int,
        @Query("eqp_id") eqp_id:Int
    ): Response<EquipmentResponse>


    companion object {
        var retrofitAPI: RetrofitAPI? = null









        @Synchronized
        fun getInstance(): RetrofitAPI {
            if (retrofitAPI == null) {
                val okHttpClient = OkHttpClient.Builder()
                    .connectTimeout(7, TimeUnit.MINUTES)
                    .readTimeout(7, TimeUnit.MINUTES)
                    .writeTimeout(7, TimeUnit.MINUTES)
                    .build()
                //    AndroidNetworking.initialize(getApplicationContext());

                //    AndroidNetworking.initialize(getApplicationContext());
                val gson = GsonBuilder()
                    .setLenient()
                    .create()

//                    var interceptor:HttpLoggingInterceptor  = HttpLoggingInterceptor()
//                    interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//                    OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
//
//                    retrofit = new Retrofit.Builder()
//                        .baseUrl("http://www.example.com/")
//                        .addConverterFactory(GsonConverterFactory.create())
//                        .client(okHttpClient).build();


                val retrofit = Retrofit.Builder()
                    .baseUrl( "https://packfit.io/api/")
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build()
                retrofitAPI = retrofit.create(RetrofitAPI::class.java)
            }
            return retrofitAPI!!
        }

    }

}