package com.codecoy.fitnesskiodkapp.ui

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import com.codecoy.fitnesskiodkapp.MainActivity
import com.codecoy.fitnesskiodkapp.MyApp
import com.codecoy.fitnesskiodkapp.R
import com.codecoy.fitnesskiodkapp.databinding.FragmentLoginBinding
import com.codecoy.fitnesskiodkapp.models.APIResponse
import com.codecoy.fitnesskiodkapp.models.LoginResponse
import com.codecoy.fitnesskiodkapp.utils.ProgressUtils.hideProgress
import com.codecoy.fitnesskiodkapp.utils.ProgressUtils.hudProgress
import com.codecoy.fitnesskiodkapp.utils.ToastUtils
import com.codecoy.fitnesskiodkapp.utils.ToastUtils.showToast
import com.codecoy.fitnesskiodkapp.viewmodels.MainViewModel
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint


//Calendar c = Calendar.getInstance();
//c.setTime(startDate);
//c.add(Calendar.DATE, 30);
//Date expDate = c.getTime();
//
//if(startDate.after(expiate)){
//    // Your time expired do your logic here.
//}


@AndroidEntryPoint
class Login : Fragment() {
    private lateinit var binding:FragmentLoginBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var context:MainActivity
   private lateinit var sp: SharedPreferences
    private lateinit var navController: NavController




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false)


       navController = MyApp.mainNavController

        viewModel = ViewModelProvider(context).get(MainViewModel::class.java)


        sp = context.getSharedPreferences("vipexpire", MODE_PRIVATE)


        viewModel.loginResponse.observe(context, Observer {
            when (it) {
                is APIResponse.Loading -> {
                  hudProgress(context)
                }
                is APIResponse.Error -> {
                      hideProgress()
               //     MyApp.showToast(requireContext(), "$it.message")
                    Log.d("TAG","${it.message}")

                }
                is APIResponse.Success<*> -> {
                    hideProgress()
                    //  Log.d(TAG, "onCreate   viewModel.movieList.observe: $it")
                    val response = it.data as LoginResponse
                    if (response.status == true) {
                      MyApp.userId = response.user?.id
                        sp.edit()
                            .putInt("starttime", (System.currentTimeMillis() / 1000).toInt())
                            .putInt("userid",MyApp.userId!!)
                            .apply()
                      navController.navigate(R.id.action_login_to_navigationScreen)
                    }else{
                        ToastUtils.showToast(context,response.message.toString())
                    }
                }
            }
        })

        viewModel.splashResponse.observe(context, Observer {
            when (it) {
                is APIResponse.Success<*> -> {
                    updateUI()
                  //  MyApp.showToast(context,"update")
                }
            }
        })


        binding.run {
            btnLogin.setOnClickListener {
      //       navController.navigate(R.id.action_login_to_navigationScreen)
                if (validation()) {
                    viewModel.checkUser(etUsername.text.toString(), etPassword.text.toString())

                } else {
                    showToast(requireContext(), "please enter Username/Password!")
                }
            }
        }


        return binding.root
    }

    private fun validation(): Boolean {
        return !(binding.etUsername.text.toString().isEmpty() || binding.etPassword.text.toString()
            .isEmpty())
    }

    private fun updateUI() {
//        Picasso.get().load(MyApp.logoUrl).fit().into(binding.imageView)
        binding.imageView.loadSvgOrOther(MyApp.logoUrl)

    }


    override fun onAttach(contex: Context) {
        context = contex as MainActivity

        super.onAttach(context)
    }

//    override fun onResume() {
//        val startTime: Int = sp.getInt("starttime", 0)
//        val currentTime = (System.currentTimeMillis() / 1000).toInt()
//        val timeofVip = currentTime - startTime //calculate the time of his VIP-being time
//
//      //  Log.v("TAG3","146 start timr :"+startTime   +" currenttime :"+currentTime)
//        if(startTime != 0) {
//         //   Log.v("TAG3","148")
//            if (timeofVip >= 2592000) //2592000 is 30 days in seconds
//            {
//              sp.edit().clear().apply()
//           //     Log.v("TAG3","152 vip :"+timeofVip)
//            } else {
//              //  Log.v("TAG3","154")
//                MyApp.userId = sp.getInt("userid",0)
//                navController.navigate(R.id.action_login_to_navigationScreen)
//            }
//        }else{
//          //  Log.v("TAG3","157")
//        }
//
//        super.onResume()
//    }

}