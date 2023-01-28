package com.codecoy.fitnesskiodkapp.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.codecoy.fitnesskiodkapp.MyApp
import com.codecoy.fitnesskiodkapp.R
import com.codecoy.fitnesskiodkapp.adapters.EquipmentAdapter
import com.codecoy.fitnesskiodkapp.callbacks.ReplaceFragment
import com.codecoy.fitnesskiodkapp.databinding.FragmentEquipmentBinding
import com.codecoy.fitnesskiodkapp.models.APIResponse
import com.codecoy.fitnesskiodkapp.models.Data
import com.codecoy.fitnesskiodkapp.models.Equipment
import com.codecoy.fitnesskiodkapp.models.EquipmentResponse
import com.codecoy.fitnesskiodkapp.utils.Constants
import com.codecoy.fitnesskiodkapp.utils.Constants.DELAY_TIME
import com.codecoy.fitnesskiodkapp.utils.ProgressUtils
import com.codecoy.fitnesskiodkapp.utils.ProgressUtils.hudProgress
import com.codecoy.fitnesskiodkapp.utils.ToastUtils.showToast
import com.codecoy.fitnesskiodkapp.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Equipment : Fragment() , ReplaceFragment{
    private lateinit var binding: FragmentEquipmentBinding
    private var list: ArrayList<Equipment> = ArrayList()
    private lateinit var viewModel: MainViewModel
    private  val handler = Handler(Looper.getMainLooper())
    private lateinit  var runnable:Runnable



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentEquipmentBinding.inflate(inflater, container, false)


        viewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)


        if(viewModel.equipmentResponse.value is APIResponse.Starting){
          viewModel.getEquipmentsDetail()
        }





        viewModel.equipmentResponse.observe(requireActivity(), Observer {
            when (it) {
                is APIResponse.Loading -> {
                    hudProgress(requireActivity())
                }
                is APIResponse.Error -> {
                    ProgressUtils.hideProgress()
                    showToast(requireContext(), "$it.message")
                    Log.d("TAG","${it.message}")

                }
                is APIResponse.Success<*> -> {
                    ProgressUtils.hideProgress()
                    //  Log.d(TAG, "onCreate   viewModel.movieList.observe: $it")
                   val response = it.data as EquipmentResponse
               //     MyApp.showToast(requireContext(), "success message")

                    // added data from arraylist to adapter class.
                    val adapter = EquipmentAdapter(response.data, requireContext() , this)

                    // setting grid layout manager to implement grid view.
                    // in this method '2' represents number of columns to be displayed in grid view.

                    // setting grid layout manager to implement grid view.
                    // in this method '2' represents number of columns to be displayed in grid view.
                    val layoutManager = GridLayoutManager(requireContext(), 2)

                    // at last set adapter to recycler view.

                    // at last set adapter to recycler view.
                    binding.recycler.setLayoutManager(layoutManager)
                    binding.recycler.setAdapter(adapter)
                }
            }
        })


        runnable = Runnable{
            MyApp.mainNavController.navigate(R.id.action_global_splash)
        }
        handler.postDelayed(runnable, Constants.DELAY_TIME)

        return binding.root
    }

    override fun onResume() {
        MyApp.bottomNav.visibility = View.VISIBLE

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



    override fun showClassDisplayFragment(
  item: Data
    ) {

    }

    override fun showEquipmentDisplayFragment(
        id: Int,
        clasVideoPath: String?,
        eqpVideoPath: String?,
        name: String,
        desc: String
    ) {
        findNavController().navigate(
            R.id.action_equipment_to_equipmentDisplay,
            Bundle().apply {
                putInt(Constants.ARG_PARAM1, id)
                putString(Constants.ARG_PARAM2, clasVideoPath)
                putString(Constants.ARG_PARAM3, eqpVideoPath)
                putString(Constants.ARG_PARAM4, name)
                putString(Constants.ARG_PARAM5, desc)
            }
        )

    }


}