package com.codecoy.fitnesskiodkapp.ui

import android.content.Context
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
import androidx.recyclerview.widget.LinearLayoutManager
import com.codecoy.fitnesskiodkapp.MainActivity
import com.codecoy.fitnesskiodkapp.MyApp
import com.codecoy.fitnesskiodkapp.R
import com.codecoy.fitnesskiodkapp.adapters.ClassAdapter
import com.codecoy.fitnesskiodkapp.adapters.ClassCategoryAdapter
import com.codecoy.fitnesskiodkapp.callbacks.ReplaceFragment
import com.codecoy.fitnesskiodkapp.callbacks.UpdateClasses
import com.codecoy.fitnesskiodkapp.databinding.FragmentClassesBinding
import com.codecoy.fitnesskiodkapp.models.APIResponse
import com.codecoy.fitnesskiodkapp.models.Class
import com.codecoy.fitnesskiodkapp.models.Data
import com.codecoy.fitnesskiodkapp.models.EquipmentResponse
import com.codecoy.fitnesskiodkapp.utils.Constants
import com.codecoy.fitnesskiodkapp.utils.Constants.DELAY_TIME
import com.codecoy.fitnesskiodkapp.utils.ToastUtils.showToast
import com.codecoy.fitnesskiodkapp.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class Classes : Fragment() , ReplaceFragment , UpdateClasses {
   private lateinit var binding: FragmentClassesBinding
   private var list: ArrayList<Class> = ArrayList()
   private lateinit var viewModel: MainViewModel
   private lateinit var context: MainActivity
   private  var adapter: ClassCategoryAdapter? = null
   private  val handler = Handler(Looper.getMainLooper())
   private lateinit  var runnable:Runnable

    // viewModels() delegate used to get
    // by view models will automatically construct the viewmodels using Hilt
  //  private val viewModel:MainViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentClassesBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(context).get(MainViewModel::class.java)

        runnable = Runnable{
            MyApp.mainNavController.navigate(R.id.action_global_splash)
        }
        handler.postDelayed(runnable,DELAY_TIME)

        if(viewModel.classResponse.value is APIResponse.Starting){
            viewModel.getClassDetail(MyApp.userId!!,0)
        }

        if(viewModel.classCategoryResponse.value is APIResponse.Starting){
            viewModel.getClassCategories()
        }


        viewModel.classResponse.observe(context, Observer {
            when (it) {
                is APIResponse.Loading -> {
                    //MyApp.hudProgress(context)
                    binding.progressBar!!.visibility = View.VISIBLE
                    if(!handler.hasCallbacks(runnable)){
                        handler.postDelayed(runnable,DELAY_TIME)
                    }
                }
                is APIResponse.Error -> {
//                    if (hud != null && hud.isShowing)
//                        hud.dismiss()
                    binding.progressBar!!.visibility = View.GONE
                    showToast(requireContext(), "$it.message")
                    Log.d("TAG","${it.message}")
                    if(!handler.hasCallbacks(runnable)){
                        handler.postDelayed(runnable,DELAY_TIME)
                    }

                }
                is APIResponse.Success<*> -> {
//                    if (hud != null && hud.isShowing) {
//                        hud.dismiss()
//                    }

                    binding.progressBar!!.visibility = View.GONE

                    adapter?.notifyDataSetChanged()

                    //  Log.d(TAG, "onCreate   viewModel.movieList.observe: $it")
                     val response = it.data as EquipmentResponse
                 //   MyApp.showToast(requireContext(), "success message")



                    // added data from arraylist to adapter class.

                    // added data from arraylist to adapter class.
                    val adapter = ClassAdapter(response.data, requireContext() , this )

                    // setting grid layout manager to implement grid view.
                    // in this method '2' represents number of columns to be displayed in grid view.

                    // setting grid layout manager to implement grid view.
                    // in this method '2' represents number of columns to be displayed in grid view.
                    val layoutManager = GridLayoutManager(requireContext(), 2)

                    // at last set adapter to recycler view.

                    // at last set adapter to recycler view.
                    binding.recycler.setLayoutManager(layoutManager)
                    binding.recycler.setAdapter(adapter)

                    if(!handler.hasCallbacks(runnable)){
                        handler.postDelayed(runnable,DELAY_TIME)
                    }

                }
            }
        })


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



                    // added data from arraylist to adapter class.

                    // added data from arraylist to adapter class.
                    var list    : ArrayList<Data> = arrayListOf()
                    var data = Data()
                    data.icon_img = ""
                    data.category_title = "All"
                    data.id = 0
                    list.add(
                      data
                    )

                    list.addAll(response.data)

                    adapter = ClassCategoryAdapter(list, requireContext() , this )

                    // setting grid layout manager to implement grid view.
                    // in this method '2' represents number of columns to be displayed in grid view.

                    // setting grid layout manager to implement grid view.
                    // in this method '2' represents number of columns to be displayed in grid view.
                    val layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL ,false)

                    // at last set adapter to recycler view.

                    // at last set adapter to recycler view.
                    binding.recCategory.setLayoutManager(layoutManager)
                    binding.recCategory.setAdapter(adapter)

                }
            }
        })







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

    override fun onAttach(contex: Context) {
        context = contex as MainActivity
        super.onAttach(context)
    }

    override fun showClassDisplayFragment(
       item: Data
    ) {
     findNavController().navigate(
         R.id.action_classes_to_classDisplay,
         Bundle().apply {
             putSerializable(Constants.ARG_PARAM1,item)
         }
     )
    }



    override fun showEquipmentDisplayFragment(
        id: Int,
        clasVideoPath: String?,
        eqpVideoPath: String?,
        name: String,
        desc: String
    ) {

    }

    override fun refreshCategory(ctgry_ID: Int) {
       // MyApp.showToast(context,"refreshing...")
        if(handler.hasCallbacks(runnable)){
            handler.removeCallbacks(runnable)
        }
        viewModel.getClassDetail(MyApp.userId!!,ctgry_ID)

    }


}