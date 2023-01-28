package com.codecoy.fitnesskiodkapp.ui

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.codecoy.fitnesskiodkapp.MainActivity

import com.codecoy.fitnesskiodkapp.MyApp
import com.codecoy.fitnesskiodkapp.R

import com.codecoy.fitnesskiodkapp.databinding.FragmentHomeBinding
import com.codecoy.fitnesskiodkapp.models.APIResponse
import com.codecoy.fitnesskiodkapp.utils.Constants.DELAY_TIME
import com.codecoy.fitnesskiodkapp.viewmodels.MainViewModel

import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class Home : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var context:MainActivity
    private lateinit var viewModel: MainViewModel

    private  val handler = Handler(Looper.getMainLooper())
    private lateinit  var runnable:Runnable

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(context).get(MainViewModel::class.java)


        viewModel.splashResponse.observe(context, Observer {
            when (it) {
                is APIResponse.Loading -> {
                    //  MyApp.showToast(context, "loading")
                }
                is APIResponse.Error -> {
                    //   MyApp.showToast(context, "error")
                }
                is APIResponse.Success<*> -> {
                    //   MyApp.showToast(context, "success")
                    //  response = it.data as SplashResponse
                    Picasso.get()
                        .load(MyApp.logoUrl)
                        .into(binding.imageView)
                 //   binding.textView1.text = MyApp.title

                }
            }
        })

        try {
//            Picasso.get()
//                .load(MyApp.logoUrl)
//                .into(binding.imageView)
       //     binding.textView1.text = MyApp.title
            binding.imageView.loadSvgOrOther(MyApp.logoUrl)


            binding.btnClasses.setOnClickListener {
                //     findNavController().navigate(R.id.action_home2_to_classes)
                MyApp.bottomNav.selectedItemId = R.id.classes
            }

            binding.btnEquipment.setOnClickListener {
                MyApp.bottomNav.selectedItemId = R.id.equipment
            }



            runnable = Runnable {
                MyApp.mainNavController.navigate(R.id.action_global_splash)
            }
            handler.postDelayed(runnable, DELAY_TIME)
        }catch (e:Exception){
            Log.e("Home","error :${e.localizedMessage}")
        }


        return binding.root

    }

    override fun onResume() {
        MyApp.bottomNav.visibility = View.GONE
        if(!handler.hasCallbacks(runnable)){
            handler.postDelayed(runnable,DELAY_TIME)
        }
        super.onResume()
    }


    override fun onPause() {
        super.onPause()
        if(handler.hasCallbacks(runnable)){
            handler.removeCallbacks(runnable)
        }
    }

    override fun onAttach(contex: Context) {
        super.onAttach(contex)
       context = contex as MainActivity
    }


}