package com.codecoy.fitnesskiodkapp.viewmodels

import android.content.Context
import android.util.Log

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.codecoy.fitnesskiodkapp.MyApp
import com.codecoy.fitnesskiodkapp.models.*
import com.codecoy.fitnesskiodkapp.repository.MainRepository
import com.codecoy.fitnesskiodkapp.ui.SplashScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject


// @HiltViewModel will make models to be
// created using Hilt's model factory
// @Inject annotation used to inject all
// dependencies to view model class
@HiltViewModel
class MainViewModel @Inject constructor(private val repository: MainRepository)  : ViewModel() {

     var loginResponse = MutableLiveData<APIResponse>()
     var equipmentResponse = MutableLiveData<APIResponse>()
     var classResponse = MutableLiveData<APIResponse>()
     var classCategoryResponse = MutableLiveData<APIResponse>()
     var recomendedEquipment = MutableLiveData<APIResponse>()
     var splashResponse = MutableLiveData<APIResponse>()
     var qrResponse = MutableLiveData<APIResponse>()
     var reviewResponse = MutableLiveData<APIResponse>()
     var bookingResponse = MutableLiveData<APIResponse>()


    var job: Job? = null
    val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        recomendedEquipment.postValue(APIResponse.Error("Exception handled: ${throwable.localizedMessage}"))

    }


    init {
        loginResponse.postValue(APIResponse.Starting)
        equipmentResponse.postValue(APIResponse.Starting)
        classResponse.postValue(APIResponse.Starting)
        classCategoryResponse.postValue(APIResponse.Starting)
        recomendedEquipment.postValue(APIResponse.Starting)
        reviewResponse.postValue(APIResponse.Starting)
        bookingResponse.postValue(APIResponse.Starting)
    }



    fun checkUser(email:String,pass:String) {
        loginResponse.postValue(APIResponse.Loading)
        val response:Call<LoginResponse> = repository.checkUser(email,pass)

        response.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    loginResponse.postValue(APIResponse.Success(response.body()))
                }
            }
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                loginResponse.postValue(APIResponse.Error(t.localizedMessage))
            }
        })
    }

    fun bookSession(id:Int,classId:Int,name:String,email:String,phone:String,type:String,context: Context){
      //  bookingResponse.postValue(APIResponse.Loading)
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = repository.bookSession(id,classId,name,email,phone,type)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    Log.v("TAG","booking success")
               //     bookingResponse.postValue(APIResponse.Success(response.body()))
                //  MyApp.showToast(context, "session booked successfully!")
                } else {
                //    bookingResponse.postValue(APIResponse.Error("Error : ${response.message()} "))
                //    MyApp.showToast(context, "Error : ${response.message()} ")
                    Log.v("TAG","booking error")
                }
            }
        }
    }

    fun getEquipmentsDetail() {
        equipmentResponse.postValue(APIResponse.Loading)
        val response:Call<EquipmentResponse> = repository.getEquipmentsDetail()

        response.enqueue(object : Callback<EquipmentResponse> {
            override fun onResponse(call: Call<EquipmentResponse>, response: Response<EquipmentResponse>) {
                if (response.isSuccessful) {
                    equipmentResponse.postValue(APIResponse.Success(response.body()))
                }
            }
            override fun onFailure(call: Call<EquipmentResponse>, t: Throwable) {
                equipmentResponse.postValue(APIResponse.Error(t.localizedMessage))
            }
        })
    }


    fun getClassDetail(userID: Int,ctgr_ID: Int) {
        MyApp.selectedCategory = ctgr_ID
        classResponse.postValue(APIResponse.Loading)
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = repository.getClassDetail(userID,ctgr_ID)
            var resp:EquipmentResponse = EquipmentResponse()
            withContext(Dispatchers.Main) {
                if(response.code() == 200){
                    classResponse.postValue(APIResponse.Success(response.body()))
                }else{
                   resp.data = arrayListOf()
                   // classResponse.postValue(APIResponse.Error("Error : ${response.message()} "))
                    classResponse.postValue(APIResponse.Success(resp))
                }

            }
        }
    }




    fun getClassCategories() {
        classCategoryResponse.postValue(APIResponse.Loading)
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = repository.getClassCategories()
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    classCategoryResponse.postValue(APIResponse.Success(response.body()))
                } else {
                    classCategoryResponse.postValue(APIResponse.Error("Error : ${response.message()} "))

                }
            }
        }
    }



    fun getRecomendedEquipments(userID:Int , eqpID:Int) {
        recomendedEquipment.postValue(APIResponse.Loading)
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = repository.getRecomendedEquipments(userID , eqpID)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    recomendedEquipment.postValue(APIResponse.Success(response.body()))
                } else {
                    recomendedEquipment.postValue(APIResponse.Error("Error : ${response.message()} "))

                }
            }
        }

    }





    fun reviewClass(id:Int,classId:Int,review:String,difficulty:Int,instructor:Int) {
        Log.v("TAG","id :$id -- classId:$classId -- review: $review -- diffic : $difficulty -- instruct:$instructor")
       // reviewResponse.postValue(APIResponse.Loading)
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = repository.reviewClass(id, classId, review, difficulty, instructor)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    Log.v("TAG","rating success")
                 //   reviewResponse.postValue(APIResponse.Success(response.body()))
                } else {
                  //  reviewResponse.postValue(APIResponse.Error("Error : ${response.message()} "))
                    Log.v("TAG","rating error")

                }
            }
        }

    }


    fun splashData(){
        splashResponse.postValue(APIResponse.Loading)
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = repository.splashData()
            lateinit var splashModel:SplashResponse
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    splashModel = response.body() as SplashResponse
                    MyApp.logoUrl = MyApp.getPath(splashModel.data.logo.toString())
                    MyApp.videoUrl = MyApp.getPath(splashModel.data.splashVideoPath.toString())

                    splashResponse.postValue(APIResponse.Success(response.body()))
                  //  MyApp.showToast(context, "session booked successfully!")
                } else {
                    splashResponse.postValue(APIResponse.Error("Api Error  ${response.message()} "))
                //   MyApp.showToast(context, "Error : ${response.message()} ")
                   // Log.v("TAG","booking error")
                }
            }
        }
    }

    fun getQRImage() {
        qrResponse.postValue(APIResponse.Loading)
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = repository.getQRImage()
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    qrResponse.postValue(APIResponse.Success(response.body()))
                    MyApp.qrImage = MyApp.getPath((response.body() as SplashResponse).data.qr_img.toString())

                    Log.v("TAG1","qr image :"+MyApp.qrImage)
                } else {
                    qrResponse.postValue(APIResponse.Error("Error : ${response.message()} "))

                }
            }
        }
    }



    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }



}
