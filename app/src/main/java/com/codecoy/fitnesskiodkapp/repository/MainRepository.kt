package com.codecoy.fitnesskiodkapp.repository

import android.util.Log
import com.codecoy.fitnesskiodkapp.MyApp
import com.codecoy.fitnesskiodkapp.models.EquipmentResponse
import com.codecoy.fitnesskiodkapp.retrofit.RetrofitAPI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject

class MainRepository @Inject constructor(private val retrofitService: RetrofitAPI) {
    fun checkUser(email:String,pass:String)  = retrofitService.checkUser(email,pass)


    fun getEquipmentsDetail()  = retrofitService.getEquipmentsDetail()



    suspend fun getEquipmentsDetail2() {
        //    recomendedEquipment.postValue(APIResponse.Loading)
       CoroutineScope(Dispatchers.IO ).launch {
            val response = retrofitService.getEquipmentsDetail2()
            withContext(Dispatchers.IO) {
                if (response.isSuccessful) {
                    MyApp.equipmentList = response.body()!!.data
                } else {
                    MyApp.equipmentList = null
                }
            }
        }
    }


    suspend fun getClassDetail(userID: Int,ctgr_ID: Int)  = retrofitService.getClassDetail(userID,ctgr_ID)

    suspend fun getClassCategories()  = retrofitService.getClassCategories()


    suspend fun getQRImage()  = retrofitService.getQRImage()


    suspend fun getRecomendedEquipments(userID:Int , eqpID:Int)  = retrofitService.getRecomendedEquipments(userID,eqpID)



    suspend fun reviewClass(id:Int,classId:Int,review:String,difficulty:Int,instructor:Int)  = retrofitService.reviewClass(id,classId,review,difficulty,instructor)



    suspend fun bookSession(id:Int,classId:Int,name:String,email:String,phone:String,type:String)  = retrofitService.bookSession(id,classId,name,email,phone,type)


    suspend fun splashData()  = retrofitService.splashData()

}