package com.codecoy.fitnesskiodkapp.ui

import android.app.Dialog

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.Group
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.codecoy.fitnesskiodkapp.MainActivity
import com.codecoy.fitnesskiodkapp.MyApp
import com.codecoy.fitnesskiodkapp.R
import com.codecoy.fitnesskiodkapp.adapters.ClassCategoryAdapter
import com.codecoy.fitnesskiodkapp.adapters.CustomDropDownAdapter
import com.codecoy.fitnesskiodkapp.databinding.FragmentBookedTrainingBinding
import com.codecoy.fitnesskiodkapp.models.APIResponse
import com.codecoy.fitnesskiodkapp.models.Data
import com.codecoy.fitnesskiodkapp.models.EquipmentResponse
import com.codecoy.fitnesskiodkapp.utils.Constants
import com.codecoy.fitnesskiodkapp.utils.Constants.BASE_URL
import com.codecoy.fitnesskiodkapp.utils.Constants.DELAY_TIME
import com.codecoy.fitnesskiodkapp.utils.ToastUtils.showToast
import com.codecoy.fitnesskiodkapp.viewmodels.MainViewModel
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_review.*


@AndroidEntryPoint
class BookedTraining : Fragment() {

    private lateinit var item: Data


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            item = it.getSerializable(Constants.ARG_PARAM1) as Data
        }
    }


   private lateinit var binding: FragmentBookedTrainingBinding
   private lateinit var viewModel: MainViewModel
   private lateinit var context:MainActivity
    private  val handler = Handler(Looper.getMainLooper())
    private lateinit  var runnable:Runnable

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =  FragmentBookedTrainingBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(context).get(MainViewModel::class.java)
//        Picasso.get().load(MyApp.logoUrl).fit().into(binding.imageView)
        binding.imageView.loadSvgOrOther(MyApp.logoUrl)
        binding.logo.loadSvgOrOther(MyApp.logoUrl)
        Picasso.get().load("${BASE_URL}${item.qr_img}").fit().into(binding.qrImage)


        if(viewModel.classCategoryResponse.value is APIResponse.Starting){
            viewModel.getClassCategories()
        }

        binding.ok.setOnClickListener {
            findNavController().navigate(R.id.action_global_home2)
        }

        viewModel.classCategoryResponse.observe(context, Observer {
            when (it) {
                is APIResponse.Loading -> {
                    //  MyApp.hudProgress(requireActivity())
                }
                is APIResponse.Error -> {
//                    if (hud != null && hud.isShowing)
//                        hud.dismiss()
//                    MyApp.showToast(requireContext(), "$it.message")
//                    Log.d("TAG","${it.message}")

                }
                is APIResponse.Success<*> -> {
//                    if (hud != null && hud.isShowing) {
//                        hud.dismiss()
//                    }

                    //  Log.d(TAG, "onCreate   viewModel.movieList.observe: $it")
                    val response = it.data as EquipmentResponse
                    //   MyApp.showToast(requireContext(), "success message")



            val customDropDownAdapter = CustomDropDownAdapter(context, response.data)
            binding.spinner.adapter = customDropDownAdapter

                }
            }
        })


        binding.run {
            button.setOnClickListener {
                //       navController.navigate(R.id.action_login_to_navigationScreen)
                if (validation()) {
                //    MyApp.showToast(context , MyApp.categoryType!!)
                    viewModel.bookSession(
                        MyApp.userId!!,
                        item.id!!,
                        etName.text.toString(),
                        etEmail.text.toString(),
                        etPhone.text.toString(),
                        MyApp.categoryType,
                        context
                    )
              updateUi()
                } else {
                    showToast(requireContext(), "please fill empty fields")
                }
            }

        }


        binding.back.setOnClickListener{
            findNavController().navigate(R.id.action_global_home2)
        }

        runnable = Runnable{
            MyApp.mainNavController.navigate(R.id.action_global_splash)
        }
        handler.postDelayed(runnable,DELAY_TIME)


        return binding.root
    }

    override fun onResume() {
        if(!handler.hasCallbacks(runnable)){
            handler.postDelayed(runnable,DELAY_TIME)
        }
        super.onResume()
    }


    override fun onPause() {
        super.onPause()
        Log.v("TAG", "return from review")
        if(handler.hasCallbacks(runnable)){
            handler.removeCallbacks(runnable)
        }
    }


    override fun onAttach(contex: Context) {
        context = contex as MainActivity

        super.onAttach(context)
    }


    fun showDialoge() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.bookin_dialog)


        val ok: Button = dialog.findViewById(R.id.ok) as Button
        val group: Group = dialog.findViewById(R.id.btn_group) as Group
        val title: TextView = dialog.findViewById(R.id.title) as TextView



        title.text = "Session Booked!\nThank you for your time."
        group.visibility = View.GONE
        ok.visibility = View.VISIBLE


        ok.setOnClickListener {
            dialog.dismiss()
            findNavController().navigate(R.id.action_global_home2)
        }
        dialog.show()
    }
    private fun updateUi() {

        //   MyApp.showToast(this@BookedTraining, "session Booked successfully!")
        //   finish()
        with(binding) {
            etName.setText("")
            etEmail.setText("")
            etPhone.setText("")

        }

        binding.layoutBookedTraining.visibility = View.GONE
        binding.layoutThankU.visibility = View.VISIBLE


      //  showDialoge()

    }

    private fun validation(): Boolean {
        return !(binding.etName.text.toString().isEmpty() || binding.etEmail.text.toString()
            .isEmpty() || binding.etPhone.text.toString().isEmpty())
    }

}